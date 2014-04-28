class SevenSegment extends IODevice
  @height = $gateSize * 2
  constructor: ->
    @segments = []
    super(7, 0)

  @prepareSegments: ->
    # BEGIN CREDIT to http://www.williammalone.com/articles/seven-segment-display/
    baseX = -10
    baseY = -18
    digitHeight = $gateSize/5
    thicknessRatio = 0.5
    paddingRatio = 0.2
    segmentRatio = 0.3
    digitWidth = digitHeight / 2
    segmentThickness = digitHeight * thicknessRatio
    segmentPadding = digitHeight * paddingRatio
    segmentLength = segmentThickness / segmentRatio
    segmentPaddingCartesian = segmentPadding * Math.sqrt(2) / 2

    segments = (new createjs.Shape() for i in [0..6])

    segments[0].angle = 0
    segments[1].angle = 90
    segments[2].angle = 90
    segments[3].angle = 0
    segments[4].angle = 90
    segments[5].angle = 90
    segments[6].angle = 0

    for i in [0..6]
      segments[i].graphics.clear()
      segments[i].graphics.beginFill('#000')
      if !segments[i].angle
          segments[i].graphics.moveTo(0, segmentThickness / 2)
          
          segments[i].graphics.lineTo(segmentThickness / 2, 0)
          segments[i].graphics.lineTo(segmentLength - segmentThickness / 2, 0)
          segments[i].graphics.lineTo(segmentLength, segmentThickness / 2)
          segments[i].graphics.lineTo(segmentLength - segmentThickness / 2, segmentThickness)
          segments[i].graphics.lineTo(segmentThickness / 2, segmentThickness)
          segments[i].graphics.lineTo(0, segmentThickness / 2)
      else
          segments[i].graphics.moveTo(segmentThickness / 2, 0)
          
          segments[i].graphics.lineTo(0, segmentThickness / 2)
          segments[i].graphics.lineTo(0, segmentLength - segmentThickness / 2)
          segments[i].graphics.lineTo(segmentThickness / 2, segmentLength)
          segments[i].graphics.lineTo(segmentThickness, segmentLength - segmentThickness / 2)
          segments[i].graphics.lineTo(segmentThickness, segmentThickness / 2)
          segments[i].graphics.lineTo(segmentThickness / 2, 0)


    segments[0].x = baseX + segmentThickness / 2 + segmentPaddingCartesian
    segments[0].y = baseY

    segments[1].x = segments[0].x + segmentLength - segmentThickness / 2 + segmentPaddingCartesian
    segments[1].y = baseY + segmentThickness / 2 + segmentPaddingCartesian

    segments[2].x = segments[1].x
    segments[2].y = segments[1].y + segmentLength + 2 * segmentPaddingCartesian

    segments[3].x = segments[0].x
    segments[3].y = segments[2].y + segmentLength - segmentThickness / 2 + segmentPaddingCartesian

    segments[4].x = baseX
    segments[4].y = segments[2].y

    segments[5].x = baseX
    segments[5].y = segments[1].y

    segments[6].x = segments[0].x
    segments[6].y = segments[1].y + segmentLength - segmentThickness / 2 + segmentPaddingCartesian
    
    # END CREDIT

    segments

  @createGraphics: (device) ->
    container = new createjs.Container()
    box = new createjs.Shape()
    box.graphics.beginFill('#fff')
    box.graphics.rect(0, 0, $gateSize, @height)
    box.x = -$gateSize * 0.5
    box.y = -$gateSize
    container.hitArea = box
    segments = @prepareSegments()
    if device
      device.segments = segments
    container.addChild(segment) for segment in segments
    container

  draw: ->
    for i in [0..6]
      @segments[i].alpha = if Socket.states[@inputSockets[i].name] == 'on' then 1 else 0.2

  @displayName: 'CHAR'
  @bitmapOffPath: '/assets/images/lightbulb_off.png'
  @bitmapOnPath: '/assets/images/lightbulb_on.png'

IODevice.registerType(SevenSegment)

