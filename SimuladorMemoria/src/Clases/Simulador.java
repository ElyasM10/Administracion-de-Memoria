package Clases;


import java.util.ArrayList;
import java.util.List;


public class Simulador {
    private List<Particion> listaParticiones;
    private List<Proceso> procesos;
    private int estrategiaActual;
    private AsignacionMemoria asignador;
    private int tamanioMemoria;
    private int tiempoSeleccion;
    private int tiempoCargaPromedio;
    private int tiempoLiberacion;
    private   List<Particion> particionesFinal = new ArrayList<>();
   // private Registro registro;




    public Simulador(List<Proceso> procesos, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, int estrategia) {
        this.procesos = procesos;
        this.tamanioMemoria = tamanioMemoria;
        this.tiempoSeleccion = tiempoSeleccion;
        this.tiempoCargaPromedio = tiempoCargaPromedio;
        this.tiempoLiberacion = tiempoLiberacion;
        this.estrategiaActual = estrategia;

        // Inicializamos la lista de particiones antes de pasarla al asignador
        this.listaParticiones = new ArrayList<>();
        this.asignador = new AsignacionMemoria();
     /*
        try {
            this.registro = new Registro("eventos.txt", "estado_particiones.txt");
        } catch (IOException e) {
            System.out.println("Error al crear los archivos de registro: " + e.getMessage());
        }
        */
    }
    //Esto solo es usado para saber si el simulador esta funcionando correctamente
    public void imprimirDatosSimulador() {
        System.out.println("Datos del Simulador:");
        System.out.println("--------------------");

        System.out.println("Tamaño de la Memoria: " + tamanioMemoria);
        System.out.println("Tiempo de Selección: " + tiempoSeleccion);
        System.out.println("Tiempo de Carga Promedio: " + tiempoCargaPromedio);
        System.out.println("Tiempo de Liberación: " + tiempoLiberacion);
        System.out.println("Estrategia de Asignación: " + estrategiaActual);

        System.out.println("Lista de Procesos:");
        for (Proceso proceso : procesos) {
            System.out.println("  - Proceso: " + proceso.getNombre() + ", Tamanio: " + proceso.getTamanio() + ", Duración: " + proceso.getDuracion());
        }

        System.out.println("Cantidad de Particiones: " + listaParticiones.size());
    }

    public Simulador() {
    }

    public List<Particion> asignarParticion(List<Particion> listaParticiones, List<Proceso> procesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
        Particion particionAsignada = null;

        switch (estrategiaActual) {
            case 1 -> {
                System.out.println("Entrando a First Fit");
                particionesFinal = asignador.firstFit(listaParticiones, procesos, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, resultado);
            }
            case 2 -> {
                System.out.println("Entrando a Best Fit");
                particionesFinal = asignador.bestFit(listaParticiones, procesos, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
            }
            case 3 -> {
                System.out.println("Entrando a Next Fit");
                particionesFinal = asignador.nextFit(listaParticiones, procesos, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
            }
            case 4 -> {
                System.out.println("Entrando a Worst Fit");
                particionesFinal = asignador.worstFit(listaParticiones, procesos, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
            }
        }

        return listaParticiones;
    }
   

    
public List<Particion> simular() {


        System.out.println("Entrando al simulador");

  //      imprimirDatosSimulador();

        Particion particionInicial = new Particion(0, -1, tamanioMemoria, true, -1,0);
        listaParticiones.add(particionInicial);

        Resultado resultado = new Resultado();



      
        particionesFinal = asignarParticion(listaParticiones, procesos, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, resultado);

      //  return resultado.getFragmentacion();
          return particionesFinal;

    }
 
    
   //     System.out.println("La fragmentacion es: "+resultado.getFragmentacion());


/*
        try {
            registro.cerrar();
        } catch (IOException e) {
            System.out.println("Error al cerrar los archivos de registro: " + e.getMessage());
        }
*/






    /*
    private void dividirParticion(Particion particion, int tamanioRequerido) {
        if (particion.gettamanio() > tamanioRequerido) {
            int nuevaTamanio = particion.gettamanio() - tamanioRequerido;
            int nuevaDireccion = particion.getDireccionComienzo() + tamanioRequerido;

            Particion nuevaParticion = new Particion(particiones.size() + 1, nuevaDireccion, nuevaTamanio);
            particiones.add(particiones.indexOf(particion) + 1, nuevaParticion);

            particion.settamanio(tamanioRequerido);
        }
        particion.setOcupada(true);
    }
*/



    private void liberarProceso(Proceso proceso, Particion particion) {

        try {
            System.out.println("Liberando partición para el proceso: " + proceso.getNombre());
            Thread.sleep(proceso.getDuracion() * 1000);
        } catch (InterruptedException e) {
            System.out.println("Error al liberar el proceso: " + e.getMessage());
        }

        particion.setEstado(false); // Marcar la partición como libre
        System.out.println("Partición liberada para el proceso: " + proceso.getNombre());
    }


    public void setEstrategiaActual(int estrategia) {
        this.estrategiaActual = estrategia;
    }




    private void calcularIndicadores() {
        int tiempoTotalRetorno = 0;
        int cantidadProcesos = procesos.size();

        for (Proceso proceso : procesos) {
            int tiempoRetorno = (proceso.getInstanteArribo() + proceso.getDuracion()) - proceso.getInstanteArribo();
            tiempoTotalRetorno += tiempoRetorno;
            System.out.printf("Proceso: %s, Tiempo de Retorno: %d%n", proceso.getNombre(), tiempoRetorno);
        }

        if (cantidadProcesos > 0) {
            double tiempoMedioRetorno = (double) tiempoTotalRetorno / cantidadProcesos;
            System.out.printf("Tiempo Medio de Retorno: %.2f%n", tiempoMedioRetorno);
        } else {
            System.out.println("No hay procesos para calcular el tiempo medio de retorno.");
        }
    }


    private void imprimirResultados() {
        System.out.println("\nDiagrama de Gantt de la simulación:");
        System.out.println("Tiempo de arribo, Proceso, Inicio, Fin");

        // Encontrar el tiempo máximo para definir el ancho del diagrama de Gantt
        int tiempoMaximo = procesos.stream()
                .mapToInt(p -> p.getInstanteArribo() + p.getDuracion())
                .max()
                .orElse(0);


        System.out.print("0");
        for (int t = 1; t <= tiempoMaximo; t++) {
            System.out.print("----");
        }
        System.out.println();

        // Para cada proceso, imprimimos una representación de su ejecución en la consola
        for (Proceso proceso : procesos) {
            int tiempoInicio = proceso.getInstanteArribo();
            int tiempoFin = tiempoInicio + proceso.getDuracion();

            // Imprimir el proceso y su representación visual en el diagrama de Gantt
            StringBuilder ganttBar = new StringBuilder();
            ganttBar.append("|");
            for (int t = 0; t < tiempoMaximo; t++) {
                if (t >= tiempoInicio && t < tiempoFin) {
                    ganttBar.append("****"); // Representación de una unidad de tiempo
                } else {
                    ganttBar.append("    "); // Espacio en blanco
                }
            }
            ganttBar.append("|");

            // Imprimir el detalle del proceso
            System.out.printf("Proceso: %s, Inicio: %d, Fin: %d %s%n",
                    proceso.getNombre(), tiempoInicio, tiempoFin, ganttBar.toString());
        }

        // Imprimir la línea de tiempo al final del diagrama de Gantt
        System.out.print("0");
        for (int t = 1; t <= tiempoMaximo; t++) {
            System.out.print("----");
        }
        System.out.println();
    }

    public List<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(List<Proceso> procesos) {
        this.procesos = procesos;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }

    public void setTiempoSeleccion(int tiempoSeleccion) {
        this.tiempoSeleccion = tiempoSeleccion;
    }

    public void setTiempoCargaPromedio(int tiempoCargaPromedio) {
        this.tiempoCargaPromedio = tiempoCargaPromedio;
    }

    public void setTiempoLiberacion(int tiempoLiberacion) {
        this.tiempoLiberacion = tiempoLiberacion;
    }



}
