package Clases.Politicas;

import Clases.Particion;
import Clases.Proceso;
import Clases.Resultado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PoliticaBestFit {



    public PoliticaBestFit(){}


    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Crear una nueva partición unificada
                Particion nuevaParticion = new Particion(-1, -1, tamanioUnificado, true, -1,0);
                listaParticiones.set(i, nuevaParticion);  // Reemplazar la partición actual por la unificada

                // Eliminar la partición siguiente
                listaParticiones.remove(i + 1);
            }else{
                i++;
            }
        }
        return listaParticiones;
    }

    public int calcularGraficoParticion(List<Particion> listaParticiones,Particion particion ,int graficarParticion) {
        for (Particion part : listaParticiones) {
            if (part.getEstado()) {
                graficarParticion += part.getTamanio();
            }
            if (part == particion) {
                // Rompe el bucle al llegar a la partición actual
                break;
            }
        }
        return graficarParticion;
    }


    public List<Particion> bestFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

        System.out.println("Estoy en la politica bestFit ");

        List<Particion> particiones = new ArrayList<>();
        int indice = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;

        while (listaProcesos.size() > 0) {
            Proceso ProcesoActual = listaProcesos.get(indice);
            System.out.println("Proceso: " + ProcesoActual.getNombre() + " esperando partición");
            System.out.println("Tiempo actual: " + tiempoActual);

            // Verificar si alguna particion debe liberarse en el tiempo actual
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            // Unificar particiones libres
            listaParticiones = unificarParticiones(listaParticiones);

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: [" + particion + "]");
            }

            // Crear lista filtrada de particiones libres
            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            // Ordenar la lista de particiones libres por tamaño ascendente
            Collections.sort(listaParticionesLibres, Comparator.comparingInt(Particion::getTamanio));

            // Buscar partición adecuada (Best Fit)
            boolean carga = true;
            int i = 0;
            while (carga && i < listaParticionesLibres.size()) {
                Particion particionAdecuada = listaParticionesLibres.get(i);

                if (particionAdecuada.getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;  // Se encontró una partición adecuada

                    // Buscar la partición en la lista original
                    Particion particion = listaParticiones.stream()
                            .filter(p -> p.getId() == particionAdecuada.getId())
                            .findFirst()
                            .orElse(null);

                    // Si la partición es exactamente del tamaño del Proceso
                    if (particion.getTamanio() == ProcesoActual.getTamanio()) {


                        int graficarParticion = 0;
                        /*
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }
                         */
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;
                        Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                        System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición: " + particionEncontrada);

                        particiones.add( particionEncontrada);
                        listaParticiones.add(particionEncontrada);
                        listaParticiones.remove(particion);
                        listaProcesos.remove(indice);

                    } else if (particion.getTamanio() > ProcesoActual.getTamanio()) {
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;



                        int graficarParticion = 0;
                        /*
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }
     */
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        Particion particionAsignada =new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                        listaParticiones.add(particionAsignada);
                        particiones.add(particionAsignada);

                        // Partición sobrante
                        Particion particionLibre = new Particion(
                                -1,
                                -1,
                                particion.getTamanio() - ProcesoActual.getTamanio(),
                                true,
                                -1,
                                0
                        );
                        listaParticiones.add(particionLibre);
                        listaParticiones.remove(particion);
                        System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición.");
                        listaProcesos.remove(indice);
                    }
                }
                i++;
            }

            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaProcesos.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();
                    System.out.println("Fragmentación externa: " + fragmentacionExterna);
                }
            }

            // Incrementar el tiempo actual
            tiempoActual++;

            System.out.println("------------------------------");
            // Simular retardo de 1 segundo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  // Pausa de 1 segundo
        }
        System.out.println("Fragmentación externa TOTAL: " + fragmentacionExterna);
        return particiones;
    }


}