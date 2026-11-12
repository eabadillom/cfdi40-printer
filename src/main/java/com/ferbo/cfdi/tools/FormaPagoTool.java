package com.ferbo.cfdi.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Herramienta utilitaria para gestionar las claves del catálogo c_FormaPago 
 * vigentes según las especificaciones del SAT para CFDI 4.0.
 */
public final class FormaPagoTool {

    // Mapa inmutable que contiene todas las claves oficiales vigentes del SAT
    private static final Map<String, String> FORMAS_PAGO;

    static {
        Map<String, String> mapaTemporal = new HashMap<>();
        mapaTemporal.put("01", "01 - Efectivo");
        mapaTemporal.put("02", "02 - Cheque nominativo");
        mapaTemporal.put("03", "03 - Transferencia electrónica de fondos");
        mapaTemporal.put("04", "04 - Tarjeta de crédito");
        mapaTemporal.put("05", "05 - Monedero electrónico");
        mapaTemporal.put("06", "06 - Dinero electrónico");
        mapaTemporal.put("08", "08 - Vales de despensa");
        mapaTemporal.put("12", "12 - Dación en pago");
        mapaTemporal.put("13", "13 - Pago por subrogación");
        mapaTemporal.put("14", "14 - Pago por consignación");
        mapaTemporal.put("15", "15 - Condonación");
        mapaTemporal.put("17", "17 - Compensación");
        mapaTemporal.put("23", "23 - Novación");
        mapaTemporal.put("24", "24 - Confusión");
        mapaTemporal.put("25", "25 - Remisión de deuda");
        mapaTemporal.put("26", "26 - Prescripción o caducidad");
        mapaTemporal.put("27", "27 - A satisfacción del acreedor");
        mapaTemporal.put("28", "28 - Tarjeta de débito");
        mapaTemporal.put("29", "29 - Tarjeta de servicios");
        mapaTemporal.put("30", "30 - Aplicación de anticipos");
        mapaTemporal.put("31", "31 - Intermediario pagos");
        mapaTemporal.put("99", "99 - Por definir");

        // Hacemos el mapa inmutable para proteger la integridad de los datos en memoria
        FORMAS_PAGO = Collections.unmodifiableMap(mapaTemporal);
    }

    // Constructor privado para evitar la instanciación de la clase utilitaria
    private FormaPagoTool() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada.");
    }

    /**
     * Obtiene la descripción formateada de la forma de pago a partir de su clave oficial del SAT.
     * 
     * @param clave Clave de 2 dígitos (ej. "03", "01", "99")
     * @return La leyenda con formato "Clave - Descripción" o null si la clave no existe.
     */
    public static String obtenerLeyendaFormaPago(String clave) {
        if (clave == null) {
            return null;
        }
        return FORMAS_PAGO.get(clave.trim());
    }
}
