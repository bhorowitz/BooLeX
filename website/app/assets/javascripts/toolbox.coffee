class Toolbox
  constructor: ->
    @gates = Gate.all
    @tools = (new Tool(gate, i) for gate, i in @gates)
    @graphics = new DisplayObject
    @graphics.addChild(tool.graphics for tool in @tools)
    @graphics.x = 40
    window.boolexStage.addChild(@graphics)



