package com.ferbo.cfdi.tools;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mx.gob.sat.cfdi.v40.Comprobante;

public class CuentaPredialTool {
    
    /**
     * Convierte una lista de números de cuenta predial en una sola cadena separada por comas.
     * 
     * @param cuentasPrediales Lista de objetos o cadenas con los números de cuenta.
     * @param prefijo Opcional (ej. "num_cta_"), pasar null o vacío si no se requiere.
     * @return Una cadena formateada como "num_cta_1234, num_cta_5678" o vacío si no hay datos.
     */
    public static String formatearCuentasPrediales(List<Comprobante.Conceptos.Concepto.CuentaPredial> cuentasPrediales, String prefijo) {
        if (cuentasPrediales == null || cuentasPrediales.isEmpty()) {
            return null;
        }

        String prefijoEfectivo = (prefijo != null) ? prefijo : "";
        
        return cuentasPrediales.stream()
                .filter(Objects::nonNull)
                .map(Comprobante.Conceptos.Concepto.CuentaPredial::getNumero)
                .map(cuenta -> prefijoEfectivo + cuenta)
                .collect(Collectors.joining(", "));
    }
}
