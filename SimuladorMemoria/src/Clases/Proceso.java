package Clases;
public class Proceso {
    //seria como la tanda

    private int id;
    private String nombre;
    private int Tamanio; //Tamaño
    private int instanteArribo;
    private int duracion;


    public Proceso(int id,String nombre,int Tamanio,int duracion,int instanteArribo){
        this.id = id;
        this.nombre = nombre;
        this.Tamanio = Tamanio;
        this.duracion = duracion;
        this.instanteArribo = instanteArribo;
    }

    public int getID(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getInstanteArribo() {
        return instanteArribo;
    }

    public void setInstanteArribo(int instanteArribo) {
        this.instanteArribo = instanteArribo;
    }



    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }


    public int getTamanio() {
        return Tamanio;
    }

    public void setTamanio(int Tamanio) {
        this.Tamanio = Tamanio;
    }

    // toString
    @Override
    public String toString() {
        return "Trabajo{" +
                "id='"+id+
                "nombre='" + nombre + '\'' +
                ", instanteArribo=" + instanteArribo +
                ", duracion=" + duracion +
                ", Tamanio=" + Tamanio +
                '}';
    }
}