package biblioteca;


import java.io.PrintWriter;
import java.net.Socket;

public class EstadoBiblioteca extends Thread {
    private Biblioteca biblioteca;
    private volatile boolean activo = true;

    public EstadoBiblioteca(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
    }

    public void run() {
        while (activo) {
            try {
                Thread.sleep(10000);
                mostrarEstadoBiblioteca();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void mostrarEstadoBiblioteca() {
        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            int totalLibros = biblioteca.obtenerTotalLibros();
            int librosPrestados = biblioteca.obtenerCantidadLibrosPrestados();
            int librosDisponibles = totalLibros - librosPrestados;

            String mensaje = "📚 Total de libros: " + totalLibros +
            "📖 Libros prestados: " + librosPrestados +
            "✅ Libros disponibles: " + librosDisponibles;

            out.println(mensaje);

        } catch (Exception e) {
            System.out.printf("No se pudo conectar con el servidor de estado: %s", e.getMessage());
        }
    }

    // Método para detener el hilo de manera controlada
    public static void enviarCommandoServidor(String comando) {
        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(comando);
                System.out.println("Comando enviado");
        } catch (Exception e) {
            System.out.printf("No se pudo conectar con el servidor de estado: %s", e.getMessage());
        }
    }

    // Método para detener el hilo de manera controlada
    public void detenerHilo() {
        activo = false;
        this.interrupt(); // En caso de que el hilo esté durmiendo, lo interrumpe
    }

    // Método para reiniciar el hilo de estado
    // Método para crear y arrancar un nuevo hilo de estado
    public static EstadoBiblioteca reiniciarHilo(Biblioteca biblioteca) {
        EstadoBiblioteca nuevoHilo = new EstadoBiblioteca(biblioteca);
        nuevoHilo.start();  // Inicia un nuevo hilo
        return nuevoHilo; // Devuelve el nuevo hilo creado
    }
}
