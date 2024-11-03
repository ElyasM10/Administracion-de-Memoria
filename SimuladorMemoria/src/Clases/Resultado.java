package Clases;

import java.util.List;

public class Resultado {

    private  int fragmentacion;
    private List<Particion> listaDeParticiones;
    private int longitudTrabajo;


    public Resultado() {
    }


    @Override
    public String toString() {
        return "Resultado{" +
                "Particiones: "+listaDeParticiones+
                ", fragmentacion=" + fragmentacion +
                ",longitud: "+longitudTrabajo+
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

    public int getLongitudTrabajo() {
        return longitudTrabajo;
    }

    public void setLongitudTrabajo(int longitudTrabajo) {
        this.longitudTrabajo = longitudTrabajo;
    }




}