class IntegratedDSL
  @circuits: {}
  @saveDSL: (name, dsl) ->
    if name of @circuits
      console.log("Tried to overwrite " + name)
    else @circuits[name] = { dsl: dsl, deps: [] }

  @getDSL: (name) ->
    if name of @circuits
      return @circuits[name].dsl
    console.log("Could not retrieve " + name)
    return undefined

  @deleteDSL: (name) ->
    if name of @circuits
      delete @circuits[name]

  @setDependencies: (name, deps) ->
    if name of @circuits
      @circuits[name].deps = deps
    else
      console.log("Could not retrieve " + name)
      return undefined

  @getDependencies: (name) ->
    if name of @circuits
      myDeps = @circuits[name].deps
      allDeps = []
      for dep in myDeps
        nextDeps = @getDependencies(dep)
        nextDeps.unshift(dep)
        allDeps.push(nextDeps)
      return allDeps
    console.log("Could not retrieve " + name)
    return []