/**
 * Created by ajr64 on 4/25/14.
 */


function DSLFactory() {
    // Public functions
    this.generateROM = function(valueMatrix) {
        var nWords = valueMatrix.length;
        var wordSize = valueMatrix[0].replace(/[^ft]/g, "").length;
        return fillInROM(createROMTemplate(nWords, wordSize), valueMatrix, nWords, wordSize);
    };

    this.generateAndChain = function(nInputs) {
        var inputs = rangeToList(0,nInputs).map(function(x) { return "i"+x });
        var dsl = "circuit ANDC_" + nInputs + "(" + inputs.join(", ") + ")\n";
        dsl += "    out " + inputs.join(" and ") + "\nend\n"
        return dsl;
    };
    
    this.generateOrChain = function(nInputs) {
        var inputs = rangeToList(0,nInputs).map(function(x) { return "i"+x });
        var dsl = "circuit ORC_" + nInputs + "(" + inputs.join(", ") + ")\n";
        dsl += "    out " + inputs.join(" or ") + "\nend\n"
        return dsl;
    };

    this.generateDecoder = function(nInputs) {
        var inputs = rangeToList(0,nInputs).map(function(x) { return "i"+x });
        var nOutputs = Math.pow(2,nInputs);
        var outputs = rangeToList(0,nOutputs).map(function(x) { return "o"+x });
        var dsl = getHeader("decoder",inputs,nOutputs);
        for (var output = 0; output < nOutputs; output++) {
            dsl += "    " + outputs[output] + " = ";
            var binaryString = pad(output.toString(2),nInputs,"0");
            var terms = [];
            for (var input = nInputs-1; input >= 0; input--) {
                var term = "";
                if (binaryString.charAt(input) === "0") term += "not ";
                term += inputs[input];
                terms.push(term);
            }
            dsl += terms.join(" and ") + "\n";
        }
        dsl += getFooter(outputs);
        return dsl;
    };

    this.generateEncoder = function(nOutputs) {
        var outputs = rangeToList(0,nOutputs).map(function(x) { return "o"+x });
        var nInputs = Math.pow(2,nOutputs);
        var inputs = rangeToList(0,nInputs).map(function(x) { return "i"+x });
        var dsl = getHeader("encoder",inputs,nOutputs);
        var encodings = rangeToList(0,nOutputs).map(function(x) {return []})
        for (var i = 0; i < nInputs; i++) {
            var binaryString = pad(i.toString(2),nOutputs,"0");
            for (var j = 0; j < nOutputs; j++) {
                if (binaryString.charAt(nOutputs - j - 1) === "1")
                    encodings[j].push(inputs[i])
            }
        }
        var outLines = encodings.map(function(line) {return line.join(" or ")})
        for (var i = 0; i < nOutputs; i++) {
            dsl += "    o"+i+" = "+outLines[i]+"\n";
        }
        dsl += getFooter(outputs);
        return dsl;
    };

    // Private Helpers
    // General Helpers
    function rangeToList(low,high) {
        var list = [];
        for (var i = low; i < high; i++)
            list.push(i);
        return list;
    }

    function pad(n, width, z) {
        z = z || '0';
        n = n + '';
        return n.length >= width ? n : new Array(width - n.length + 1).join(z) + n;
    }

    function getHeader(type,inputs,nOutputs) {
        return "circuit " + type[0].toUpperCase() + "_" + inputs.length + "_" +
            nOutputs + "(" + inputs.join(", ") + ")\n";
    }
    
    function getFooter(outputs) {
        return "    out " + outputs.join(", ") + "\nend";
    }

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
                line.push(wordNames[i] + j);
            }
            words.push(line);
        }
        var dsl = getHeader("rom",wordNames,wordSize);
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
        dsl += getFooter(outputs);
        return dsl;
    }

    function fillInROM(dsl, valueMatrix, nWords, wordSize) {
        var values = valueMatrix.join("").replace(/[^ft]/g, "");

        if (values.length !== (nWords * wordSize)) {
            console.log("ERROR: Wrong number of values");
            return undefined;
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