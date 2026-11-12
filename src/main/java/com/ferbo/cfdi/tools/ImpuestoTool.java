package com.ferbo.cfdi.tools;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.cfdi.catalogs.CatalogoImpuestos;

import mx.gob.sat.cfdi.v40.Comprobante;

public class ImpuestoTool {
    private static final Logger log = LogManager.getLogger(ImpuestoTool.class);

    public static String formatearTraslados(Comprobante.Impuestos.Traslados traslados) {
        if (traslados == null || traslados.getTraslado() == null || traslados.getTraslado().isEmpty()) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        traslados.getTraslado().forEach(traslado -> {
            String impuestoFormateado = String.format("%s, Base: %s, Tipo: %s, Valor: %s, Importe: %s",
                    CatalogoImpuestos.obtenerLeyenda(traslado.getImpuesto()),
                    FormatTool.formatearMoneda(traslado.getBase(), new Locale("es", "MX")),
                    traslado.getTipoFactor(),
                    traslado.getTasaOCuota(),
                    FormatTool.formatearMoneda(traslado.getImporte(), new Locale("es", "MX")));
            log.debug(impuestoFormateado);
            resultado.append(impuestoFormateado).append("\n");
        });

        return resultado.toString().trim().replaceAll("\\n+$", "");
    }

    public static String formatearRetenciones(Comprobante.Impuestos.Retenciones retenciones) {
        if (retenciones == null || retenciones.getRetencion() == null || retenciones.getRetencion().isEmpty()) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        retenciones.getRetencion().forEach(retencion -> {
            String impuestoFormateado = String.format("%s, Importe: %s",
                    CatalogoImpuestos.obtenerLeyenda(retencion.getImpuesto()),
                    FormatTool.formatearMoneda(retencion.getImporte(), new Locale("es", "MX")));
            log.debug(impuestoFormateado);
            resultado.append(impuestoFormateado).append("\n");
        });

        return resultado.toString().trim().replaceAll("\\n+$", "");
    }
}
