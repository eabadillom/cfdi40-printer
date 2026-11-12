package com.ferbo.cfdi.tools;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.sat.cfdi.v40.Comprobante;

public class FolioRelacionadoTool {
    private static final Logger log = LogManager.getLogger(FolioRelacionadoTool.class);

    public static String formatearFoliosRelacionados(List<Comprobante.CfdiRelacionados> foliosRelacionados) {
        log.debug("Folios relacionados: {}", foliosRelacionados);

        if (foliosRelacionados == null || foliosRelacionados.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Comprobante.CfdiRelacionados cfdiRelacionados : foliosRelacionados) {
            cfdiRelacionados.getCfdiRelacionado().forEach(folio -> {
                sb.append(folio.getUUID()).append("\n");
            });
        }

        return sb.toString().trim();
    }
    
}
