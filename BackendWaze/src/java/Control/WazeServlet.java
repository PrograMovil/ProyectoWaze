package Control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Model.Distancia;
import Model.Duracion;
import Model.LatLng;
import Model.Models;
import Model.Ruta;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SheshoVega
 */
@WebServlet(urlPatterns = {"/WazeServlet"})
public class WazeServlet extends HttpServlet {
    
    Gson gson = new Gson();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
//        String accion = request.getParameter("action");
//        
//        if(accion != null){
//            try{
//                PrintWriter out = response.getWriter();
//                Gson gson = new Gson();
//                switch (accion) {
//                    
//                    case "Testing": {
//                        System.out.println("Hello Backend!!!!");
//                        response.getWriter().write("Hello Waze!!!!");                        
//                    }
//                    break;
//                    case "calcularRuta": {
////                        String rutasJSON = request.getParameter("rutas");
//                        System.out.println("Llegan rutas!");
//                        System.out.println(rutasJSON);
//                        response.getWriter().write("Gracias por las rutas");
//                    }
//                    break;
//                    
//                }
//            }catch (Exception ex) {
//                Logger.getLogger(WazeServlet.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } 
        
    }
    
    public void printHTML(String msg, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Mensaje</title>");            
        out.println("</head>");
        out.println("<body>");
        out.println("<p>"+msg+"</p>");
        out.println("</body>");
        out.println("</html>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        String data;
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
          BufferedReader reader = request.getReader();
          while ((line = reader.readLine()) != null)
            jb.append(line);
        } catch (Exception e) { /*report an error*/ }
        
//        recibiendo la data desde el cliente
        data = jb.toString();
//        System.out.println(data);
        
        ArrayList<Ruta> rutasArray = new ArrayList<Ruta>();
        
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(data);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for(int i=0;i<jsonArray.size();i++){
            Ruta ruta = new Ruta();
            JsonObject jsonItem = (JsonObject) jsonArray.get(i);
//            System.out.println(jsonItem.toString());
            ruta.setDistance(gson.fromJson(jsonItem.get("distance"), Distancia.class));            
            ruta.setDuration(gson.fromJson(jsonItem.get("duration"), Duracion.class));
            ruta.setEndAddress(gson.fromJson(jsonItem.get("endAddress"), String.class));
            ruta.setStartAddress(gson.fromJson(jsonItem.get("startAddress"), String.class));
            JsonObject endLoca = (JsonObject) jsonItem.get("endLocation");        
            ruta.setEndLocation(new LatLng(endLoca.get("latitude").getAsDouble(),endLoca.get("longitude").getAsDouble()));
            JsonObject startLoca = (JsonObject) jsonItem.get("startLocation");        
            ruta.setStartLocation(new LatLng(startLoca.get("latitude").getAsDouble(),startLoca.get("longitude").getAsDouble()));
            JsonArray jsonPoints = jsonItem.get("points").getAsJsonArray();
            List<LatLng> puntos = new ArrayList<LatLng>();
            for(int j=0;j<jsonPoints.size();j++){
                JsonObject point = (JsonObject) jsonPoints.get(j);
                LatLng latLng = new LatLng(point.get("latitude").getAsDouble(),point.get("longitude").getAsDouble());
                puntos.add(latLng);
            }
            ruta.setPoints(puntos);
            rutasArray.add(ruta);
        }
        
        System.out.println(rutasArray.toString());
        
//        print de prueba
//        for(int i=0;i<rutasList.length;i++){
//            System.out.println("Ruta " + i + ": Distancia: " + rutasList[i].getDistance().text);
//            System.out.println(rutasList[i].toString());
//        }
        
//        Aqui hacer lo de la genetica --->
        

//        <--- luego el array resultado tipo List<Ruta> enviarlo al cliente:
        response.getWriter().write(gson.toJson(rutasArray));
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
