package spice;

import spice.elements.*;

import java.io.BufferedReader;
import java.util.LinkedList;

/**
 * Created by EllepsisRT on 28.05.2015.
 */
public class Parser {
    private LinkedList<Element> inputElements;
    private int maxNode;
    private int voltageSourcesCount;
    private boolean hasAnyDiode = false;

    public Parser(){
        inputElements = new LinkedList<Element>();
    }

    public Element parseString(String input) throws Exception {
        final int inputLength = 4;
        Element elem;
        int plusNode, minusNode, number;
        double value;

        String[] inputFields = input.replaceAll("\\s+", " ").trim().split("\\*")[0].split(" ");
        if (inputFields.length != inputLength){
            if (inputFields.length == 1 && inputFields[0].length() == 0) return null;
            else throw new Exception("Invalid string");
        }

        try{
            plusNode = Integer.parseInt(inputFields[1]);
            minusNode = Integer.parseInt(inputFields[2]);
            value = Double.parseDouble(inputFields[3]);
            number = Integer.parseInt(inputFields[0].substring(1));
        }
        catch (Exception e){
            throw new Exception("Can't parse nodes or values");
        }
        if (plusNode<0 || minusNode<0) throw new Exception("Invalid node index");
        maxNode = Math.max(Math.max(plusNode, minusNode), maxNode);

        switch (inputFields[0].charAt(0)){
            case 'R':
                elem = new Resistor(plusNode, minusNode, value, number);
                break;
            case 'V':
                elem = new VoltageSource(plusNode, minusNode, value, number);
                voltageSourcesCount++;
                break;
            case 'I':
                elem = new CurrentSource(plusNode, minusNode, value, number );
                break;
            case 'D':
                elem = new Diode(plusNode, minusNode, value, number);
                hasAnyDiode = true;
                break;
            default:
                throw new Exception("Invalid node type");
        }
        return elem;
    }

    public void parseInput(BufferedReader inputStream) throws Exception {
        String line;
        if (inputStream == null) throw new Exception("Null buffer");
        while ((line = inputStream.readLine())!=null){
            Element element = parseString(line);
            if (element != null) inputElements.add(element);
        }
    }

    public boolean isHasAnyDiode() {
        return hasAnyDiode;
    }

    public LinkedList<Element> getInputElements() {
        return inputElements;
    }

    public int getMaxNode() {
        return maxNode;
    }

    public int getVoltageSourcesCount(){
        return voltageSourcesCount;
    }
}
