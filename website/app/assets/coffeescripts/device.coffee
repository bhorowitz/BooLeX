class Device extends Collectable
  self = this
  constructor: (@numIns, @numOuts, klass) ->
    @initSockets()
    @initGraphics()
    @initEvents()
    @dragged = false
    $allDevices.push(@)
    super(klass)

  initSockets: ->
    @inputSockets = @outputSockets = []
    if @numIns > 0
      @inputSockets = (new Socket(this, i, 'in') for i in [1..@numIns])
    if @numOuts > 0
      @outputSockets = (new Socket(this, i, 'out') for i in [1..@numOuts])

  initGraphics: ->
    @graphics = new createjs.Container()
    @box = @constructor.createGraphics(@)
    @selected = new createjs.Shape()
    @selected.graphics.beginFill('rgba(0, 0, 255, 0.1)')
    @selected.graphics.beginStroke('rgba(0, 0, 255, 0.3)')
    @selected.graphics.drawRect(0, 0, $gateSize + 20, $gateSize + 20)
    @selected.x = @selected.y = -($gateSize * 0.5 + 10)
    @selected.visible = false
    @graphics.addChild(@selected)
    @graphics.addChild(@box)
    @sockets = @drawSockets()
    window.boolexStage.addChild(@graphics)  

  drawSockets: ->
    for socket, i in @inputSockets
      socket.graphics.x = -$socketPadding
      socket.graphics.y = ($gateSize / (@numIns + 1)) * (i + 1) - $halfGateSize
      @graphics.addChild(socket.graphics)
    for socket, i in @outputSockets
      socket.graphics.x = $socketPadding
      socket.graphics.y = ($gateSize / (@numOuts + 1)) * (i + 1) - $halfGateSize
      @graphics.addChild(socket.graphics)

  initEvents: ->
    @box.on("mousedown", (e) =>
      @dragged = false
      @offset =
        x: @graphics.x-e.stageX,
        y: @graphics.y-e.stageY
    )
    @box.on("pressmove", @drag)
    @box.on("click", (e) =>
      unless @dragged
        console.log(@id)
        @click()
    )

  inputDevices: ->
    #TODO
    [inputSocket.wires[0].fromSocket for inputSocket in @inputSockets if inputSocket.wires.length]

  startDrag: (e) =>
    @graphics.offset =
      x: @graphics.x-e.stageX
      y: @graphics.y-e.stageY
    boolexStage.dragged = @

  drag: (e) =>
    @dragged = true
    @graphics.x = e.stageX + @graphics.offset.x
    @graphics.y = e.stageY + @graphics.offset.y
    for socket in @inputSockets.concat(@outputSockets)
      for wire in socket.wires
        wire.redraw()
    # window.boolexStage.update()

  select: ->
    @selected.visible = true

  deselect: ->
    @selected.visible = false

  click: ->
    Device.deselectAll()
    @select()
    # abstract

  destroy: ->
    @graphics.parent.removeChild(@graphics)
    $allDevices = $.grep($allDevices, (device) -> device.id != @id)

  # helper function to create a device's DisplayObject. Helps to easily build toolbox display.
  @createGraphics: (device) ->
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

  @deselectAll: ->
    for device in $allDevices
      device.deselect()

  x: ->
    @graphics.x

  y: ->
    @graphics.y
