package Clases;


import java.util.*;
import java.util.stream.Collectors;


public class AsignacionMemoria {



    public int  tamanoRestante = 0;


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



    //Significado estados: true libre / false ocupada
    public List<Particion> firstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
        int fragmentacionExterna = 0;
        int tiempoActual = 0;
        int indice = 0;
        List<Particion> particiones = new ArrayList<>();

        while (!listaProcesos.isEmpty()) {
            Proceso ProcesoActual = listaProcesos.get(indice);
            System.out.println("El Proceso: " + ProcesoActual.getNombre() + " está esperando una partición, tamaño: " + ProcesoActual.getTamanio());
            System.out.println("Tiempo actual: " + tiempoActual);

            // Liberar particiones si ha llegado su tiempo de finalización
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

            // Buscar primera partición que pueda albergar el proceso
            boolean carga = true;
            int i = 0;

            while (carga && i < listaParticiones.size()) {
                Particion particion = listaParticiones.get(i);

                if (particion.getEstado() && particion.getTamanio() >= ProcesoActual.getTamanio()) {
                    carga = false;
                    int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                    if (particion.getTamanio() == ProcesoActual.getTamanio()) {

 
                        int graficarParticion = 0;
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }
                                

                        Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                        System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición");
                        System.out.println(particionEncontrada);

                        particiones.add(particionEncontrada);
                        //listaParticiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                        listaParticiones.remove(particion);
                    

                    } else if (particion.getTamanio() > ProcesoActual.getTamanio()) {

 
                        int graficarParticion = 0;
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }  
                        Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );

                        System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición");
                        System.out.println(particionEncontrada);

                        particiones.add(particionEncontrada);
                        listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionEncontrada);
                        Particion particionSobrante = new Particion(
                                -1,
                                -1,
                                particion.getTamanio() - ProcesoActual.getTamanio(),
                                true,
                                -1,
                                0
                        );
                        listaParticiones.add(listaParticiones.indexOf(particion) + 2, particionSobrante);

                        listaParticiones.remove(particion);
                    }

                    listaProcesos.remove(indice);  // Eliminar el proceso una vez asignado
                }
                i++;
            }

            // Calcular fragmentación externa
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    fragmentacionExterna += particion.getTamanio();
                    System.out.println("Fragmentación externa: " + fragmentacionExterna);
                }
            }


            // Mostrar particiones después de la actualización
            for (Particion particion : listaParticiones) {
                System.out.println("Particiones disponibles después de actualizar: [" + particion + "]");
            }

            // Incrementar el tiempo actual
            tiempoActual++;
            System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
            System.out.println("------------------------------");

            // Simular retardo de 1 segundo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("fragmentación externa TOTAL: " + fragmentacionExterna);
        return particiones;
    }


    public  List<Particion> bestFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {

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
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }  

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
                        for (Particion part : listaParticiones) {
                            if (part.getEstado()) {
                                graficarParticion += part.getTamanio();
                            }
                            if (part == particion) {
                                // Rompe el bucle al llegar a la partición actual
                                break;
                            }
                        }  

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


    public List<Particion> nextFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {


            List<Particion> particiones = new ArrayList<>();
            int index = 0;
            int tiempoActual = 0;
            int fragmentacionExterna = 0;
            int i = 0;

            while (!listaProcesos.isEmpty()) {
                Proceso ProcesoActual = listaProcesos.get(index);
                System.out.println("Proceso: " + ProcesoActual.getNombre() + " esperando particion");
                System.out.println("Tiempo actual: " + tiempoActual);

                for (Particion particion : listaParticiones) {
                    System.out.println("Particiones: [" + particion + "]");
                }

                // Recorrer particiones y liberar si su tiempo de finalización es igual al tiempo actual
                for (Particion particion : listaParticiones) {
                    if (particion.getTiempoFinalizacion() == tiempoActual) {
                        particion.setEstado(true);
                    }
                }

                listaParticiones = unificarParticiones(listaParticiones);

                boolean carga = true;

                while (carga && i < listaParticiones.size()) {
                    Particion particion = listaParticiones.get(i);
                    if (particion.getEstado() && particion.getTamanio() >= ProcesoActual.getTamanio()) {
                        carga = false;

                        if (particion.getTamanio() == ProcesoActual.getTamanio()) {
            
                             int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                             int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                            
                                int graficarParticion = 0;
                                for (Particion part : listaParticiones) {
                                    if (part.getEstado()) {
                                        graficarParticion += part.getTamanio();
                                    }
                                    if (part == particion) {
                                        // Rompe el bucle al llegar a la partición actual
                                        break;
                                    }
                                }  

                             Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                            System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición");
                            particiones.add(particionEncontrada);

                            listaParticiones.add(listaParticiones.indexOf(particion), particionEncontrada);
                            listaParticiones.remove(particion);

                            listaProcesos.remove(ProcesoActual);
                        } else if (particion.getTamanio() > ProcesoActual.getTamanio()) {
                            int tiempoInicio = tiempoCargaPromedio + tiempoSeleccion + tiempoActual;
                            int tiempoFinalizacion = tiempoInicio + ProcesoActual.getDuracion() + tiempoLiberacion;

                            int graficarParticion = 0;
                            for (Particion part : listaParticiones) {
                                if (part.getEstado()) {
                                    graficarParticion += part.getTamanio();
                                }
                                if (part == particion) {
                                    // Rompe el bucle al llegar a la partición actual
                                    break;
                                }
                            }  

                            Particion particionEncontrada = new Particion(
                                i,
                                tiempoInicio,
                                ProcesoActual.getTamanio(),
                                false,
                                tiempoFinalizacion,
                                graficarParticion
                        );
                            listaParticiones.add(listaParticiones.indexOf(particion) + 1, particionEncontrada);
                            particiones.add(particionEncontrada);

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
                            listaProcesos.remove(ProcesoActual);

                            System.out.println("El Proceso " + ProcesoActual.getNombre() + " encontró partición");
                        }
                    }
                    i++;
                }

                // Calcular fragmentación externa
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

                System.out.println("Longitud de la lista de Procesos: " + listaProcesos.size());
                System.out.println("------------------------------");

    
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  // Pausa de 1 segundo
        
              System.out.println("Fragmentación externa TOTAL: " + fragmentacionExterna);
    
            return particiones;
        }
    

    public List<Particion> worstFit(List<Particion> listaParticiones, List<Proceso> listaProcesos, int tiempoSeleccion, int tiempoCargaPromedio, int tiempoLiberacion, Resultado resultado) {
      
        
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
                            for (Particion part : listaParticiones) {
                                if (part.getEstado()) {
                                    graficarParticion += part.getTamanio();
                                }
                                if (part == particion) {
                                    // Rompe el bucle al llegar a la partición actual
                                    break;
                                }
                            }  
                            
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
                            for (Particion part : listaParticiones) {
                                if (part.getEstado()) {
                                    graficarParticion += part.getTamanio();
                                }
                                if (part == particion) {
                                    // Rompe el bucle al llegar a la partición actual
                                    break;
                                }
                            }  

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


    /* 
    // Obtener el objeto con el mayor tiempoFinalizacion
    Particion objetoMayorTiempoFinalizacion = Collections.max(listaParticiones, (p1, p2) -> Integer.compare(p1.getTiempoFinalizacion(), p2.getTiempoFinalizacion()));

    // Aquí se define la longitud X como el tiempoFinalizacion del objeto con mayor tiempoFinalizacion
    int longitudX = objetoMayorTiempoFinalizacion.getTiempoFinalizacion();
*/

