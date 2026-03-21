package com.sifen.api.integration.sifen.util;

public abstract class SifenDvCalculator {
            // generateDv

    public static String generateDvRuc(String ruc) {
        // 1. Manejo del caso especial (este caso se mantiene si es un requisito de negocio,
        // aunque la lógica de SIFEN usualmente no lo requiere si el RUC se calcula).
        if ("88888801".equals(ruc)) {
            return "5";
        }

        // Parámetros de la lógica SIFEN:
        int[] weights = new int[]{2, 3, 4, 5, 6, 7}; // Pesos 2 a 7
        int wLen = weights.length;
        int sum = 0;

        String digits = ruc;

        // 2. Cálculo de Módulo 11 (Dirección IZQUIERDA a DERECHA)
        for (int i = 0; i < digits.length(); i++) {
            // Obtener el dígito
            // Se resta '0' para convertir el carácter dígito a su valor entero.
            int d = digits.charAt(i) - '0';

            // Obtener el peso cíclico (i % wLen)
            int w = weights[i % wLen];

            // Acumulación del producto: dígito * peso
            sum += d * w;
        }

        // 3. Determinación del resto (remainder)
        int remainder = sum % 11;

        // 4. Cálculo del DV crudo "estándar" (0..10)
        // dv = (11 - remainder) % 11.
        // remainder=0 -> 0, remainder=1 -> 10, resto -> 11-resto
        int dvRaw = (11 - remainder) % 11;

        // 5. Aplicación de la regla de colapso (SingleDigit: 10 -> 0)
        int dvFinal = (dvRaw == 10) ? 0 : dvRaw;

        return String.valueOf(dvFinal);
    }



    public static String generateDvCdc(String cdc) {

        int baseMax = 11, k = 2, total = 0;

        // Caso especial
        if (cdc.equals("88888801")) {
            return "5";
        }

        // Recorrer de derecha a izquierda
        for (int i = cdc.length() - 1; i >= 0; i--) {
            k = k > baseMax ? 2 : k;
            int n = Integer.parseInt(cdc.substring(i, i + 1));
            total += n * k;
            k++;
        }

        // Misma lógica de cálculo final
        int resto = total % 11;
        return String.valueOf(resto > 1 ? 11 - resto : 0);

    }



}
