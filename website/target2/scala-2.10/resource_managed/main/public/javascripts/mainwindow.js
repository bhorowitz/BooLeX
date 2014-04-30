(function() {
  var $gateSize, $halfGateSize, $socketPadding, $socketSize, AndGate, Collectable, Gate, NotGate, OrGate, Socket, Tool, Toolbox, Wire, XorGate, distance, distanceSquared,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  $(document).ready(function() {
    var canvas, toolbox;
    canvas = $('#boolex-stage');
    canvas[0].width = canvas.parent().innerWidth();
    canvas[0].height = $(window).innerHeight();
    canvas.mousedown(function(e) {
      return e.preventDefault();
    });
    window.boolexStage = new createjs.Stage("boolex-stage");
    toolbox = new Toolbox();
    return createjs.Ticker.on('tick', function() {
      return window.boolexStage.update();
    });
  });

  $gateSize = 50;

  $halfGateSize = $gateSize / 2;

  $socketSize = 4;

  $socketPadding = $halfGateSize + 8;

  distanceSquared = function(x1, y1, x2, y2) {
    var dx, dy;
    dx = x2 - x1;
    dy = y2 - y1;
    return dx * dx + dy * dy;
  };

  distance = function(x1, y1, x2, y2) {
    return Math.sqrt(distanceSquared(x1, y1, x2, y2));
  };

  Collectable = (function() {

    function Collectable() {
      this.id = Collectable.createUuid();
      this.constructor.add(this);
    }

    Collectable.prototype.destroy = function() {
      return this.constructor.remove(this);
    };

    Collectable.add = function(obj) {
      if (this.all == null) {
        this.all = [];
        this.hash = {};
      }
      this.all.push(obj);
      return this.hash[obj.id] = obj;
    };

    Collectable.remove = function(obj) {
      if (this.all != null) {
        this.hash[obj.id] = null;
        return this.all = $.grep(this.all, function(obj2) {
          return obj2.id !== obj.id;
        });
      }
    };

    Collectable.find = function(id) {
      return this.hash[id];
    };

    Collectable.createUuid = function() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r, v;
        r = Math.random() * 16 | 0;
        v = c === 'x' ? r : r & 0x3 | 0x8;
        return v.toString(16);
      });
    };

    return Collectable;

  })();

  Tool = (function() {

    function Tool(gateClass, index) {
      this.gateClass = gateClass;
      this.index = index;
      this.graphics = new createjs.Container();
      this.graphics.addChild(gateClass.createGraphics());
      this.graphics.y = this.index * ($gateSize + Toolbox.padding);
      this.graphics.on('mousedown', function(e) {
        var newGate, _ref;
        newGate = new gateClass();
        _ref = (function(coord) {
          return [coord.x, coord.y];
        })(this.localToGlobal(0, 0)), newGate.graphics.x = _ref[0], newGate.graphics.y = _ref[1];
        return newGate.startDrag(e);
      });
    }

    return Tool;

  })();

  Toolbox = (function() {

    function Toolbox() {
      var gate, i, tool, _i, _len, _ref;
      this.gates = Gate.types;
      this.tools = (function() {
        var _i, _len, _ref, _results;
        _ref = this.gates;
        _results = [];
        for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
          gate = _ref[i];
          _results.push(new Tool(gate, i));
        }
        return _results;
      }).call(this);
      this.graphics = new createjs.Container();
      _ref = this.tools;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        tool = _ref[_i];
        this.graphics.addChild(tool.graphics);
      }
      this.graphics.x = $halfGateSize;
      this.graphics.y = $halfGateSize;
      window.boolexStage.addChild(this.graphics);
      window.boolexStage.update();
    }

    Toolbox.padding = 5;

    return Toolbox;

  })();

  Wire = (function(_super) {

    __extends(Wire, _super);

    function Wire(socket1, socket2) {
      this.socket1 = socket1;
      this.socket2 = socket2;
      this.stopDrag = __bind(this.stopDrag, this);

      this.drag = __bind(this.drag, this);

      this.startDrag = __bind(this.startDrag, this);

      this.initGraphics();
      this.target = null;
      Wire.__super__.constructor.call(this);
      if (this.socket1 != null) {
        this.socket1.wires.push(this.id);
      }
      if (this.socket2 != null) {
        this.socket2.wires.push(this.id);
      }
    }

    Wire.prototype.initGraphics = function() {
      this.graphics = new createjs.Container();
      this.line = new createjs.Shape();
      this.graphics.addChild(this.line);
      return window.boolexStage.addChild(this.graphics);
    };

    Wire.prototype.startDrag = function(e) {
      var socket1Pos;
      this.line.graphics.clear();
      this.line.graphics.beginStroke('black');
      socket1Pos = this.socket1.graphics.localToGlobal(0, 0);
      this.line.graphics.moveTo(socket1Pos.x, socket1Pos.y);
      this.line.graphics.lineTo(e.stageX, e.stageY);
      e.on('mousemove', this.drag);
      return e.on('mouseup', this.stopDrag);
    };

    Wire.prototype.drag = function(e) {
      var socket, socket1Pos, _i, _len, _ref, _results;
      this.line.graphics.clear();
      this.line.graphics.beginStroke('black');
      socket1Pos = this.socket1.graphics.localToGlobal(0, 0);
      this.line.graphics.moveTo(socket1Pos.x, socket1Pos.y);
      if (this.target != null) {
        this.line.graphics.lineTo(this.target.x(), this.target.y());
      } else {
        this.line.graphics.lineTo(e.stageX, e.stageY);
      }
      this.target = null;
      _ref = Socket.all;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        socket = _ref[_i];
        if (distance(socket.x(), socket.y(), e.stageX, e.stageY) < $socketSize * 2) {
          this.target = socket;
          break;
        } else {
          _results.push(void 0);
        }
      }
      return _results;
    };

    Wire.prototype.stopDrag = function(e) {
      if (this.target) {
        this.socket2 = this.target;
        return this.socket2.wires.push(this.id);
      } else {
        return this.destroy();
      }
    };

    Wire.prototype.redraw = function() {
      var socket1Pos, socket2Pos;
      this.line.graphics.clear();
      this.line.graphics.beginStroke('black');
      socket1Pos = this.socket1.graphics.localToGlobal(0, 0);
      socket2Pos = this.socket2.graphics.localToGlobal(0, 0);
      this.line.graphics.moveTo(socket1Pos.x, socket1Pos.y);
      return this.line.graphics.lineTo(socket2Pos.x, socket2Pos.y);
    };

    Wire.prototype.destroy = function() {
      window.boolexStage.removeChild(this.graphics);
      if (this.socket1 != null) {
        this.socket1.wires = $.grep(this.socket1.wires, function(id) {
          return id !== this.id;
        });
      }
      if (this.socket2 != null) {
        this.socket2.wires = $.grep(this.socket2.wires, function(id) {
          return id !== this.id;
        });
      }
      return Wire.__super__.destroy.call(this);
    };

    return Wire;

  })(Collectable);

  Socket = (function(_super) {

    __extends(Socket, _super);

    function Socket(gate, index, type) {
      this.gate = gate;
      this.index = index;
      this.type = type;
      this.initGraphics();
      this.initEvents();
      this.wires = [];
      Socket.__super__.constructor.call(this);
    }

    Socket.prototype.initGraphics = function() {
      this.graphics = new createjs.Container();
      this.circle = new createjs.Shape();
      this.circle.graphics.beginFill('black').drawCircle(0, 0, $socketSize);
      this.circle.graphics.beginFill('white').drawCircle(0, 0, $socketSize - 2);
      return this.graphics.addChild(this.circle);
    };

    Socket.prototype.initEvents = function() {
      var _this = this;
      return this.graphics.on('mousedown', function(e) {
        var wire;
        wire = new Wire(_this, null);
        return wire.startDrag(e);
      });
    };

    Socket.prototype.x = function() {
      return this.graphics.localToGlobal(0, 0).x;
    };

    Socket.prototype.y = function() {
      return this.graphics.localToGlobal(0, 0).y;
    };

    return Socket;

  })(Collectable);

  Gate = (function(_super) {

    __extends(Gate, _super);

    function Gate(numIns, numOuts) {
      this.numIns = numIns;
      this.numOuts = numOuts;
      this.drag = __bind(this.drag, this);

      this.startDrag = __bind(this.startDrag, this);

      this.initSockets();
      this.initGraphics();
      this.initEvents();
      Gate.__super__.constructor.call(this);
    }

    Gate.prototype.initSockets = function() {
      var i;
      this.inputSockets = (function() {
        var _i, _ref, _results;
        _results = [];
        for (i = _i = 1, _ref = this.numIns; 1 <= _ref ? _i <= _ref : _i >= _ref; i = 1 <= _ref ? ++_i : --_i) {
          _results.push(new Socket(this, i, 'in'));
        }
        return _results;
      }).call(this);
      return this.outputSockets = (function() {
        var _i, _ref, _results;
        _results = [];
        for (i = _i = 1, _ref = this.numOuts; 1 <= _ref ? _i <= _ref : _i >= _ref; i = 1 <= _ref ? ++_i : --_i) {
          _results.push(new Socket(this, i, 'out'));
        }
        return _results;
      }).call(this);
    };

    Gate.prototype.initGraphics = function() {
      var i, socket, _i, _j, _len, _len1, _ref, _ref1;
      this.graphics = new createjs.Container();
      this.box = this.constructor.createGraphics();
      this.graphics.addChild(this.box);
      _ref = this.inputSockets;
      for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
        socket = _ref[i];
        socket.graphics.x = -$socketPadding;
        socket.graphics.y = ($gateSize / (this.numIns + 1)) * (i + 1) - $halfGateSize;
        this.graphics.addChild(socket.graphics);
      }
      _ref1 = this.outputSockets;
      for (i = _j = 0, _len1 = _ref1.length; _j < _len1; i = ++_j) {
        socket = _ref1[i];
        socket.graphics.x = $socketPadding;
        socket.graphics.y = ($gateSize / (this.numOuts + 1)) * (i + 1) - $halfGateSize;
        this.graphics.addChild(socket.graphics);
      }
      return window.boolexStage.addChild(this.graphics);
    };

    Gate.prototype.initEvents = function() {
      var self;
      self = this;
      this.box.on("mousedown", function(e) {
        return this.offset = {
          x: self.graphics.x - e.stageX,
          y: self.graphics.y - e.stageY
        };
      });
      return this.box.on("pressmove", this.drag);
    };

    Gate.prototype.startDrag = function(e) {
      this.graphics.offset = {
        x: this.graphics.x - e.stageX,
        y: this.graphics.y - e.stageY
      };
      return e.on('mousemove', this.drag);
    };

    Gate.prototype.drag = function(e) {
      var socket, wireId, _i, _len, _ref, _results;
      this.graphics.x = e.stageX + this.graphics.offset.x;
      this.graphics.y = e.stageY + this.graphics.offset.y;
      _ref = this.inputSockets.concat(this.outputSockets);
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        socket = _ref[_i];
        _results.push((function() {
          var _j, _len1, _ref1, _results1;
          _ref1 = socket.wires;
          _results1 = [];
          for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
            wireId = _ref1[_j];
            _results1.push(Wire.find(wireId).redraw());
          }
          return _results1;
        })());
      }
      return _results;
    };

    Gate.createGraphics = function() {
      var bounds, box, container, text;
      container = new createjs.Container();
      box = new createjs.Shape();
      box.graphics.beginFill('black').drawRect(0, 0, $gateSize, $gateSize);
      box.graphics.beginFill('white').drawRect(2, 2, $gateSize - 4, $gateSize - 4);
      box.x = box.y = -$halfGateSize;
      container.addChild(box);
      text = new createjs.Text(this.displayName, '14px Helvetica');
      bounds = text.getBounds();
      text.x = -bounds.width / 2;
      text.y = -bounds.height / 2;
      container.addChild(text);
      return container;
    };

    Gate.displayName = 'GATE';

    Gate.bitmap = "";

    Gate.types = [];

    Gate.registerType = function(gateClass) {
      return this.types.push(gateClass);
    };

    return Gate;

  })(Collectable);

  AndGate = (function(_super) {

    __extends(AndGate, _super);

    function AndGate() {
      AndGate.__super__.constructor.call(this, 2, 1);
    }

    AndGate.displayName = 'AND';

    AndGate.bitmap = '/assets/images/and_gate.png';

    return AndGate;

  })(Gate);

  OrGate = (function(_super) {

    __extends(OrGate, _super);

    function OrGate() {
      OrGate.__super__.constructor.call(this, 2, 1);
    }

    OrGate.displayName = 'OR';

    return OrGate;

  })(Gate);

  NotGate = (function(_super) {

    __extends(NotGate, _super);

    function NotGate() {
      NotGate.__super__.constructor.call(this, 1, 1);
    }

    NotGate.displayName = 'NOT';

    return NotGate;

  })(Gate);

  XorGate = (function(_super) {

    __extends(XorGate, _super);

    function XorGate() {
      XorGate.__super__.constructor.call(this, 2, 1);
    }

    XorGate.displayName = 'XOR';

    return XorGate;

  })(Gate);

  
  $.window({
   title: "Cyclops Studio",
   url: "http://apps.fstoke.me/"
});;


  Gate.registerType(AndGate);

  Gate.registerType(OrGate);

  Gate.registerType(NotGate);

  Gate.registerType(XorGate);

}).call(this);
