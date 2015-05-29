package spice.elements;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.math.BigDecimal;
import java.util.LinkedList;

/**
 * Created by EllepsisRT on 28.05.2015.
 */
public class Diode extends Element{

    double Rvalue;
    double Ivalue;

    public Diode(int plusNode, int minusNode, double value, int number){
        super(plusNode, minusNode, value, number);
    }

    private double calculateValues(){
        final double Is = 1e-12;
        final double Vt = 25.85e-3;
        double Vd = value;
        final int n = 1;
        double I = Is*(Math.exp(Vd/(n*Vt))-1);
        Rvalue = I/Vt;
        Ivalue = I - Rvalue*Vd;
        return I;
    }

    public LinkedList<Element> convertToElements(){
        LinkedList<Element> elements = new LinkedList<>();
        calculateValues();
        elements.add(new CurrentSource(plusNode, minusNode, Ivalue, number));
        elements.add(new Resistor(plusNode, minusNode, 1/Rvalue, number));
        return  elements;
    }
}
