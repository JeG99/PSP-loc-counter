/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa2;

//.b=3
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    /**
     * @param args the command line arguments
     */
    //.i
    public static void main(String[] args) throws IOException {
        //.d=4
        // Se inicializa una lista de strings para guardar nombres de clases.
        List<String> fileNames = new ArrayList<>(); //.m
        // Se inicializa una lista de arreglos de enteros para guardar las cuentas de LDC.
        List<int[]> counts = new ArrayList<>();
        // Scanner para leer de stdin
        //.b=1
        Scanner scanner = new Scanner(System.in);
        // String line sirve de buffer para leer la entrada standard línea por línea.
        String line = "";
        
        //.b=1
        try {
            // Se instancía un objeto Formatter para producir la presentación de resultados más tarde.
            Formater formater = new Formater();
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if(line == null || line.isEmpty()) // Caracter vacío para terminación de lectura 
                    break;

                // Nueva instancia de FileScanner para realizar la lectura del archivo leido
                FileScanner fileScanner = new FileScanner(line);
                // Realización del conteo
                fileScanner.scanFile();

                // Se retira del string leido la extensión del archivo
                String fileName = line.substring(0, line.indexOf("."));
                
                // Si la parte leida es de una clase que no se habia leido ... 
                if(!fileNames.contains(fileName)){
                    // ... se agrega su nombre y ...
                    fileNames.add(fileName);
                    // ... se agrega un arreglo de contadores en su respectivo index
                    int[] lineCounts = {fileScanner.ldcCount, fileScanner.items, fileScanner.base, fileScanner.deleted, fileScanner.modified, fileScanner.added};
                    counts.add(lineCounts);
                }
                else { // Si la parte leida es de una clase existente ...
                    // ... se suman las cuentas de esta parte a las de la clase en la matriz de cuentas
                    int index = fileNames.indexOf(fileName);
                    counts.get(index)[0] += fileScanner.ldcCount;
                    counts.get(index)[1] += fileScanner.items;
                    counts.get(index)[2] += fileScanner.base;
                    counts.get(index)[3] += fileScanner.deleted;
                    counts.get(index)[4] += fileScanner.modified;
                    counts.get(index)[5] += fileScanner.added;
                }
                
            }
            // Se vacían los datos recopilados en el formater
            for(int i = 0; i < counts.size(); i++) {
                formater.loadClass(fileNames.get(i), counts.get(i)[0], counts.get(i)[1], counts.get(i)[2], counts.get(i)[3], counts.get(i)[4], counts.get(i)[5]);
            }
            // Se muestran los resultados
            formater.display();
        }
        // Manejo de errores
        //.b=4
        catch(FileNotFoundException e) { // El archivo no existe
            System.out.println("Error: '" + line + "' no existe en el directorio actual");
        }
        catch(ArithmeticException e) { // El archivo está vacío
            System.out.println("Error: '" + line + "' está vacío");
        }
        
    }
    
}