(function() {
  var IODevice,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  IODevice = (function(_super) {

    __extends(IODevice, _super);

    function IODevice(numIns, numOuts) {
      IODevice.__super__.constructor.call(this, numIns, numOuts, IODevice);
      $(window).trigger('update');
    }

    IODevice.prototype.draw = function() {};

    IODevice.registerType = function(ioDeviceClass) {
      return this.types.push(ioDeviceClass);
    };

    IODevice.displayName = 'IO';

    IODevice.types = [];

    return IODevice;

  })(Device);

}).call(this);
