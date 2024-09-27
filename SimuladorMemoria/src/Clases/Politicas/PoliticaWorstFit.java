package Clases.Politicas;

import Clases.Particion;
import Clases.Proceso;
import Clases.Resultado;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PoliticaWorstFit {

    public PoliticaWorstFit(){}

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


    public List<Particion> worstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

        System.out.println("Estoy en la politica worstFit ");

        List<Particion> particiones = new ArrayList<>();
        int index = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;


        while (!listaProcesos.isEmpty()) {
            Proceso ProcesoActual = listaProcesos.get(index);
            System.out.println("Proceso: " + ProcesoActual.getNombre() + " esperando particion");
            System.out.println("Tiempo actual: " + tiempoActual);

            // Buscar particiones con tiempo de finalización igual al tiempo actual y ponerlas libres
            for (Particion particion : listaParticiones) {
                if (particion.getTiempoFinalizacion() == tiempoActual) {
                    particion.setEstado(true);
                }
            }

            listaParticiones = unificarParticiones(listaParticiones);

            // Filtrar particiones libres
            List<Particion> listaParticionesLibres = listaParticiones.stream()
                    .filter(Particion::getEstado)
                    .sorted(Comparator.comparingInt(Particion::getTamanio).reversed())
                    .collect(Collectors.toList());

            listaParticionesLibres.forEach(p -> System.out.println("Particiones libres ordenadas de mayor a menor: [" + p + "]"));

            int i = 0;
            boolean carga = true;

            // Buscar la primera partición libre que pueda abarcar el tamaño necesario
            while (carga && i < listaParticionesLibres.size()) {
                if (listaParticionesLibres.get(i).getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;


                    // Encontramos la partición en la lista original
                    Particion particion = listaParticiones.stream()
                            .filter(p -> p.getId() == listaParticionesLibres.get(index).getId())
                            .findFirst()
                            .orElse(null);


                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

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
                        }*/
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );

                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                        listaParticiones.remove(particion);
                        listaProcesos.remove(index);

                    } else if (particion.getTamanio() > ProcesoActual.getTamanio()) {

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


                        Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionEncontrada);

                        Particion particionLibre = new Particion(
                                -1,
                                -1,
                                particion.getTamanio() - ProcesoActual.getTamanio(),
                                true,
                                -1,
                                0
                        );
                        listaParticiones.add(listaParticiones.indexOf(particion) + 2, particionLibre);
                        listaParticiones.remove(particion);
                        listaProcesos.remove(index);
                    }
                }
            }
            i++;
        }

        // Calcular fragmentación externa
        for (Particion particion : listaParticiones) {
            if (particion.getEstado()) {
                fragmentacionExterna += particion.getTamanio();

            }
        }

        System.out.println("Fragmentación externa: " + fragmentacionExterna);
        tiempoActual++;

        System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
        System.out.println("------------------------------");


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  // Pausa de 1 segundo

        System.out.println("Fragmentación externa TOTAL: " + fragmentacionExterna);

        return particiones;


    }




}
