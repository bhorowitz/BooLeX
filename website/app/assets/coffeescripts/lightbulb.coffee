class Lightbulb extends IODevice
  constructor: ->
    super(1, 0)

  @createGraphics: (device) ->
    container = new createjs.Container()
    bitmapOff = new createjs.Bitmap(@bitmapOffPath)
    bitmapOff.x = -$gateSize * 0.5
    bitmapOff.y = -$gateSize * 0.5
    bitmapOn = new createjs.Bitmap(@bitmapOnPath)
    bitmapOn.x = -$gateSize * 0.5
    bitmapOn.y = -$gateSize * 0.5
    bitmapOn.visible = false
    container.addChild(bitmapOff)
    container.addChild(bitmapOn)
    if device?
      device.bitmapOff = bitmapOff
      device.bitmapOn = bitmapOn
    box = new createjs.Shape()
    box.graphics.beginFill(createjs.Graphics.getRGB(255,0,0));
    box.graphics.rect(0, 0, $gateSize, $gateSize)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize * 0.5
    container.hitArea = box
    container

  draw: ->
    if Socket.states[@inputSockets[0].name] == 'on'
      @bitmapOn.visible = true
      @bitmapOff.visible = false
    else
      @bitmapOn.visible = false
      @bitmapOff.visible = true

  @displayName: 'BULB'
  @bitmapOffPath: '/assets/images/lightbulb_off.png'
  @bitmapOnPath: '/assets/images/lightbulb_on.png'

IODevice.registerType(Lightbulb)

