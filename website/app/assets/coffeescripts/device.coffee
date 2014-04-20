class Device extends Collectable
  constructor: (@numIns, @numOuts, klass) ->
    @initSockets()
    @initGraphics()
    @initEvents()
    super(klass)

  initSockets: ->
    @inputSockets = @outputSockets = []
    if @numIns > 0
      @inputSockets = (new Socket(this, i, 'in') for i in [1..@numIns])
    if @numOuts > 0
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

  inputDevices: ->
    #TODO
    [inputSocket.wires[0].fromSocket for inputSocket in @inputSockets if inputSocket.wires.length]

  startDrag: (e) =>
    @graphics.offset =
      x: @graphics.x-e.stageX
      y: @graphics.y-e.stageY
    e.on('mousemove', @drag)

  drag: (e) =>
    @graphics.x = e.stageX + @graphics.offset.x
    @graphics.y = e.stageY + @graphics.offset.y
    for socket in @inputSockets.concat(@outputSockets)
      for wire in socket.wires
        wire.redraw()
    # window.boolexStage.update()

  # helper function to create a device's DisplayObject. Helps to easily build toolbox display.
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
