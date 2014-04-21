class IODevice extends Device
  constructor: (numIns, numOuts) ->
    super(numIns, numOuts, IODevice)

  draw: ->
    # abstract method, must be overrided

  @registerType: (ioDeviceClass) ->
    @types.push(ioDeviceClass)

  @displayName: 'IO'
  @types: []

