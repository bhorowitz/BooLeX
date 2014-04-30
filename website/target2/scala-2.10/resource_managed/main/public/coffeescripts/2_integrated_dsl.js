(function() {
  var IntegratedDSL;

  IntegratedDSL = (function() {

    function IntegratedDSL() {}

    IntegratedDSL.circuits = {};

    IntegratedDSL.saveDSL = function(name, dsl) {
      if (name in this.circuits) {
        return console.log("Tried to overwrite " + name);
      } else {
        return this.circuits[name] = {
          dsl: dsl,
          deps: []
        };
      }
    };

    IntegratedDSL.getDSL = function(name) {
      if (name in this.circuits) {
        return this.circuits[name].dsl;
      }
      console.log("Could not retrieve " + name);
      return void 0;
    };

    IntegratedDSL.deleteDSL = function(name) {
      if (name in this.circuits) {
        return delete this.circuits[name];
      }
    };

    IntegratedDSL.setDependencies = function(name, deps) {
      if (name in this.circuits) {
        return this.circuits[name].deps = deps;
      } else {
        console.log("Could not retrieve " + name);
        return void 0;
      }
    };

    IntegratedDSL.getDependencies = function(name) {
      var allDeps, dep, myDeps, nextDeps, _i, _len;
      if (name in this.circuits) {
        myDeps = this.circuits[name].deps;
        allDeps = [];
        for (_i = 0, _len = myDeps.length; _i < _len; _i++) {
          dep = myDeps[_i];
          nextDeps = this.getDependencies(dep);
          nextDeps.unshift(dep);
          allDeps.push(nextDeps);
        }
        return allDeps;
      }
      console.log("Could not retrieve " + name);
      return [];
    };

    return IntegratedDSL;

  })();

}).call(this);
