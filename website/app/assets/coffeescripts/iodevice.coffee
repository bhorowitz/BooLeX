class IODevice extends Device
  constructor: (numIns, numOuts) ->
    $(window).on('current', (e) ->
      devices = IODevice.all
      for device in devices
        device.draw()
    )
    super(numIns, numOuts, IODevice)

  draw: ->
    # abstract method, must be overrided

  @registerType: (ioDeviceClass) ->
    @types.push(ioDeviceClass)

  @displayName: 'IO'
  @types: []

