package estructuras;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Contenedor implements Cloneable{
    private final String tipo;
    private final float ancho;
    private final float largo;
    private final float alto;
    private final float limitePeso;
    private float pesoCargado;
    private ArrayList<EMS> ems = new ArrayList<>();
    private ArrayList<Empaque> solucion = new ArrayList<>();

    public Contenedor(String tipo, float ancho, float largo, float alto, float limitePeso) {
        this.tipo=tipo;
        this.ancho = ancho;
        this.largo = largo;
        this.alto = alto;
        this.limitePeso = limitePeso;
        this.pesoCargado=0;
        ems.add(new EMS(new float[]{0, 0, 0}, new float[]{ancho, largo, alto}));
    }

    public String getTipo() {
        return tipo;
    }

    public float getAncho() {
        return ancho;
    }

    public float getLargo() {
        return largo;
    }

    public float getAlto() {
        return alto;
    }

    public float getLimitePeso() {
        return limitePeso;
    }

    public ArrayList<EMS> getEms() {
        return ems;
    }

    public ArrayList<Empaque> getSolucion() {
        return solucion;
    }

    public float getVolumen() {
        return ancho*largo*alto;
    }

    public float getPesoCargado() {
        return pesoCargado;
    }

    public void setPesoCargado(float pesoCargado) {
        this.pesoCargado = pesoCargado;
    }

    @Override
    protected Contenedor clone() throws CloneNotSupportedException {
        return new Contenedor(this.tipo,this.ancho,this.largo,this.alto,this.limitePeso);
    }

    public void posicionar(Empaque empaquePos) {
        EMS ems1,ems2;
        float xOverlap, yOverlap, areaOverlap, areaPaquete, factorArea;
        float r1p1[] = {empaquePos.getxAxis(),empaquePos.getyAxis()};
        float r1p2[] = {empaquePos.getxAxis()+empaquePos.getPaquete().getAncho(),empaquePos.getyAxis()+empaquePos.getPaquete().getLargo()};
        Paquete paquete = empaquePos.getPaquete();
        pesoCargado+=paquete.getPeso();

        for (Empaque empaque: solucion) {
            if(empaque.getzAxis()+empaque.getPaquete().getAlto()>(empaquePos.getzAxis()+empaquePos.getPaquete().getAlto())) continue;
            float r2p1[]={empaque.getxAxis(),empaque.getyAxis()};
            float r2p2[]={empaque.getxAxis()+empaque.getPaquete().getAncho(),empaque.getyAxis()+empaque.getPaquete().getLargo()};
            if (r2p1[0]>r1p2[0] || r2p2[0]<r1p1[0]){
                continue;
            }
            else {
                xOverlap=Math.min(r1p2[0],r2p2[0])-Math.max(r1p1[0],r2p1[0]);
            }
            if(r2p1[1]>r1p2[1] || r2p2[1]<r1p1[1]){
                continue;
            }
            else {
                yOverlap=Math.min(r1p2[1],r2p2[1])-Math.max(r1p1[1],r2p1[1]);
            }
            areaOverlap=xOverlap*yOverlap;
            areaPaquete=empaque.getPaquete().getAncho()*empaque.getPaquete().getLargo();
            factorArea=areaOverlap/areaPaquete;
            empaque.setPesoTotalApilado(empaque.getPesoTotalApilado()+factorArea*empaquePos.getPaquete().getPeso());

        }
        solucion.add(empaquePos);
        ems1= new EMS(new float[]{empaquePos.getxAxis(),empaquePos.getyAxis(),empaquePos.getzAxis()},
                new float[]{empaquePos.getxAxis()+empaquePos.getPaquete().getAncho(),
                        empaquePos.getyAxis()+empaquePos.getPaquete().getLargo(),
                        empaquePos.getzAxis()+empaquePos.getPaquete().getAlto()});
        ems2= new EMS(new float[]{empaquePos.getxAxis(),empaquePos.getyAxis(),empaquePos.getzAxis()},
                new float[]{empaquePos.getxAxis2(),empaquePos.getyAxis2(),empaquePos.getzAxis2()});
        this.actualizarEMS(ems1,ems2);
    }

    public void limpiarEMS(ArrayList<Empaque> solucion) {
        ArrayList<EMS> paraEliminar = new ArrayList<>();
        for (int i=0;i<this.ems.size();i++) {
            for (Empaque emp : solucion) {
                if (this.ems.get(i).getP1()[0]==emp.getxAxis()&&this.ems.get(i).getP1()[1]==emp.getyAxis()&&this.ems.get(i).getP1()[2]==emp.getzAxis()){
                    paraEliminar.add(this.ems.get(i));
                }
            }
        }
        for (EMS ems:
             paraEliminar) {
            this.ems.remove(ems);
        }
    }

    private void actualizarEMS(EMS ems1, EMS ems2) {
        boolean esValido;
        ArrayList<EMS> copiaEMS = new ArrayList<EMS>(List.copyOf(this.ems));
        for (int i=0;i<copiaEMS.size();i++) {
            EMS emsAct = copiaEMS.get(i);
            if(overlapped(ems1,emsAct)){
                this.ems.remove(emsAct);
                ArrayList<EMS> nuevosEMS = new ArrayList<>();
                //nuevosEMS.add(new EMS(emsAct.getP1(),new float[]{ems1.getP1()[0],emsAct.getP2()[1],emsAct.getP2()[2]}));
                nuevosEMS.add(new EMS(new float[]{ems1.getP2()[0],emsAct.getP1()[1],emsAct.getP1()[2]},emsAct.getP2()));
                //nuevosEMS.add(new EMS(emsAct.getP1(),new float[]{emsAct.getP2()[0],ems1.getP1()[1],emsAct.getP2()[2]}));
                nuevosEMS.add(new EMS(new float[]{emsAct.getP1()[0],ems1.getP2()[1],emsAct.getP1()[2]},emsAct.getP2()));
                //nuevosEMS.add(new EMS(emsAct.getP1(),new float[]{emsAct.getP2()[0],emsAct.getP2()[1],ems1.getP1()[2]}));
                nuevosEMS.add(new EMS(new float[]{emsAct.getP1()[0],emsAct.getP1()[1],ems1.getP2()[2]},emsAct.getP2()));

                for (EMS nuevoEMS:nuevosEMS) {
                    esValido = true;
                    if (nuevoEMS.getP1()[0] == nuevoEMS.getP2()[0] || nuevoEMS.getP1()[1] == nuevoEMS.getP2()[1] || nuevoEMS.getP1()[2] == nuevoEMS.getP2()[2]) {
                        esValido = false;
                    }
                    for (EMS otroEMS : this.ems) {
                        if (inscribed(nuevoEMS, otroEMS)) {
                            esValido = false;
                        }
                    }
                    if (esValido) {
                        this.ems.add(nuevoEMS);
                    }
                }
            }
        }

    }

    public boolean inscribed(EMS ems1, EMS ems2) {
        if(ems2.getP1()[0]<=ems1.getP1()[0] && ems2.getP1()[1]<=ems1.getP1()[1] && ems2.getP1()[2]<=ems1.getP1()[2] &&
                ems1.getP2()[0]<=ems2.getP2()[0] && ems1.getP2()[1]<=ems2.getP2()[1] && ems1.getP2()[2]<=ems2.getP2()[2]){
            return true;
        }
        return false;
    }

    public boolean overlapped(EMS ems1, EMS ems2) {
        if(ems1.getP2()[0]>ems2.getP1()[0] && ems1.getP2()[1]>ems2.getP1()[1] && ems1.getP2()[2]>ems2.getP1()[2] &&
                ems1.getP1()[0]<ems2.getP2()[0] && ems1.getP1()[1]<ems2.getP2()[1] && ems1.getP1()[2]<ems2.getP2()[2]){
            return true;
        }
        return false;
    }

    public void imprimirSolucion() {
        System.out.println("Contenedor tipo: " + this.tipo);
        for (Empaque emp:
             solucion) {
            System.out.println("Paquete: "+emp.getPaquete().getId()+"------ Posición: "+emp.getxAxis()+", "+emp.getyAxis()+", "+emp.getzAxis());
        }
    }
}
