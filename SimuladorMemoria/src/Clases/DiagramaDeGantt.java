package Clases;


import java.awt.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class DiagramaDeGantt {
    private List<Particion> particiones;
    private int tamanioMemoria;
    private int fragmentacionExterna;
    private String politica;
    private int tiempoRetorno;


    public DiagramaDeGantt(List<Particion> particiones, int tamanioMemoria, int fragmentacionExterna, int tiemporRetorno) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
        this.fragmentacionExterna = fragmentacionExterna;
        this.tiempoRetorno = tiemporRetorno;
    }

    public int getTamanioMemoria() {
        return tamanioMemoria;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }


    // Metodo para actualizar la lista de Particions y redibujar el panel
    public void setDatos(List<Particion> particiones, int tamanioMemoria, int fragmentacion, String politica, int TRT) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
        this.fragmentacionExterna = fragmentacion;
        this.politica = politica;
        this.tiempoRetorno = TRT;

    }

    public void imprimirDiagrama() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDIAGRAMA DE GANTT\n");
        sb.append("Fragmentación Externa: ").append(fragmentacionExterna).append("\n");
        sb.append("Política: ").append(politica).append("\n\n");

        // Imprimir el eje X (Tiempo) sin saltos
        sb.append("     "); // Espacio para la etiqueta del eje Y
        for (int j = 1; j <= tiempoRetorno; j++) {
            sb.append(String.format("%3d", j));
        }
        sb.append("\n");

        // Línea de referencia para el eje X
        sb.append("     +");
        sb.append("-".repeat(tiempoRetorno * 3));
        sb.append("\n");

        // Imprimir el eje Y (Memoria) de 0 a tamaño máximo en orden ascendente
        for (int i = tamanioMemoria; i >= 0; i -= 10) {
            sb.append(String.format("%4dk |", i)); // Etiqueta del eje Y

            // Relleno de la línea con particiones o espacios vacíos
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
