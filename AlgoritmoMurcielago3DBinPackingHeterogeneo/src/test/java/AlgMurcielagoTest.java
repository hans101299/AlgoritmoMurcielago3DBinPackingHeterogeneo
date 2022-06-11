import algoritmoMurcielago.AlgoritmoMurcielago;
import estructuras.Contenedor;
import estructuras.Murcielago;
import estructuras.Paquete;
import estructuras.Rotacion;
import org.junit.Test;
import org.junit.Assert;

import java.util.*;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlgMurcielagoTest {
    @Test
    public void testAlgGenetico() throws Exception{
        ArrayList<Paquete> paquetes = new ArrayList<>();
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        ArrayList<Contenedor> solucion;
        paquetes.add(new Paquete("P1",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P2",5,2,2,2,4,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(5,2,2,2)))));
        paquetes.add(new Paquete("P3",3,5,1,2,10,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(3,5,1,2)))));
        paquetes.add(new Paquete("P4",5,5,5,2,2,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(5,5,5,2)))));
        paquetes.add(new Paquete("P5",6,7,3,2,10,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(6,7,3,2)))));
        paquetes.add(new Paquete("P6",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P7",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P8",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P9",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P10",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P12",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P13",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P14",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P15",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));
        paquetes.add(new Paquete("P16",4,2,7,2,1000,new ArrayList<Rotacion>(Arrays.asList(new Rotacion(4,2,7,2)))));

        contenedores.add(new Contenedor("1",6,20,10,100000));
        contenedores.add(new Contenedor("2",6,10,5,50000));

        AlgoritmoMurcielago alg = new AlgoritmoMurcielago();
        List<Murcielago> pob = alg.crearPoblacionInicial(paquetes,contenedores,15,0.8f,2,3);
        Assert.assertEquals(pob.size(), 15);
        for (Murcielago crom:pob) {
            Assert.assertEquals(crom.getSecPaquetesCromosoma().size(),paquetes.size());
            Assert.assertEquals(crom.getSecContenedorCromosoma().size(),paquetes.size());
        }

    }
}
