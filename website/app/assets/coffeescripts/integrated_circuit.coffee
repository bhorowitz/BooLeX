class IntegratedCircuit extends Gate
  constructor: (gates_or_dsl) -> # must be passed either a list of gates or dsl
    if gates_or_dsl instanceof Array
      # list of gates
      @gates = gates_or_dsl
      @inputSockets = []
      @outputSockets = []
      ins = Gate.inputs(@gates)
      outs = Gate.outs(@gates)
      inputConnections = ((wire.fromSocket for wire in socket.wires) for socket in ins)
      outputConnections = ((wire.toSocket for wire in socket.wires) for socket in outs)
      @name = 'ic_' + Math.floor(Math.random()*100)
      sumX = 0
      sumY = 0
      for gate in @gates
        sumX += gate.x()
        sumY += gate.y()
        gate.destroy()
      x = sumX/@gates.length
      y = sumY/@gates.length
      # add all input sockets, maintaining connections
      for socket, i in ins
        newSocket = new Socket(@, i, 'in', socket.name)
        @inputSockets.push(newSocket)
        for fromSocket in inputConnections[i]
          wire = new Wire(fromSocket, newSocket)

      # add all output sockets, maintaining connections
      for socket, i in outs
        newSocket = new Socket(@, i, 'out', socket.name)
        @outputSockets.push(newSocket)
        for toSocket in outputConnections[i]
          wire = new Wire(newSocket, toSocket)
      @dsl = @circuitDSL()
    else
      @dsl = gates_or_dsl
      # NOTE: dsl must be valid!!
      match = /circuit ([a-zA-Z0-9_\-]+)\(([^)]*)\)/m.exec(@dsl)
      @name = match[1]
      inputs = (str.trim() for str in match[2].split(','))
      match = /^  out ([a-zA-Z0-9_\- ,]+)$/m.exec(@dsl)
      outputs = (str.trim() for str in match[1].split(','))
      @inputSockets = (new Socket(@, i, 'in') for name, i in inputs)
      @outputSockets = (new Socket(@, i, 'out') for name, i in outputs)
      x = $stageWidth * 0.5
      y = $stageHeight * 0.5
    super(@inputSockets.length, @outputSockets.length)
    @graphics.x = x
    @graphics.y = y

  randomizeSocketLocalNames: ->
    for socket in @inputSockets.concat(@outputSockets)
      socket.localName = Socket.makeName()

  @createGraphics: (device) ->
    container = new createjs.Container()
    box = new createjs.Shape()
    box.graphics.beginFill('black').drawRect(0, 0, $gateSize, $gateSize)
    box.graphics.beginFill('white').drawRect(2, 2, $gateSize - 4, $gateSize - 4)
    box.x = box.y = -$halfGateSize
    container.addChild(box)
    text = new createjs.Text(device.name, '14px Helvetica')
    bounds = text.getBounds()
    text.x = -bounds.width / 2
    text.y = -bounds.height / 2
    container.addChild(text)
    container

  initSockets: ->
    for socket in @inputSockets.concat(@outputSockets)
      socket.device = @

  createDSL: ->
    "#{(socket.name for socket in @outputSockets).join(', ')} = #{@name}(#{(socket.name for socket in @inputSockets).join(', ')})"

  circuitDSL: ->
    if @dsl
      return @dsl
    integratedCircuits = ''
    unless @gates
      return "circuit #{@name}()\nend"
    main = "circuit #{@name}(#{(s.name for s in @inputSockets).unique().join(', ')})\n" # TODO: Sort by y position or something for integrated circuits
    for gate in @gates
      if gate instanceof IntegratedCircuit
        integratedCircuits += gate.circuitDSL() + '\n\n'
      main += '  ' + gate.createDSL() + '\n'
    main += '  out ' + (socket.name for socket in @outputSockets).join(', ') + '\n'
    main += 'end'
    integratedCircuits + main