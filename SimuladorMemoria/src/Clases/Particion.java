package Clases;

public class Particion {
    private int id;
    private int tiempoInicio;
    private int tiempoFinalizacion;
    private int tamanio;
    private int graficarParticion;
    private boolean estado;
    private Proceso proceso;
    private static int ultimoId = 0;
    //  private EstrategiaAsignacion estrategiaActual;

/*
      public enum EstrategiaAsignacion {
        FIRST_FIT,
        BEST_FIT,
        NEXT_FIT,
        WORST_FIT
    }
  */
public static int generarId() {
    ultimoId++;
    return ultimoId;
}

    public Particion(int tiempoInicio, int tamanio, boolean estado, int tiempoFinalizacion,int graficarParticion) {
        this.id = generarId();
        this.tiempoInicio = tiempoInicio;
        this.tamanio = tamanio;
        this.estado = estado; 
        this.tiempoFinalizacion = tiempoFinalizacion;
        this.graficarParticion = graficarParticion;
    }


    public void setGraficarParticion(int graficarParticion){
        this.graficarParticion = graficarParticion;
    }


   public int  getGraficarParticion(){
        return graficarParticion;
    }
    
    public void setId(int id){
        this.id = id;
    }

    public void setProceso(Proceso proceso){
        this.proceso = proceso;
    }

    public Proceso getProceso(){
        return proceso;
    }


    public int getId() {
        return id;
    }

    public int getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(int tiempoInicio){
        this.tiempoInicio = tiempoInicio;
    }

    public int getTiempoFinalizacion(){
        return  tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(int tiempoFinalizacion){
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio){
        this.tamanio = tamanio;
    }


    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "Particion{" +
                "id=" + id +
                ", tiempoInicio=" + tiempoInicio +
                ", tamanio=" + tamanio +
                ", estado=" + estado +
                ", tiempoFinalizacion="+tiempoFinalizacion+
                '}';
    }
}