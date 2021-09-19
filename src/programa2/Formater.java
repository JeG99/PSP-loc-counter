/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa2;

//.b=2

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Formater {
    int totalLines;
    //.d=4
    String outputBase;
    String outputNew;
    String outputReused;
    
    /**
    *
    * Constructor de la clase.
    * Formater
    * params: fileName, commentLines, blankLines, totalLines
    * return: -
    * 
    * Este es el método constructor de la clase.
    */
    //.i
    public Formater() { //.m
        this.totalLines = 0; //.m
        //.d=4
        this.outputBase = "CLASES BASE:\n";
        this.outputNew = "CLASES NUEVAS:\n";
        this.outputReused = "CLASES REUSADAS:\n";
    }
    
    /**
    *
    * Carga de datos.loadClass
    * params: nombre de clase, total, items, base, borradas, modificadas, agregadas
    * return: -
    * Esta función es usada para guardar en 3 strings el nombre de una clase y sus cuentas de LDC según su clasificación.
    */
    //.i
    public void loadClass(String fileName, int total, int items, int base, int deleted, int modified, int added) {
        // B>0 y (M>0 o D>0 o A>0)
        if(base > 0 && (modified > 0 || deleted > 0 || added > 0)) {
            // CLASE BASE
            this.outputBase += "   " + fileName + ": T=" + total + ", I=" + items + ", B=" + base +  ", D=" + deleted + ", M=" + modified + ", A=" + added + "\n"; 
        }
        // B=0 y M=0 y D=0 y A>0
        if(base == 0 && modified == 0 && deleted == 0 && added > 0) {
            // CLASE NUEVA
            this.outputNew += "   " + fileName + ": T=" + total + ", I=" + items + "\n";
        }
        // B>0 y M=0 y D=0 y A=0
        if(base > 0 && modified == 0 && deleted == 0 && added == 0) {
            // CLASE REUSADA
            this.outputReused += "   " + fileName + ": T=" + total + ", I=" + items + ", B=" + base + "\n";
        }
        this.totalLines += total;
    }
    
    /**
    *
    * Presentación de resultados.
    * display
    * params: -
    * return: -
    * 
    * Esta función imprime los resultados de cuentas, no es necesario más formato pues loadClass guarda en las 3 strings de tipos de clases los datos tabulados.
    */
    //.b=1
    //.i
    public void display() throws FileNotFoundException {
        this.outputBase += "--------------------------------------------\n";
        this.outputNew += "--------------------------------------------\n";
        this.outputReused += "--------------------------------------------\nTotal de LDC=" + this.totalLines;
        System.out.println(this.outputBase + this.outputNew + this.outputReused); //.m
        //.d=6
        PrintWriter outputTxt = new PrintWriter("ConteoLDC.txt");
        outputTxt.println(this.outputBase + this.outputNew + this.outputReused);
        outputTxt.close();
    }
    
}
