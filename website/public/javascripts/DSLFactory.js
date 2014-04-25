/**
 * Created by ajr64 on 4/25/14.
 */


function DSLFactory() {
    // Public functions
    this.generateFullROM = function(valueMatrix) {
        var nWords = valueMatrix.length;
        var wordSize = valueMatrix[0].replace(/[^01]/g, "").length;
        return fillInROM(createROMTemplate(nWords, wordSize), valueMatrix, nWords, wordSize);
    };

    // Private Helpers
    // General Helpers
    function nextId(id) {
        if(id === "")
            return "a";
        var prefix = id.substring(0, id.length-1);
        var last = id[id.length-1];
        if(last === "z")
            return (nextId(prefix) + "a");
        return prefix + String.fromCharCode(id.charCodeAt(id.length-1) + 1);
    }

    function getWordNames(nWords) {
        var names = [];
        var cur = nextId("");
        for(var i = 0; i < nWords; i++) {
            names.push(cur);
            cur = nextId(cur);
        }
        return names;
    }

    // ROM Generators
    function createROMTemplate(nWords, wordSize) {
        var wordNames = getWordNames(nWords);
        var words = [];
        for(var i = 0; i < wordNames.length; i++) {
            var line = [];
            for(var j = 0; j < wordSize; j++) {
                line.push(wordNames[i] + j); // Append NUMBER to STRING. WTF, JS?
            }
            words.push(line);
        }
        var dsl = "circuit ROM_" + nWords + "_" + wordSize + "(" + wordNames.join(",") + ")\n";
        var outputs = [];
        for(var i = 0; i < wordSize; i++) {
            var out = "o" + i;
            outputs.push(out);
            dsl += "    " + out + " = ";
            var terms = [];
            for(var j = 0; j < nWords; j++) {
                terms.push("(" + words[j][i] + " and " + wordNames[j] + ")");
            }
            dsl += terms.join(" or ") + "\n";
        }
        dsl += "    out " + outputs.join(", ") + "\nend";
        return dsl;
    }

    function fillInROM(dsl, valueMatrix, nWords, wordSize) {
        var values = valueMatrix.join("").replace(/[^01]/g, "");

        if (values.length !== (nWords * wordSize)) {
            console.log("ERROR: Wrong number of values");
            return;
        }

        var wordNames = getWordNames(valueMatrix.length);
        var words = [];

        for (var i = 0; i < wordNames.length; i++)
            for (var j = 0; j < wordSize; j++)
                words.push(wordNames[i] + j);

        for (var i = 0; i < values.length; i++)
            dsl = dsl.replace(new RegExp(words[i], "g"), values[i]);

        return dsl;
    }


}
