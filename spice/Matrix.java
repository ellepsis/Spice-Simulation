package spice;

import spice.elements.*;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by EllepsisRT on 28.05.2015.
 */
public class Matrix {
    double[][] matrix;
    private String[] names;
    private int currentNamesIndex = 0;
    private int xLength, yLength, voltageSourcesCount;

    public Matrix(int xLength, int yLength, int voltageSourcesCount){
        if (xLength <=0 || yLength<=0 || voltageSourcesCount<=0)
            throw new RuntimeException("Can't initialize empty matrix");
        this.yLength = yLength+voltageSourcesCount+2;
        this.xLength = xLength+voltageSourcesCount+1;
        this.voltageSourcesCount = voltageSourcesCount;
        matrix = new double[this.xLength][this.yLength];
        for(int i = 0; i< this.xLength;i++){
            Arrays.fill(matrix[i], 0);
        }
        names = new String[voltageSourcesCount];

    }

    public void fillMatrix(LinkedList<Element> elements) throws Exception {
        for(Element elem : elements){
            if (elem.getMinusNode()> xLength || elem.getPlusNode() > xLength)
                throw new Exception("Node value biggest then matrix");
            if (elem instanceof Resistor){
                addResistor((Resistor) elem);
            }
            else if(elem instanceof VoltageSource){
                addVoltageSource((VoltageSource) elem);
            }
            else if (elem instanceof Diode){
                Diode diode = (Diode) elem;
                fillMatrix(diode.convertToElements());
            }
            else addCurrentSource((CurrentSource) elem);
        }
    }

    private void addResistor(Resistor r){
        int m = r.getMinusNode();
        int p = r.getPlusNode();
        double v = r.getValue();
        matrix[p][p] += (double)1/v;
        matrix[p][m] -= (double)1/v;
        matrix[m][p] -= (double)1/v;
        matrix[m][m] += (double)1/v;
    }

    private void addVoltageSource(VoltageSource vol){
        int m = vol.getMinusNode();
        int p = vol.getPlusNode();
        double v = vol.getValue();
        matrix[xLength-voltageSourcesCount+currentNamesIndex][p] = 1;
        matrix[xLength-voltageSourcesCount+currentNamesIndex][m] = -1;
        matrix[m][xLength-voltageSourcesCount+currentNamesIndex] = -1;
        matrix[p][xLength-voltageSourcesCount+currentNamesIndex] = 1;
        matrix[xLength-voltageSourcesCount+currentNamesIndex][yLength-1] = v;
        names[currentNamesIndex++] = "V"+vol.getNumber();
    }

    private void addCurrentSource(CurrentSource c){
        int m = c.getMinusNode();
        int p = c.getPlusNode();
        double v = c.getValue();
        matrix[p][yLength-1] -= v;
        matrix[m][yLength-1] += v;
    }

    public void cutFirstRowAndCol(){
        double[][] tmpMatrix = new double[xLength-1][yLength-1];
        for(int i =0; i< xLength-1; i++){
            tmpMatrix[i] = Arrays.copyOfRange(matrix[i+1],1,yLength);
        }
        matrix = tmpMatrix;
        yLength--;
        xLength--;
    }

    public int getXLength() {
        return xLength;
    }

    public int getYLength() {
        return yLength;
    }

    public int getVoltageSourcesCount(){
        return voltageSourcesCount;
    }

    public String[] getNames(){
        return names;
    }
}
