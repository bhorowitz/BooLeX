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
      $this.removeClass('btn-danger').addClass('btn-primary').text('Start')
      $this.data('running', null)
    else
      sendBoolex()
      $this.removeClass('btn-primary').addClass('btn-danger').text('Stop')
      $this.data('running', true)
      

$gateSize = 40
$halfGateSize = $gateSize/2
$socketSize = 4
$socketPadding = $halfGateSize + 4

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

sendBoolex = ->
  dsl = Gate.createDSL()
  connection = new WebSocket("ws://localhost:9000/boolex?dsl=#{encodeURIComponent(dsl)}", ['soap', 'xmpp']);
  connection.onopen = (msg) ->
    connection.send('start')
    connection.send('update')
    console.log(msg)
  connection.onerror = (error) ->
    console.log('WebSocket Error ' + error)

  connection.onmessage = (e) ->
    console.log('Server: ' + e.data)

# socket = io.connect('http://localhost:9000/boolex')
# socket.on('connect', ->
#   # socket.send('hi')

#   socket.on('message', (msg) ->
#     console.log(msg)
#   )
# )
