package Clases.Politicas;

import Clases.*;

import java.util.ArrayList;
import java.util.List;

public class PoliticaNextFit {

    public PoliticaNextFit(){}

    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Creo una nueva particion unificada
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

            // Sumar el tamanio de la partición al eje Y
            graficarParticion += p.getTamanio();
        }
        return graficarParticion;
    }


    public Resultado nextFit(List<Particion> listaParticiones, List<Tarea> listaTareas, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

      System.out.println("Estoy en la politica nextFit ");

        List<Particion> particiones = new ArrayList<>();
        int index = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;
        int i = 0;

        while (!listaTareas.isEmpty()) {
            Tarea tareaActual = listaTareas.get(index);
            System.out.println("Tarea: " + tareaActual.getNombre() + " esperando particion");
            System.out.println("Tiempo actual: " + tiempoActual);

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones: [" + particion + "]");
            }

            // Recorrer particiones y liberar si su tiempo de finalizacion es igual al tiempo actual
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            listaParticiones = unificarParticiones(listaParticiones);

            boolean preparado = true;

            while (preparado && i < listaParticiones.size()) {
                Particion particion = listaParticiones.get(i);
                if (particion.getEstado() && particion.getTamanio() >= tareaActual.getTamanio()) {
                    preparado = false;

                    if (particion.getTamanio() == tareaActual.getTamanio()) {

                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + tareaActual.getDuracion() + tiempoLiberacion;


                        //Calculo el tamanio de la particion para despues graficarla
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
                        particiones.add(particionEncontrada);

                        listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                        listaParticiones.remove(particion);

                        listaTareas.remove(tareaActual);
                    } else if (particion.getTamanio() > tareaActual.getTamanio()) {
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + tareaActual.getDuracion() + tiempoLiberacion;

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
                        listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionEncontrada);
                        particiones.add(particionEncontrada);

                        Particion particionLibre = new Particion(
                                -1,
                                particion.getTamanio() - tareaActual.getTamanio(),
                                true,
                                -1,
                                0,
                                -1
                        );
                        listaParticiones.add(listaParticiones.indexOf(particion) + 2, particionLibre);

                        listaParticiones.remove(particion);
                        listaTareas.remove(tareaActual);

                        System.out.println("La Tarea " + tareaActual.getNombre() + " encontro una particion");
                    }
                }
                i++;
            }

            // Calcular fragmentacion externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado() ) {
                    fragmentacionExterna+= particion.getTamanio();

                }
            }

            System.out.println("Fragmentación externa: " + fragmentacionExterna);
            tiempoActual++;

            if (i == listaParticiones.size()) {
                i = 0;
            }

            System.out.println("Longitud de la lista de Tareas: " + listaTareas.size());
            System.out.println("------------------------------");


        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Particion particionConMayorTiempo = listaParticiones != null && !listaParticiones.isEmpty()
                ? listaParticiones.stream()
                .max((p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()))
                .orElse(null)
                : null;

        int longitud = particionConMayorTiempo != null
                ? particionConMayorTiempo.getTiempoFinalizacion()
                : tiempoActual;


        System.out.println("fragmentación externa TOTAL: " + fragmentacionExterna);
        resultado.setlistaDeParticiones(particiones);
        resultado.setLongitudTarea(longitud);
        return resultado;
    }


}
