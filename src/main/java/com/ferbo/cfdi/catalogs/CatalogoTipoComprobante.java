package com.ferbo.cfdi.catalogs;

public class CatalogoTipoComprobante {

    /**
     * Devuelve la leyenda oficial correspondiente al tipo de CFDI.
     * 
     * @param clave Clave del tipo de comprobante (ej. "I", "E", "T")
     * @return String con la descripción oficial o un mensaje por defecto si no existe.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null) {
            return "No especificado";
        }

        // Convertimos a mayúsculas y quitamos espacios para evitar errores de dedo
        switch (clave.trim().toUpperCase()) {
            case "I":
                return "Ingreso";
            case "E":
                return "Egreso";
            case "T":
                return "Traslado";
            case "N":
                return "Nómina";
            case "P":
                return "Pago";
            default:
                return "Tipo de comprobante no válido (" + clave + ")";
        }
    }
}
