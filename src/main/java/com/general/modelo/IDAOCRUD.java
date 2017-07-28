package com.general.modelo;

import java.util.List;

public interface IDAOCRUD<T> {
    public Boolean insertar(T Objeto);
    public T buscar(T Objeto);
    public Boolean modificar(T Objeto);
    public Boolean eliminar(T Objeto);
    public List<T> listar();
}