package com.ferbo.cfdi.catalogs;

/**
 * Clase de utilidad para obtener las descripciones de los catálogos del SAT.
 */
public class CatalogoExportacion {

    /**
     * Devuelve la leyenda correspondiente a la clave de exportación del CFDI 4.0.
     * 
     * @param clave Clave de exportación (ej. "01", "02")
     * @return String con la descripción oficial o un mensaje por defecto si no existe.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null) {
            return "No especificado";
        }

        switch (clave.trim()) {
            case "01":
                return "No aplica";
            case "02":
                return "Definitiva con clave A1";
            case "03":
                return "Temporal";
            case "04":
                return "Definitiva con clave distinta a A1 o cuando no existe enajenación en términos del CFF";
            default:
                return "Clave de exportación no válida (" + clave + ")";
        }
    }
}
