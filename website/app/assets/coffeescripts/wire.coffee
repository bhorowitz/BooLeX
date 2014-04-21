class Wire extends Collectable
  constructor: (@fromSocket, @toSocket) ->
    @initGraphics()
    @target = null
    super()
    if @fromSocket? and @toSocket?
      @fromSocket.wires.push(@)
      @toSocket.wires.push(@)
    else if @fromSocket?
      @startSocket = @fromSocket
      @endSocket = 'toSocket'
    else if @toSocket?
      @startSocket = @toSocket
      @endSocket = 'fromSocket'

  initGraphics: ->
    @graphics = new createjs.Container()
    @line = new createjs.Shape()
    @graphics.addChild(@line)
    window.boolexStage.addChild(@graphics)

  startDrag: (e) =>
    @line.graphics.clear()
    startSocketPos = @startSocket.graphics.localToGlobal(0, 0)
    @drawTo(startSocketPos.x, startSocketPos.y, e.stageX, e.stageY)
    e.on('mousemove', @drag)
    e.on('mouseup', @stopDrag)

  drag: (e) =>
    startSocketPos = @startSocket.graphics.localToGlobal(0, 0)
    @line.graphics.moveTo(startSocketPos.x, startSocketPos.y)

    # snap the line to a socket if we're in range.
    if @target?
      @drawTo(startSocketPos.x, startSocketPos.y, @target.x(), @target.y())
    else
      @drawTo(startSocketPos.x, startSocketPos.y, e.stageX, e.stageY)
    @target = null
    for socket in Socket.all
      # TODO: filter away all sockets on the same gate as @fromSocket
      if distance(socket.x(), socket.y(), e.stageX, e.stageY) < $socketSize * 2
        if socket.type != @startSocket.type
          unless socket.type == 'in' && socket.wires.length
            @target = socket
            break

  drawTo: (x1, y1, x2, y2) ->
    @line.graphics.clear()
    if Socket.states[@fromSocket.name] == 'on'
      @line.shadow = new createjs.Shadow("#00aaff", 0, 0, 5);
    else
      @line.shadow = null
    @line.graphics.beginStroke('black')
    @line.graphics.moveTo(x1, y1)

    @line.graphics.lineTo((x2 + x1) * 0.5, y1)
    @line.graphics.lineTo((x2 + x1) * 0.5, y2)
    @line.graphics.lineTo(x2, y2)

  stopDrag: (e) =>
    if @target
      @[@endSocket] = @target
      Socket.connect(@fromSocket, @toSocket, @)
      $(window).trigger('refreshDSL')
    else
      # we didn't make it to a second socket, destroy this
      @destroy()

  redraw: ->
    fromSocketPos = @fromSocket.graphics.localToGlobal(0, 0)
    toSocketPos = @toSocket.graphics.localToGlobal(0, 0)
    @drawTo(fromSocketPos.x, fromSocketPos.y, toSocketPos.x, toSocketPos.y)

  destroy: ->
    window.boolexStage.removeChild(@graphics)
    if @fromSocket?
      @fromSocket.wires = $.grep(@fromSocket.wires, (wire) -> wire.id != @id)
    if @toSocket?
      @toSocket.wires = $.grep(@toSocket.wires, (wire) -> wire.id != @id)
    super()