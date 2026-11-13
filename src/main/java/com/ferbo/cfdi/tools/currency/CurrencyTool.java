package com.ferbo.cfdi.tools.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.ferbo.cfdi.tools.currency.enums.MonedaISO;
import com.ferbo.cfdi.tools.currency.interfaces.Divisa;


/**
 * Clase de utilidad para convertir cantidades numéricas a su representación en texto en español, incluyendo la conversión a formato de moneda.
 * Esta clase soporta cantidades grandes, incluyendo billones, millones y miles, y puede manejar diferentes monedas según el código ISO proporcionado.
 * 
 * @author Esteban
 * @version 1.0
 * @since 2026-11-11
 */
public class CurrencyTool {

    private static final String[] UNIDADES = { "", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO",
            "NUEVE" };

    private static final String[] DECENAS = { "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISEIS",
            "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA",
            "OCHENTA", "NOVENTA" };

    private static final String[] CENTENAS = { "CIEN", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS",
            "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS" };

    public static String obtenerDescripcionMoneda(String codigoMoneda) {
        Divisa moneda = MonedaISO.desdeCodigo(codigoMoneda);
        if (moneda == null) {
            throw new IllegalArgumentException("Código de moneda no válido: " + codigoMoneda);
        }
        return moneda.getNombre();
    }

    public static String convertirAMoneda(BigDecimal cantidad, String codigoMoneda) {
        if (cantidad == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }

        //Obtenemos una instancia de la interfaz Divisa
        Divisa moneda = MonedaISO.desdeCodigo(codigoMoneda);
        int precision = moneda.getDecimales();

        BigDecimal cantidadRedondeada = cantidad.setScale(precision, RoundingMode.HALF_UP);
        long parteEntera = cantidadRedondeada.longValue();

        int parteDecimal = 0;
        if (precision > 0) {
            parteDecimal = cantidadRedondeada.remainder(BigDecimal.ONE)
                    .multiply(BigDecimal.valueOf(Math.pow(10, precision))).setScale(0, RoundingMode.HALF_UP).intValue();
        }

        String textoEntero = convertirNumeroATexto(parteEntera);
        StringBuilder resultado = new StringBuilder();

        if (parteEntera == 0) {
            resultado.append("CERO");
        } else {
            resultado.append(textoEntero);
        }

        resultado.append(" ").append(moneda.getTextoPorCantidad(parteEntera)).append(" ");

        if (precision > 0) {
            String formatoFraccion = "%0" + precision + "d/%d %s";
            resultado.append(
                    String.format(formatoFraccion, parteDecimal, (int) Math.pow(10, precision), codigoMoneda));
        } else {
            resultado.append(moneda.getSufijo());
        }

        return resultado.toString();
    }

    private static String convertirNumeroATexto(long numeroPrimitivo) {
    // Para evitar desbordamientos y soportar billones, convertimos el valor a BigDecimal
    BigDecimal numero = new BigDecimal(numeroPrimitivo);
    
    if (numero.compareTo(BigDecimal.ZERO) == 0) {
        return "CERO";
    }
    
    List<String> partes = new ArrayList<>();
    
    BigDecimal unBillon = new BigDecimal("1000000000000");
    BigDecimal unMillon = new BigDecimal("1000000");
    BigDecimal unMil = new BigDecimal("1000");
    
    // 1. PROCESAR BILLONES (Escala larga: 10^12)
    if (numero.compareTo(unBillon) >= 0) {
        BigDecimal totalBillones = numero.divideToIntegralValue(unBillon);
        long valorBillones = totalBillones.longValue();
        
        if (valorBillones == 1) {
            partes.add("UN BILLÓN");
        } else {
            // Reutiliza la misma lógica recursiva/estructural para leer los miles/cientos de billones
            partes.add(convertirNumeroATexto(valorBillones));
            partes.add("BILLONES");
        }
        numero = numero.remainder(unBillon);
    }
    
    // 2. PROCESAR MILLONES (Escala: 10^6)
    if (numero.compareTo(unMillon) >= 0) {
        long totalMillones = numero.divideToIntegralValue(unMillon).longValue();
        
        if (totalMillones == 1) {
            partes.add("UN MILLÓN");
        } else {
            int milesDeMillones = (int) (totalMillones / 1000);
            int millonesRestantes = (int) (totalMillones % 1000);
            
            if (milesDeMillones > 0) {
                if (milesDeMillones == 1) {
                    partes.add("MIL");
                } else {
                    partes.add(convertirCentenas(milesDeMillones));
                    partes.add("MIL");
                }
            }
            
            if (millonesRestantes > 0) {
                partes.add(convertirCentenas(millonesRestantes));
            }
            
            partes.add("MILLONES");
        }
        numero = numero.remainder(unMillon);
    }
    
    // 3. PROCESAR MILES ESTÁNDAR (Escala: 10^3)
    if (numero.compareTo(unMil) >= 0) {
        int miles = numero.divideToIntegralValue(unMil).intValue();
        if (miles == 1) {
            partes.add("MIL");
        } else {
            partes.add(convertirCentenas(miles));
            partes.add("MIL");
        }
        numero = numero.remainder(unMil);
    }
    
    // 4. PROCESAR CENTENAS RESTANTES
    if (numero.compareTo(BigDecimal.ZERO) > 0) {
        partes.add(convertirCentenas(numero.intValue()));
    }
    
    return String.join(" ", partes).trim();
}


    private static String convertirCentenas(int numero) {
        if (numero == 0)
            return "";
        if (numero < 10)
            return UNIDADES[numero];
        if (numero < 20)
            return DECENAS[numero - 10];

        if (numero < 100) {
            int decena = (numero / 10) - 2 + 10;
            int unidad = numero % 10;
            if (unidad == 0) {
                return DECENAS[decena];
            } else if (numero >= 21 && numero <= 29) {
                return "VEINTI" + UNIDADES[unidad];
            } else {
                return DECENAS[decena] + " Y " + UNIDADES[unidad];
            }
        }

        int centena = (numero / 100) - 1;
        int resto = numero % 100;
        String textoCentena = CENTENAS[centena];

        if (resto == 0)
            return textoCentena;
        if (centena == 0 && resto > 0)
            textoCentena = "CIENTO";

        return textoCentena + " " + convertirCentenas(resto);
    }

    public static void main(String[] args) {
        // Ejecución en JDK 8
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("10500.34"), "MXN"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("10450624303.34"), "MXN"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("4310450624303.34"), "MXN"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("11946.24"), "MXN"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("1250.75"), "USD"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("45000.50"), "CAD"));
        System.out.println(CurrencyTool.convertirAMoneda(new BigDecimal("0"), "XXX"));
        System.out.println(CurrencyTool.obtenerDescripcionMoneda("MXN")); // PESO MEXICANO
        System.out.println(CurrencyTool.obtenerDescripcionMoneda("XXX")); // XXX
    }
}
