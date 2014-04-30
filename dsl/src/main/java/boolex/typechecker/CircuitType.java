package boolex.typechecker;

/**
 * Represents a circuit type. Stores number of formals and number of outputs
 *
 * I really wish Java had Scala's case classes
 * @author Alex Reinking
 */
public class CircuitType extends BooLeXType {
    private int nFormals;
    private int nOutputs;

    public CircuitType(int nFormals) {
        this.nFormals = nFormals;
    }

    public int getFormals() {
        return nFormals;
    }

    public int getOutputs() {
        return nOutputs;
    }

    public void setOutputs(int nOutputs) {
        this.nOutputs = nOutputs;
    }
}
