(function() {
  var $allDevices, $clockTime, $clocks, $gateSize, $halfGateSize, $isImmediate, $openConnection, $selectedDevices, $socketPadding, $socketSize, $stageHeight, $stageWidth;

  $gateSize = 40;

  $halfGateSize = $gateSize / 2;

  $socketSize = 4;

  $socketPadding = $halfGateSize + 4;

  $openConnection = null;

  $allDevices = [];

  $selectedDevices = {};

  $stageWidth = void 0;

  $stageHeight = void 0;

  $clockTime = 1500;

  $clocks = [];

  $isImmediate = false;

}).call(this);
