package models;

import java.util.ArrayList;

public class Usuario {
    private Integer id;
    private String nombre;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }

    public Usuario(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre;
    }
}
