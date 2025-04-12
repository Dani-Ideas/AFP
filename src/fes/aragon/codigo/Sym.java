/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fes.aragon.codigo;

/**
 *
 * @author MASH
 */
public class Sym {
  public static final int ENTERO = 0;
  public static final int ID = 1;  
  public static final int PUNTOCOMA = 2;
  public static final int SALTOLINEA = 3;  
  public static final int EOF = 4;
  public static final int IGUAL = 5;
  public static final int MAS = 6;
  public static final int MENOS = 7;
  public static final int LETRA_S = 20; // Símbolo inicial
  public static final int LETRA_A = 21;
  public static final int LETRA_B = 22;
  public static final int LETRA_C = 23;
  public static final int LETRA_D = 24;
  public static final String[] terminales = new String[] {
  "ENTERO",  
  "ID",
  "PUNTOCOMA",
  "SALTOLINEA",
  "EOF",
  "IGUAL",
  "MAS",
  "MENOS",
  "LETRA_A",
  "LETRA_B",
  "LETRA_C",
  "LETRA_D"
  };
}
