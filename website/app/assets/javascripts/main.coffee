$(document).ready ->
  # Create a stage by getting a reference to the canvas

  canvas = $('#boolex-stage')
  canvas[0].width = canvas.parent().innerWidth()
  canvas[0].height = $(window).innerHeight()

  window.boolexStage = new createjs.Stage("boolex-stage")

  toolbox = new Toolbox()


