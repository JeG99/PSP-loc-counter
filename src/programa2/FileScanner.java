/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programa2;

//.b=9
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileScanner {
    //.b=4
    String fileName;
    //.d=1
    int commentCount;
    int blankCount;
    int totalCount;
    int semicolonCount;
    int items;
    int base;
    int deleted;
    int modified;
    int added;
    int ldcCount;
    
    /**
    *
    * Constructor de la clase.
    * FileScanner
    * params: fileName
    * return: -
    */
    //.i
    //.b=5
    public FileScanner(String fileName) {
        this.fileName = fileName;
        //.d=1
        this.commentCount = 0;
        this.blankCount = 0;
        this.totalCount = 0;
        this.semicolonCount = 0;
        this.items = 0;
        this.base = 0;
        this.deleted = 0;
        this.modified = 0;
        this.added = 0;
        this.ldcCount = 0;
    }
    
    /**
    *
    * Escaneo del archivo y cálculo de líneas.
    * scanFile
    * params: -
    * return: -
    * 
    * Este método 
    */
    //.i
    //.b=6
    public void scanFile() throws FileNotFoundException, IOException {
        // Bandera para multiline comments
        Boolean openComment = false;
        // Buffer para leer línea por línea el archivo
        String line = "";
        // Ruta relativa del archivo
        String filePath = this.fileName;
        // Archivo para lectura
        File file = new File(filePath);
        
        // Lista de chars que servirá para leer el conteo de cada tag
        ArrayList<Character> numBuff = new ArrayList<>();
        // Se abre el archivo
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        // Temporal entero para sumar cuentas
        int count = 0;
        // Bandera para strings
        boolean insideString = false;
        
        while((line = bufferedReader.readLine()) != null) {
            // Tokenización de la línea usando como delim los caracteres vacíos
            String[] lineTokens = line.split("\\s+");
            
            // Sí se leen comillas ...
            if(line.contains("\"")){
                // Se activa la bandera de string
                insideString = true;
            }
            if(insideString) { // Leyendo un string
                // Índice de los primeros quotes
                int firstQuoteIndex = line.indexOf('"');
                // Índice de los segundos quotes
                int secondQuoteIndex = line.indexOf('"', firstQuoteIndex);
                // Substring que es la string completa
                String stringToken = line.substring(firstQuoteIndex, secondQuoteIndex);
                //System.out.println(stringToken); //-----------------------------------------------------------
                
                // Si el string contiene etiquetas ...
                if(stringToken.contains("//.i")){
                    // ... elimina excedente
                    this.items--;
                    if(this.items < 0) {
                        this.items = 0;
                    }
                }
                if(stringToken.contains("//.b")){
                    this.base--;
                    if(this.base < 0) {
                        this.base = 0;
                    }
                }
                if(stringToken.contains("//.d")){
                    this.deleted--;
                    if(this.deleted < 0) {
                        this.deleted = 0;
                    }
                }
                if(stringToken.contains("//.m")){
                    this.modified--;
                    if(this.modified < 0) {
                        this.modified = 0;
                    }
                }
                // Salir de string
                insideString = false;
                //System.out.println(this.modified); //-----------------------------------------------------------
            }
            else {
                // Lectura normal
                for(int i = 0; i < lineTokens.length; i++) {

                    // Se leen los tags
                    if(lineTokens[i].contains("//.i")){
                        // .m y .i suman directo
                        this.items++;
                    }
                    if(lineTokens[i].contains("//.b")){
                        // .b y .d usan el buffer para pasar a int y sumar
                        count = Integer.parseInt(lineTokens[i].split("=")[1]);
                        this.base += count;
                    }
                    if(lineTokens[i].contains("//.d")){
                        count = Integer.parseInt(lineTokens[i].split("=")[1]);
                        this.deleted += count;
                    }
                    if(lineTokens[i].contains("//.m")){
                        this.modified++;
                    }
                }
            }
            
            // Conteo global de líneas (LDC y no LDC)
            //.b=11
            this.totalCount++;
            
            // Manejo de comentarios
            if(!openComment){
                
                // Se tokeniza la línea y se eliminan los espacios en blanco antes y después de la misma
                String[] tokens = line.trim().split("\\s+");
                
                // Si la línea estaba en blanco suma a las no LDC
                if(line.isBlank()) {
                    this.blankCount++;
                }
                // multiline comments
                else if(line.contains("/*")) { //.m
                    this.commentCount++;
                    if(!line.contains("*/")){
                        openComment = true;
                    }
                }
                // single line comments
                else if(line.contains("//")) { //.m
                    if(tokens[0].charAt(0) == '/' && tokens[0].charAt(1) == '/') {
                        this.commentCount++;
                    }
                }
                // Conteo de llaves
                if(line.endsWith("{") || line.endsWith("}") || line.endsWith("};")){
                    if(tokens.length == 1 && ("}".equals(tokens[0]) || "{".equals(tokens[0]) || "};".equals(tokens[0]))) {
                        this.semicolonCount++;
                    }
                }

            } else {
                
                // Si .m está dentro de un comment ...
                if(line.contains("//.m")){
                    // ... se eliminan excedentes
                    this.modified--;
                    if(this.modified < 0) {
                        this.modified = 0;
                    }
                }
                // Suma por comentario multiline
                this.commentCount++;
                // Fin de multiline comment
                if(line.contains("*/")) 
                    openComment = false;
                
            }
        }
        
        // Pequeña lectura char por char de \n para posible corrección de líneas totales contadas
        //.b=10
        int total = 0;
        if(this.totalCount > 0) {
            String strFile = new String(Files.readAllBytes(Paths.get(filePath)));
            int character;
            Reader reader = new StringReader(strFile);
            for(int i = 0; i < strFile.length(); i++) {
                character = reader.read();
                if(character == 10) {
                    total++;
                }
            }
            total++;
        }
        
        // Aplicación de la anterior corrección
        //.b=3
        if(total > this.totalCount && this.totalCount > 0){
            this.blankCount++;
            this.totalCount++;
        }
        
        // Cálculo de LDC
        this.ldcCount = this.totalCount - this.blankCount - this.semicolonCount - this.commentCount;
        // Cálculo de líneas agregadas
        this.added = this.ldcCount - this.base + this.deleted;
        // Excepción por archivo vacío
        //.b=2
        if(this.totalCount == 0){
            throw new ArithmeticException();
        }
    }
}