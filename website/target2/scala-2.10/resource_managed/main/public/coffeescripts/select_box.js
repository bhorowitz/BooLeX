(function() {
  var SelectBox,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  SelectBox = (function(_super) {

    __extends(SelectBox, _super);

    function SelectBox(startX, startY) {
      this.startX = startX;
      this.startY = startY;
      this.box = new createjs.Shape();
      this.endX = this.startX;
      this.endY = this.startY;
    }

    SelectBox.prototype.draw = function() {
      this.box.graphics.clear();
      this.box.graphics.beginStroke("rgba(0,0,0,0.3)");
      this.box.graphics.moveTo(this.startX, this.startY);
      this.box.graphics.lineTo(this.endX, this.startY);
      this.box.graphics.lineTo(this.endX, this.endY);
      this.box.graphics.lineTo(this.startX, this.endY);
      return this.box.graphics.lineTo(this.startX, this.startY);
    };

    SelectBox.prototype.between = function(n, low, high) {
      if (low < high && n > low && n < high || low > high && n < low && n > high) {
        return true;
      }
      return false;
    };

    SelectBox.prototype.selectDevicesUnder = function() {
      var device, x, y, _i, _len, _ref, _results;
      _results = [];
      for (_i = 0, _len = $allDevices.length; _i < _len; _i++) {
        device = $allDevices[_i];
        _ref = [device.x(), device.y()], x = _ref[0], y = _ref[1];
        if (this.between(x, this.startX, this.endX) && this.between(y, this.startY, this.endY)) {
          _results.push(device.select());
        } else {
          _results.push(device.deselect());
        }
      }
      return _results;
    };

    return SelectBox;

  })(createjs.Shape);

}).call(this);
