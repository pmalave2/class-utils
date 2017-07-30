package com.general.modelo;

import java.util.List;

public interface IDAOCRUD<T> {
    public Boolean insertar(T obj);
    public T buscar(T obj);
    public Boolean modificar(T obj);
    public Boolean eliminar(T obj);
    public List<T> listar();
}