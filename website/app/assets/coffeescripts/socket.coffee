class Socket extends Collectable
  constructor: (@device, @index, @type, @name) ->
    @initGraphics()
    @initEvents()
    @wires = []
    @name ||= Socket.makeName()
    Socket.states[@name] = 'off'
    super()

  initGraphics: ->
    @graphics = new createjs.Container()
    @circle = new createjs.Shape()
    @circle.graphics.beginFill('black').drawCircle(0, 0, $socketSize)
    @box = new createjs.Shape()
    @box.graphics.beginFill('black').drawRect(-$socketSize, -$socketSize, $socketSize * 2, $socketSize * 2)
    @graphics.hitArea = @box
    # @circle.graphics.beginFill('white').drawCircle(0, 0, $socketSize - 2)
    @graphics.addChild(@circle)

  initEvents: ->
    @graphics.on 'mousedown', (e) =>
      if @type == 'out'
        wire = new Wire(this, null)
      else
        wire = new Wire(null, this)
      wire.startDrag(e)

  connectedSockets: ->
    getter = if @type == 'in' then 'fromSocket' else 'toSocket'
    (wire[getter] for wire in @wires when wire[getter]?)

  connectedToGate: (gates) ->
    if gates
      gate_ids = (gate.id for gate in gates)
    for socket in @connectedSockets()
      if socket.device instanceof Gate
        # if a list of gates was passed, we need to make sure this gate is one of them
        if !gates || $.inArray(socket.device.id, gate_ids) >= 0
          return true
    false

  connectedToLightbulb: ->
    for socket in @connectedSockets()
      if socket.device instanceof Lightbulb
        return true
    false

  x: ->
    @graphics.localToGlobal(0, 0).x

  y: ->
    @graphics.localToGlobal(0, 0).y

  destroy: ->
    for wire in @wires
      wire.destroy()
    Socket.remove(@)

  isRealOutput: (gates) ->
    return @type == 'out' && (!@connectedToGate(gates) || @connectedToLightbulb())

  @makeName: ->
    name = "t#{@nameCounter}"
    @nameCounter++
    name

  @connect: (fromSocket, toSocket, wire) ->
    fromSocket.wires.push(wire)
    toSocket.wires.push(wire)
    toSocket.name = fromSocket.name

  @states: {}

  @nameCounter: 0

