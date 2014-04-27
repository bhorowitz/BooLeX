package boolex.typechecker;

/**
 * Created by alex on 3/11/14.
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
