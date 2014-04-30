(function() {
  var Lightbulb,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Lightbulb = (function(_super) {

    __extends(Lightbulb, _super);

    function Lightbulb() {
      Lightbulb.__super__.constructor.call(this, 1, 0);
    }

    Lightbulb.createGraphics = function(device) {
      var bitmapOff, bitmapOn, box, container;
      container = new createjs.Container();
      bitmapOff = new createjs.Bitmap(this.bitmapOffPath);
      bitmapOff.x = -$gateSize * 0.5;
      bitmapOff.y = -$gateSize * 0.5;
      bitmapOn = new createjs.Bitmap(this.bitmapOnPath);
      bitmapOn.x = -$gateSize * 0.5;
      bitmapOn.y = -$gateSize * 0.5;
      bitmapOn.visible = false;
      container.addChild(bitmapOff);
      container.addChild(bitmapOn);
      if (device != null) {
        device.bitmapOff = bitmapOff;
        device.bitmapOn = bitmapOn;
      }
      box = new createjs.Shape();
      box.graphics.beginFill(createjs.Graphics.getRGB(255, 0, 0));
      box.graphics.rect(0, 0, $gateSize, $gateSize);
      box.x = -$gateSize * 0.5;
      box.y = -$gateSize * 0.5;
      container.hitArea = box;
      return container;
    };

    Lightbulb.prototype.draw = function() {
      if (Socket.states[this.inputSockets[0].name] === 'on') {
        this.bitmapOn.visible = true;
        return this.bitmapOff.visible = false;
      } else {
        this.bitmapOn.visible = false;
        return this.bitmapOff.visible = true;
      }
    };

    Lightbulb.displayName = 'BULB';

    Lightbulb.bitmapOffPath = '/assets/images/lightbulb_off.png';

    Lightbulb.bitmapOnPath = '/assets/images/lightbulb_on.png';

    return Lightbulb;

  })(IODevice);

  IODevice.registerType(Lightbulb);

}).call(this);
