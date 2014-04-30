(function() {
  var Device,
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; },
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Device = (function(_super) {
    var self;

    __extends(Device, _super);

    self = Device;

    Device.height = $gateSize;

    Device.width = $gateSize;

    function Device(numIns, numOuts, klass) {
      this.numIns = numIns;
      this.numOuts = numOuts;
      this.drag = __bind(this.drag, this);

      this.startDrag = __bind(this.startDrag, this);

      this.initSockets();
      this.initGraphics();
      this.initEvents();
      this.dragged = false;
      $allDevices.push(this);
      Device.__super__.constructor.call(this, klass);
      $(window).trigger('refreshDSL');
    }

    Device.prototype.initSockets = function() {
      var i;
      this.inputSockets = this.outputSockets = [];
      if (this.numIns > 0) {
        this.inputSockets = (function() {
          var _i, _ref, _results;
          _results = [];
          for (i = _i = 1, _ref = this.numIns; 1 <= _ref ? _i <= _ref : _i >= _ref; i = 1 <= _ref ? ++_i : --_i) {
            _results.push(new Socket(this, i, 'in'));
          }
          return _results;
        }).call(this);
      }
      if (this.numOuts > 0) {
        return this.outputSockets = (function() {
          var _i, _ref, _results;
          _results = [];
          for (i = _i = 1, _ref = this.numOuts; 1 <= _ref ? _i <= _ref : _i >= _ref; i = 1 <= _ref ? ++_i : --_i) {
            _results.push(new Socket(this, i, 'out'));
          }
          return _results;
        }).call(this);
      }
    };

    Device.prototype.initGraphics = function() {
      this.graphics = new createjs.Container();
      this.box = this.constructor.createGraphics(this);
      this.selected = new createjs.Shape();
      this.selected.graphics.beginFill('rgba(0, 0, 255, 0.05)');
      this.selected.graphics.beginStroke('rgba(0, 0, 255, 0.3)');
      this.selected.graphics.drawRect(0, 0, $gateSize + 20, $gateSize + 20);
      this.selected.x = this.selected.y = -($gateSize * 0.5 + 10);
      this.selected.visible = false;
      this.graphics.addChild(this.selected);
      this.graphics.addChild(this.box);
      this.sockets = this.drawSockets();
      return window.boolexStage.addChild(this.graphics);
    };

    Device.prototype.drawSockets = function() {
      var i, socket, _i, _j, _len, _len1, _ref, _ref1, _results;
      _ref = this.inputSockets;
      for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
        socket = _ref[i];
        socket.graphics.x = -$socketPadding * (this.constructor.width / $gateSize);
        socket.graphics.y = (this.constructor.height / (this.numIns + 1)) * (i + 1) - this.constructor.height * 0.5;
        this.graphics.addChild(socket.graphics);
      }
      _ref1 = this.outputSockets;
      _results = [];
      for (i = _j = 0, _len1 = _ref1.length; _j < _len1; i = ++_j) {
        socket = _ref1[i];
        socket.graphics.x = $socketPadding * (this.constructor.width / $gateSize);
        socket.graphics.y = (this.constructor.height / (this.numOuts + 1)) * (i + 1) - this.constructor.height * 0.5;
        _results.push(this.graphics.addChild(socket.graphics));
      }
      return _results;
    };

    Device.prototype.initEvents = function() {
      var _this = this;
      this.box.on("mousedown", function(e) {
        _this.dragged = false;
        return _this.graphics.offset = {
          x: _this.graphics.x - e.stageX,
          y: _this.graphics.y - e.stageY
        };
      });
      this.box.on("pressmove", this.drag);
      return this.box.on("click", function(e) {
        if (!_this.dragged) {
          console.log(_this.id);
          return _this.click();
        }
      });
    };

    Device.prototype.inputDevices = function() {
      var inputSocket;
      return [
        (function() {
          var _i, _len, _ref, _results;
          if (inputSocket.wires.length) {
            _ref = this.inputSockets;
            _results = [];
            for (_i = 0, _len = _ref.length; _i < _len; _i++) {
              inputSocket = _ref[_i];
              _results.push(inputSocket.wires[0].fromSocket);
            }
            return _results;
          }
        }).call(this)
      ];
    };

    Device.prototype.startDrag = function(e) {
      this.graphics.offset = {
        x: this.graphics.x - e.stageX,
        y: this.graphics.y - e.stageY
      };
      return boolexStage.dragged = this;
    };

    Device.prototype.drag = function(e) {
      var socket, wire, _i, _len, _ref, _results;
      this.dragged = true;
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
            wire = _ref1[_j];
            _results1.push(wire.redraw());
          }
          return _results1;
        })());
      }
      return _results;
    };

    Device.prototype.select = function() {
      $selectedDevices[this.id] = this;
      return this.selected.visible = true;
    };

    Device.prototype.deselect = function() {
      if ($selectedDevices[this.id]) {
        delete $selectedDevices[this.id];
      }
      return this.selected.visible = false;
    };

    Device.prototype.click = function() {
      Device.deselectAll();
      return this.select();
    };

    Device.prototype.destroy = function(keepSockets) {
      var socket, _i, _len, _ref,
        _this = this;
      if (keepSockets == null) {
        keepSockets = false;
      }
      if (!keepSockets) {
        _ref = this.inputSockets.concat(this.outputSockets);
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          socket = _ref[_i];
          socket.destroy();
        }
      }
      if (this.graphics.parent) {
        this.graphics.parent.removeChild(this.graphics);
      }
      window.$allDevices = $.grep($allDevices, function(device) {
        return device.id !== _this.id;
      });
      $(window).trigger('refreshDSL');
      return Device.__super__.destroy.call(this);
    };

    Device.createGraphics = function(device) {
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

    Device.deselectAll = function() {
      var device, _i, _len, _results;
      _results = [];
      for (_i = 0, _len = $allDevices.length; _i < _len; _i++) {
        device = $allDevices[_i];
        _results.push(device.deselect());
      }
      return _results;
    };

    Device.prototype.x = function() {
      return this.graphics.x;
    };

    Device.prototype.y = function() {
      return this.graphics.y;
    };

    return Device;

  })(Collectable);

}).call(this);
