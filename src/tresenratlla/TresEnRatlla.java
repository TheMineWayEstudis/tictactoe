package tresenratlla;

import java.util.Random;
import java.util.Scanner;

public class TresEnRatlla {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static String[][] tauler = new String[3][3]; // Tauler (PL = Jugador, IA = Màquina)
    static String nom = "", caracter = "", IAcaracter = "";
    static boolean comencat = false;
    static boolean jugador = false;

    public static void main(String[] args) {
        Inicialitzar();

        do {
            String guanyador = Comprovar();
            if (!guanyador.equals("")) {
                System.out.println("***************************************");
                System.out.println("HA GUANYAT " + (guanyador.equals("PL") ? "el/la jugador/a " + nom : "la IA"));
                System.out.println("***************************************");
                ReiniciarJoc();
            }
            if(Comptar() >= 9) {
                System.out.println("****************************");
                System.out.println("Hi ha hagut un empat");
                System.out.println("****************************");
                ReiniciarJoc();
            }
            int opcio = Menu();
            if(opcio == 3) {
                System.out.print("\nSegur que vols abandonar? (s/n) ");
                if(GetYesNo()) break;
                continue;
            }
            switch(opcio) {
                case 1:
                    if(comencat) {
                        Error(2);
                        break;
                    }
                    System.out.print("\nQuin és el teu nom? ");
                    nom = scanner.next();
                    System.out.print("Vols jugar amb X o O? ");
                    caracter = GetBetween("X","O");
                    IAcaracter = caracter.equals("X") ? "O" : "X"; // La màquina agafa el contrari
                    jugador = random.nextInt(2) == 0;
                    System.out.println("\nTot llest per jugar!");
                    comencat = true;
                    break;
                case 2:
                    if(!comencat) {
                        Error(3);
                        break;
                    }
                    ColocarFitxa();
                    break;
                default: Error(999); break;
            }
        } while(true);
        System.out.println("S'ha acabat el programa. Adeu!");
    }

    static void ColocarFitxa() {
        Mostrar_taulell();
        if(jugador) Fitxa_jugador(); // Col·loca la fitxa el jugador
        else Colocar(Fitxa_maquina(),false); // Col·loca fitxa la IA
        jugador = !jugador;
    }
    static void Fitxa_jugador() {
        System.out.println("On vols col·locar la teva fitxa?");
        int fila; // y
        do {
            System.out.print("Fila: "); // y
            fila = GetNumber(1,3) - 1;
            if((Buscar(0,fila) ? 1 : 0) + (Buscar(1,fila) ? 1 : 0) + (Buscar(2,fila) ? 1 : 0) < 3) break;
            Error(4);
        } while(true);
        System.out.print("Columna: "); // x
        do {
            int columna = GetNumber(1,3) - 1;
            if(!Buscar(columna, fila)) {
                Colocar(new Vector2(columna,fila),true);
                return;
            }
            Error(5);
        } while(true);
    }
    static void Colocar(Vector2 pos, boolean player) {
        tauler[pos.x][pos.y] = player ? "PL" : "IA";
        System.out.println("\nTaulell  actualitzat:");
        Mostrar_taulell();
    }

    static Vector2 Fitxa_maquina() {
        Vector2 pos;
        if (tauler[1][1].equals("")) return new Vector2(1,1); // "El centre mola" -Cs
        
        //Guanyar
        //Vertical
        for(int x = 0; x < tauler.length; x++) {
            int c = 0;
            int p = -1;
            for(int y = 0; y < tauler[x].length; y++) {
                if(tauler[x][y].equals(IAcaracter)) c++;
                else if(tauler[x][y].equals("")) p = y;
            }
            if(p >= 0) return new Vector2(x,p);
        }
        //Horizontal
        for(int y = 0; y < tauler[0].length; y++) {
            int c = 0;
            int p = -1;
            for(int x = 0; x < tauler.length; x++) {
                if(tauler[x][y].equals(IAcaracter)) c++;
                else if(tauler[x][y].equals("")) p = y;
            }
            if(p >= 0) return new Vector2(p,y);
        }
        //Diagonal
        
        
        System.out.println("LA AI NO FURULA");
        
        do {
            pos = new Vector2(random.nextInt(3),random.nextInt(3));
        } while(Buscar(pos.x, pos.y));
        return pos;
    }
    
    static int Menu() {
        System.out.println("\n*** MENÚ PRINCIPAL ***");
        System.out.println("1. Inicia joc");
        System.out.println("2. Col·local fitxa");
        System.out.println("3. Sortir");
        System.out.print("Introdueix una opció: ");
        return GetNumber(1,3);
    }
    static void Inicialitzar() {
        for(int x = 0; x < tauler.length; x++) {
            for(int y = 0; y < tauler[x].length; y++) {
                tauler[x][y] = "";
            }
        }
    }
    static boolean Buscar(int x, int y) {
        return !tauler[x][y].equals("");
    }
    static void Mostrar_taulell() {
        System.out.println();
        Divisor();
        for(int x = 0; x < tauler.length; x++) {
            for(int y = 0; y < tauler[x].length; y++) {
                System.out.print("| " + (tauler[y][x].equals("") ? " " : (tauler[y][x].equals("PL") ? caracter : IAcaracter)) + " ");
            }
            System.out.println("|");
            Divisor();
        }
    }
    static int Comptar() {
        int c = 0;
        for(String[] x: tauler)
            for(String y: x) if(!y.equals("")) c++;
        return c;
    }
    static String Comprovar() {
        String last = "-";
        for(int x = 0; x < tauler.length; x++) {
            int c = 0;
            for(int y = 0; y < tauler[x].length; y++) {
                if(!last.equals(tauler[x][y])) {
                    last = tauler[x][y];
                    c = 0;
                }
                c++;
            }
            if(c >= 3) return last;
        }
        last = "-";
        for(int y = 0; y < tauler[0].length; y++) {
            int c = 0;
            for(int x = 0; x < tauler.length; x++) {
                if(!last.equals(tauler[x][y])) {
                    last = tauler[x][y];
                    c = 0;
                }
                c++;
            }
            if(c >= 3) return last;
        }
        last = "-";
        int d = 0;
        for(int i = 0; i < tauler.length; i++) {
            if(!last.equals(tauler[i][i])) {
                last = tauler[i][i];
                d = 0;
            }
            d++;
        }
        if(d >= 3) return last;
        d = 0;
        last = "-";
        for(int i = tauler.length - 1; i >= 0; i--) {
            if(!last.equals(tauler[i][i])) {
                last = tauler[i][i];
                d = 0;
            }
            d++;
        }
        if(d >= 3) return last;
        return ""; // Sense guanyador encara
    }

    static void ReiniciarJoc() {
        comencat = false;
        Inicialitzar();
    }
    static boolean GetYesNo() {
        return GetBetween("S","N").equals("S");
    }
    static int GetNumber(int min, int max) {
        do {
            int num = scanner.nextInt();
            if(num >= min && num <= max) return num;
            Error(1);
        } while(true);
    }
    static String GetBetween(String... valors) {
        do {
            String val = scanner.next().toUpperCase();
            for(String valor: valors) if(valor.equals(val)) return val;
            Error(6);
        } while(true);
    }
    static void Divisor() {
        for(int i = 0; i < 7; i++) System.out.print("-");
        System.out.println();
    }

    static void Error(int error) {
        switch (error) {
            case 1: Error("Valor fora de rang"); break;
            case 2: Error("Ja has començat el joc anteriorment!"); break;
            case 3: Error("No has iniciat el joc encara!"); break;
            case 4: Error("La fila ja està plena!"); break;
            case 5: Error("La casella ja està ocupada!"); break;
            case 6: Error("El valor introduit no és vàlid."); break;
            default: Error("Error desconegut"); break;
        }
    }
    static void Error(String error) {
        System.out.println("[!] " + error);
    }

    static class Vector2 {
        public int x = 0;
        public int y = 0;

        public Vector2(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public static Vector2 Zero() {
            return new Vector2(0,0);
        }
    }
}