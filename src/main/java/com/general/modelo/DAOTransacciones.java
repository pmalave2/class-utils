package com.general.modelo;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class DAOTransacciones extends ConnManager {

    public DAOTransacciones() {
    }

    public DAOTransacciones(String driver, String url, String servidor, Integer puerto, String nombreBD, String propiedades, String usuarioBD, String passwordUsuarioBD) {
        super(driver, url, servidor, puerto, nombreBD, propiedades, usuarioBD, passwordUsuarioBD);
    }
    
    public DAOTransacciones(String jndi) {
        super(jndi);
    }

    public List<String[][]> getTransacciones(CallableStatement sentencia) throws Exception {
        List<String[][]> lista = new ArrayList<>();
        
        lista.add(getResultsArray(consultar(sentencia)));
                
        while (sentencia.getMoreResults()) {
            lista.add(getResultsArray(sentencia.getResultSet()));
        }
        
        return lista;
    }
    
    
    public String[][] getTransacciones(PreparedStatement sentencia) throws Exception {
        return getResultsArray(consultar(sentencia));
    }
    
    public List<List<String>> getTransactiones(PreparedStatement sentencia) throws Exception {
        List<List<String>> filas;

        System.out.println("CONSULTA INICIADA");
        filas = getResults(consultar(sentencia));
    	System.out.println("CONSULTA FINALIZADA");
        
        return filas;
    }
    
    public List<String> getTransactiones(PreparedStatement sentencia, Integer col) throws Exception {
        List<String> filas = new ArrayList<>();
        
        for (List<String> e : getTransactiones(sentencia)){
            filas.add(e.get(col));
        }
        
        return filas;
    }
    
    private List<List<String>> getResults(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Integer columnsNumber = rsmd.getColumnCount();
        List<String> columnas;
        List<List<String>> filas = new ArrayList<>();
        
        while (rs.next()){
            columnas = new ArrayList<>();
            for (Integer i = 1 ; i <= columnsNumber ; i++){
                columnas.add(getColumn(rs, i));
            }
            filas.add(columnas);
        }
        
        rs.close();
        
        return filas;
    }
    
    private String[][] getResultsArray(ResultSet rs) throws Exception {
        List<List<String>> filas;
        String[][] registros = null;

        System.out.println("CONSULTA INICIADA");
        filas = getResults(rs);
    	System.out.println("CONSULTA FINALIZADA");
        
        if (filas.isEmpty()){
            System.out.println("Sin informacion para procesar");
        } else {
            System.out.println("filas: " + filas.size() + " | columnas: " + filas.get(0).size());

            registros = new String[filas.size()][];
            for (Integer i = 0; i < filas.size(); i++) {
                    List<String> row = filas.get(i);
                    registros[i] = row.toArray(new String[row.size()]);
                    filas.get(i).clear();
            }
            filas.clear();
        }

        return registros;
    }

    public String getColumn(ResultSet rs, Integer columnIndex) throws SQLException {
        return StringUtils.trim(rs.getString(columnIndex));
    }
}