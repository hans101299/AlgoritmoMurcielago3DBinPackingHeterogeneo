package algoritmoMurcielago;

import com.sun.javafx.scene.control.MultipleAdditionAndRemovedChange;
import estructuras.Contenedor;
import estructuras.Murcielago;
import estructuras.Paquete;

import java.util.*;
import java.util.stream.Collectors;

public class AlgoritmoMurcielago {
    public ArrayList<Contenedor> ejecutarAlgoritmoMurcielago(ArrayList<Paquete> secPaquetes, ArrayList<Contenedor> secContenedores, int tamPoblacion,
                                                             int maxIteraciones, int maxEstancamientos,float gammaMurcielago, float alphaMurcielago, int numProbar, float alfa, int numEMSProbar, int numPaquetesProbar){
        List<Murcielago> poblacion = crearPoblacionInicial(secPaquetes,secContenedores,tamPoblacion,alfa,numEMSProbar,numPaquetesProbar);
        Murcielago mejorSolucionAnterior = poblacion.stream().min(Comparator.comparingDouble(Murcielago::getFitness)).get();
        actualizarVelocidad(poblacion, mejorSolucionAnterior);
        Murcielago mejorSolucionActual, nuevoMurcielago;
        Random rand = new Random();
        int numIteracion=0, numEstancamiento=0;
        boolean estancamiento;
        System.out.println("Desperdicio: "+mejorSolucionAnterior.getFitness()+" ----- Contenedores Usados: "+mejorSolucionAnterior.getSolucion().size());
        while (numIteracion<maxIteraciones && numEstancamiento<maxEstancamientos){

            estancamiento=false;
            int index=0;
            for (Murcielago murcielago: poblacion) {
                if(murcielago.getFrecuenciaPulso()<rand.nextFloat()){
                    nuevoMurcielago = randomWalk(poblacion.stream().sorted(Comparator.comparingDouble(Murcielago::getFitness)).collect(Collectors.toCollection(ArrayList::new)),
                            numProbar, numEMSProbar, numPaquetesProbar);

                }
                else {
                    nuevoMurcielago = murcielago.actualizar(numEMSProbar,numPaquetesProbar);
                }

                //nuevoMurcielago.calcularFitness(numEMSProbar, numPaquetesProbar);
                if (rand.nextFloat()<nuevoMurcielago.getVolumen() &&
                        nuevoMurcielago.getFitness() < murcielago.getFitness()){
                    poblacion.set(index,nuevoMurcielago);
                    nuevoMurcielago.actualizarA(alphaMurcielago);
                    nuevoMurcielago.actualizarR(gammaMurcielago * (numIteracion+1));
                }
                if(rand.nextFloat()>nuevoMurcielago.getVolumen() &&
                        nuevoMurcielago.getFitness() < mejorSolucionAnterior.getFitness()){
                    System.out.println("Desperdicio: "+nuevoMurcielago.getFitness());
                }

                actualizarVelocidad(poblacion, mejorSolucionAnterior);
                index++;
            }
            mejorSolucionActual=poblacion.stream().min(Comparator.comparingDouble(Murcielago::getFitness)).get();
            if(Math.abs(mejorSolucionActual.getFitness()-mejorSolucionAnterior.getFitness())<0.00001){
                numEstancamiento++;
                estancamiento=true;
            }
            if(mejorSolucionActual.getFitness()<mejorSolucionAnterior.getFitness()){
                mejorSolucionAnterior=mejorSolucionActual;
                System.out.println("Desperdicio: "+mejorSolucionAnterior.getFitness()+" ----- Contenedores Usados: "+mejorSolucionAnterior.getSolucion().size()+ "------- Iteración: " + Integer.toString(numIteracion+1));
                if(!estancamiento) numEstancamiento=0;
                if(mejorSolucionActual.getFitness()<0){
                    for (Paquete p: mejorSolucionActual.getSecPaquetesCromosoma()) {
                        System.out.print(p.getId()+'-');
                    }
                    for (Contenedor c: mejorSolucionActual.getSecContenedorCromosoma()) {
                        System.out.print(c.getTipo()+'-');
                    }
                }
            }
            numIteracion++;
        }
        return mejorSolucionAnterior.getSolucion();
    }

    private void actualizarVelocidad(List<Murcielago> poblacion, Murcielago mejorSolucionAnterior) {
        int velocidad;
        for (Murcielago murcielago: poblacion) {
            velocidad = 0;
            for (int i=0;i<murcielago.getSecPaquetesCromosoma().size();i++) {
                if(!murcielago.getSecPaquetesCromosoma().get(i).getId().equals(mejorSolucionAnterior.getSecPaquetesCromosoma().get(i).getId())){
                    velocidad+=1;
                }
            }
            murcielago.setVelocidad(velocidad);
        }
    }

    private Murcielago randomWalk(ArrayList<Murcielago> poblacionOrdenada, int numProbar, int numEMSProbar, int numPaquetesProbar) {
        int numPoblacion = poblacionOrdenada.size();
        int num =  Math.min(numPoblacion,numProbar);
        Random rand = new Random();
        int indice = rand.nextInt(num);
        Murcielago murcielagoSelec = poblacionOrdenada.get(indice);
        if(rand.nextFloat()<0.5f)
            return murcielagoSelec.opt3(numEMSProbar,numPaquetesProbar);
        else
            return  murcielagoSelec.opt2(numEMSProbar,numPaquetesProbar);

    }

    public List<Murcielago> crearPoblacionInicial(ArrayList<Paquete> secPaquetes, ArrayList<Contenedor> secContenedores,
                                                  int tamPoblacion, float alfa, int numContenedoresProbar, int numPaquetesProbar) {
        ArrayList<Murcielago> Poblacion = new ArrayList<Murcielago>();
        int tamPaquetes;
        ArrayList<Paquete> secPaquetesOrdenado = new ArrayList<Paquete>(List.copyOf(secPaquetes));
        Collections.sort(secPaquetesOrdenado, Comparator.comparing(Paquete::getVolumen)
            .thenComparing(Paquete::getLimiteApilacion)
            .thenComparing(Paquete::getLargo)
            .thenComparing(Paquete::getAncho).reversed());
        tamPaquetes = secPaquetesOrdenado.size();
        for (int i=0; i <tamPoblacion; i++){
            Murcielago murcielago = new Murcielago(crearSecPaquetesAleatorio(secPaquetesOrdenado,alfa),
                    crearSecContenedoresAleatorio(secContenedores, tamPaquetes));
            murcielago.calcularFitness(numContenedoresProbar,numPaquetesProbar);
            Poblacion.add(murcielago);
        }
        return Poblacion;
    }

    public List<Contenedor> crearSecContenedoresAleatorio(List<Contenedor> secContenedores, int tamPaquetes) {
        ArrayList<Contenedor> SecContenedoresAleatorio = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<tamPaquetes;i++){
            SecContenedoresAleatorio.add(secContenedores.get(rand.nextInt(secContenedores.size())));
        }
        return SecContenedoresAleatorio;
    }

    public List<Paquete> crearSecPaquetesAleatorio(List<Paquete> secPaquetesOrdenado, float alfa) {
        ArrayList<Paquete> SecPaquetesAleatorio = new ArrayList<>();
        ArrayList<Paquete> secPaquetesOrdenadoCopia = new ArrayList<Paquete>(List.copyOf(secPaquetesOrdenado));
        Random rand = new Random();
        while (secPaquetesOrdenadoCopia.size()!=0){
            List<Paquete> RCL = crearRCL(secPaquetesOrdenadoCopia,alfa);
            Paquete paqueteSeleccionado = RCL.get(rand.nextInt(RCL.size()));
            SecPaquetesAleatorio.add(paqueteSeleccionado);
            secPaquetesOrdenadoCopia.remove(paqueteSeleccionado);
        }
        return SecPaquetesAleatorio;
    }

    public List<Paquete> crearRCL(ArrayList<Paquete> secPaquetesOrdenadoCopia, float alfa) {
        ArrayList<Paquete> RCL = new ArrayList<>();
        float max = secPaquetesOrdenadoCopia.get(0).getVolumen();
        float min = secPaquetesOrdenadoCopia.get(secPaquetesOrdenadoCopia.size()-1).getVolumen();
        for (Paquete paquete: secPaquetesOrdenadoCopia) {
            if(paquete.getVolumen()>=min+alfa*(max-min)){
                RCL.add(paquete);
            }
        }
        return RCL;
    }
}
