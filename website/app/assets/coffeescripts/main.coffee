$(document).ready ->
  # Create a stage by getting a reference to the canvas

  canvas = $('#boolex-stage')
  canvas[0].width = canvas.parent().innerWidth()
  canvas[0].height = $(window).innerHeight()
  canvas.mousedown (e) ->
    e.preventDefault()

  window.boolexStage = new createjs.Stage("boolex-stage")

  toolbox = new Toolbox()

  createjs.Ticker.on('tick', ->
    window.boolexStage.update()
  )

  $(window).on('refreshDSL', ->
    console.log(Gate.createDSL())
  )

  $('#start-stop-button').click ->
    $this = $(this)
    if $this.data('running')?
      stopBoolex()
      $this.removeClass('btn-danger').addClass('btn-primary').text('Start')
      $this.data('running', null)
    else
      startBoolex()
      $this.removeClass('btn-primary').addClass('btn-danger').text('Stop')
      $this.data('running', true)

  $(window).bind('update', (e, isManual=true, socket=null) ->
    devices = IODevice.all
    for device in devices
      device.draw()
    for wire in Wire.all
      wire.redraw()
    if $openConnection? && socket?
      console.log(socket)
      console.log("Sending: "+JSON.stringify(
        command: 'update',
        socket: { name: socket.name, value: Socket.states[socket.name] == 'on' }
      ))

      $openConnection.send(JSON.stringify(
        command: 'update',
        socket: { name: socket.name, value: Socket.states[socket.name] == 'on' }
      ))
  )

$gateSize = 40
$halfGateSize = $gateSize/2
$socketSize = 4
$socketPadding = $halfGateSize + 4
$openConnection = null

distanceSquared = (x1, y1, x2, y2) ->
  dx = x2 - x1
  dy = y2 - y1
  dx * dx + dy * dy

distance = (x1, y1, x2, y2) ->
  Math.sqrt(distanceSquared(x1, y1, x2, y2))

window.cometMessage = (message) ->
  console.log("Event received: #{message}")

window.echo = (message) ->
  $('#echoer').attr('src', "/echo/#{encodeURIComponent(message)}")

startBoolex = ->
  dsl = Gate.createDSL()
  $openConnection = new WebSocket("ws://localhost:9000/boolex?dsl=#{encodeURIComponent(dsl)}", ['soap', 'xmpp'])
  
  $openConnection.onopen = (msg) ->
    $openConnection.send(JSON.stringify(
      command: 'start',
      initialValues: { name: socket.name, value: Socket.states[socket.name] == 'on'} for socket in Gate.inputs()
    ))


  $openConnection.onerror = (error) ->
    console.log('WebSocket Error ' + error)

  $openConnection.onmessage = (e) ->
    console.log("Received: #{e.data}")
    res = JSON.parse(e.data)
    if res.command == 'update'
      Socket.states[res.socket.name] = if res.socket.value == 'false' then 'off' else 'on'
    $(window).trigger('update', false)

stopBoolex = ->
  $openConnection.close()
  $openConnection = null

# socket = io.connect('http://localhost:9000/boolex')
# socket.on('connect', ->
#   # socket.send('hi')

#   socket.on('message', (msg) ->
#     console.log(msg)
#   )
# )
