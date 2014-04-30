class Device extends Collectable
  self = this
  @height = $gateSize
  @width = $gateSize
  constructor: (@numIns, @numOuts, klass) ->
    @initSockets()
    @initGraphics()
    @initEvents()
    @dragged = false
    $allDevices.push(@)
    super(klass)
    $(window).trigger('refreshDSL')

  # initialize sockets
  initSockets: ->
    @inputSockets = @outputSockets = []
    if @numIns > 0
      @inputSockets = (new Socket(this, i, 'in') for i in [1..@numIns])
    if @numOuts > 0
      @outputSockets = (new Socket(this, i, 'out') for i in [1..@numOuts])

  # initialize graphics object and draw it
  initGraphics: ->
    @graphics = new createjs.Container()
    @box = @constructor.createGraphics(@)
    @selected = new createjs.Shape()
    @selected.graphics.beginFill('rgba(0, 0, 255, 0.05)')
    @selected.graphics.beginStroke('rgba(0, 0, 255, 0.3)')
    @selected.graphics.drawRect(0, 0, $gateSize + 20, $gateSize + 20)
    @selected.x = @selected.y = -($gateSize * 0.5 + 10)
    @selected.visible = false
    @graphics.addChild(@selected)
    @graphics.addChild(@box)
    @sockets = @drawSockets()
    window.boolexStage.addChild(@graphics)  

  # draw sockets in a pretty, even way
  drawSockets: ->
    for socket, i in @inputSockets
      socket.graphics.x = -$socketPadding * (@constructor.width / $gateSize)
      socket.graphics.y = (@constructor.height / (@numIns + 1)) * (i + 1) - @constructor.height*0.5
      @graphics.addChild(socket.graphics)
    for socket, i in @outputSockets
      socket.graphics.x = $socketPadding * (@constructor.width / $gateSize)
      socket.graphics.y = (@constructor.height / (@numOuts + 1)) * (i + 1) - @constructor.height*0.5
      @graphics.addChild(socket.graphics)

  # initialize event handlers
  initEvents: ->
    @box.on("mousedown", (e) =>
      @dragged = false
      @graphics.offset =
        x: @graphics.x-e.stageX,
        y: @graphics.y-e.stageY
    )
    @box.on("pressmove", @drag)
    @box.on("click", (e) =>
      unless @dragged
        console.log(@id)
        @click()
    )

  # return all devices that are connected to input sockets of this one
  inputDevices: ->
    [inputSocket.wires[0].fromSocket for inputSocket in @inputSockets if inputSocket.wires.length]

  # start following the mouse
  startDrag: (e) =>
    @graphics.offset =
      x: @graphics.x-e.stageX
      y: @graphics.y-e.stageY
    boolexStage.dragged = @

  # follow the mouse (fired on mousemove)
  drag: (e) =>
    @dragged = true
    @graphics.x = e.stageX + @graphics.offset.x
    @graphics.y = e.stageY + @graphics.offset.y
    for socket in @inputSockets.concat(@outputSockets)
      for wire in socket.wires
        wire.redraw()

  # select this device, adding it to global list of selected devices
  select: ->
    $selectedDevices[@id] = @
    @selected.visible = true

  # deselect this device, removing it from the global list
  deselect: ->
    if $selectedDevices[@id]
      delete $selectedDevices[@id]
    @selected.visible = false

  # called when user clicks this device
  click: ->
    Device.deselectAll()
    @select()
    # abstract

  # destroy this device completely, optionally destroying its sockets as well
  destroy: (keepSockets=false)->
    unless keepSockets
      for socket in @inputSockets.concat(@outputSockets)
        socket.destroy()
    if @graphics.parent
      @graphics.parent.removeChild(@graphics)
    window.$allDevices = $.grep($allDevices, (device) => device.id != @id)
    $(window).trigger('refreshDSL')
    super()

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

  # static method to deselect all devices
  @deselectAll: ->
    for device in $allDevices
      device.deselect()

  # helper method to return the x position of this device
  x: ->
    @graphics.x

  # helper method to return the x position of this device
  y: ->
    @graphics.y

