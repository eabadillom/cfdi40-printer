package com.ferbo.cfdi.catalogs;

/**
 * Clase de utilidad para obtener las descripciones del catálogo c_ObjetoImp del SAT.
 */
public class CatalogoObjetoImpuesto {

    /**
     * Devuelve el nombre oficial del Objeto de Impuesto correspondiente a la clave del SAT.
     * 
     * @param clave Clave de dos dígitos del objeto de impuesto (ej. "01", "02", "03", "04")
     * @return String con la descripción oficial o un mensaje por defecto si no existe.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null) {
            return "No especificado";
        }

        switch (clave.trim()) {
            case "01":
                return "01 - No objeto de impuesto";
            case "02":
                return "02 - Con objeto de impuesto";
            case "03":
                return "03 - Con objeto de impuesto y no obligado al desglose";
            case "04":
                return "04 - Con objeto de impuesto y no causa impuesto";
            default:
                return "Objeto de impuesto no válido (" + clave + ")";
        }
    }
}
