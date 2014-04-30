(function() {
  var Toolbox;

  Toolbox = (function() {

    function Toolbox() {
      var device, i, tool, y, _i, _j, _len, _len1, _ref, _ref1;
      this.devices = Gate.types.concat(IODevice.types);
      this.tools = (function() {
        var _i, _len, _ref, _results;
        _ref = this.devices;
        _results = [];
        for (i = _i = 0, _len = _ref.length; _i < _len; i = ++_i) {
          device = _ref[i];
          _results.push(new Tool(device, i));
        }
        return _results;
      }).call(this);
      y = 0;
      _ref = this.tools;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        tool = _ref[_i];
        tool.graphics.y = y + tool.deviceClass.height / 2;
        tool.graphics.x = $halfGateSize;
        y += tool.deviceClass.height + Toolbox.padding;
      }
      this.graphics = new createjs.Container();
      _ref1 = this.tools;
      for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
        tool = _ref1[_j];
        this.graphics.addChild(tool.graphics);
      }
      window.boolexStage.addChild(this.graphics);
    }

    Toolbox.padding = 5;

    return Toolbox;

  })();

}).call(this);
