package Control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Model.Ruta;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
        Ruta[] rutasList = gson.fromJson(data, Ruta[].class);

//        print de prueba
        for(int i=0;i<rutasList.length;i++){
            System.out.println("Ruta " + i + ": Distancia: " + rutasList[i].getDistance().text);
        }
        
//        Aqui hacer lo de la genetica --->
        

//        <--- luego el array resultado tipo List<Ruta> enviarlo al cliente:
        response.getWriter().write(gson.toJson(rutasList));
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
