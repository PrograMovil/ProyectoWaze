
package Control;

import Model.Distancia;
import Model.Duracion;
import Model.LatLng;
import Model.Ruta;
import PG.Genetica;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Control {

    public Control() {
    }
    
    public ArrayList<Ruta> evaluarRutas(ArrayList<Ruta> rutas){
        Ruta mejorRuta=new Ruta();
        Genetica gen=new Genetica(rutas);
        try {
            mejorRuta=gen.calcularMejorRuta(rutas);
        } catch (Exception ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Ruta> aux=new ArrayList<>();
        aux.add(mejorRuta);
        return aux;
    }
    /*
    public static void main(String[] args) {
        ArrayList<Ruta> rutas=new ArrayList<>();
        
        List<LatLng> points= new ArrayList<>();
        points.add(0,new LatLng(23.4,1.2));
        points.add(1,new LatLng(134.2,222.2));
        points.add(2,new LatLng(13.2,218.2));
        rutas.add(new Ruta(new Distancia("abc", 12),new Duracion("cba", 2),"Heredia", new LatLng(255.6, 382.1), "San Jose", new LatLng(292.3,172.3), points));
        
        List<LatLng> points2= new ArrayList<>();
        points.add(0,new LatLng(53.4,5.2));
        points.add(1,new LatLng(334.2,112.2));
        points.add(2,new LatLng(223.2,238.2));
        rutas.add(new Ruta(new Distancia("abc", 22),new Duracion("cba", 4),"Cartago", new LatLng(253.6, 322.1), "Rusia", new LatLng(294.3,171.3), points));
        
        List<LatLng> points3= new ArrayList<>();
        points.add(0,new LatLng(56.4,6.2));
        points.add(1,new LatLng(166.2,262.2));
        points.add(2,new LatLng(16.2,268.6));
        rutas.add(new Ruta(new Distancia("abc", 33),new Duracion("cba", 6),"LA", new LatLng(265.6, 362.1), "SF", new LatLng(262.3,176.3), points));
        
        List<LatLng> points4= new ArrayList<>();
        points.add(0,new LatLng(27.4,1.0));
        points.add(1,new LatLng(194.2,222.2));
        points.add(2,new LatLng(199.2,298.2));
        rutas.add(new Ruta(new Distancia("abc", 3),new Duracion("cba", 1),"Mexico", new LatLng(295.6, 392.1), "Panama", new LatLng(299.3,172.9), points));
        
        
        Genetica gen=new Genetica(rutas);
        try {
            gen.calcularMejorRuta(rutas);
        } catch (Exception ex) {
            System.out.println("Error al correr algoritmo");;
        }
        
    }*/
    
}
