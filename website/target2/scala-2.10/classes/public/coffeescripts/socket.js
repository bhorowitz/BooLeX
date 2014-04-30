(function() {
  var Socket,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Socket = (function(_super) {

    __extends(Socket, _super);

    function Socket(device, index, type, name) {
      this.device = device;
      this.index = index;
      this.type = type;
      this.name = name;
      this.initGraphics();
      this.initEvents();
      this.wires = [];
      this.name || (this.name = Socket.makeName());
      Socket.states[this.name] = 'off';
      Socket.__super__.constructor.call(this);
    }

    Socket.prototype.initGraphics = function() {
      this.graphics = new createjs.Container();
      this.circle = new createjs.Shape();
      this.circle.graphics.beginFill('black').drawCircle(0, 0, $socketSize);
      this.box = new createjs.Shape();
      this.box.graphics.beginFill('black').drawRect(-$socketSize, -$socketSize, $socketSize * 2, $socketSize * 2);
      this.graphics.hitArea = this.box;
      return this.graphics.addChild(this.circle);
    };

    Socket.prototype.initEvents = function() {
      var _this = this;
      return this.graphics.on('mousedown', function(e) {
        var wire;
        if (_this.type === 'out') {
          wire = new Wire(_this, null);
        } else {
          wire = new Wire(null, _this);
        }
        return wire.startDrag(e);
      });
    };

    Socket.prototype.connectedSockets = function() {
      var getter, wire, _i, _len, _ref, _results;
      getter = this.type === 'in' ? 'fromSocket' : 'toSocket';
      _ref = this.wires;
      _results = [];
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        wire = _ref[_i];
        if (wire[getter] != null) {
          _results.push(wire[getter]);
        }
      }
      return _results;
    };

    Socket.prototype.connectedToGate = function(gates) {
      var gate, gate_ids, socket, _i, _len, _ref;
      if (gates) {
        gate_ids = (function() {
          var _i, _len, _results;
          _results = [];
          for (_i = 0, _len = gates.length; _i < _len; _i++) {
            gate = gates[_i];
            _results.push(gate.id);
          }
          return _results;
        })();
      }
      _ref = this.connectedSockets();
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        socket = _ref[_i];
        if (socket.device instanceof Gate) {
          if (!gates || $.inArray(socket.device.id, gate_ids) >= 0) {
            return true;
          }
        }
      }
      return false;
    };

    Socket.prototype.connectedToLightbulb = function() {
      var socket, _i, _len, _ref;
      _ref = this.connectedSockets();
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        socket = _ref[_i];
        if (socket.device instanceof Lightbulb) {
          return true;
        }
      }
      return false;
    };

    Socket.prototype.x = function() {
      return this.graphics.localToGlobal(0, 0).x;
    };

    Socket.prototype.y = function() {
      return this.graphics.localToGlobal(0, 0).y;
    };

    Socket.prototype.destroy = function() {
      var wire, _i, _len, _ref;
      _ref = this.wires;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        wire = _ref[_i];
        wire.destroy();
      }
      return Socket.remove(this);
    };

    Socket.prototype.isRealOutput = function(gates) {
      return this.type === 'out' && (!this.connectedToGate(gates) || this.connectedToLightbulb());
    };

    Socket.makeName = function() {
      var name;
      name = "t" + this.nameCounter;
      this.nameCounter++;
      return name;
    };

    Socket.connect = function(fromSocket, toSocket, wire) {
      fromSocket.wires.push(wire);
      toSocket.wires.push(wire);
      return toSocket.name = fromSocket.name;
    };

    Socket.states = {};

    Socket.nameCounter = 0;

    return Socket;

  })(Collectable);

}).call(this);
