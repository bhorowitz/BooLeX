class Collectable
  # Collectable is an abstract class that keeps track of all instances
  # it gives all instances a universally-unique identifier (uuid)
  constructor: (@klass) ->
    @id = Collectable.createUuid()
    @klass ||= @constructor
    @klass.add(this)

  destroy: ->
    @klass.remove(this)

  @add: (obj) ->
    unless @all?
      @all = []
      @hash = {}
    @all.push(obj)
    @hash[obj.id] = obj

  @collection: ->
    @all

  @remove: (obj) ->
    if @all?
      @hash[obj.id] = null
      @all = $.grep(@all, (obj2) -> obj2.id != obj.id)

  @find: (id) ->
    @hash[id]

  # function courtesy of https://gist.github.com/bmc/1893440
  @createUuid = ->
    'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) ->
      r = Math.random() * 16 | 0
      v = if c is 'x' then r else (r & 0x3|0x8)
      v.toString(16)
    )