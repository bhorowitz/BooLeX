$(document).ready ->
  # Create a stage by getting a reference to the canvas

  canvas = $('#boolex-stage')
  canvas[0].width = canvas.parent().innerWidth()
  canvas[0].height = $(window).innerHeight()
  canvas.mousedown (e) ->
    e.preventDefault()

  window.boolexStage = new createjs.Stage("boolex-stage")

  toolbox = new Toolbox()

  createjs.Ticker.on('tick', ->
    window.boolexStage.update()
  )

$gateSize = 50
$halfGateSize = $gateSize/2
$socketSize = 4
$socketPadding = $halfGateSize + 8

distanceSquared = (x1, y1, x2, y2) ->
  dx = x2 - x1
  dy = y2 - y1
  dx * dx + dy * dy

distance = (x1, y1, x2, y2) ->
  Math.sqrt(distanceSquared(x1, y1, x2, y2))



class Collectable
  # Collectable is an abstract class that keeps track of all instances
  # it gives all instances a universally-unique identifier (uuid)
  constructor: ->
    @id = Collectable.createUuid()
    @constructor.add(this)

  destroy: ->
    @constructor.remove(this)

  @add: (obj) ->
    unless @all?
      @all = []
      @hash = {}
    @all.push(obj)
    @hash[obj.id] = obj

  @remove: (obj) ->
    if @all?
      @hash[obj.id] = null
      @all = $.grep(@all, (obj2) -> obj2.id != obj.id)

  @find: (id) ->
    @hash[id]

  # function courtesy of https://gist.github.com/bmc/1893440
  @createUuid = ->
    'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) ->
      r = Math.random() * 16 | 0
      v = if c is 'x' then r else (r & 0x3|0x8)
      v.toString(16)
    )

class Tool
  constructor: (@gateClass, @index) ->
    @graphics = new createjs.Container()
    @graphics.addChild(gateClass.createGraphics())
    @graphics.y = @index * ($gateSize + Toolbox.padding)
    @graphics.on('mousedown', (e) ->
      newGate = new gateClass()
      [newGate.graphics.x, newGate.graphics.y] = ((coord) -> [coord.x, coord.y])(@localToGlobal(0, 0))
      newGate.startDrag(e)
    )

class Toolbox
  constructor: ->
    @gates = Gate.types
    @tools = (new Tool(gate, i) for gate, i in @gates)
    @graphics = new createjs.Container()
    @graphics.addChild(tool.graphics) for tool in @tools
    @graphics.x = $halfGateSize
    @graphics.y = $halfGateSize
    window.boolexStage.addChild(@graphics)
    window.boolexStage.update()

  @padding: 5

class Wire extends Collectable
  constructor: (@socket1, @socket2) ->
    @initGraphics()
    @target = null
    super()
    if @socket1?
      @socket1.wires.push(@id)
    if @socket2?
      @socket2.wires.push(@id)

  initGraphics: ->
    @graphics = new createjs.Container()
    @line = new createjs.Shape()
    @graphics.addChild(@line)
    window.boolexStage.addChild(@graphics)

  startDrag: (e) =>
    @line.graphics.clear()
    @line.graphics.beginStroke('black')
    socket1Pos = @socket1.graphics.localToGlobal(0, 0)
    @line.graphics.moveTo(socket1Pos.x, socket1Pos.y)
    @line.graphics.lineTo(e.stageX, e.stageY)
    e.on('mousemove', @drag)
    e.on('mouseup', @stopDrag)

  drag: (e) =>
    @line.graphics.clear()
    @line.graphics.beginStroke('black')
    socket1Pos = @socket1.graphics.localToGlobal(0, 0)
    @line.graphics.moveTo(socket1Pos.x, socket1Pos.y)

    # snap the line to a socket if we're in range.
    if @target?
      @line.graphics.lineTo(@target.x(), @target.y())
    else
      @line.graphics.lineTo(e.stageX, e.stageY)
    @target = null
    for socket in Socket.all
      # TODO: filter away all sockets on the same gate as @socket1
      if distance(socket.x(), socket.y(), e.stageX, e.stageY) < $socketSize * 2
        @target = socket
        break

  stopDrag: (e) =>
    if @target
      @socket2 = @target
      @socket2.wires.push(@id)
    else
      # we didn't make it to a second socket, destroy this
      @destroy()

  redraw: ->
    @line.graphics.clear()
    @line.graphics.beginStroke('black')
    socket1Pos = @socket1.graphics.localToGlobal(0, 0)
    socket2Pos = @socket2.graphics.localToGlobal(0, 0)
    @line.graphics.moveTo(socket1Pos.x, socket1Pos.y)
    @line.graphics.lineTo(socket2Pos.x, socket2Pos.y)

  destroy: ->
    window.boolexStage.removeChild(@graphics)
    if @socket1?
      @socket1.wires = $.grep(@socket1.wires, (id) -> id != @id)
    if @socket2?
      @socket2.wires = $.grep(@socket2.wires, (id) -> id != @id)
    super()

class Socket extends Collectable
  constructor: (@gate, @index, @type) ->
    @initGraphics()
    @initEvents()
    @wires = []
    super()

  initGraphics: ->
    @graphics = new createjs.Container()
    @circle = new createjs.Shape()
    @circle.graphics.beginFill('black').drawCircle(0, 0, $socketSize)
    @circle.graphics.beginFill('white').drawCircle(0, 0, $socketSize - 2)
    @graphics.addChild(@circle)

  initEvents: ->
    @graphics.on 'mousedown', (e) =>
      wire = new Wire(this, null)
      wire.startDrag(e)

  x: ->
    @graphics.localToGlobal(0, 0).x

  y: ->
    @graphics.localToGlobal(0, 0).y

class Gate extends Collectable
  constructor: (@numIns, @numOuts) ->
    @initSockets()
    @initGraphics()
    @initEvents()
    super()

  initSockets: ->
    @inputSockets = (new Socket(this, i, 'in') for i in [1..@numIns])
    @outputSockets = (new Socket(this, i, 'out') for i in [1..@numOuts])

  initGraphics: ->
    @graphics = new createjs.Container()
    @box = @constructor.createGraphics()
    @graphics.addChild(@box)
    for socket, i in @inputSockets
      socket.graphics.x = -$socketPadding
      socket.graphics.y = ($gateSize / (@numIns + 1)) * (i + 1) - $halfGateSize
      @graphics.addChild(socket.graphics)
    for socket, i in @outputSockets
      socket.graphics.x = $socketPadding
      socket.graphics.y = ($gateSize / (@numOuts + 1)) * (i + 1) - $halfGateSize
      @graphics.addChild(socket.graphics)
    window.boolexStage.addChild(@graphics)

  initEvents: ->
    self = this
    @box.on("mousedown", (e) ->
      @offset =
        x: self.graphics.x-e.stageX,
        y: self.graphics.y-e.stageY
    )
    @box.on("pressmove", @drag)

  startDrag: (e) =>
    @graphics.offset =
      x: @graphics.x-e.stageX
      y: @graphics.y-e.stageY
    e.on('mousemove', @drag)

  drag: (e) =>
    @graphics.x = e.stageX + @graphics.offset.x
    @graphics.y = e.stageY + @graphics.offset.y
    for socket in @inputSockets.concat(@outputSockets)
      for wireId in socket.wires
        Wire.find(wireId).redraw()
    # window.boolexStage.update()

  # helper function to create a Gate's DisplayObject. Helps to easily build toolbox display.
  @createGraphics: ->
    container = new createjs.Container()
    box = new createjs.Shape()
    box.graphics.beginFill('black').drawRect(0, 0, $gateSize, $gateSize)
    box.graphics.beginFill('white').drawRect(2, 2, $gateSize - 4, $gateSize - 4)
    box.x = box.y = -$halfGateSize
    container.addChild(box)
    text = new createjs.Text(@displayName, '14px Helvetica')
    bounds = text.getBounds()
    text.x = -bounds.width / 2
    text.y = -bounds.height / 2
    container.addChild(text)
    container

  @displayName: 'GATE'
  @bitmap: "" # path to png file with gate graphic TODO

  @types: []

  @registerType: (gateClass) ->
    @types.push(gateClass)


class AndGate extends Gate
  constructor: ->
    super(2, 1)

  @displayName: 'AND'
  @bitmap: '/assets/images/and_gate.png'


class OrGate extends Gate
  constructor: ->
    super(2, 1)

  @displayName: 'OR'


class NotGate extends Gate
  constructor: ->
    super(1, 1)

  @displayName: 'NOT'


class XorGate extends Gate
  constructor: ->
    super(2, 1)

  @displayName: 'XOR'

Gate.registerType(AndGate)
Gate.registerType(OrGate)
Gate.registerType(NotGate)
Gate.registerType(XorGate)
