class Lightbulb extends IODevice
  constructor: ->
    @bitmap = 
    super(1, 0)

  @createGraphics: ->
    container = new createjs.Container()
    bitmapOff = new createjs.Bitmap(@bitmapOff)
    bitmapOff.x = -$gateSize * 0.5
    bitmapOff.y = -$gateSize * 0.5
    container.addChild(bitmapOff)
    box = new createjs.Shape()
    box.graphics.beginFill(createjs.Graphics.getRGB(255,0,0));
    box.graphics.rect(0, 0, $gateSize, $gateSize)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize * 0.5
    container.hitArea = box
    container

  draw: ->
    if @inputSocket[0].on
      unless @graphics.contains(@light)
        @graphics.addChild(@light)
    else
      @graphics.removeChild(@light)

  @displayName: 'BULB'
  @bitmapOff: '/assets/images/Lightbulb_off.png'
  @bitmapOn: '/assets/images/Lightbulb_on.png'

IODevice.registerType(Lightbulb)

