import algoritmoMurcielago.AlgoritmoMurcielago;
import estructuras.Contenedor;
import estructuras.Murcielago;
import estructuras.Paquete;

import java.util.ArrayList;

import IO.EntradaDatos;

public class App{
    public static void main(String[] args) {
        ArrayList<Paquete> paquetes = null;
        ArrayList<Paquete> paquetes2 = new ArrayList<>();
        ArrayList<Contenedor> contenedores = null;
        ArrayList<Contenedor> contenedores2 = new ArrayList<>();
        try {
            paquetes = EntradaDatos.leerDatosPaquetes();
            contenedores = EntradaDatos.leerDatosContenedores();
        }catch (Exception e){
            e.printStackTrace();
        }
/*
        paquetes2.add(paquetes.get(6));
        paquetes2.add(paquetes.get(9));
        paquetes2.add(paquetes.get(17));
        paquetes2.add(paquetes.get(21));
        paquetes2.add(paquetes.get(0));
        paquetes2.add(paquetes.get(1));
        paquetes2.add(paquetes.get(14));
        paquetes2.add(paquetes.get(4));
        paquetes2.add(paquetes.get(8));
        paquetes2.add(paquetes.get(7));
        paquetes2.add(paquetes.get(18));
        paquetes2.add(paquetes.get(15));
        paquetes2.add(paquetes.get(16));
        paquetes2.add(paquetes.get(2));
        paquetes2.add(paquetes.get(13));
        paquetes2.add(paquetes.get(20));
        paquetes2.add(paquetes.get(19));
        paquetes2.add(paquetes.get(11));
        paquetes2.add(paquetes.get(3));
        paquetes2.add(paquetes.get(5));
        paquetes2.add(paquetes.get(10));
        paquetes2.add(paquetes.get(12));

        contenedores2.add(contenedores.get(1));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(1));
        contenedores2.add(contenedores.get(1));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(1));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(2));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));
        contenedores2.add(contenedores.get(0));

        Murcielago mur = new Murcielago(paquetes2,contenedores2);
        mur.calcularFitness(3,5);
        System.out.println(mur.getFitness());
        for (Contenedor cont:
                mur.getSolucion()) {
            cont.imprimirSolucion();
        }
*/

        ArrayList<Contenedor> solucion;
        AlgoritmoMurcielago alg = new AlgoritmoMurcielago();
        solucion=alg.ejecutarAlgoritmoMurcielago(paquetes,contenedores,80,500,500,1.2f,
                0.99f,5,0.7f,3,5);
        System.out.println("Solucion");
        for (Contenedor cont:
             solucion) {
            cont.imprimirSolucion();
        }

    }
}
