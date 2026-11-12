package com.ferbo.cfdi.tools;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.cfdi.catalogs.CatalogoImpuestos;

import mx.gob.sat.cfdi.v40.Comprobante;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto.Impuestos.Retenciones;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto.Impuestos.Traslados;

public class ConceptoImpuestoTool {

    private static final Logger log = LogManager.getLogger(ConceptoImpuestoTool.class);

    /**
     * Genera una representación textual legible de los impuestos trasladados aplicados 
     * a un concepto comercial dentro de un CFDI 4.0.
     * 
     * Este método consolida los detalles fiscales (Impuesto, Tipo Factor, Tasa/Cuota e Importe) 
     * para facilitar auditorías visuales, impresión en representaciones impresas o auditoría en logs.
     *
     * @param impuestos Nodo de impuestos del concepto a evaluar.
     * @return Texto con el desglose de los traslados separados por renglones, 
     *         o {@code null} si el concepto no grava impuestos trasladados.
     */
    public static String formatearTraslados(Comprobante.Conceptos.Concepto.Impuestos impuestos) {
        if (impuestos == null) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        Traslados traslados = impuestos.getTraslados();

        if(traslados == null || traslados.getTraslado() == null || traslados.getTraslado().isEmpty()) {
            return null;
        }

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
        log.debug("Traslados formateados: {}", resultado.toString());
        return resultado.toString().trim().replaceAll("\\n+$", "");
    }

    /**
     * Genera una representación textual legible de los impuestos retenidos aplicados 
     * a un concepto comercial dentro de un CFDI 4.0.
     * 
     * Este método consolida los detalles fiscales de las retenciones (Impuesto, Tipo Factor, 
     * Tasa/Cuota e Importe) para facilitar auditorías visuales, impresión en representaciones 
     * impresas o auditoría en logs.
     *
     * @param impuestos Nodo de impuestos del concepto a evaluar.
     * @return Texto con el desglose de las retenciones separadas por renglones, 
     *         o {@code null} si el concepto no presenta impuestos retenidos.
     */
    public static String formatearRetenciones(Comprobante.Conceptos.Concepto.Impuestos impuestos) {
        if (impuestos == null) {
            return null;
        }

        StringBuilder resultado = new StringBuilder();

        Retenciones retenciones = impuestos.getRetenciones();

        if (retenciones == null || retenciones.getRetencion() == null || retenciones.getRetencion().isEmpty()) {
            return null;
        }

        retenciones.getRetencion().forEach(retencion -> {
            String impuestoFormateado = String.format("%s, Base: %s, Tipo: %s, Valor: %s, Importe: %s",
                    CatalogoImpuestos.obtenerLeyenda(retencion.getImpuesto()),
                    FormatTool.formatearMoneda(retencion.getBase(), new Locale("es", "MX")),
                    retencion.getTipoFactor(),
                    retencion.getTasaOCuota(),
                    FormatTool.formatearMoneda(retencion.getImporte(), new Locale("es", "MX")));
            log.debug(impuestoFormateado);
            resultado.append(impuestoFormateado).append("\n");
        });
        log.debug("Retenciones formateadas: {}", resultado.toString());
        return resultado.toString().trim().replaceAll("\\n+$", "");
    }
}
