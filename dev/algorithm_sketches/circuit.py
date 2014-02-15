#A quick design of a circuit algorithm that can handle sequential circuits.

#When you flip a gate and evaluate the circuit, it will traverse the circuit from that gate
#keeping track of what gates it has visited in the past, if it comes across that gate again
#it will not evaluate on it.

#See bottom for a sample implementation for the flip/flop gate (using these conventions)
#https://faculty-web.msoe.edu/tritt/hs/hsdig035.html

class wire(object):
    checked_gates = []
    def __init__(self,kk='null',state=False):
        self.input = 0
        self.output = 0
        self.index = kk
        self.state = state
    def evaluate(self):
        if not self.output==0:
            if self.output not in wire.checked_gates:
                self.output.evaluate()
               

class gate(object):
    def __init__(self,into, output, gtype = 1):
        self.input = into
        self.output = output
        for i in self.input:
            i.output = self
        for i in self.output:
            i.input = self
        self.type = gtype
        self.value = False
    def evaluate(self):
        wire.checked_gates.append(self)
        l = []
        if self.type == 1: #and
            if not len(self.input) == 2:
                print "error!"
            wire1 = self.input[0]
            wire2 = self.input[1]
            self.value = wire1.state&wire2.state
            self.output[0].state = self.value
            self.output[0].evaluate()
        if self.type == 2: #or
            if not len(self.input) == 2:
                print "error!"
            wire1 = self.input[0]
            wire2 = self.input[1]
            self.value = wire1.state^wire2.state
            self.output[0].state = self.value
            self.output[0].evaluate()
        if self.type == 3: #xor
            if not len(self.input) == 2:
                print "error!"
            wire1 = self.input[0]
            wire2 = self.input[1]
            self.value = not (wire1.state or wire2.state)
            self.output[0].state = self.value
            self.output[0].evaluate()
        if self.type == 4: #split
            if not len(self.input) == 1:
                print "error!"
            value = self.input[0].state
            self.output[0].state = value
            self.output[1].state = value
            self.output[0].evaluate()
            self.output[1].evaluate()
   
def seq_updater(wires,j):
    for i in wires:
        wire.checked_gates = []
        j[i].state = not j[i].state
        j[i].output.evaluate()

def new_state(table,j):
    for i,val in enumerate(table):
        j[i].state = val

j = [wire(kk, False) for kk in range(0,9)]

#Sample and gate network
#l1 = gate([j[1],j[2]],[j[3]])
#l2 = gate([j[4],j[5]],[j[6]])
#l3 = gate([j[3],j[6]],[j[7]])

#RS flip flot 
x1 = gate([j[1],j[5]],[j[3]],3) #xor gate at R
x2 = gate([j[2],j[7]],[j[4]],3) #xor gate at S
s1 = gate([j[4]],[j[5],j[6]],4) #split gate at S
s2 = gate([j[3]],[j[7],j[8]],4) #split gate at R
print "initial state"
print [(j[i].index,j[i].state) for i in range(1,9)]
print "changing state to S=True, Q=True"
new_state([False, False, True, False, False, False, False, True, True],j) #setting S =1, Q=1, R=0 Q' = 0
print [(j[i].index,j[i].state) for i in range(1,9)]
print "flipping S"
seq_updater([2],j)
print [(j[i].index,j[i].state) for i in range(1,9)]
print "flipping R"
seq_updater([1],j)
print [(j[i].index,j[i].state) for i in range(1,9)]
print "flipping R again"
seq_updater([1],j)
print [(j[i].index,j[i].state) for i in range(1,9)]

