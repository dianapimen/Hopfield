import java.io.File;
import java.util.Scanner;

public class HopfielAritmetica {

    // Método para leer la matriz de 10x10 desde archivo y aplanarla a 100 elementos (-1 y 1)
    public static int[] leerFiguraDesdeArchivo(String rutaArchivo, int filas, int columnas) {
        int N = filas * columnas;
        int[] patron = new int[N];
        int index = 0;

        try {
            File archivo = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                patron[index] = (val <= 0) ? -1 : 1;
                index++;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + rutaArchivo + " -> " + e.getMessage());
        }
        return patron;
    }

    // Método para imprimir la figura en consola
    public static void imprimirFigura(String mensaje, int[] patron, int filas, int columnas) {
        System.out.println(mensaje);
        for (int i = 0; i < filas; i++) {
            System.out.print("  ");
            for (int j = 0; j < columnas; j++) {
                int val = patron[i * columnas + j];
                System.out.print((val == 1 ? "1 " : "0 "));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int filas = 10;
        int columnas = 10;
        int N = filas * columnas; // 100 neuronas
        
        // Limitamos estrictamente a las primeras 4 figuras para prevenir la saturación de la red
        int numFiguras = 4;

        int[][] patrones = new int[numFiguras][N];

        // 1 y 2. Cargar únicamente figura1.txt hasta figura4.txt desde el dataset
        for (int f = 0; f < numFiguras; f++) {
            String ruta = "dataset/figura" + (f + 1) + ".txt";
            patrones[f] = leerFiguraDesdeArchivo(ruta, filas, columnas);
        }

        System.out.println("¡Las 4 figuras seleccionadas de 100 neuronas han sido cargadas exitosamente!\n");

        // 5, 6, 7 y 8. Calcular la matriz de pesos T sumando las matrices externas de los 4 patrones
        int[][] T = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int sumaPesos = 0;
                for (int f = 0; f < numFiguras; f++) {
                    sumaPesos += patrones[f][i] * patrones[f][j];
                }
                T[i][j] = sumaPesos;
            }
        }

        // 9. Poner a cero la diagonal principal de la matriz T
        for (int i = 0; i < N; i++) {
            T[i][i] = 0;
        }

        // 10. Definir patrón de entrada leyendo test.txt (con ruido de la raíz cuadrada)
        int[] patronEntrada = leerFiguraDesdeArchivo("dataset/test.txt", filas, columnas);
        imprimirFigura("Patrón de Entrada Inicial (Paso 10):", patronEntrada, filas, columnas);

        int[] patronResultante = new int[N];
        boolean iguales = false;

        // 12. Bucle para repetir el proceso hasta la convergencia
        while (!iguales) {
            
            // Multiplicación del patrón de entrada por la matriz de pesos T
            int[] producto = new int[N];
            for (int j = 0; j < N; j++) {
                int suma = 0;
                for (int i = 0; i < N; i++) {
                    suma += patronEntrada[i] * T[i][j];
                }
                producto[j] = suma;
            }

            // 11. Aplicar función de activación escalón F(x)
            for (int j = 0; j < N; j++) {
                if (producto[j] > 0) {
                    patronResultante[j] = 1;
                } else if (producto[j] < 0) {
                    patronResultante[j] = -1;
                } else {
                    patronResultante[j] = patronEntrada[j]; 
                }
            }

            // 12. Comprobar si el patrón de entrada es igual al patrón resultante
            iguales = true;
            for (int i = 0; i < N; i++) {
                if (patronEntrada[i] != patronResultante[i]) {
                    iguales = false;
                    break;
                }
            }

            // Validación del paso final
            if (iguales) {
                System.out.println("ENCONTRADO");
                imprimirFigura("Figura Final Estable:", patronResultante, filas, columnas);
            } else {
                // Sino, repetir proceso, regresar a paso 10 con patrón de entrada = patrón resultante
                for (int i = 0; i < N; i++) {
                    patronEntrada[i] = patronResultante[i];
                }
            }
        }
    }
}
