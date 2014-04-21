class Switch extends IODevice
  constructor: ->
    super(0, 1)
    @outSocket = @outputSockets[0]
    self = @
    @graphics.on('click', ->
      if Socket.states[self.outSocket.name] == 'on'
        Socket.states[self.outSocket.name] = 'off'
      else
        Socket.states[self.outSocket.name] = 'on'
      $(window).trigger('update', [true, self.outSocket])
    )

  @createGraphics: (device) ->
    container = new createjs.Container()
    switchWell = new createjs.Shape()
    switchWell.graphics.setStrokeStyle(2)
    switchWell.graphics.beginStroke('#000000')
    switchWell.graphics.drawRoundRect(-10, -5, 20, 10, 10)
    switcher = new createjs.Shape()
    switcher.graphics.beginFill('#000000')
    switcher.graphics.drawCircle(0, 0, 3)
    container.addChild(switchWell)
    container.addChild(switcher)
    if device?
      device.switcher = switcher
    box = new createjs.Shape()
    box.graphics.beginFill(createjs.Graphics.getRGB(255,0,0));
    box.graphics.rect(0, 0, $gateSize, $gateSize)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize * 0.5
    container.hitArea = box
    container

  draw: ->
    if Socket.states[@outSocket.name] == 'on'
      @switcher.x = 5
    else
      @switcher.x = -5

  @displayName: 'SWITCH'

IODevice.registerType(Switch)

