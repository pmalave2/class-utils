package com.general.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;

public class Archivo {
    private File archivo;
    private FileWriterWithEncoding fw;
    private OutputStream os;
    private Charset oscs;

    public Archivo(){
    }

    public Archivo(File archivo) {
        this.archivo = archivo;
    }
    
    public Archivo(String archivo) {
        this.archivo = new File(archivo);
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
    public void setOutputStreamCharset(String encoder) {
        this.oscs = Charset.forName(encoder);
    }

    public List<String> leer() { 
        Scanner s;

        List<String> data = new ArrayList<>();

        try {
            s = new Scanner(archivo);
            while (s.hasNextLine()) {                
                data.add(s.nextLine());
            }

            s.close();
        } catch (FileNotFoundException e) {
            return data;
        }

        return data;
    }

    public void openFileWriter(Boolean append) throws IOException {
        fw = new FileWriterWithEncoding(archivo, StandardCharsets.ISO_8859_1, append);
    }
    public void openFileWriter(Charset cs, Boolean append) throws IOException {
        fw = new FileWriterWithEncoding(archivo, cs, append);
    }
    
    public void openOutputStream(Boolean append) throws IOException {
        os = new FileOutputStream(archivo, append);
    }

    public void closeFileWriter() throws IOException {
        fw.close();
    }

    public void closeOutputStream() throws IOException {
        os.close();
    }
    
    public void escribir(String linea) throws IOException {
        fw.append(linea + System.getProperty("line.separator"));
        fw.flush();
    }
    
    public void escribir(Integer length) throws IOException {
        fw.append(StringUtils.repeat(" ", length) + System.getProperty("line.separator"));
        fw.flush();
    }

    public void escribirEBCDIC(String linea) throws IOException {
        os.write(linea.getBytes(oscs));
        os.write(System.getProperty("line.separator").getBytes());
    }
    
    public void deleteWorkFiles(String ruta){
    	File f = new File(ruta); // current directory

        FilenameFilter acpFilter = (File dir, String name) -> {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".acp") || lowercaseName.endsWith(".dec");
            };

        File[] files = f.listFiles(acpFilter);
        for (File file : files) {
            if (!file.isDirectory()) {
                if (!file.delete()) {
                    System.err.println("Can't remove " + file.getAbsolutePath());
                }
            } 
        }

    }
    
    public static void makeDirectory(String ruta){
    	File arch = new File(ruta);
    	
    	if (!arch.exists()) {
            if (!arch.mkdir()){
                arch.mkdirs();
            }
    	}
    }
    
    public Boolean moveFile(String directoryPath) throws IOException{
    	File path = new File(directoryPath);
    	
    	makeDirectory(directoryPath);
    	
    	if (!archivo.exists())
            return Boolean.FALSE;    		
    	
        return Files.move(archivo.toPath(), path.toPath().resolve(archivo.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING).toFile().exists();
    }    

    public Boolean copyFile(String directoryPath) throws IOException{
    	File path = new File(directoryPath);

    	makeDirectory(directoryPath);
    	
    	if (!archivo.exists())
            return Boolean.FALSE;    		
    	
        return Files.copy(archivo.toPath(), path.toPath().resolve(archivo.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING).toFile().exists();
    }
        
    public void renameFile(File arc){
        if (archivo.renameTo(arc)){
            archivo = arc;
        }
    }
    
    public static void copyFiles(String path, Archivo... files) throws IOException{
        for (Archivo arc : files){
            arc.copyFile(path);
            arc.getArchivo().delete();
        }
    }
}