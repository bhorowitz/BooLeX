class Clock extends IODevice
  constructor: ->
    $clocks.push(@)
    super(0, 1)
    @outSocket = @outputSockets[0]
    @type = 1

  click: ->
    wantedType = parseInt(prompt("Speed of clock (higher is slower):"))
    if !isNaN(wantedType) && wantedType > 0
      @type = wantedType

  @createGraphics: (device) ->
    container = new createjs.Container()
    clockWell = new createjs.Shape()
    clockWell.graphics.setStrokeStyle(2)
    clockWell.graphics.beginStroke('#000')
    clockWell.graphics.drawRect(-10, -10, 20, 20)
    clockSign = new createjs.Shape()
    clockSign.graphics.setStrokeStyle(1).beginStroke('#000')
    clockSign.graphics.moveTo(-5, -3)
    clockSign.graphics.lineTo(-5, 3)
    clockSign.graphics.lineTo(0, 3)
    clockSign.graphics.lineTo(0, -3)
    clockSign.graphics.lineTo(5, -3)
    clockSign.graphics.lineTo(5, 3)
    if device
      device.clockSign = clockSign
    container.addChild(clockWell)
    container.addChild(clockSign)
    box = new createjs.Shape()
    box.graphics.beginFill(createjs.Graphics.getRGB(255,0,0));
    box.graphics.rect(0, 0, $gateSize, $gateSize)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize * 0.5
    container.hitArea = box
    container

  draw: ->
    if @outSocket and Socket.states[@outSocket.name] == 'on' and $openConnection
      @clockSign.shadow = new createjs.Shadow("#00aaff", 0, 0, 5);
    else
      @clockSign.shadow = null

  @displayName: 'CLOCK'

IODevice.registerType(Clock)

