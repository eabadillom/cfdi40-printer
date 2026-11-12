package com.ferbo.cfdi.catalogs;

import java.util.HashMap;
import java.util.Map;

public class CatalogoImpuestos {

    // Mapa estático para almacenar la relación clave-leyenda del catálogo c_Impuesto
    private static final Map<String, String> CATALOGO = new HashMap<>();

    // Bloque estático para inicializar los valores oficiales del SAT
    static {
        CATALOGO.put("001", "001 - ISR");
        CATALOGO.put("002", "002 - IVA");
        CATALOGO.put("003", "003 - IEPS");
    }

    /**
     * Obtiene la leyenda formateada de un impuesto a partir de su clave oficial.
     * 
     * @param clave Clave del impuesto (ej. "002")
     * @return Leyenda combinada (ej. "002 - IVA") o la clave original si no se encuentra.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null || clave.trim().isEmpty()) {
            return "Clave inválida";
        }
        
        // Retorna el valor mapeado; si no existe, devuelve la clave original como fallback
        return CATALOGO.getOrDefault(clave.trim(), clave);
    }
}
