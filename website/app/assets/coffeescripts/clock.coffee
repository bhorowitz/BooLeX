class Clock extends IODevice
  constructor: ->
    $clocks.push(@)
    super(0, 1)
    @outSocket = @outputSockets[0]

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
    container.addChild(clockWell)
    container.addChild(clockSign)
    box = new createjs.Shape()
    box.graphics.beginFill(createjs.Graphics.getRGB(255,0,0));
    box.graphics.rect(0, 0, $gateSize, $gateSize)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize * 0.5
    container.hitArea = box
    container

  @displayName: 'CLOCK'

IODevice.registerType(Clock)

