package biblioteca;

import models.Libro;
import models.Usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Biblioteca {

    private List<Libro> libros;
    private List<Usuario> usuarios;
    private Map<Usuario, List<Libro>> prestamos;

    private HistorialPrestamos historial;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void mostrarHistorialPrestamos() {
        historial.mostrarHistorial();
    }

    /** CONSTRUCTOR DE BIBLIOTECA */
    public Biblioteca() {
        this.libros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.prestamos = new HashMap<>();
        this.historial = new HistorialPrestamos();
        cargarLibrosDesdeArchivo("libros.csv");
    }

    /**
     * AGREGAR NUEVO LIBRO
     * @param libro
     */
    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    /**
     * REGISTRAR NUEVO USUARIO CON LISTA VACIA DE LIBROS PRESTADOS
     * @param usuario
     */
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        prestamos.put(usuario, new ArrayList<>()); // Inicializa su lista de préstamos
    }

    /**
     * OBTENER TOTAL DE LIBROS REGISTRADOS
     * @return
     */
    public int obtenerTotalLibros() {
        return libros.size();
    }

    /**
     * OBTENER LA CANTIDAD DE LIBROS PRESTADOS
     * @return
     */
    public int obtenerCantidadLibrosPrestados() {
        return (int) libros.stream().filter(libro -> !libro.isDisponible()).count();
    }

    /**
     * METODO PARA PRESTAR UN LIBRO DISPONIBLE A UN USUARIO
     * @param libroId
     * @param usuario
     */
    public boolean prestarLibro(Integer libroId, Usuario usuario) {
        Libro libro = buscarLibroPorId(libroId);
        if (libro != null && libro.isDisponible()) {
            libro.setDisponible(false);
            prestamos.get(usuario).add(libro);
            historial.registrarPrestamo(usuario, libro);
            return true;
        } else {
            System.out.println("El libro no está disponible o no existe.");
            return false;
        }
    }

    /**
     * OBTENER LOS LIBROS PRESTADOS POR USUARIO
     * @param usuario
     * @return
     */
    public List<Libro> obtenerLibrosPrestados(Usuario usuario) {
        return prestamos.getOrDefault(usuario, Collections.emptyList());
    }

    public Libro buscarLibroPorId(Integer id) {
        return libros.stream()
                .filter(libro -> libro.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void cargarLibrosDesdeArchivo(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                agregarLibro(new Libro(Integer.parseInt(partes[0]), partes[1], partes[2]));
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    // Clase interna para gestionar historial de préstamos
    private static class HistorialPrestamos {
        private List<String> historial = new ArrayList<>();

        public void registrarPrestamo(Usuario usuario, Libro libro) {
            historial.add("El libro" + libro.getTitulo() + " se prestó al usuario " + usuario.getNombre());
        }

        public void mostrarHistorial() {
            if (historial.isEmpty()) {
                System.out.println("No hay préstamos registrados.");
            } else {
                System.out.println("Historial de préstamos:");
                historial.forEach(System.out::println);
            }
        }
    }
}
