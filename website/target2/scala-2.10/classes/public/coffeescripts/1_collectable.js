(function() {
  var Collectable;

  Collectable = (function() {

    function Collectable(klass) {
      this.klass = klass;
      this.id = Collectable.createUuid();
      this.klass || (this.klass = this.constructor);
      this.klass.add(this);
    }

    Collectable.prototype.destroy = function() {
      return this.klass.remove(this);
    };

    Collectable.add = function(obj) {
      if (this.all == null) {
        this.all = [];
        this.hash = {};
      }
      this.all.push(obj);
      return this.hash[obj.id] = obj;
    };

    Collectable.collection = function() {
      return this.all;
    };

    Collectable.remove = function(obj) {
      if (this.all != null) {
        this.hash[obj.id] = null;
        return this.all = $.grep(this.all, function(obj2) {
          return obj2.id !== obj.id;
        });
      }
    };

    Collectable.find = function(id) {
      return this.hash[id];
    };

    Collectable.createUuid = function() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r, v;
        r = Math.random() * 16 | 0;
        v = c === 'x' ? r : r & 0x3 | 0x8;
        return v.toString(16);
      });
    };

    return Collectable;

  })();

}).call(this);
