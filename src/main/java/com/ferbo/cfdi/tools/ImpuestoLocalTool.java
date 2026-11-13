package com.ferbo.cfdi.tools;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.gob.sat.cfdi.implocal.ImpuestosLocales;

public class ImpuestoLocalTool {
    private static Logger log = LogManager.getLogger(ImpuestoLocalTool.class);
    
    public static String formatearRetenciones(List<?> retencionesLocales) {
        StringBuilder sb = new StringBuilder();
        

        if(retencionesLocales == null) {
            return null;
        }

        if(retencionesLocales.isEmpty()) {
            return null;
        }
        
        for (Object item : retencionesLocales) {
            if (item instanceof ImpuestosLocales.RetencionesLocales) {
                ImpuestosLocales.RetencionesLocales ret = (ImpuestosLocales.RetencionesLocales) item;
                sb.append(formatearRetenciones(ret)).append("\n");
            } else if (item instanceof ImpuestosLocales.TrasladosLocales) {
                ImpuestosLocales.TrasladosLocales tras = (ImpuestosLocales.TrasladosLocales) item;
                sb.append(formatearTraslados(tras)).append("\n");
            }
        }

        log.debug("Retenciones locales formateadas: \n" + sb.toString().trim());

        return sb.toString().trim().replaceAll("\\n+$", "");
    }

    private static String formatearTraslados(ImpuestosLocales.TrasladosLocales traslado) {

        if(traslado == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String trasladoFormateado = String.format("Imp. Local: %s, Tasa: %s%%, Importe: %s",
                        traslado.getImpLocTrasladado(),
                        traslado.getTasadeTraslado(),
                        FormatTool.formatearMoneda(traslado.getImporte(), new Locale("es", "MX")));
                sb.append(trasladoFormateado).append("\n");

        return sb.toString().trim().replaceAll("\\n+$", "");
    }

    private static String formatearRetenciones(ImpuestosLocales.RetencionesLocales retencion) {

        if(retencion == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String retencionFormateada = String.format("Imp. Local: %s, Tasa: %s%%, Importe: %s",
                        retencion.getImpLocRetenido(),
                        retencion.getTasadeRetencion(),
                        FormatTool.formatearMoneda(retencion.getImporte(), new Locale("es", "MX")));
                sb.append(retencionFormateada).append("\n");

        return sb.toString().trim().replaceAll("\\n+$", "");
    }

}
