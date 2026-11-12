package com.ferbo.cfdi.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.cfdi.catalogs.CatalogoImpuestos;

import mx.gob.sat.cfdi.pagos20.Pagos.Pago;


public class DoctoRelacionadoImpuestoTool {
    private static final Logger log = LogManager.getLogger(DoctoRelacionadoImpuestoTool.class);

    /**
     * Genera una representación textual legible de los impuestos trasladados aplicados
     * a un documento relacionado dentro de un CFDI 4.0.
     * 
     * Este método consolida los detalles fiscales (Impuesto, Tipo Factor, Tasa/Cuota e Importe)
     * para facilitar auditorías visuales, impresión en representaciones impresas o auditoría en
     * logs.
     * 
     * @param impuestos Nodo de impuestos del documento relacionado a evaluar.
     * @return Texto con el desglose de los traslados separados por renglones,
     *         o {@code null} si el documento no grava impuestos trasladados.
     */
    
    public static String formatearTraslados(Pago.DoctoRelacionado.ImpuestosDR impuestos) {
        if (impuestos == null) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        if (impuestos.getTrasladosDR() == null || impuestos.getTrasladosDR().getTrasladoDR() == null || impuestos.getTrasladosDR().getTrasladoDR().isEmpty()) {
            return null;
        }

        impuestos.getTrasladosDR().getTrasladoDR().forEach(traslado -> {
            String impuestoFormateado = String.format("%s, Base: %s, Tipo: %s, Valor: %s, Importe: %s",
                    CatalogoImpuestos.obtenerLeyenda(traslado.getImpuestoDR()),
                    FormatTool.formatearMoneda(traslado.getBaseDR(), new java.util.Locale("es", "MX")),
                    traslado.getTipoFactorDR(),
                    traslado.getTasaOCuotaDR(),
                    FormatTool.formatearMoneda(traslado.getImporteDR(), new java.util.Locale("es", "MX")));
            log.debug(impuestoFormateado);
            resultado.append(impuestoFormateado).append("\n");
        });

        return resultado.toString().trim().replaceAll("\\n+$", "");
    }

    /**
     * Genera una representación textual legible de los impuestos retenidos aplicados
     * a un documento relacionado dentro de un CFDI 4.0.
     * 
     * Este método consolida los detalles fiscales de las retenciones (Impuesto, Tipo Factor,
     * Tasa/Cuota e Importe) para facilitar auditorías visuales, impresión en representaciones impresas o auditoría en logs.
     * 
     * @param impuestos Nodo de impuestos del documento relacionado a evaluar.
     * @return Texto con el desglose de las retenciones separadas por renglones,
     *         o {@code null} si el documento no grava impuestos retenidos.
     */
    public static String formatearRetenciones(Pago.DoctoRelacionado.ImpuestosDR impuestos) {
        if (impuestos == null) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        if (impuestos.getRetencionesDR() == null || impuestos.getRetencionesDR().getRetencionDR() == null || impuestos.getRetencionesDR().getRetencionDR().isEmpty()) {
            return null;
        }

        impuestos.getRetencionesDR().getRetencionDR().forEach(retencion -> {
            String impuestoFormateado = String.format("%s, Importe: %s",
                    CatalogoImpuestos.obtenerLeyenda(retencion.getImpuestoDR()),
                    FormatTool.formatearMoneda(retencion.getImporteDR(), new java.util.Locale("es", "MX")));
            log.debug(impuestoFormateado);
            resultado.append(impuestoFormateado).append("\n");
        });

        return resultado.toString().trim().replaceAll("\\n+$", "");
    }
    
}
