class Toolbox
  constructor: ->
    @devices = Gate.types.concat IODevice.types
    @tools = (new Tool(device, i) for device, i in @devices)
    y = 0
    for tool in @tools
      tool.graphics.y = y + tool.deviceClass.height / 2
      tool.graphics.x = $halfGateSize
      y += tool.deviceClass.height + Toolbox.padding
    @graphics = new createjs.Container()
    @graphics.addChild(tool.graphics) for tool in @tools
    window.boolexStage.addChild(@graphics)

  @padding: 5
