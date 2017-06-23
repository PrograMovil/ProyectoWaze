package com.proyecto.waze;

/**
 * Created by SheshoVega on 22/06/2017.
 */

public class Variables {
    //    URL base del backend
    private static final String URLBase = "http://192.168.0.10:8084/BackendWaze/WazeServlet?"; // Cambiarla segun donde se ejecute el backend

    public static String getURLBase() {
        return URLBase;
    }
}
