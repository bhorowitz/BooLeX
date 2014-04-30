(function() {
  var Clock,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Clock = (function(_super) {

    __extends(Clock, _super);

    function Clock() {
      $clocks.push(this);
      Clock.__super__.constructor.call(this, 0, 1);
      this.outSocket = this.outputSockets[0];
      this.type = 1;
    }

    Clock.prototype.click = function() {
      var wantedType;
      wantedType = parseInt(prompt("Speed of clock (higher is slower):"));
      if (!isNaN(wantedType) && wantedType > 0) {
        return this.type = wantedType;
      }
    };

    Clock.createGraphics = function(device) {
      var box, clockSign, clockWell, container;
      container = new createjs.Container();
      clockWell = new createjs.Shape();
      clockWell.graphics.setStrokeStyle(2);
      clockWell.graphics.beginStroke('#000');
      clockWell.graphics.drawRect(-10, -10, 20, 20);
      clockSign = new createjs.Shape();
      clockSign.graphics.setStrokeStyle(1).beginStroke('#000');
      clockSign.graphics.moveTo(-5, -3);
      clockSign.graphics.lineTo(-5, 3);
      clockSign.graphics.lineTo(0, 3);
      clockSign.graphics.lineTo(0, -3);
      clockSign.graphics.lineTo(5, -3);
      clockSign.graphics.lineTo(5, 3);
      if (device) {
        device.clockSign = clockSign;
      }
      container.addChild(clockWell);
      container.addChild(clockSign);
      box = new createjs.Shape();
      box.graphics.beginFill(createjs.Graphics.getRGB(255, 0, 0));
      box.graphics.rect(0, 0, $gateSize, $gateSize);
      box.x = -$gateSize * 0.5;
      box.y = -$gateSize * 0.5;
      container.hitArea = box;
      return container;
    };

    Clock.prototype.draw = function() {
      if (this.outSocket && Socket.states[this.outSocket.name] === 'on' && $openConnection) {
        return this.clockSign.shadow = new createjs.Shadow("#00aaff", 0, 0, 5);
      } else {
        return this.clockSign.shadow = null;
      }
    };

    Clock.displayName = 'CLOCK';

    return Clock;

  })(IODevice);

  IODevice.registerType(Clock);

}).call(this);
