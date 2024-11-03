package Clases;


import java.awt.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class GanttPanel extends JPanel {
    private List<Particion> particiones;

    public int getTamanioMemoria() {
        return tamanioMemoria;
    }

    public void setTamanioMemoria(int tamanioMemoria) {
        this.tamanioMemoria = tamanioMemoria;
    }

    private  int tamanioMemoria;

    public GanttPanel(List<Particion> particiones,int tamanioMemoria)
    {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (particiones == null || particiones.isEmpty()) {
            return; // No hay Particions para mostrar
        }

        int chartHeight = getHeight();
        int chartWidth = getWidth();
        int alturaRectangulo = 30;
        int xOffset = 50;  // Posición inicial x para los rectángulos
        int yOffset = 50;  // Posición inicial y para los rectángulos
        int xAxisHeight = chartHeight - 50;
        int separacionVertical = 10;
        int spacingX = 50; // Espaciado entre los números del eje X

        // Configurar el rango y el tamaño de las particiones
        g2d.setStroke(new BasicStroke(2));

        // Dibujar el eje Y (invirtiendo el sentido del eje Y)
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset - 10, yOffset, xOffset - 10, xAxisHeight); // Eje Y
        g2d.drawLine(xOffset - 10, xAxisHeight, chartWidth - 10, xAxisHeight); // Eje X

        // Etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Calcular el rango máximo para el eje Y
        int intervalos = (int) Math.ceil((double) tamanioMemoria / 10.0);


        for (int i = 0; i <= intervalos; i++) {
            int y = xAxisHeight - i * 50;
            g2d.drawLine(xOffset - 10, y, xOffset, y);
            // Añadir "k" a las etiquetas del eje Y
            g2d.drawString(String.valueOf(i * 10) + "k", xOffset - 40, y + 5);
        }

        // Etiquetas del eje X
        for (int i = 0; i <= (chartWidth - xOffset) / spacingX; i++) {
            int x = xOffset + i * spacingX;
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5);
            g2d.drawString(String.valueOf(i * 10), x - 10, xAxisHeight + 20); // Multiplica i por 10 para mantener el escalado original
        }

        // Dibujar los Particions
        Random random = new Random();
        int y = yOffset; // Reajustamos la posición inicial de y según el eje Y

        // Calcular el ancho total disponible para los rectángulos
        int maxAnchoDisponible = chartWidth - xOffset - 50; // Dejar un margen de 50 px a la derecha

        // Encontrar la duración máxima de los Particions para calcular la escala
        int maxDuracion = particiones.stream().mapToInt(Particion::getGraficarParticion).max().orElse(1);

        // Calcular el factor de escala para que todos los Particions quepan dentro del gráfico
        double escala = (double) maxAnchoDisponible / (maxDuracion * 10); // Ajuste de escala

        for (Particion Particion : particiones) {
            int duracion = Particion.getTamanio();
            int anchoRectangulo = (int) (duracion * 10 * escala); // Escalar el ancho del rectángulo

            // Generar un color aleatorio para cada Particion
            Color colorParticion = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(colorParticion);
            g2d.fillRect(xOffset, y, anchoRectangulo, alturaRectangulo);

            // Dibujar borde del rectángulo
            g2d.setColor(Color.BLACK);
            g2d.drawRect(xOffset, y, anchoRectangulo, alturaRectangulo);

            // Mostrar el nombre del Particion
          //  g2d.setColor(Color.WHITE); // Usar blanco para que sea visible sobre los colores de fondo
          //  g2d.drawString(Particion.getNombre(), xOffset + 5, y + 20);

            // Mover la posición y para el siguiente Particion
            y += alturaRectangulo + separacionVertical;
        }
    }
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (particiones == null || particiones.isEmpty()) {
            return; // No hay particiones para mostrar
        }

        int chartHeight = getHeight();
        int chartWidth = getWidth();
        int alturaRectangulo = 30;
        int xOffset = 50;  // Posición inicial x para los rectángulos
        int yOffset = 50;  // Posición inicial y para los rectángulos
        int xAxisHeight = chartHeight - 50;
        int separacionVertical = 10;
        int spacingX = 50; // Espaciado entre los números del eje X

        // Dibujar el eje Y
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xOffset - 10, yOffset, xOffset - 10, xAxisHeight); // Eje Y
        g2d.drawLine(xOffset - 10, xAxisHeight, chartWidth - 10, xAxisHeight); // Eje X

        // Etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Calcular el rango máximo para el eje Y, basado en el tamaño de memoria
        int intervalos = (int) Math.ceil((double) tamanioMemoria / 10.0);

        // Dibujar intervalos en el eje Y
        for (int i = 0; i <= intervalos; i++) {
            int y = xAxisHeight - (i * (xAxisHeight - yOffset)) / intervalos;
            g2d.drawLine(xOffset - 10, y, xOffset, y);
            g2d.drawString(String.valueOf(i * 10) + "k", xOffset - 40, y + 5);
        }

        // Etiquetas del eje X
        for (int i = 0; i <= (chartWidth - xOffset) / spacingX; i++) {
            int x = xOffset + i * spacingX;
            g2d.drawLine(x, xAxisHeight, x, xAxisHeight + 5);
            g2d.drawString(String.valueOf(i * 10), x - 10, xAxisHeight + 20); // Multiplica i por 10 para mantener el escalado original
        }

        // Dibujar las particiones
        Random random = new Random();
        int y = yOffset; // Reajustamos la posición inicial de y según el eje Y

        // Calcular el ancho total disponible para los rectángulos
        int maxAnchoDisponible = chartWidth - xOffset - 50; // Dejar un margen de 50 px a la derecha

        // Encontrar la duración máxima de las particiones para calcular la escala
        int maxDuracion = particiones.stream().mapToInt(Particion::getGraficarParticion).max().orElse(1);

        // Calcular el factor de escala para que todos los particiones quepan dentro del gráfico
        double escala = (double) maxAnchoDisponible / (maxDuracion * 10); // Ajuste de escala

        // Escalar el tamaño de las particiones al eje Y según el tamaño de la memoria
        double escalaY = (double) (xAxisHeight - yOffset) / tamanioMemoria;

        for (Particion particion : particiones) {
            int duracion = particion.getGraficarParticion();
            int anchoRectangulo = (int) (duracion * 10 * escala); // Escalar el ancho del rectángulo

            // Escalar el tamaño en el eje Y
            int altoRectangulo = (int) (particion.getTamanio() * escalaY);

            // Generar un color aleatorio para cada partición
            Color colorParticion = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(colorParticion);
            g2d.fillRect(xOffset, y, anchoRectangulo, altoRectangulo);

            // Dibujar borde del rectángulo
            g2d.setColor(Color.BLACK);
            g2d.drawRect(xOffset, y, anchoRectangulo, altoRectangulo);

            // Mostrar el nombre del partición centrado
            g2d.setColor(Color.WHITE); // Usar blanco para que sea visible sobre los colores de fondo
            g2d.drawString("T" + particion.getId() + "-" + particion.getTamanio() + "K", xOffset + anchoRectangulo / 2 - 10, y + altoRectangulo / 2);

            // Mover la posición y para el siguiente partición
            y += altoRectangulo + separacionVertical;
        }
    }


    // Metodo para actualizar la lista de Particions y redibujar el panel
    public void setParticiones(List<Particion> particiones,int tamanioMemoria) {
        this.particiones = particiones;
        this.tamanioMemoria = tamanioMemoria;
        repaint();
    }
}