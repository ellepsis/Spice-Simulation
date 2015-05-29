package spice;

import spice.elements.Diode;
import spice.elements.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by EllepsisRT on 28.05.2015.
 */

public class Spice {

    static double[] result = null;
    static Matrix matrix = null;

    public static void main(String[] args){
        BufferedReader bufferedReader = null;
        try {
            if (args.length == 1) {
                bufferedReader = new BufferedReader(new FileReader(args[0]));
            }
            else {
                bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Can't open file");
            return;
        }
        Parser parser = new Parser();
        try {
            parser.parseInput(bufferedReader);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        startCalculate(parser);
        printResult(matrix, result);
        return;
    }

    private static void startCalculate(Parser parser){
        double[] previousResult;
        LinkedList<Diode> diodes = null;
        for (Element elem : parser.getInputElements()){
            if (elem instanceof Diode){
                if (diodes ==null) diodes = new LinkedList<>();
                diodes.add((Diode)elem);
            }
        }
        matrix = calculate(parser);
        if (parser.isHasAnyDiode()){
            previousResult = new double[result.length];
            for (int i =0; i<10000; i++){
                double maxDifference = 0;
                result = new double[result.length];
                matrix = calculate(parser);
                for (int j =0; j< previousResult.length; j++){
                    maxDifference = Math.max(Math.abs(result[j] - previousResult[j]), maxDifference);
                }
                if (maxDifference < 0.001) break;
                previousResult = result;
                for(Diode diode : diodes){
                    double newValue;
                    if (diode.getMinusNode() == 0)  newValue = result[diode.getPlusNode()-1];
                    else if (diode.getPlusNode() == 0) newValue = result[diode.getMinusNode()-1];
                    else newValue = result[diode.getPlusNode()-1] - result[diode.getMinusNode()-1];
                    diode.setValue(newValue);
                }
            }
        }
    }

    private static Matrix calculate(Parser parser){
        Matrix matrix = new Matrix(parser.getMaxNode(), parser.getMaxNode(), parser.getVoltageSourcesCount());
        try {
            matrix.fillMatrix(parser.getInputElements());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        matrix.cutFirstRowAndCol();
        try {
            result = GaussMethod.GaussProcess(matrix);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return matrix;
    }

    private static void printResult(Matrix m, double[] result){
        int i;
        int voltCount = m.getVoltageSourcesCount();
        for (i =0; i<m.getXLength()-voltCount; i++)
            System.out.println("Node #"+i+" Voltage: "+result[i] +"V");
        for (String str : m.getNames())
            System.out.println("Current in "+ str +": "+result[i++] +"A");
    }
}
