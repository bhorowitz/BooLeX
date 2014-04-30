(function() {
  var distance, distanceSquared, initBoolexStage, startBoolex, stopBoolex;

  Array.prototype.unique = function() {
    var key, res, seen, _i, _ref;
    seen = {};
    res = [];
    for (key = _i = 0, _ref = this.length; 0 <= _ref ? _i < _ref : _i > _ref; key = 0 <= _ref ? ++_i : --_i) {
      if (!(this[key] in seen)) {
        res.push(this[key]);
      }
      seen[this[key]] = true;
    }
    return res;
  };

  $(document).ready(function() {
    window.canvas = $('#boolex-stage');
    window.$stageWidth = canvas[0].width = canvas.parent().innerWidth();
    window.$stageHeight = canvas[0].height = $(window).innerHeight();
    canvas.mousedown(function(e) {
      return e.preventDefault();
    });
    return initBoolexStage();
  });

  initBoolexStage = function() {
    var dslFactory, selectBox, stageArea, t, toolbox;
    window.boolexStage = new createjs.Stage("boolex-stage");
    selectBox = null;
    stageArea = new createjs.Shape();
    stageArea.graphics.beginFill('rgba(255,255,255,0.01)').drawRect(0, 0, canvas[0].width, canvas[0].height);
    boolexStage.addChild(stageArea);
    toolbox = new Toolbox();
    boolexStage.dragged = null;
    boolexStage.on('mousedown', function(e) {
      if (boolexStage.getObjectUnderPoint(e.stageX, e.stageY).id === stageArea.id) {
        Device.deselectAll();
        selectBox = new SelectBox(e.stageX, e.stageY);
        return boolexStage.addChild(selectBox.box);
      }
    });
    boolexStage.on('stagemousemove', function(e) {
      if (boolexStage.dragged != null) {
        return boolexStage.dragged.drag(e);
      }
    });
    boolexStage.on('stagemouseup', function(e) {
      if (boolexStage.dragged && boolexStage.dragged.stopDrag) {
        boolexStage.dragged.stopDrag(e);
      }
      return boolexStage.dragged = null;
    });
    boolexStage.on('pressmove', function(e) {
      if (selectBox != null) {
        selectBox.endX = e.stageX;
        selectBox.endY = e.stageY;
        selectBox.draw();
        return selectBox.selectDevicesUnder();
      }
    });
    boolexStage.on('pressup', function(e) {
      if (selectBox != null) {
        boolexStage.removeChild(selectBox.box);
        return selectBox = null;
      }
    });
    createjs.Ticker.on('tick', function() {
      return boolexStage.update();
    });
    t = setInterval(function() {
      var clock, _i, _len;
      $(window).trigger('refreshDSL');
      if ($openConnection) {
        $openConnection.send(JSON.stringify({
          command: 'heartbeat'
        }));
        for (_i = 0, _len = $clocks.length; _i < _len; _i++) {
          clock = $clocks[_i];
          if (window.numTicks % clock.type === 0) {
            if (Socket.states[clock.outSocket.name] === 'on') {
              Socket.states[clock.outSocket.name] = 'off';
            } else {
              Socket.states[clock.outSocket.name] = 'on';
            }
            $(window).trigger('update', [false, clock.outSocket]);
          }
        }
        return window.numTicks++;
      }
    }, 1500);
    $(window).on('refreshDSL', function() {
      var dsl;
      dsl = Gate.createDSL();
      dsl = syntaxColor(dsl);
      return $('pre#code').html(dsl);
    });
    $(document).keydown(function(e) {
      var active, device, id;
      active = $(document.activeElement);
      if (!active.is('input') && !active.is('textarea') && (e.which === 46 || e.which === 8)) {
        for (id in $selectedDevices) {
          device = $selectedDevices[id];
          device.destroy();
        }
        window.$selectedDevices = {};
        return false;
      }
    });
    $('#start-stop-button').click(function() {
      var $this;
      $this = $(this);
      if ($this.data('running') != null) {
        stopBoolex();
        $(window).trigger('update');
        $this.removeClass('btn-danger').addClass('btn-primary').text('Start');
        return $this.data('running', null);
      } else {
        window.numTicks = 0;
        startBoolex();
        $this.removeClass('btn-primary').addClass('btn-danger').text('Stop');
        return $this.data('running', true);
      }
    });
    $('#integrated-circuit-button').click(function() {
      var device, gates, ic, id;
      gates = [];
      for (id in $selectedDevices) {
        device = $selectedDevices[id];
        if (device instanceof Gate) {
          gates.push(device);
        }
      }
      window.$selectedDevices = {};
      ic = new IntegratedCircuit(gates);
      boolexStage.addChild(ic.graphics);
      return $(window).trigger('update');
    });
    $('#load-circuit-button').click(function() {
      return $('#load-circuit-modal').modal('show');
    });
    $('#insert-circuit-button').click(function() {
      var dsl, ic;
      $('#load-circuit-modal').modal('hide');
      dsl = $('#circuit-dsl').val();
      ic = new IntegratedCircuit(dsl);
      boolexStage.addChild(ic.graphics);
      return $(window).trigger('update');
    });
    $('#new-button').click(function() {
      var device, _i, _len, _results;
      _results = [];
      for (_i = 0, _len = $allDevices.length; _i < _len; _i++) {
        device = $allDevices[_i];
        _results.push(device.destroy());
      }
      return _results;
    });
    $('#tutorial-button').click(function() {
      return startTutorial();
    });
    $('#next-button').click(function() {
      return nextSlide();
    });
    $('#previous-button').click(function() {
      return previousSlide();
    });
    $('#cancel-button').click(function() {
      return stopTutorial();
    });
    dslFactory = new DSLFactory();
    $('#insert-rom').click(function() {
      var dsl, ic, lines;
      $('#rom-modal').modal('hide');
      lines = $('#rom-rows').val().replace(/0/g, 'f').replace(/1/g, 't').split("\n");
      dsl = dslFactory.generateROM(lines);
      console.log(dsl);
      if (!dsl) {
        alert("Invalid ROM size!");
        return;
      }
      ic = new IntegratedCircuit(dsl);
      boolexStage.addChild(ic.graphics);
      return $(window).trigger('update');
    });
    $('#insert-decoder').click(function() {
      var dsl, ic, n;
      $('#decoder-modal').modal('hide');
      n = parseInt($('#decoder-n').val());
      dsl = dslFactory.generateDecoder(n);
      if (!dsl) {
        alert("Invalid decoder size!");
        return;
      }
      ic = new IntegratedCircuit(dsl);
      boolexStage.addChild(ic.graphics);
      return $(window).trigger('update');
    });
    $('.load-premade-circuit').click(function() {
      var $this, circuit;
      $this = $(this);
      circuit = $this.data('circuit');
      if (circuit === 'rom') {
        return $('#rom-modal').modal('show');
      } else if (circuit === 'encoder') {
        return $('#decoder-modal').modal('show');
      } else if (circuit === 'decoder') {
        return $('#decoder-modal').modal('show');
      }
    });
    $('#toggle-gate-delay').click(function() {
      window.$isImmediate = !$(this).hasClass('active');
      if ($isImmediate) {
        return $(this).find('span').text('On');
      } else {
        return $(this).find('span').text('Off');
      }
    });
    return $(window).bind('update', function(e, isManual, socket) {
      var device, devices, wire, _i, _j, _len, _len1, _ref;
      if (isManual == null) {
        isManual = true;
      }
      if (socket == null) {
        socket = null;
      }
      devices = IODevice.all || [];
      for (_i = 0, _len = devices.length; _i < _len; _i++) {
        device = devices[_i];
        device.draw();
      }
      _ref = Wire.all || [];
      for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
        wire = _ref[_j];
        wire.redraw();
      }
      if ((typeof $openConnection !== "undefined" && $openConnection !== null) && (socket != null)) {
        console.log(socket);
        console.log("Sending: " + JSON.stringify({
          command: 'update',
          socket: {
            name: socket.name,
            value: Socket.states[socket.name] === 'on'
          }
        }));
        return $openConnection.send(JSON.stringify({
          command: 'update',
          socket: {
            name: socket.name,
            value: Socket.states[socket.name] === 'on'
          }
        }));
      }
    });
  };

  distanceSquared = function(x1, y1, x2, y2) {
    var dx, dy;
    dx = x2 - x1;
    dy = y2 - y1;
    return dx * dx + dy * dy;
  };

  distance = function(x1, y1, x2, y2) {
    return Math.sqrt(distanceSquared(x1, y1, x2, y2));
  };

  window.cometMessage = function(message) {
    return console.log("Event received: " + message);
  };

  window.echo = function(message) {
    return $('#echoer').attr('src', "/echo/" + (encodeURIComponent(message)));
  };

  startBoolex = function() {
    var $openConnection, dsl;
    dsl = Gate.createDSL();
    $openConnection = new WebSocket("ws://" + location.host + "/boolex", ['soap', 'xmpp']);
    $openConnection.onopen = function(msg) {
      var socket;
      $openConnection.send(JSON.stringify({
        command: 'initialize',
        dsl: dsl,
        gateDelay: !$isImmediate
      }));
      return $openConnection.send(JSON.stringify({
        command: 'start',
        initialValues: (function() {
          var _i, _len, _ref, _results;
          _ref = Gate.inputs();
          _results = [];
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            socket = _ref[_i];
            _results.push({
              name: socket.name,
              value: Socket.states[socket.name] === 'on'
            });
          }
          return _results;
        })()
      }));
    };
    $openConnection.onerror = function(error) {
      return console.log('WebSocket Error ' + error);
    };
    return $openConnection.onmessage = function(e) {
      var res;
      console.log("Received: " + e.data);
      res = JSON.parse(e.data);
      if (res.command === 'update') {
        Socket.states[res.socket.name] = res.socket.value === 'false' ? 'off' : 'on';
      }
      return $(window).trigger('update', false);
    };
  };

  stopBoolex = function() {
    var $openConnection;
    $openConnection.close();
    return $openConnection = null;
  };

}).call(this);
