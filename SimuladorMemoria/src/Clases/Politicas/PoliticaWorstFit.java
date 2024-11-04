package Clases.Politicas;

import Clases.Particion;
import Clases.Proceso;
import Clases.Resultado;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PoliticaWorstFit {

    public PoliticaWorstFit() {
    }

    public List<Particion> unificarParticiones(List<Particion> listaParticiones) {
        for (int i = 0; i < listaParticiones.size() - 1; i++) {
            Particion particionActual = listaParticiones.get(i);
            Particion particionSiguiente = listaParticiones.get(i + 1);

            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                // Unificar las particiones
                int tamanioUnificado = particionActual.getTamanio() + particionSiguiente.getTamanio();

                // Crear una nueva partición unificada
                Particion nuevaParticion = new Particion(-1, tamanioUnificado, true, -1, 0,-1);
                listaParticiones.set(i, nuevaParticion);  // Reemplazar la partición actual por la unificada

                // Eliminar la partición siguiente
                listaParticiones.remove(i + 1);
            } else {
                i++;
            }
        }
        return listaParticiones;
    }

    public int calcularGraficoParticion(List<Particion> listaParticiones,Particion particion ,int graficarParticion) {
        for (Particion p : listaParticiones) {
            // Verifica si la partición está activa y es la última de la lista
            if (p.getEstado() && p.equals(listaParticiones.get(listaParticiones.size() - 1))) {
                break;
            }

            // Si la partición actual es igual a la partición en cuestión, detener el bucle
            if (p.equals(particion)) {
                break;
            }

            // Sumar el tamaño de la partición al eje Y
            graficarParticion += p.getTamanio();
        }
        return graficarParticion;
    }


    public Resultado worstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

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

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles: " + particion);
            }

            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            listaParticionesLibres.sort(Comparator.comparingInt(Particion::getTamanio).reversed());


            int i = 0;
            boolean carga = true;

            // Buscar la primera partición libre que pueda abarcar el tamaño necesario
            while (carga && i < listaParticionesLibres.size()) {
                Particion particionL = listaParticionesLibres.get(i);
                if (particionL.getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;

                    if (particionL.getTamanio() == ProcesoActual.getTamanio()) {
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                        int graficarParticion = 0;

                        graficarParticion = calcularGraficoParticion(listaParticiones, particionL, graficarParticion);

                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                ProcesoActual.getID()
                        );

                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particionL), particionEncontrada);
                        listaParticiones.remove(particionL);
                        listaProcesos.remove(ProcesoActual);

                    } else if (particionL.getTamanio() > ProcesoActual.getTamanio()){

                      int graficarParticion = 0;
                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                        graficarParticion = calcularGraficoParticion(listaParticiones, particionL, graficarParticion);

                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                ProcesoActual.getID()
                        );
                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particionL) + 1, particionEncontrada);

                        Particion particionLibre = new Particion(
                                -1,
                                particionL.getTamanio() - ProcesoActual.getTamanio(),
                                true,
                                -1,
                                0,
                                -1
                        );
                        listaParticiones.add(listaParticiones.indexOf(particionL) + 2, particionLibre);
                        listaParticiones.remove(particionL);
                        listaProcesos.remove(ProcesoActual);
                    }
                }
                i++;
            }
            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaProcesos.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();

                }
            }

            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles después de actualizar: [" + particion + "]");
            }



            System.out.println("Fragmentacion externa: " + fragmentacionExterna);
            tiempoActual++;

            System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
            System.out.println("------------------------------");


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  // Pausa de 1 segundo

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
            resultado.setLongitudTrabajo(longitud);


        }
        return resultado;
    }

}


