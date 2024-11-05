package Clases;


import java.util.ArrayList;
import java.util.List;
import Clases.Politicas.*;


public class Simulador {
    private List<Particion> listaParticiones;
    private List<Tarea> tareas;
    private int estrategiaActual;
    private int tamanioMemoria;
    private int tiempoSeleccion;
    private int tiempoCargaPromedio;
    private int tiempoLiberacion;
    private   List<Particion> particionesFinal = new ArrayList<>();





    public Simulador(List<Tarea> tareas, int tamanioMemoria, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, int estrategia) {
        this.tareas = tareas;
        this.tamanioMemoria = tamanioMemoria;
        this.tiempoSeleccion = tiempoSeleccion;
        this.tiempoCargaPromedio = tiempoCargaPromedio;
        this.tiempoLiberacion = tiempoLiberacion;
        this.estrategiaActual = estrategia;
        // Inicializamos la lista de particiones
        this.listaParticiones = new ArrayList<>();
    }
    //Esto solo es usado para saber si el simulador esta funcionando correctamente
    public void imprimirDatosSimulador() {
        System.out.println("Datos del Simulador:");
        System.out.println("--------------------");

        System.out.println("Tamanio de la Memoria: " + tamanioMemoria);
        System.out.println("Tiempo de Seleccion: " + tiempoSeleccion);
        System.out.println("Tiempo de Carga Promedio: " + tiempoCargaPromedio);
        System.out.println("Tiempo de Liberacion: " + tiempoLiberacion);
        System.out.println("Estrategia de Asignacion: " + estrategiaActual);

        System.out.println("Lista de Procesos:");
        for (Tarea tarea : tareas) {
            System.out.println("  - Tarea: " + tarea.getNombre() + ", Tamanio: " + tarea.getTamanio() + ", Duracion: " + tarea.getDuracion());
        }

        System.out.println("Cantidad de Particiones: " + listaParticiones.size());
    }

    public Simulador() {
    }

    public Resultado asignarParticion(List<Particion> listaParticiones, List<Tarea> tareas, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
        switch (estrategiaActual) {
            case 1 -> {
                System.out.println("Simulador: First Fit");
                PoliticaFirstFit ff = new PoliticaFirstFit();
                resultado = ff.firstFit(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, resultado);
            }
            case 2 -> {
                System.out.println("Simulador: Best Fit");
                PoliticaBestFit bf = new PoliticaBestFit();

                resultado = bf.bestFit(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, resultado);
            }
            case 3 -> {
                System.out.println("Simulador: Next Fit");
                PoliticaNextFit nf = new PoliticaNextFit();
                //particionesFinal = asignador.nextFit(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
                resultado = nf.nextFit(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
            }
            case 4 -> {
                System.out.println("Simulador: Worst Fit");
                PoliticaWorstFit wf = new PoliticaWorstFit();
                resultado= wf.worstFit(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion,resultado);
            }
        }

        return resultado;
    }
   

    
public Resultado  simular() {
        System.out.println("Entrando al simulador");
  //      imprimirDatosSimulador();
        Particion particionInicial = new Particion(-1, tamanioMemoria, true, -1,0,-1);
        listaParticiones.add(particionInicial);

        Resultado resultado = new Resultado();

        resultado = asignarParticion(listaParticiones, tareas, tiempoSeleccion, tiempoCargaPromedio, tiempoLiberacion, resultado);

        // Imprimir resultados de los tiempos de retorno
        for (Particion p : resultado.getlistaDeParticiones()) {
            System.out.println("Tiempo retorno de la tarea " + p.getIdTarea() + ": " + p.getTiempoFinalizacion());
        }
        System.out.println("Tiempo de retorno de la tanda: " + resultado.getLongitudTarea());

        // Calcular y mostrar el tiempo medio de retorno
        double total = resultado.getlistaDeParticiones().stream().mapToDouble(Particion::getTiempoFinalizacion).sum();
        System.out.println("Tiempo medio de retorno: " + (total / resultado.getlistaDeParticiones().size()));



    return resultado;
    }
 

    public void setEstrategiaActual(int estrategia) {
        this.estrategiaActual = estrategia;
    }


    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {

        this.tareas = tareas;
    }

    public void setTareas(int tamanioMemoria) {
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
