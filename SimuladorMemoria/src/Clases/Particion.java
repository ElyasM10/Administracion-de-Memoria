package Clases;

public class Particion {
    private int id;
    private int tiempoInicio;
    private int tiempoFinalizacion;
    private int tamanio;
    private int graficarParticion;
    private boolean estado;
    private static int ultimoId = 0;
    private int idTarea;

public static int generarId() {
    ultimoId++;
    return ultimoId;
}

    public Particion(int tiempoInicio, int tamanio, boolean estado, int tiempoFinalizacion,int graficarParticion,int idTarea) {
        this.id = generarId(); //Genero un id para la particion
        this.tiempoInicio = tiempoInicio;
        this.tamanio = tamanio;
        this.estado = estado; 
        this.tiempoFinalizacion = tiempoFinalizacion;
        this.graficarParticion = graficarParticion;
        this.idTarea = idTarea;
    }

    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idProceso) {
        this.idTarea = idProceso;
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
                ",idTarea="+ idTarea +
                '}';
    }
}