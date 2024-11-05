package Clases.Politicas;

import Clases.Particion;
import Clases.Tarea;
import Clases.Resultado;

import java.util.ArrayList;
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

                // Crear una nueva particion unificada
                Particion nuevaParticion = new Particion(-1,  tamanioUnificado, true, -1,0,-1);
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

    public Resultado bestFit(List<Particion> listaParticiones, List<Tarea> listaTareas, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {


        List<Particion> particiones = new ArrayList<>();
        int indice = 0;
        int tiempoActual = 0;
        int fragmentacionExterna = 0;

        while (!listaTareas.isEmpty())  {
            Tarea tareaActual = listaTareas.get(indice);
            System.out.println("Tarea: " + tareaActual.getNombre() + " esperando partición, Tamanio: "+ tareaActual.getTamanio());
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
                System.out.println("Particiones Disponibles: [" + particion + "]");
            }

            // Creo la  lista filtrada de particiones libres
            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            // Ordenar la lista de particiones libres por tamanio ascendente
            listaParticionesLibres.sort(Comparator.comparingInt(Particion::getTamanio));

            // Buscar la particion adecuada
            boolean preparado = true;
            int i = 0;
            while (preparado && i < listaParticionesLibres.size()) {
                Particion particionAdecuada = listaParticionesLibres.get(i);

                if (particionAdecuada.getTamanio() >= tareaActual.getTamanio()) {
                    preparado = false;  // Se encontro una particion adecuada

                    // Buscar la particion en la lista original
                    Particion particion = listaParticiones.stream()
                            .filter(p -> p.getId() == particionAdecuada.getId())
                            .findFirst()
                            .orElse(null);



                        int graficarParticion = 0;
                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                        int tiempoFinalizacion = tiempoInicio + tareaActual.getDuracion() + tiempoLiberacion;
                        Particion particionEncontrada = new Particion(
                                tiempoInicio,
                                tareaActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion,
                                tareaActual.getID()
                        );
                        System.out.println("La Tarea " + tareaActual.getNombre() + " encontro una particion: " + particionEncontrada);

                        particiones.add( particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                        listaParticiones.remove(particion);

                        if (particion.getTamanio() > tareaActual.getTamanio()) {

                        graficarParticion = calcularGraficoParticion(listaParticiones,particion,graficarParticion);

                        Particion particionLibre = new Particion(
                                -1,
                                particion.getTamanio() - tareaActual.getTamanio(),
                                true,
                                -1,
                                0,
                                -1

                        );
                        listaParticiones.add(listaParticiones.indexOf(particionEncontrada) + 1, particionLibre);
                        System.out.println("La Tarea " + tareaActual.getNombre() + " encontro una particion.");
                    }
                    listaTareas.remove(indice);
                }
                i++;
            }


            for (Particion particion : listaParticiones) {
                if (particion.getEstado() && !listaTareas.isEmpty()) {
                    fragmentacionExterna += particion.getTamanio();
                    System.out.println("Fragmentacion externa: " + fragmentacionExterna);
                }
            }

            // Incrementar el tiempo actual
            tiempoActual++;
            System.out.println("Longitud de la lista de Tareas: " + listaTareas.size());
            System.out.println("------------------------------");

            // Simula el retardo de 1 segundo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Particion particionConMayorTiempo = listaParticiones != null && !listaParticiones.isEmpty()
                ? listaParticiones.stream()
                .max((p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()))
                .orElse(null)
                : null;

        int longitud = particionConMayorTiempo != null
                ? particionConMayorTiempo.getTiempoFinalizacion()
                : tiempoActual;




        System.out.println("Fragmentación externa TOTAL: " + fragmentacionExterna);
        resultado.setlistaDeParticiones(particiones);
        resultado.setLongitudTarea(longitud);
        return resultado;
    }


}