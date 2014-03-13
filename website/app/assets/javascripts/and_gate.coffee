class AndGate extends Gate
  @constructor: ->
    super(2, 1)

  @bitmap: '/images/andgate.png'

Gate.register(AndGate)