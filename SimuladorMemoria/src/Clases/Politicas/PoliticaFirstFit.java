package Clases.Politicas;

import java.util.ArrayList;
import java.util.List;


import Clases.*;

public class PoliticaFirstFit {


    public PoliticaFirstFit(){}

       public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Crear una nueva particion unificada
                Particion nuevaParticion = new Particion(-1, tamanioUnificado, true, -1,0,-1);
                listaParticiones.set(i, nuevaParticion);  // Reemplazar la particion actual por la unificada

                // Eliminar la particion siguiente
                listaParticiones.remove(i + 1);
            }else{
                i++;
            }
        }
        return listaParticiones;
    }
    public int calcularGraficoParticion(List<Particion> listaParticiones,Particion particion ,int graficarParticion) {
        for (Particion p : listaParticiones) {
            // Verifica si la particion esta activa y es la ultima de la lista
            if (p.getEstado() && p.equals(listaParticiones.get(listaParticiones.size() - 1))) {
                break;
            }

            // Si la particion actual es igual a la particion en cuestion, detener el bucle
            if (p.equals(particion)) {
                break;
            }

            // Sumar el tamanio de la particion al eje Y
            graficarParticion += p.getTamanio();
        }
        return graficarParticion;
    }

    //Significado estados: true libre / false ocupada
    public Resultado firstFit(List<Particion> listaParticiones, List<Tarea> listaTareas, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
        int fragmentacionExterna = 0;
        int tiempoActual = 0;
        int indice = 0;
        List<Particion> particiones = new ArrayList<>();

        System.out.println("Estoy en la politica firstFit ");

        while (!listaTareas.isEmpty()) {
            Tarea tareaActual = listaTareas.get(indice);
            System.out.println("La Tarea: " + tareaActual.getNombre() + " esta esperando una particion, tamanio: " + tareaActual.getTamanio());
            System.out.println("Tiempo actual: " + tiempoActual);

            // Liberar particiones si ha llegado su tiempo de finalizacion
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            // Unificar particiones libres
            listaParticiones = unificarParticiones(listaParticiones);

            // Mostrar particiones disponibles
            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: " + particion);
            }

            // Buscar la primera particion que pueda contener a el proceso
            boolean preparado = true;
            int i = 0;

            while (preparado && i < listaParticiones.size()) {
                Particion particion = listaParticiones.get(i);

                if (particion.getEstado() && particion.getTamanio() >= tareaActual.getTamanio()) {
                    preparado = false;
                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + tareaActual.getDuracion() + tiempoLiberacion;

                    if (particion.getTamanio() == tareaActual.getTamanio()) { //Encontro una particion adecuada

                        //Calculo el tamanio de la particion para graficarla,
                        int graficarParticion = 0;
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);
                                

                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                tareaActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                tareaActual.getID()
                        );
                        System.out.println("La Tarea " + tareaActual.getNombre() + " encontro una particion");
                        System.out.println(particionEncontrada);

                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                        listaParticiones.remove(particion);
                    

                    } else if (particion.getTamanio() > tareaActual.getTamanio()) {

                        int graficarParticion = 0;
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                tareaActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                tareaActual.getID()
                        );
                        System.out.println("La Tarea " + tareaActual.getNombre() + " encontro una particion");
                        System.out.println(particionEncontrada);
                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionEncontrada);
                        Particion particionSobrante = new Particion(
                                -1,
                                particion.getTamanio() - tareaActual.getTamanio(),
                                true,
                                -1,
                                0,
                                -1
                        );
                        listaParticiones.add(listaParticiones.indexOf(particion) + 2, particionSobrante);

                        listaParticiones.remove(particion);
                    }

                    listaTareas.remove(indice);  // Eliminar el proceso una vez asignado
                }
                i++;
            }



            // Calcular fragmentacion externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaTareas.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();
                    System.out.println("Fragmentacion externa: " + fragmentacionExterna);
                }
            }


            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles actualmente: [" + particion + "]");
            }

            tiempoActual++;
            System.out.println("Tamanio de la lista de Tareas: " + listaTareas.size());
            System.out.println("------------------------------");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Calculo el mayor tiempo de finalizacion
        Particion particionConMayorTiempo = listaParticiones != null && !listaParticiones.isEmpty()
                ? listaParticiones.stream()
                .max((p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()))
                .orElse(null)
                : null;

        int longitud = particionConMayorTiempo != null
                ? particionConMayorTiempo.getTiempoFinalizacion()
                : tiempoActual;


        System.out.println("fragmentacion externa TOTAL: " + fragmentacionExterna);
         resultado.setlistaDeParticiones(particiones);
         resultado.setLongitudTarea(longitud);

         //Devuelvo la lista con las particiones y la longitud
        return resultado;
    }
}
