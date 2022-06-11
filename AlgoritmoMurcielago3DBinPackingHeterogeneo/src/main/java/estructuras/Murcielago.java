package estructuras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Murcielago {
    private static final int CANT2OPT = 4;
    private static final int CANT3OPT = 4;
    private List<Paquete> secPaquetesCromosoma;
    private List<Contenedor> secContenedorCromosoma;
    private ArrayList<Contenedor> solucion;
    private float fitness;
    private int velocidad;
    private float frecuenciaPulso;
    private float volumen;

    public Murcielago(List<Paquete> secPaquetesCromosoma, List<Contenedor> secContenedorCromosoma) {
        this.secPaquetesCromosoma = secPaquetesCromosoma;
        this.secContenedorCromosoma = secContenedorCromosoma;
        Random rand = new Random();
        this.frecuenciaPulso = rand.nextFloat();
        this.volumen = rand.nextFloat() + 1;
    }

    public List<Paquete> getSecPaquetesCromosoma() {
        return secPaquetesCromosoma;
    }

    public List<Contenedor> getSecContenedorCromosoma() {
        return secContenedorCromosoma;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public float getFrecuenciaPulso() {
        return frecuenciaPulso;
    }

    public float getVolumen() {
        return volumen;
    }

    public float getFitness() {
        return fitness;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public ArrayList<Contenedor> getSolucion() {
        return solucion;
    }

    public void calcularFitness(int numEMSProbar, int numPaquetesProbar) {
        int k;
        Contenedor contenedorAbierto = null;
        ArrayList<EMS> ems;
        ArrayList<Contenedor> contenedoresAbiertos = new ArrayList<>();
        ArrayList<Paquete> secPaquetes = new ArrayList<Paquete>(List.copyOf(this.secPaquetesCromosoma));
        ArrayList<Contenedor> secContenedores = new ArrayList<Contenedor>(List.copyOf(this.secContenedorCromosoma));
        while (secPaquetes.size()!=0){
            ArrayList<Empaque> opciones = new ArrayList<Empaque>();
            boolean paqueteUbicado = false;
            for (Contenedor contenedor: contenedoresAbiertos) {
                contenedor.limpiarEMS(contenedor.getSolucion());
                ems = contenedor.getEms();
                for (int i = 0; i < ems.size() && !paqueteUbicado; i++) {
                    k=i+numEMSProbar;
                    while (i<k && i<ems.size()){
                        for (int j = 0; j < numPaquetesProbar && j<secPaquetes.size(); j++) {
                            for (Rotacion rotacion: secPaquetes.get(j).getRotaciones()) {
                                if (ems.get(i).posicionar(rotacion,contenedor.getSolucion(),contenedor.getLimitePeso(),contenedor.getPesoCargado())){
                                    opciones.add(new Empaque(ems.get(i),secPaquetes.get(j),rotacion));
                                }
                            }
                        }
                        i++;
                    }
                    if(opciones.size()!=0){
                        //Ordenar o tener una cola de prioridad para opciones
                        Empaque seleccionado = seleccionarOpcion(opciones);
                        //Actualizar ems en la misma funcion
                        contenedor.posicionar(seleccionado);
                        //eliminar el paquete de la lista (agregar Id al paquete)
                        paqueteUbicado = true;
                        secPaquetes.removeIf(obj->obj.getId().equals(seleccionado.getPaquete().getId()));
                    }
                }
                if (paqueteUbicado) break;
            }
            while(secContenedores.size()!=0 && !paqueteUbicado){
                try {
                    contenedorAbierto = secContenedores.remove(0).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                ems = contenedorAbierto.getEms();
                for (int i = 0; i < numPaquetesProbar && i<secPaquetes.size(); i++) {
                    for (Rotacion rotacion: secPaquetes.get(i).getRotaciones()) {
                        if (ems.get(0).posicionar(rotacion, contenedorAbierto.getSolucion(), contenedorAbierto.getLimitePeso(), contenedorAbierto.getPesoCargado())){
                            opciones.add(new Empaque(ems.get(0),secPaquetes.get(i),rotacion));
                        }
                    }
                }
                if(opciones.size()!=0){
                    //Ordenar o tener una cola de prioridad para opciones
                    Empaque seleccionado = seleccionarOpcion(opciones);
                    //Actualizar ems en la misma funcion
                    contenedorAbierto.posicionar(seleccionado);
                    //eliminar el paquete de la lista (agregar Id al paquete)
                    paqueteUbicado = true;
                    secPaquetes.removeIf(obj->obj.getId().equals(seleccionado.getPaquete().getId()));
                    contenedoresAbiertos.add(contenedorAbierto);
                }
            }
            if (!paqueteUbicado){
                fitness = 9999999;
                return;
            }
        }
        float sumaVolumenPaquetes=0;
        float sumaVolumenContenedores=0;
        for (Paquete paquete:this.secPaquetesCromosoma) {
            sumaVolumenPaquetes+=paquete.getVolumen();
        }
        for (Contenedor contenedor: contenedoresAbiertos) {
            sumaVolumenContenedores+=contenedor.getVolumen();
        }
        fitness=sumaVolumenContenedores-sumaVolumenPaquetes;
        solucion=contenedoresAbiertos;
    }

    private Empaque seleccionarOpcion(ArrayList<Empaque> opciones) {
        Empaque mejorOpcion;
        float mejorValor;
        mejorOpcion = opciones.get(0);
        mejorValor = 9999999;
        for (Empaque emp: opciones){
            if(emp.getxAxis2()-emp.getxAxis()-emp.getPaquete().getAncho()<mejorValor){
                mejorOpcion=emp;
                mejorValor=emp.getxAxis2()-emp.getxAxis()-emp.getPaquete().getAncho();
            }
            if(emp.getyAxis2()-emp.getyAxis()-emp.getPaquete().getLargo()<mejorValor){
                mejorOpcion=emp;
                mejorValor=emp.getyAxis2()-emp.getyAxis()-emp.getPaquete().getLargo();
            }
            if(emp.getzAxis2()-emp.getzAxis()-emp.getPaquete().getAlto()<mejorValor){
                mejorOpcion=emp;
                mejorValor=emp.getzAxis2()-emp.getzAxis()-emp.getPaquete().getAlto();
            }
        }
        return mejorOpcion;
    }

    public Murcielago actualizar(int numEMSProbar, int numPaquetesProbar) {
        if(this.velocidad<this.secPaquetesCromosoma.size()/2){
            return this.opt2(numEMSProbar,numPaquetesProbar);
        }
        else {
            return this.opt3(numEMSProbar,numPaquetesProbar);
        }
    }

    public Murcielago opt2(int numEMSProbar, int numPaquetesProbar) {
        ArrayList<Integer> numeros =  IntStream.rangeClosed(0,this.secPaquetesCromosoma.size()-1).boxed().collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer[]> combinaciones = new ArrayList<>();
        Random rand = new Random();
        Paquete auxPaquete;
        Contenedor auxContenedor;
        ArrayList<Paquete> copiaPaquetes = null;
        ArrayList<Contenedor> copiaContenedores = null;
        Murcielago murcielagoAnt = this, murcielagoAct;
        int longNumeros = numeros.size(), distancia, inferior, superior;
        for (int i = 0; i<CANT2OPT; i++){
            combinaciones.add(new Integer[]{numeros.remove(rand.nextInt(longNumeros)),numeros.remove(rand.nextInt(longNumeros-1))});
            numeros =  IntStream.rangeClosed(0,this.secPaquetesCromosoma.size()-1).boxed().collect(Collectors.toCollection(ArrayList::new));
        }
        for (Integer[] comb:combinaciones) {
            distancia = Math.abs(comb[0]-comb[1]);
            distancia = (distancia+1)/2;
            inferior = Math.min(comb[0],comb[1]);
            superior = Math.max(comb[0],comb[1]);
            copiaPaquetes = new ArrayList<>(List.copyOf(this.secPaquetesCromosoma));
            for (int i = 0; i <distancia; i++) {
                auxPaquete=copiaPaquetes.get(inferior+i);
                copiaPaquetes.set(inferior+i,copiaPaquetes.get(superior-i));
                copiaPaquetes.set(superior-i,auxPaquete);

            }
            Collections.reverse(combinaciones);

            for (Integer[] comb2: combinaciones) {
                distancia = Math.abs(comb2[0]-comb2[1]);
                distancia = (distancia+1)/2;
                inferior = Math.min(comb2[0],comb2[1]);
                superior = Math.max(comb2[0],comb2[1]);

                copiaContenedores = new ArrayList<>(List.copyOf(this.secContenedorCromosoma));
                for (int j = 0; j < distancia; j++) {
                    auxContenedor = copiaContenedores.get(inferior + j);
                    copiaContenedores.set(inferior + j, copiaContenedores.get(superior - j));
                    copiaContenedores.set(superior - j, auxContenedor);
                }
                murcielagoAct = new Murcielago(copiaPaquetes,copiaContenedores);
                murcielagoAct.calcularFitness(numEMSProbar, numPaquetesProbar);

                if(murcielagoAct.getFitness() < murcielagoAnt.getFitness()){
                    murcielagoAnt = murcielagoAct;
                }

            }
            Collections.reverse(combinaciones);
        }
        return murcielagoAnt;

    }
    public Murcielago opt3(int numEMSProbar, int numPaquetesProbar) {
        ArrayList<Integer> numeros =  IntStream.rangeClosed(1,this.secPaquetesCromosoma.size()-1).boxed().collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Integer[]> combinaciones = new ArrayList<>();
        ArrayList<Integer[]> opciones = new ArrayList<>();
        ArrayList<Integer[]> opcionesCont = new ArrayList<>();
        Random rand = new Random();
        Paquete auxPaquete;
        Contenedor auxContenedor;
        ArrayList<Paquete> copiaPaquetes;
        ArrayList<Contenedor> copiaContenedores;
        Murcielago murcielagoAnt = this, murcielagoAct;
        int longNumeros = numeros.size(), distancia, inferior, superior;
        for (int i = 0; i<CANT3OPT; i++){
            combinaciones.add(new Integer[]{numeros.remove(rand.nextInt(longNumeros)),numeros.remove(rand.nextInt(longNumeros-1))});
            numeros =  IntStream.rangeClosed(1,this.secPaquetesCromosoma.size()-1).boxed().collect(Collectors.toCollection(ArrayList::new));
        }
        for (Integer[] comb:combinaciones) {
            inferior = Math.min(comb[0],comb[1]);
            superior = Math.max(comb[0],comb[1]);
            opciones.add(new Integer[]{0, inferior-1, inferior, superior});
            opciones.add(new Integer[]{0, inferior, superior, this.secPaquetesCromosoma.size()-1});
            opciones.add(new Integer[]{inferior, superior-1, superior, this.secPaquetesCromosoma.size()-1});
            opcionesCont = new ArrayList<>(opciones);
            Collections.reverse(opcionesCont);
            int index=0;

            for (Integer[] opcion:opciones) {
                distancia = Math.abs(opcion[0]-opcion[1]);
                distancia = (distancia+1)/2;
                inferior = Math.min(opcion[0],opcion[1]);
                superior = Math.max(opcion[0],opcion[1]);

                copiaPaquetes = new ArrayList<>(List.copyOf(this.secPaquetesCromosoma));
                for (int i = 0; i <distancia; i++) {
                    auxPaquete=copiaPaquetes.get(inferior+i);
                    copiaPaquetes.set(inferior+i,copiaPaquetes.get(superior-i));
                    copiaPaquetes.set(superior-i,auxPaquete);
                }

                distancia = Math.abs(opcion[2]-opcion[3]);
                distancia = (distancia+1)/2;
                inferior = Math.min(opcion[2],opcion[3]);
                superior = Math.max(opcion[2],opcion[3]);

                for (int i = 0; i <distancia; i++) {
                    auxPaquete=copiaPaquetes.get(inferior+i);
                    copiaPaquetes.set(inferior+i,copiaPaquetes.get(superior-i));
                    copiaPaquetes.set(superior-i,auxPaquete);
                }

                Integer[] opcionContenedore = opcionesCont.get(index);

                distancia = Math.abs(opcionContenedore[0]-opcionContenedore[1]);
                distancia = (distancia+1)/2;
                inferior = Math.min(opcionContenedore[0],opcionContenedore[1]);
                superior = Math.max(opcionContenedore[0],opcionContenedore[1]);
                copiaContenedores = new ArrayList<>(List.copyOf(this.secContenedorCromosoma));
                for (int j = 0; j < distancia; j++) {
                    auxContenedor = copiaContenedores.get(inferior + j);
                    copiaContenedores.set(inferior + j, copiaContenedores.get(superior - j));
                    copiaContenedores.set(superior - j, auxContenedor);
                }

                distancia = Math.abs(opcionContenedore[2]-opcionContenedore[3]);
                distancia = (distancia+1)/2;
                inferior = Math.min(opcionContenedore[2],opcionContenedore[3]);
                superior = Math.max(opcionContenedore[2],opcionContenedore[3]);
                copiaContenedores = new ArrayList<>(List.copyOf(this.secContenedorCromosoma));
                for (int j = 0; j < distancia; j++) {
                    auxContenedor = copiaContenedores.get(inferior + j);
                    copiaContenedores.set(inferior + j, copiaContenedores.get(superior - j));
                    copiaContenedores.set(superior - j, auxContenedor);
                }
                murcielagoAct = new Murcielago(copiaPaquetes,copiaContenedores);
                murcielagoAct.calcularFitness(numEMSProbar, numPaquetesProbar);

                if(murcielagoAct.getFitness() < murcielagoAnt.getFitness()){
                    murcielagoAnt = murcielagoAct;
                }
            }
        }
        return murcielagoAnt;
    }

    public void actualizarA(float alphaMurcielago) {
        this.volumen = this.volumen*alphaMurcielago;
    }

    public void actualizarR(float v) {
        this.frecuenciaPulso = this.frecuenciaPulso*v;
    }
}
