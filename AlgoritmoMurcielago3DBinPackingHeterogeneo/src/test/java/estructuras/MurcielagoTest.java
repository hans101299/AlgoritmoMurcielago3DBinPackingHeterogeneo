package estructuras;

import algoritmoMurcielago.AlgoritmoMurcielago;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MurcielagoTest {

    @Test
    void Cromosoma() {
        AlgoritmoMurcielago alg = new AlgoritmoMurcielago();
        ArrayList<Paquete> paquetes = new ArrayList<>();
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        paquetes.add(new Paquete("P1",4,2,2,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P2",5,2,2,2,4,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(5,2,2,2)))));
        paquetes.add(new Paquete("P3",3,5,1,2,10,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(3,5,1,2)))));
        paquetes.add(new Paquete("P4",5,5,5,2,2,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(5,5,5,2)))));
        paquetes.add(new Paquete("P5",6,7,3,2,10,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(6,7,3,2)))));
        paquetes.add(new Paquete("P6",4,2,1,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P7",4,2,1,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P8",4,2,1,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));

        contenedores.add(new Contenedor("1",6,20,10,100000));
        contenedores.add(new Contenedor("1",6,10,5,50000));

        Murcielago crom = new Murcielago(alg.crearSecPaquetesAleatorio(paquetes,0.8f),alg.crearSecContenedoresAleatorio(contenedores,paquetes.size()));
        Murcielago crom2 = new Murcielago(alg.crearSecPaquetesAleatorio(paquetes,0.8f),alg.crearSecContenedoresAleatorio(contenedores,paquetes.size()));
        Assert.assertNull(crom.getSolucion());
        crom.calcularFitness(1,2);
        float fitnessAct = crom.getFitness();
        Assert.assertNotNull(crom.getSolucion());
    }
}
