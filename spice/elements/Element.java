package spice.elements;

/**
 * Created by EllepsisRT on 28.05.2015.
 */
public abstract class Element {
    int plusNode;
    int minusNode;
    double value;
    int number;

    public Element(int plusNode, int minusNode, double value, int number){
        this.plusNode = plusNode;
        this.minusNode = minusNode;
        this.value = value;
        this.number = number;
    }

    public Element(){
    }

    public int getNumber() {
        return number;
    }

    public int getPlusNode() {
        return plusNode;
    }

    public int getMinusNode() {
        return minusNode;
    }

    public double getValue() {
        return value;
    }

    public void setPlusNode(int plusNode) {
        this.plusNode = plusNode;
    }

    public void setMinusNode(int minusNode) {
        this.minusNode = minusNode;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
}
