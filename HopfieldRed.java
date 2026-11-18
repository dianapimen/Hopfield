public class HopfieldRed {
    
    
    public static void imprimirVector(String mensaje, int[] v) {
        System.out.print(mensaje + " [");
        for (int i = 0; i < v.length; i++) {
            System.out.print(v[i] + (i < v.length - 1 ? ", " : ""));
        }
        System.out.println("]");
    }
    
    
    public static void imprimirMatriz(String mensaje, int[][] m) {
        System.out.println(mensaje + ":");
        for (int i = 0; i < m.length; i++) {
            System.out.print("  [");
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + (j < m[i].length - 1 ? ", " : ""));
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {
        // Pasos 1, 2 y 3
        int[] x1 = {1, 1, 1, -1};
        int[] x2 = {-1, -1, -1, 1};
        
        int n = 4;
        int[][] T = new int[n][n];
        
        imprimirVector("Patrón X1:", x1);
        imprimirVector("Patrón X2:", x2);
        
        // Pasos 4
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                T[i][j] = (x1[i] * x1[j]) + (x2[i] * x2[j]);
            }
        }
        
        imprimirMatriz("Matriz T antes de limpiar la diagonal (Paso 8)", T);
        
        // Paso 9: Poner en 0 la diagonal principal
        for (int i = 0; i < n; i++) {
            T[i][i] = 0;
        }
        
        imprimirMatriz("Matriz T final con diagonal en 0 (Paso 9)", T);
        
        // Paso 10: Definir el patrón de entrada inicial
        int[] patronEntrada = {1, -1, 1, -1};
        int[] patronResultante = new int[n];
        
        boolean iguales = false;
        
        System.out.println("\n--- INICIO DEL PROCESO DE ITERACIÓN ---");
        
        // Bucle para repetir el proceso hasta la convergencia (Paso 12)
        while (!iguales) {
            imprimirVector("Patrón de entrada actual:", patronEntrada);
            
            // Multiplicación del patrón de entrada por la matriz de pesos T
            int[] producto = new int[n];
            for (int j = 0; j < n; j++) {
                int suma = 0;
                for (int i = 0; i < n; i++) {
                    suma += patronEntrada[i] * T[i][j];
                }
                producto[j] = suma;
            }
            
            imprimirVector("Resultado de Multiplicación (Entrada * T):", producto);
            
            // Paso 11: Aplicar función de activación escalón
            for (int j = 0; j < n; j++) {
                if (producto[j] > 0) {
                    patronResultante[j] = 1;
                } else if (producto[j] < 0) {
                    patronResultante[j] = -1;
                } else {
                    patronResultante[j] = patronEntrada[j]; // Si es 0, se mantiene sin cambios
                }
            }
            
            imprimirVector("Patrón resultante tras activación:", patronResultante);
            
            // Paso 12: Comprobar si el patrón de entrada es igual al patrón resultante
            iguales = true;
            for (int i = 0; i < n; i++) {
                if (patronEntrada[i] != patronResultante[i]) {
                    iguales = false;
                    break;
                }
            }
            
            // Verificación del paso final
            if (iguales) {
                System.out.println("ENCONTRADO");
            } else {
                // Sino, repetir proceso, regresar a paso 10 con patrón de entrada = patrón resultante
                System.out.println("No son iguales, regresando al paso 10...\n");
                for (int i = 0; i < n; i++) {
                    patronEntrada[i] = patronResultante[i];
                }
            }
        }
    }
}
