package com.general.modelo;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

public class DAOTransacciones extends ConnManager {
    static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DAOTransacciones.class.getName());

    public DAOTransacciones() {
    }

    public DAOTransacciones(String driver, String url, String servidor, Integer puerto, String nombreBD, String propiedades, String usuarioBD, String passwordUsuarioBD) {
        super(driver, url, servidor, puerto, nombreBD, propiedades, usuarioBD, passwordUsuarioBD);
    }
    
    public DAOTransacciones(String jndi) {
        super(jndi);
    }

    public List<String[][]> getTransacciones(CallableStatement sentencia) throws Exception {
        List<List<String>> filas;
        List<String[][]> lista = new ArrayList<>();
        String[][] registros = null;
        
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

        LOGGER.info("CONSULTA INICIADA");
        filas = getResults(consultar(sentencia));
    	LOGGER.info("CONSULTA FINALIZADA");
        
        return filas;
    }
    
    public List<String> getTransactiones(PreparedStatement sentencia, Integer col) throws Exception {
        List<String> filas = new ArrayList<>();
        
        for (List<String> e : getTransactiones(sentencia)){
            filas.add(e.get(col));
        }
        
        return filas;
    }
    
    private List<List<String>> getResults(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        Integer columnsNumber = rsmd.getColumnCount();
        List<String> columnas;
        List<List<String>> filas = new ArrayList<>();
        
        while (rs.next()){
            columnas = new ArrayList<>();
            for (Integer i = 1 ; i <= columnsNumber ; i++){
                columnas.add(StringUtils.trim(rs.getString(i)));
            }
            filas.add(columnas);
        }
        
        rs.close();
        
        return filas;
    }
    
    private String[][] getResultsArray(ResultSet rs) throws Exception {
        List<List<String>> filas;
        String[][] registros = null;

        LOGGER.info("CONSULTA INICIADA");
        filas = getResults(rs);
    	LOGGER.info("CONSULTA FINALIZADA");
        
        if (filas.isEmpty()){
            LOGGER.info("Sin informacion para procesar");
        } else {
            LOGGER.info("filas: " + filas.size() + " | columnas: " + filas.get(0).size());

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

}