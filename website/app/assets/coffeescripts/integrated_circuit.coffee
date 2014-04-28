class IntegratedCircuit extends Gate
  constructor: (gates_or_dsl) -> # must be passed either a list of gates or dsl
    if gates_or_dsl instanceof Array
      # list of gates
      @gates = gates_or_dsl
      @inputSockets = []
      @outputSockets = []

      concatenate = (items) -> [].concat.apply([], items)
      setMinus = (as, bs) -> (a for a in as when $.inArray(a, bs) < 0)

      internalInputSet  = concatenate(gate.inputSockets for gate in @gates).map((inSocket) -> inSocket.id)
      internalOutputSet = concatenate(gate.outputSockets for gate in @gates).map((outSocket) -> outSocket.id)
      allSockets = concatenate([internalInputSet, internalOutputSet])

      # Check for toSocket nullity
      sources = setMinus(concatenate(internalInputSet.map((id) -> wire.fromSocket.id for wire in Socket.find(id).wires)), allSockets).unique()
      destinations = setMinus(concatenate(internalOutputSet.map((id) -> wire.toSocket.id for wire in Socket.find(id).wires)), allSockets).unique()

      sources = sources.sort((a,b) -> Socket.find(a).y() - Socket.find(b).y())
      destinations = destinations.sort((a,b) -> Socket.find(a).y() - Socket.find(b).y())

      sourceNames = sources.map((x) -> Socket.find(x).name).unique()
      destinationNames = destinations.map((x) -> Socket.find(x).name).unique()

      @name = "ic_" + Math.floor(Math.random()*100)

      integratedDSL = "circuit #{@name}(" + sourceNames.join(", ") + ")\n    "
      internalDSL = @gates.map((gate) -> gate.createDSL()).join("\n    ")
      integratedDSL += internalDSL + "\n    out " + destinationNames.join(", ") + "\nend"

      for gate in @gates
        if gate instanceof IntegratedCircuit
          integratedDSL = gate.dsl + "\n\n" + integratedDSL

      @dsl = integratedDSL

      outConnections = []
      for name, i in destinationNames
        outConnections[i] = []
        for dest in destinations
          socket = Socket.find(dest)
          outConnections[i].push(socket) if socket.name == name

      inConnections = sources.map((x) -> [Socket.find(x)])

      sumX = 0
      sumY = 0
      for gate in @gates
        sumX += gate.x()
        sumY += gate.y()
        gate.destroy()
      x = sumX/@gates.length
      y = sumY/@gates.length

      super(sourceNames.length, destinationNames.length)

      for socks, i in outConnections
        for sock in socks
          new Wire(@outputSockets[i], sock)

      for socks, i in inConnections
        for sock in socks
          new Wire(sock, @inputSockets[i])

    else
      @dsl = gates_or_dsl
      # NOTE: dsl must be valid!!
      match = /circuit ([a-zA-Z0-9_\-]+)\(([^)]*)\)/m.exec(@dsl)
      @name = match[1]
      inputs = (str.trim() for str in match[2].split(','))
      match = /^  out ([a-zA-Z0-9_\- ,]+)$/m.exec(@dsl)
      outputs = (str.trim() for str in match[1].split(','))
      x = $stageWidth * 0.5
      y = $stageHeight * 0.5
      super(inputs.length, outputs.length)
    @graphics.x = x
    @graphics.y = y
    $(window).trigger('refreshDSL')

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