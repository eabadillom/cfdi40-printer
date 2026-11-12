package com.ferbo.cfdi.tools;

public class MetodoPagoTool {

    /**
     * Obtiene la clave y descripción del catálogo c_MetodoPago (CFDI 4.0) del SAT.
     *
     * @param clave Clave del método de pago ("PUE" o "PPD").
     * @return Cadena con el formato "CLAVE - Descripción", o la clave original si no coincide.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null || clave.trim().isEmpty()) {
            return null;
        }
        
        String claveNormalizada = clave.trim().toUpperCase();

        switch (claveNormalizada) {
            case "PUE":
                return "PUE - Pago en una sola exhibición";
            case "PPD":
                return "PPD - Pago en parcialidades o diferido";
            default:
                return clave;
        }
    }
}

