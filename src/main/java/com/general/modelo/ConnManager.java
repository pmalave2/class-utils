package com.general.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnManager {
    private String driver;
    private String url;
    private String nombreIPServidorBD;
    private Integer puertoServidorBD;
    private String nombreBD;
    private String propiedades;
    private String usuarioBD;
    private String passwordUsuarioBD;
    private String connectionString;
    private String jndi;

    private Connection conexion;
    private PreparedStatement sentencia;
    private ResultSet filasConsulta;

    public ConnManager() {
    }

    public ConnManager(String driver, String url, String servidor, Integer puerto, String nombreBD, String propiedades, String usuarioBD, String passwordUsuarioBD) {
        this.driver = driver;
        this.url = url;
        this.nombreIPServidorBD = servidor;
        this.puertoServidorBD = puerto;
        this.nombreBD = nombreBD;
        this.propiedades = propiedades;
        this.usuarioBD = usuarioBD;
        this.passwordUsuarioBD = passwordUsuarioBD;
        this.connectionString = url + servidor + ":" + puerto + "/" + nombreBD + propiedades;
    }
    
    public ConnManager(String jndi){
        this.jndi = jndi;
    }
    
    public ConnManager conectar() throws Exception {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error con el driver " + ex.getMessage(), ex);
        }

        conexion = DriverManager.getConnection(connectionString, usuarioBD, passwordUsuarioBD);
        
        return this;
    }
    
    public ConnManager conectarJDNI() throws SQLException  {
        try {
            Context initContext = new InitialContext();
            Context webContext = (Context) initContext.lookup("java:/comp/env");

            DataSource ds = (DataSource) webContext.lookup(jndi);
            conexion = ds.getConnection();
        } catch (NamingException ex) {
            throw new SQLException("Error en el nombre de DataSource " + ex.getMessage(), ex);
        } catch (SQLException ex) {
            throw new SQLException("Error obteniedo el DataSource " + ex.getMessage(), ex);
        }
        
        return this;
    }

    public Integer actualizar(PreparedStatement sentencia) throws Exception {
        try {
            return sentencia.executeUpdate();
        }
        catch (SQLException ex) {
            throw new SQLException("Error en modificacion de BD \nCodigo:" + ex.getErrorCode() + " Explicacion:" + ex.getMessage(), ex);
        }
    }

    public ResultSet consultar(PreparedStatement sentencia) throws Exception{
        try {
        	/** solo para Select */
            return sentencia.executeQuery();
        }
        catch (SQLException ex) {
            throw new SQLException("Error en consulta de BD \nCodigo:" + ex.getErrorCode() + " Explicacion:" + ex.getMessage(), ex);
        }
    }

    public void desconectar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            conexion = null;
        }
    }
    
    public PreparedStatement crearSentencia(String sql) throws Exception{
        try {
            return conexion.prepareStatement(sql);
        } catch (SQLException ex) {
            throw new SQLException("Error en creacion de sentecia DB \nCodigo:" + ex.getErrorCode() + " Explicacion:" + ex.getMessage(), ex);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public ResultSet getFilasConsulta() {
        return filasConsulta;
    }

    public void setFilasConsulta(ResultSet filasConsulta) {
        this.filasConsulta = filasConsulta;
    }

    public String getNombreBD() {
        return nombreBD;
    }

    public void setNombreBD(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public String getNombreIPServidorBD() {
        return nombreIPServidorBD;
    }

    public void setNombreIPServidorBD(String nombreIPServidorBD) {
        this.nombreIPServidorBD = nombreIPServidorBD;
    }

    public String getPasswordUsuarioBD() {
        return passwordUsuarioBD;
    }

    public void setPasswordUsuarioBD(String passwordUsuarioBD) {
        this.passwordUsuarioBD = passwordUsuarioBD;
    }

    public int getPuertoServidorBD() {
        return puertoServidorBD;
    }

    public void setPuertoServidorBD(Integer puertoServidorBD) {
        this.puertoServidorBD = puertoServidorBD;
    }

    public PreparedStatement getSentencia() {
        return sentencia;
    }

    public void setSentencia(PreparedStatement sentencia) {
        this.sentencia = sentencia;
    }

    public String getUrl() {
        return url + nombreIPServidorBD + ":" + puertoServidorBD + "/" + nombreBD + propiedades;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public String getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(String propiedades) {
        this.propiedades = propiedades;
    }

    public String getConnectionString() {
        return connectionString = url + nombreIPServidorBD + ":" + puertoServidorBD + "/" + nombreBD + propiedades;
    }
    
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}