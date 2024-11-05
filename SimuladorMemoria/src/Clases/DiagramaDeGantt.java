package Clases;



import java.util.List;


public class DiagramaDeGantt {
    private List<Particion> particiones;
    private int tamanioMemoria;
    private int fragmentacionExterna;
    private int tiempoRetorno;
    private String estrategiaSeleccionada;

    public DiagramaDeGantt(List<Particion> particiones, int tamanioMemoria, int fragmentacionExterna, int tiemporRetorno,String estrategia) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
        this.fragmentacionExterna = fragmentacionExterna;
        this.tiempoRetorno = tiemporRetorno;
        this.estrategiaSeleccionada = estrategia;
    }

    public int getTamanioMemoria() {
        return tamanioMemoria;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }

   //Aca muestro el diagrama
    public void imprimirDiagrama() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDIAGRAMA DE GANTT\n");
        sb.append("Autor: Elias Maldonado").append("\n");
        sb.append("Politica: ").append(estrategiaSeleccionada).append("\n\n");
        // Imprimir el eje X (Tiempo) sin saltos
        sb.append("     "); // Espacio para la etiqueta del eje Y
        for (int j = 1; j <= tiempoRetorno; j++) {
            sb.append(String.format("%3d", j));
        }
        sb.append("\n");

        // Linea de referencia para el eje X
        sb.append("     +");
        sb.append("-".repeat(tiempoRetorno * 3));
        sb.append("\n");

        // Imprimir el eje Y (Memoria) de 0 a tamanio maximo en orden ascendente
        for (int i = tamanioMemoria; i >= 0; i -= 10) {
            sb.append(String.format("%4dk |", i)); // Etiqueta del eje Y

            // Relleno de la linea con particiones o espacios vacios
            for (int j = 1; j <= tiempoRetorno; j++) {
                Particion particionEnTiempo = obtenerParticionEnTiempo(j, i);

                if (particionEnTiempo != null) {
                    sb.append("T" + particionEnTiempo.getIdTarea());
                } else {
                    sb.append("  -");
                }
            }
            sb.append("\n");
        }

        // Imprimir en consola
        System.out.println(sb.toString());
    }



    private Particion obtenerParticionEnTiempo(int tiempo, int memoria) {
        for (Particion particion : particiones) {
            if (tiempo >= particion.getTiempoInicio() && tiempo < particion.getTiempoFinalizacion() &&
                    memoria >= particion.getGraficarParticion() &&
                    memoria < particion.getGraficarParticion() + particion.getTamanio()) {
                return particion;
            }
        }
        return null;
    }
}
