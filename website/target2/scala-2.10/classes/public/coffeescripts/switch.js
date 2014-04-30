(function() {
  var Switch,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Switch = (function(_super) {

    __extends(Switch, _super);

    function Switch() {
      Switch.__super__.constructor.call(this, 0, 1);
      this.outSocket = this.outputSockets[0];
    }

    Switch.prototype.click = function() {
      if (Socket.states[this.outSocket.name] === 'on') {
        Socket.states[this.outSocket.name] = 'off';
      } else {
        Socket.states[this.outSocket.name] = 'on';
      }
      return $(window).trigger('update', [true, this.outSocket]);
    };

    Switch.createGraphics = function(device) {
      var box, container, switchWell, switcher;
      container = new createjs.Container();
      switchWell = new createjs.Shape();
      switchWell.graphics.setStrokeStyle(2);
      switchWell.graphics.beginStroke('#000000');
      switchWell.graphics.drawRoundRect(-10, -5, 20, 10, 10);
      switcher = new createjs.Shape();
      switcher.graphics.beginFill('#000000');
      switcher.graphics.drawCircle(0, 0, 3);
      container.addChild(switchWell);
      container.addChild(switcher);
      if (device != null) {
        device.switcher = switcher;
      }
      box = new createjs.Shape();
      box.graphics.beginFill(createjs.Graphics.getRGB(255, 0, 0));
      box.graphics.rect(0, 0, $gateSize, $gateSize);
      box.x = -$gateSize * 0.5;
      box.y = -$gateSize * 0.5;
      container.hitArea = box;
      return container;
    };

    Switch.prototype.draw = function() {
      this.outSocket || (this.outSocket = this.outputSockets[0]);
      if (Socket.states[this.outSocket.name] === 'on') {
        return this.switcher.x = 5;
      } else {
        return this.switcher.x = -5;
      }
    };

    Switch.displayName = 'SWITCH';

    return Switch;

  })(IODevice);

  IODevice.registerType(Switch);

}).call(this);
