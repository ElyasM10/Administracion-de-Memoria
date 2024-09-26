package Clases;

import java.util.List;

public class Resultado {

    private  int fragmentacion;
    private List<Particion> listaDeParticiones;


    public Resultado() {
    }


    @Override
    public String toString() {
        return "Resultado{" +
                "Particiones: "+listaDeParticiones+
                ", fragmentacion=" + fragmentacion +
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



}