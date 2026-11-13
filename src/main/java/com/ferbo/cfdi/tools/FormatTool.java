package com.ferbo.cfdi.tools;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatTool {

    /**
     * Convierte un BigDecimal a String con formato de moneda.
     * Usa la configuración regional (Locale) por defecto del sistema.
     */
    public static String formatearMoneda(BigDecimal monto) {
        if (monto == null) {
            return ""; // O puedes lanzar una excepción según tu lógica de negocio
        }
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
        return formatoMoneda.format(monto);
    }

    /**
     * Convierte un BigDecimal a String con formato de moneda según un Locale específico.
     * Útil si tu servidor está en una región pero necesitas mostrar otra moneda (ej. USD).
     */
    public static String formatearMoneda(BigDecimal monto, Locale locale) {
        if (monto == null || locale == null) {
            return "";
        }
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(locale);
        return formatoMoneda.format(monto);
    }
}
