(function() {
  var AndGate, BufferGate, Gate, NandGate, NorGate, NotGate, OrGate, XnorGate, XorGate,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Gate = (function(_super) {

    __extends(Gate, _super);

    function Gate(numIns, numOuts) {
      this.numIns = numIns;
      this.numOuts = numOuts;
      Gate.__super__.constructor.call(this, this.numIns, this.numOuts, Gate);
    }

    Gate.registerType = function(gateClass) {
      return this.types.push(gateClass);
    };

    Gate.createGraphics = function(device) {
      var bitmap, box, container;
      container = new createjs.Container();
      bitmap = new createjs.Bitmap(this.bitmap);
      bitmap.x = -$gateSize * 0.5;
      bitmap.y = -$gateSize * 0.5;
      container.addChild(bitmap);
      box = new createjs.Shape();
      box.graphics.beginFill(createjs.Graphics.getRGB(255, 0, 0));
      box.graphics.rect(0, 0, $gateSize, $gateSize);
      box.x = -$gateSize * 0.5;
      box.y = -$gateSize * 0.5;
      container.hitArea = box;
      return container;
    };

    Gate.prototype.createDSL = function() {};

    Gate.inputs = function(gates) {
      var gate, socket, sockets;
      if (gates) {
        sockets = [].concat.apply([], (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = gates.length; _i < _len; _i++) {
            gate = gates[_i];
            _results.push((function() {
              var _j, _len1, _ref, _results1;
              _ref = gate.inputSockets;
              _results1 = [];
              for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
                socket = _ref[_j];
                if (!socket.connectedToGate(gates)) {
                  _results1.push(socket);
                }
              }
              return _results1;
            })());
          }
          return _results;
        })());
      } else {
        sockets = $.grep(Socket.all, function(socket) {
          return socket.type === 'in' && !socket.connectedToGate(gates);
        });
      }
      return sockets;
    };

    Gate.outs = function(gates) {
      var gate, socket, sockets;
      if (gates) {
        sockets = [].concat.apply([], (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = gates.length; _i < _len; _i++) {
            gate = gates[_i];
            _results.push((function() {
              var _j, _len1, _ref, _results1;
              _ref = gate.outputSockets;
              _results1 = [];
              for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
                socket = _ref[_j];
                if (socket.isRealOutput()) {
                  _results1.push(socket);
                }
              }
              return _results1;
            })());
          }
          return _results;
        })());
      } else {
        sockets = $.grep(Socket.all, function(socket) {
          return socket.isRealOutput();
        });
      }
      return sockets;
    };

    Gate.createDSL = function() {
      var concatenate, gate, inputs, integratedCircuits, main, outs, s, socket, _i, _len, _ref;
      inputs = this.inputs();
      outs = this.outs();
      integratedCircuits = [];
      if (Gate.all == null) {
        return "circuit main()\nend";
      }
      concatenate = function(items) {
        return [].concat.apply([], items);
      };
      main = "circuit main(" + (((function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = inputs.length; _i < _len; _i++) {
          s = inputs[_i];
          _results.push(s.name);
        }
        return _results;
      })()).unique().join(', ')) + ")\n";
      _ref = this.all;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        gate = _ref[_i];
        if (gate instanceof IntegratedCircuit) {
          integratedCircuits.push(gate.name);
          integratedCircuits.push(concatenate(IntegratedDSL.getDependencies(gate.name)));
        }
        main += '  ' + gate.createDSL() + '\n';
      }
      main += '  out ' + ((function() {
        var _j, _len1, _results;
        _results = [];
        for (_j = 0, _len1 = outs.length; _j < _len1; _j++) {
          socket = outs[_j];
          _results.push(socket.name);
        }
        return _results;
      })()).join(', ') + '\n';
      main += 'end';
      integratedCircuits = concatenate(integratedCircuits).reverse().unique().map(function(ic) {
        return IntegratedDSL.getDSL(ic);
      }).join("\n\n");
      return integratedCircuits + (integratedCircuits !== "" ? "\n\n" : "") + main;
    };

    Gate.displayName = 'GATE';

    Gate.bitmap = "";

    Gate.types = [];

    return Gate;

  })(Device);

  AndGate = (function(_super) {

    __extends(AndGate, _super);

    function AndGate() {
      AndGate.__super__.constructor.call(this, 2, 1);
    }

    AndGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " * " + in2.name;
    };

    AndGate.displayName = 'AND';

    AndGate.bitmap = '/assets/images/and_gate.png';

    return AndGate;

  })(Gate);

  OrGate = (function(_super) {

    __extends(OrGate, _super);

    function OrGate() {
      OrGate.__super__.constructor.call(this, 2, 1);
    }

    OrGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " + " + in2.name;
    };

    OrGate.displayName = 'OR';

    OrGate.bitmap = '/assets/images/or_gate.png';

    return OrGate;

  })(Gate);

  NotGate = (function(_super) {

    __extends(NotGate, _super);

    function NotGate() {
      NotGate.__super__.constructor.call(this, 1, 1);
    }

    NotGate.prototype.createDSL = function() {
      var in1, out;
      in1 = this.inputSockets[0];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + "'";
    };

    NotGate.displayName = 'NOT';

    NotGate.bitmap = '/assets/images/not_gate.png';

    return NotGate;

  })(Gate);

  XorGate = (function(_super) {

    __extends(XorGate, _super);

    function XorGate() {
      XorGate.__super__.constructor.call(this, 2, 1);
    }

    XorGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " ^ " + in2.name;
    };

    XorGate.displayName = 'XOR';

    XorGate.bitmap = '/assets/images/xor_gate.png';

    return XorGate;

  })(Gate);

  NandGate = (function(_super) {

    __extends(NandGate, _super);

    function NandGate() {
      NandGate.__super__.constructor.call(this, 2, 1);
    }

    NandGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " nand " + in2.name;
    };

    NandGate.displayName = 'NAND';

    NandGate.bitmap = '/assets/images/nand_gate.png';

    return NandGate;

  })(Gate);

  NorGate = (function(_super) {

    __extends(NorGate, _super);

    function NorGate() {
      NorGate.__super__.constructor.call(this, 2, 1);
    }

    NorGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " nor " + in2.name;
    };

    NorGate.displayName = 'NOR';

    NorGate.bitmap = '/assets/images/nor_gate.png';

    return NorGate;

  })(Gate);

  XnorGate = (function(_super) {

    __extends(XnorGate, _super);

    function XnorGate() {
      XnorGate.__super__.constructor.call(this, 2, 1);
    }

    XnorGate.prototype.createDSL = function() {
      var in1, in2, out;
      in1 = this.inputSockets[0];
      in2 = this.inputSockets[1];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name + " xnor " + in2.name;
    };

    XnorGate.displayName = 'XNOR';

    XnorGate.bitmap = '/assets/images/xnor_gate.png';

    return XnorGate;

  })(Gate);

  BufferGate = (function(_super) {

    __extends(BufferGate, _super);

    function BufferGate() {
      BufferGate.__super__.constructor.call(this, 1, 1);
    }

    BufferGate.prototype.createDSL = function() {
      var in1, out;
      in1 = this.inputSockets[0];
      out = this.outputSockets[0];
      return "" + out.name + " = " + in1.name;
    };

    BufferGate.displayName = 'BUFF';

    BufferGate.bitmap = '/assets/images/buffer_gate.png';

    return BufferGate;

  })(Gate);

  Gate.registerType(AndGate);

  Gate.registerType(OrGate);

  Gate.registerType(NotGate);

  Gate.registerType(XorGate);

  Gate.registerType(NandGate);

  Gate.registerType(NorGate);

  Gate.registerType(XnorGate);

  Gate.registerType(BufferGate);

}).call(this);
