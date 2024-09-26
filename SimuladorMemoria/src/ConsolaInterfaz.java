import Clases.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolaInterfaz {
    private Simulador simulador;
    public static final int FIRST_FIT = 1;
    public static final int BEST_FIT = 2;
    public static final int NEXT_FIT = 3;
    public static final int WORST_FIT = 4;

    public static void main(String[] args) {
        ConsolaInterfaz consola = new ConsolaInterfaz();
        consola.cargarYSimular();
    }

    private void cargarYSimular() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del archivo: ");
        String nombreArchivo = scanner.nextLine();

        System.out.print("Tamaño de la memoria: ");
        int tamanioMemoria = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de selección: ");
        int tiempoSeleccion = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de carga promedio: ");
        int tiempoCargaPromedio = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de liberación: ");
        int tiempoLiberacion = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Política de asignación:");
        System.out.println("1 - First Fit");
        System.out.println("2 - Best Fit");
        System.out.println("3 - Next Fit");
        System.out.println("4 - Worst Fit");
        System.out.print("Selecciona una opción (1-4): ");
        
        int estrategiaAsignacion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del scanner

        List<Proceso> listaProcesos = cargarProcesosDesdeArchivo(nombreArchivo);

        if (listaProcesos == null || listaProcesos.isEmpty()) {
            System.out.println("No se pudieron cargar procesos del archivo.");
            return;
        }

        // Validar y ajustar la estrategia de asignación
        if (estrategiaAsignacion < 1 || estrategiaAsignacion > 4) {
            System.out.println("Política no reconocida. Se usará FIRST_FIT por defecto.");
            estrategiaAsignacion = FIRST_FIT; // Usar FIRST_FIT como predeterminado
        }

        // Ajustar estrategia a constantes
        switch (estrategiaAsignacion) {
            case 1:
                estrategiaAsignacion = FIRST_FIT;
                break;
            case 2:
                estrategiaAsignacion = BEST_FIT;
                break;
            case 3:
                estrategiaAsignacion = NEXT_FIT;
                break;
            case 4:
                estrategiaAsignacion = WORST_FIT;
                break;
        }



            // Crear el simulador con la estrategia seleccionada
            simulador = new Simulador(listaProcesos, tamanioMemoria, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, estrategiaAsignacion);
            
            List<Particion> particionesAgraficar = new ArrayList<>();

            // Ejecutar la simulación
            particionesAgraficar = simulador.simular();

       //     mostrarResultados(particionesAgraficar);

         //   int fragmentacionExterna = simulador.simular();
        //    System.out.println("Fragmentación externa: " + fragmentacionExterna);
        
    }


    private static List<Proceso> cargarProcesosDesdeArchivo(String nombreArchivo) {
        List<Proceso> procesos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] datos = linea.split(",");
                if (datos.length != 5) {
                    System.out.println("Línea en el archivo no válida: " + linea);
                    continue;
                }

                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    int memoriaRequerida = Integer.parseInt(datos[2].trim());
                    int duracion = Integer.parseInt(datos[3].trim());
                    int instanteArribo = Integer.parseInt(datos[4].trim());

                    Proceso proceso = new Proceso(id, nombre, memoriaRequerida, duracion, instanteArribo);
                    procesos.add(proceso);
                } catch (NumberFormatException e) {
                    System.out.println("Error de formato en la línea: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return procesos;
    }
}
