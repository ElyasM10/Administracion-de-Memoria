package Clases;

public class Particion {
    private int id;
    private int tiempoInicio;
    private int tiempoFinalizacion;
    private int tamanio;
    private boolean estado;
    private Proceso proceso;
    //  private EstrategiaAsignacion estrategiaActual;

/*
      public enum EstrategiaAsignacion {
        FIRST_FIT,
        BEST_FIT,
        NEXT_FIT,
        WORST_FIT
    }
  */

    public Particion(int id, int tiempoInicio, int tamanio, boolean estado, int tiempoFinalizacion) {
        this.id = id;
        this.tiempoInicio = tiempoInicio;
        this.tamanio = tamanio;
        this.estado = estado; // Asigna el estado recibido como parámetro
        this.tiempoFinalizacion = tiempoFinalizacion;
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