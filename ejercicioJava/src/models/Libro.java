package models;

public class Libro {
    private Integer id;
    private String titulo;
    private String autor;
    private boolean disponible;

    public Libro(Integer id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;
    }

    public Integer getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return id + ", " + titulo + " - " + autor + (disponible ? " (Disponible)" : " (No disponible)");
    }
}
