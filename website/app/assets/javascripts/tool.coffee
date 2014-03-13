class Tool
  constructor: (@gate_class, @index) ->
    @graphics = new DisplayObject
    @graphics.addChild(new Bitmap(gate_class.bitmap))
    @graphics.y = 40 + @index * 50
