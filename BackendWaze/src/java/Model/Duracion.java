/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author SheshoVega
 */
public class Duracion {
    public String text;
    public int value;

    public Duracion(String text, int value) {
        this.text = text;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + "text : " + text + ", value : " + value + '}';
    }
    
    
}
