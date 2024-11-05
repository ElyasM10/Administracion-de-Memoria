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
        //Cargo los datos para la simulacion y creo la consola
        ConsolaInterfaz consola = new ConsolaInterfaz();
        consola.cargarYSimular();
    }

    private void cargarYSimular() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del archivo: ");
        String nombreArchivo = scanner.nextLine();

        System.out.print("Tamanio de la memoria: ");
        int tamanioMemoria = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de seleccion: ");
        int tiempoSeleccion = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de carga promedio: ");
        int tiempoCargaPromedio = Integer.parseInt(scanner.nextLine());
        System.out.print("Tiempo de liberacion: ");
        int tiempoLiberacion = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Politica de asignacion:");
        System.out.println("1 - First Fit");
        System.out.println("2 - Best Fit");
        System.out.println("3 - Next Fit");
        System.out.println("4 - Worst Fit");
        System.out.print("Selecciona una opcion (1-4): ");
        
        int estrategiaAsignacion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del scanner

        //Asigno los procesos del archivo a una lista
        List<Tarea> listaTareas = cargarProcesosDesdeArchivo(nombreArchivo);

        if (listaTareas == null || listaTareas.isEmpty()) {
            System.out.println("No se pudieron cargar procesos del archivo.");
            return;
        }

        String estrategiaSeleccionada="";
        // Validar y ajustar la estrategia de asignacion
        if (estrategiaAsignacion < 1 || estrategiaAsignacion > 4) {
            System.out.println("Politica no reconocida. Se usara FIRST_FIT por defecto.");
            estrategiaAsignacion = FIRST_FIT; // Usar FIRST_FIT como predeterminado
            estrategiaSeleccionada="First Fit";
        }

        // Selecion de estrategia 
     
        switch (estrategiaAsignacion) {
            case 1:
                estrategiaAsignacion = FIRST_FIT;
                estrategiaSeleccionada="First Fit";
                break;
            case 2:
                estrategiaAsignacion = BEST_FIT;
                estrategiaSeleccionada="Best Fit";
                break;
            case 3:
                estrategiaAsignacion = NEXT_FIT;
                estrategiaSeleccionada="Next Fit";
                break;
            case 4:
                estrategiaAsignacion = WORST_FIT;
                estrategiaSeleccionada="Worst Fit";
                break;
        }
            // Crear el simulador con la estrategia seleccionada
            simulador = new Simulador(listaTareas, tamanioMemoria, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, estrategiaAsignacion);

            // Ejecutar la simulacion
            Resultado res = simulador.simular();

            //Creo el diagrama para mostrarlo en consola


        DiagramaDeGantt dg = new DiagramaDeGantt(res.getlistaDeParticiones(),tamanioMemoria,res.getFragmentacion(),res.getLongitudTarea(),  estrategiaSeleccionada);
        dg.imprimirDiagrama();
        System.out.println("No pude ajustar correctamente el tiempo de inicio del eje X");

    }


    private static List<Tarea> cargarProcesosDesdeArchivo(String nombreArchivo) {
        List<Tarea> tareas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] datos = linea.split(",");
                if (datos.length != 5) {
                    System.out.println("Linea en el archivo no válida: " + linea);
                    continue;
                }

                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    int memoriaRequerida = Integer.parseInt(datos[2].trim());
                    int duracion = Integer.parseInt(datos[3].trim());
                    int instanteArribo = Integer.parseInt(datos[4].trim());

                    Tarea tarea = new Tarea(id, nombre, memoriaRequerida, duracion, instanteArribo);
                    tareas.add(tarea);
                } catch (NumberFormatException e) {
                    System.out.println("Error de formato en la linea: " + linea + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return tareas;
    }
}
