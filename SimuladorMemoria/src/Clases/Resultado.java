package Clases;

import java.util.List;

public class Resultado {

    private  int fragmentacion;
    private List<Particion> listaDeParticiones;
    private int longitudTarea;


    public Resultado() {
    }


    @Override
    public String toString() {
        return "Resultado{" +
                "Particiones: "+listaDeParticiones+
                ", fragmentacion=" + fragmentacion +
                ",longitud: "+ longitudTarea +
                '}';
    }

    public int getFragmentacion() {
        return fragmentacion;
    }

    public void setFragmentacion(int fragmentacion) {
        this.fragmentacion = fragmentacion;
    }

    
    public List<Particion> getlistaDeParticiones() {
        return  listaDeParticiones;
    }

    public void setlistaDeParticiones(List<Particion> listaDeParticiones) {
        this. listaDeParticiones = listaDeParticiones;
    }

    public int getLongitudTarea() {
        return longitudTarea;
    }

    public void setLongitudTarea(int longitudTarea) {
        this.longitudTarea = longitudTarea;
    }




}