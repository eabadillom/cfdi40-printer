package com.ferbo.cfdi.catalogs;

/**
 * Clase de utilidad para obtener las descripciones del catálogo c_RegimenFiscal del SAT.
 */
public class CatalogoRegimenFiscal {

    /**
     * Devuelve el nombre oficial del Régimen Fiscal correspondiente a la clave del SAT.
     * 
     * @param clave Clave de tres dígitos del régimen fiscal (ej. "601", "605", "626")
     * @return String con la descripción oficial o un mensaje por defecto si no existe.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null) {
            return "No especificado";
        }

        switch (clave.trim()) {
            case "601":
                return "601 - General de Ley Personas Morales";
            case "603":
                return "603 - Personas Morales con Fines no Lucrativos";
            case "605":
                return "605 - Sueldos y Salarios e Ingresos Asimilados a Salarios";
            case "606":
                return "606 - Arrendamiento";
            case "607":
                return "607 - Régimen de Enajenación o Adquisición de Bienes";
            case "608":
                return "608 - Demás ingresos";
            case "610":
                return "610 - Residentes en el Extranjero sin Establecimiento Permanente en México";
            case "611":
                return "611 - Ingresos por Dividendos (socios y accionistas)";
            case "612":
                return "612 - Personas Físicas con Actividades Empresariales y Profesionales";
            case "614":
                return "614 - Ingresos por Intereses";
            case "615":
                return "615 - Régimen de los ingresos por obtención de premios";
            case "616":
                return "616 - Sin obligaciones fiscales";
            case "620":
                return "620 - Sociedades Cooperativas de Producción que optan por diferir sus ingresos";
            case "621":
                return "621 - Incorporación Fiscal";
            case "622":
                return "622 - Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras";
            case "623":
                return "623 - Opcional para Grupos de Sociedades";
            case "624":
                return "624 - Coordinados";
            case "625":
                return "625 - Régimen de las Actividades Empresariales con ingresos a través de Plataformas Tecnológicas";
            case "626":
                return "626 - Régimen Simplificado de Confianza (RESICO)";
            default:
                return "Régimen fiscal no válido (" + clave + ")";
        }
    }
}
