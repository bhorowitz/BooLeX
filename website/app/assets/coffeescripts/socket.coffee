class Socket extends Collectable
  constructor: (@device, @index, @type) ->
    @initGraphics()
    @initEvents()
    @wires = []
    @name = Socket.randomName()
    Socket.states[@name] = 'off'
    super()

  initGraphics: ->
    @graphics = new createjs.Container()
    @circle = new createjs.Shape()
    @circle.graphics.beginFill('black').drawCircle(0, 0, $socketSize)
    # @circle.graphics.beginFill('white').drawCircle(0, 0, $socketSize - 2)
    @graphics.addChild(@circle)

  initEvents: ->
    @graphics.on 'mousedown', (e) =>
      if @type == 'out'
        wire = new Wire(this, null)
      else
        wire = new Wire(null, this)
      wire.startDrag(e)

  connectedSockets: ->
    getter = if @type == 'in' then 'fromSocket' else 'toSocket'
    (wire[getter] for wire in @wires when wire[getter]?)

  connectedToGate: ->
    for socket in @connectedSockets()
      if socket.device instanceof Gate
        return true
    false

  x: ->
    @graphics.localToGlobal(0, 0).x

  y: ->
    @graphics.localToGlobal(0, 0).y

  @randomName: ->
    possible = 'abcdefghijklmnopqrstuvwxyz'
    name = ''
    for i in [0..4]
      name += possible[Math.floor(Math.random() * 26)]
    name

  @connect: (fromSocket, toSocket, wire) ->
    fromSocket.wires.push(wire)
    toSocket.wires.push(wire)
    toSocket.name = fromSocket.name

  @states: {}

