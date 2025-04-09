import biblioteca.Biblioteca;
import biblioteca.EstadoBiblioteca;
import models.Libro;
import models.Usuario;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // INICIALIZAR BIBLIOTECA
        Biblioteca biblioteca = new Biblioteca();

        // LEVANTAR EL HILO PARA VE EL ESTADO ACTUAL DE LA BIBLIOTECA
        EstadoBiblioteca estado = new EstadoBiblioteca(biblioteca);
        estado.start();

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n--- Menú Biblioteca ---");
            System.out.println("1. Agregar libro");
            System.out.println("2. Registrar usuario");
            System.out.println("3. Prestar libro");
            System.out.println("4. Mostrar historial de préstamos");
            System.out.println("5. Detener hilo de estado");
            System.out.println("6. Reiniciar hilo de estado");
            System.out.println("7. apagar servidor de estado");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese ID del libro: ");
                    int idLibro = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    System.out.print("Ingrese título del libro: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Ingrese autor del libro: ");
                    String autor = scanner.nextLine();
                    biblioteca.agregarLibro(new Libro(idLibro, titulo, autor));
                    System.out.println("📚 Libro agregado correctamente.");
                    break;
                case 2:
                    System.out.print("Ingrese ID del usuario: ");
                    int idUsuario = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese nombre del usuario: ");
                    String nombre = scanner.nextLine();
                    biblioteca.registrarUsuario(new Usuario(idUsuario, nombre));
                    System.out.println("👤 Usuario registrado correctamente.");
                    break;
                case 3:
                    System.out.print("Ingrese ID del usuario que solicita el préstamo: ");
                    int usuarioId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese ID del libro a prestar: ");
                    int libroId = scanner.nextInt();
                    scanner.nextLine();

                    Usuario usuario = biblioteca.getUsuarios()
                            .stream()
                            .filter(u -> u.getId() == usuarioId)
                            .findFirst()
                            .orElse(null);

                    if (usuario != null) {
                        if (biblioteca.prestarLibro(libroId, usuario)) {
                            System.out.println("✅ Préstamo realizado con éxito.");
                        }
                    } else {
                        System.out.println("⚠ Usuario no encontrado.");
                    }
                    break;
                case 4:
                    biblioteca.mostrarHistorialPrestamos();
                    break;
                case 5:
                    estado.detenerHilo(); // Detenemos el hilo de estado
                    System.out.println("⏸ Hilo de estado detenido.");
                    break;
                case 6:
                    estado = EstadoBiblioteca.reiniciarHilo(biblioteca); // Reiniciar el hilo de estado
                    System.out.println("⏸ Reiniciando hlo de estado.");
                    break;
                case 7:
                    System.out.println("👋 Enviar comando a servidor...");
                    EstadoBiblioteca.enviarCommandoServidor("SHUTDOWN");
                    break;
                case 8:
                    salir = true;
                    System.out.println("👋 Saliendo del sistema...");
                    break;
                default:
                    System.out.println("⚠ Opción no válida, intente de nuevo.");
            }
        }
        scanner.close();
    }
}