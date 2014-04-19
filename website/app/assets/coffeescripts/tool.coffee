class Tool
  constructor: (@deviceClass, @index) ->
    @graphics = new createjs.Container()
    @graphics.addChild(deviceClass.createGraphics())
    @graphics.y = @index * ($gateSize + Toolbox.padding)
    @graphics.on('mousedown', (e) ->
      newGate = new deviceClass()
      [newGate.graphics.x, newGate.graphics.y] = ((coord) -> [coord.x, coord.y])(@localToGlobal(0, 0))
      newGate.startDrag(e)
    )
