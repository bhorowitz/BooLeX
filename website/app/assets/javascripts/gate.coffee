class Gate
  constructor: (@numIns, @numOuts) ->
    @inputSockets = (new Socket(null, this) for i in [1..numIns])
    @outputSockets = (new Socket(null, this) for i in [1..numOuts])
    @graphics = new Bitmap(@constructor.bitmap)
    window.boolexStage.addChild(@graphics)
    graphics.addEventListener 'mousedown', (e) ->
      e.addEventListener 'mousemove', (e2) ->
        graphics.x = e2.stageX
        graphics.y = e2.stageY

  @bitmap: "" # path to png file with gate graphic

  @all: []

  @register: (gateClass) ->
    @all.push(gateClass)

