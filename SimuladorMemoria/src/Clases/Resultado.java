package Clases;

public class Resultado {

    private  int fragmentacion;


    public Resultado() {
    }


    @Override
    public String toString() {
        return "Resultado{" +
                ", fragmentacion=" + fragmentacion +
                '}';
    }

    public int getFragmentacion() {
        return fragmentacion;
    }

    public void setFragmentacion(int fragmentacion) {
        this.fragmentacion = fragmentacion;
    }
}