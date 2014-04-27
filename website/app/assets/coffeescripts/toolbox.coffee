class Toolbox
  constructor: ->
    @devices = Gate.types.concat IODevice.types
    @tools = (new Tool(device, i) for device, i in @devices)
    @graphics = new createjs.Container()
    @graphics.addChild(tool.graphics) for tool in @tools
    @graphics.x = $halfGateSize
    @graphics.y = $halfGateSize
    window.boolexStage.addChild(@graphics)

  @padding: 5
