class SelectBox extends createjs.Shape

  constructor: (@startX, @startY) ->
    @box = new createjs.Shape()
    @endX = @startX
    @endY = @startY

  draw: ->
    @box.graphics.clear()
    @box.graphics.beginStroke("rgba(0,0,0,0.3)")
    @box.graphics.moveTo(@startX, @startY)
    @box.graphics.lineTo(@endX, @startY)
    @box.graphics.lineTo(@endX, @endY)
    @box.graphics.lineTo(@startX, @endY)
    @box.graphics.lineTo(@startX, @startY)

  between: (n, low, high) ->
    if low < high && n > low && n < high || low > high && n < low && n > high
      return true
    return false

  selectDevicesUnder: ->
    for device in $allDevices
      [x, y] = [device.x(), device.y()]
      if @between(x, @startX, @endX) && @between(y, @startY, @endY)
        device.select()
      else
        device.deselect()