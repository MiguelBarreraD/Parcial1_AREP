package org.example;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.text.html.parser.Element;


public class compreflex {

    /* 
     * http://localhost:35000/consulta?comando=binaryInvoke(java.lang.Math, max, double, 4.5, double, -3.7)
     * java.lang.Math,max,double,4.5,double,-3.7
    */

    public Object getMethod(String args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, NumberFormatException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        System.out.println("Esto son los parametros que llegaron: " +args);
        String[] elementos = args.split(",");
        System.out.println(elementos.length);
        String clase = elementos[0];
        String metodo = elementos[1];
        Class<?> c = Class.forName(clase);
        
        if (elementos.length == 6) {
            System.out.println("Esta entrando aqui");
            String tipo_1 = elementos[2];
            String tipo_2 = elementos[4];
            String parametro_1 = elementos[3];
            String parametro_2 = elementos[5];
            if (tipo_1 == "double" && tipo_2 == "double") {
                Method m = c.getMethod(metodo, Double.TYPE , Double.TYPE);
                return m.invoke(null, Double.parseDouble(parametro_1), Double.parseDouble(parametro_2));
            } 
            if (tipo_1 == "int" && tipo_2 == "int") {
                Method m = c.getMethod(metodo, Integer.TYPE , Integer.TYPE);
                return m.invoke(null, Integer.parseInt(parametro_1), Integer.parseInt(parametro_2));
            } 
            if (tipo_1 == "String" && tipo_2 == "String") {
                Method m = c.getMethod(metodo,  String.class , String.class);
                return m.invoke(null, parametro_1, parametro_2);
            } 
        }
        return null;
    }
}
