import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Main {
    private static final int PUERTO = 5000;
    private static final ExecutorService poolLCientes = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService monitorEstado = Executors.newSingleThreadScheduledExecutor();
    private static ServerSocket serverSocket;
    private static boolean executed = true;

    public static void main(String[] args) {

        try {

            serverSocket = new ServerSocket(PUERTO);
            System.out.printf("Servidor de estado iniciado en el puerto %n", PUERTO);

            // HILO QUE NO SE PUEDE DETENER (INFINITO HASTA QUE SE DETENGA LA EJECUCION)
            /*while(true){
                Socket cliente = serverSocket.accept();
                System.out.printf("cliente conectado desde %s", cliente.getInetAddress());

                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {
                        String linea;
                        while((linea = in.readLine()) != null) {
                            System.out.println("\n Estado recibido desde el cliente");
                            System.out.println(linea);
                        }
                    } catch (Exception ex) {
                        System.err.printf("Error en la conexion %s", ex.getMessage());
                    }
                }).start();
            }*/

            //HILO QUE PUEDE SER DETENIDO
            while (executed) {
                Socket cliente = serverSocket.accept();
                if (!monitorEstado.isShutdown()) {
                    System.out.printf("cliente conectado desde %s", cliente.getInetAddress());

                    monitorEstado.scheduleAtFixedRate(() -> {
                        System.out.println("\n Servidor sigue activo...");
                    }, 0, 30, TimeUnit.SECONDS);
                }
                poolLCientes.submit(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {
                        String linea;
                        while((linea = in.readLine()) != null) {
                            if ("SHUTDOWN".equalsIgnoreCase(linea.trim())) {
                                System.out.println("\n Comando SHUTDOWN recibido. Apagando servidor");
                                apagarServidor();
                            } else {
                                System.out.println("\n Estado recibido desde el cliente");
                                System.out.println(linea);
                            }
                        }
                    } catch (Exception ex) {
                        System.err.printf("Error en la conexion %s", ex.getMessage());
                    }
                });
            }
        } catch (Exception ex) {
            System.err.printf("Error en la conexion %s%n", ex.getMessage());
        }
    }

    private static void apagarServidor() {
        System.out.println("Apagar el servidor");
        executed = false;
        try {
            poolLCientes.shutdownNow();
            monitorEstado.shutdownNow();
            serverSocket.close();
            System.out.println("Servidor Detenido coreectamente");
        } catch (Exception e) {
            System.out.println("Error al detener el servidor" + e.getMessage());
        }
    }


}