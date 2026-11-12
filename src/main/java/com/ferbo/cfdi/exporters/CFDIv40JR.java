package com.ferbo.cfdi.exporters;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.cfdi.tools.currency.CurrencyTool;
import com.ferbo.cfdi.v40.model.CFDIPrintWrapper;
import com.ferbo.cfdi.v40.service.CFDIReaderService;

import mx.gob.sat.cfdi.v40.Comprobante;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Componente unificado para la conversión de XML CFDI 4.0 a flujos binarios PDF.
 */
public class CFDIv40JR {

    private String observacion = null;
    private String logo = null;

    private static final Logger log = LogManager.getLogger(CFDIv40JR.class.getName());
    private final CFDIReaderService readerService;

    public CFDIv40JR() {
        this.readerService = new CFDIReaderService();
    }

    /**
     * Procesa un flujo XML en memoria, ejecuta su lógica interna según el tipo
     * de comprobante y genera el PDF resultante utilizando los formatos embebidos.
     *
     * @param xmlStream Flujo de datos en memoria del XML del CFDI.
     * @return Arreglo de bytes (byte[]) con el PDF generado listo para su distribución.
     * @throws Exception Si ocurre un fallo en el parseo, reglas de negocio o compilación visual.
     */
    public byte[] generarFormatoImpreso(InputStream xmlStream) throws Exception {
        log.info("Iniciando conversión de CFDI a formato binario PDF...");

        // 1. Ejecutar motor de lectura y procesamiento de negocio (Factory / Strategy)
        // Este paso realiza el unmarshal de JAXB y delega a su respectivo Processor.
        CFDIPrintWrapper wrapper = readerService.ejecutarCargaJR(xmlStream);
        
        Comprobante comprobanteRaiz = wrapper.getComprobante();
        Map<String, Object> parametrosJasper = wrapper.getJasperParameters();
        parametrosJasper.put("comprobante", comprobanteRaiz);

        //1.1 Verificar la conversión del total a texto
        log.debug("Total moneda {}: {}", comprobanteRaiz.getMoneda().value(), CurrencyTool.convertirAMoneda(comprobanteRaiz.getTotal(), comprobanteRaiz.getMoneda().value()));
        
        //1.2 Inyectar observación del CFDI si está presente. Este atributo no es parte del CFDI v.4.0, pero se puede agregar si se proporciona
        //desde el proceso de Facturación.
        if(this.observacion != null) {
            parametrosJasper.put("observacion", this.observacion);
        }

        if(this.logo != null) {
            parametrosJasper.put("logo", this.logo);
        }

        // 2. Resolver y extraer la ruta del reporte correspondiente (Inyectado por el Processor)
        String subreportDir = (String) parametrosJasper.get("SUBREPORT_DIR");
        if (subreportDir == null) {
            throw new IllegalStateException("El procesador asignado no definió un directorio de reportes válido.");
        }
        
        String rutaReportePrincipal = "reports/factura/v40/cfdi-v40.jasper";
        log.info("Buscando formato embebido en la ruta: " + rutaReportePrincipal);

        // 3. Obtener el archivo compilado .jasper desde los recursos internos del JAR
        InputStream jasperStream = getClass().getClassLoader().getResourceAsStream(rutaReportePrincipal);
        if (jasperStream == null) {
            throw new FileNotFoundException("No se encontró el formato predefinido en el Classpath: " + rutaReportePrincipal);
        }

        // 4. Preparar la fuente de datos principal (El Comprobante Raíz)
        List<Comprobante> listaRaiz = Collections.singletonList(comprobanteRaiz);
        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(listaRaiz);

        try {
            // 5. Inyectar los datos en el lienzo de JasperReports
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametrosJasper, mainDataSource);

            // 6. Exportar el lienzo procesado a un arreglo de bytes crudo (PDF)
            log.info("Exportando reporte a mapa de bytes...");
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Fallo crítico durante el llenado o exportación del PDF en JasperReports", e);
            throw e;
        }
    }

    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
