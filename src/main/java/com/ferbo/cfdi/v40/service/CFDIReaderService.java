package com.ferbo.cfdi.v40.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.ferbo.cfdi.v40.model.CFDIPrintWrapper;
import com.ferbo.cfdi.v40.model.TipoCFDI;
import com.ferbo.cfdi.v40.strategy.CFDIProcessor;
import com.ferbo.cfdi.v40.strategy.impl.FacturaProcessor;
import com.ferbo.cfdi.v40.strategy.impl.NominaProcessor;
import com.ferbo.cfdi.v40.strategy.impl.PagoProcessor;

import mx.gob.sat.cfdi.implocal.ImpuestosLocales;
import mx.gob.sat.cfdi.nomina12.Nomina;
import mx.gob.sat.cfdi.pagos20.Pagos;
import mx.gob.sat.cfdi.tfd11.TimbreFiscalDigital;
import mx.gob.sat.cfdi.v40.Comprobante;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class CFDIReaderService {

    private static final Map<TipoCFDI, CFDIProcessor> procesadores = new HashMap<>();

    static {
        procesadores.put(TipoCFDI.FACTURA, new FacturaProcessor());
        procesadores.put(TipoCFDI.COMPLEMENTO_PAGO, new PagoProcessor());
        procesadores.put(TipoCFDI.NOMINA, new NominaProcessor());
    }

    /**
     * Lee el XML desde un Stream en memoria, ejecuta su lógica de negocio 
     * específica y retorna el Comprobante listo para JasperReports.
     */
    public Comprobante ejecutarCarga(InputStream xmlStream) throws Exception {
        if (xmlStream == null)
        	throw new IllegalArgumentException("El InputStream del XML no puede ser nulo.");

        // 1. JAXB procesa el InputStream directamente en memoria
        //Si se agregan más complementos del SAT, esta línea deberá agregar cada una de las clases que representan a cada complemento de pago.
        JAXBContext context = JAXBContext.newInstance(Comprobante.class, Pagos.class, Nomina.class, TimbreFiscalDigital.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Comprobante comprobante = (Comprobante) unmarshaller.unmarshal(xmlStream);

        // 2. Detectar el tipo de CFDI
        TipoCFDI tipoDetectado = TipoCFDI.detectar(comprobante);

        // 3. Buscar y ejecutar su estrategia de negocio
        CFDIProcessor procesador = procesadores.get(tipoDetectado);
        if (procesador == null) {
            throw new UnsupportedOperationException("No se ha registrado un procesador para: " + tipoDetectado);
        }

        // Se ejecuta la lógica (p.ej. guardar en BD, validar estatus, etc.)
        procesador.procesar(comprobante);

        // 4. Retornamos el objeto limpio y estructurado
        return comprobante;
    }
    
    public CFDIPrintWrapper ejecutarCargaJR(InputStream xmlStream) throws Exception {
        // 1. Parseo inicial en memoria
        JAXBContext context = JAXBContext.newInstance(Comprobante.class, Pagos.class, Nomina.class, TimbreFiscalDigital.class, ImpuestosLocales.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Comprobante comprobante = (Comprobante) unmarshaller.unmarshal(xmlStream);

        // 2. Inicializar el mapa de parámetros de Jasper
        Map<String, Object> parametrosJasper = new HashMap<>();

        // 3. Extraer e inyectar lo común de CUALQUIER CFDI 4.0

        // 3.1. Conceptos
        if (comprobante.getConceptos() != null) {
            List<Concepto> listaConceptos = comprobante.getConceptos().getConcepto();
            parametrosJasper.put("DS_CONCEPTOS", new JRBeanCollectionDataSource(listaConceptos));
        }

        // 3.2. Timbre Fiscal Digital (si existe)
        TimbreFiscalDigital tfd = null;
        if (comprobante.getComplemento() != null) {
            for (Object comp : comprobante.getComplemento().getAny()) {
                if (comp instanceof TimbreFiscalDigital) {
                    tfd = (TimbreFiscalDigital) comp;
                    parametrosJasper.put("DS_TFD", new JRBeanCollectionDataSource(Arrays.asList(tfd)));
                    parametrosJasper.put("DS_TFD_FOOTER", new JRBeanCollectionDataSource(Arrays.asList(tfd)));
                    parametrosJasper.put("uuid", tfd.getUUID());
                    break; // Solo necesitamos el primer TimbreFiscalDigital
                }
            }
        }

        // 3.3. Impuestos Locales (si existe)
        if(comprobante.getComplemento() != null) {
            for (Object comp : comprobante.getComplemento().getAny()) {
                if (comp instanceof ImpuestosLocales) {
                    ImpuestosLocales impuestosLocales = (ImpuestosLocales) comp;
                    parametrosJasper.put("DS_IMPUESTOS_LOCALES", new JRBeanCollectionDataSource(Arrays.asList(impuestosLocales)));
                    break;
                }
            }
        }

        // 3.3. Emisor, Receptor e Impuestos
        if(comprobante.getEmisor() != null) {
            parametrosJasper.put("DS_EMISOR", new JRBeanCollectionDataSource(Arrays.asList(comprobante.getEmisor())));
        }

        if(comprobante.getReceptor() != null) {
            parametrosJasper.put("DS_RECEPTOR", new JRBeanCollectionDataSource(Arrays.asList(comprobante.getReceptor())));
        }

        if(comprobante.getImpuestos() != null) {
            parametrosJasper.put("DS_IMPUESTOS", new JRBeanCollectionDataSource(Arrays.asList(comprobante.getImpuestos())));
        }


        parametrosJasper.put("SUBREPORT_DIR", "reports/factura/v40/");
        
        // 4. Determinar tipo y delegar extracción del complemento
        TipoCFDI tipoDetectado = TipoCFDI.detectar(comprobante);
        CFDIProcessor procesador = procesadores.get(tipoDetectado);
        
        if (procesador == null) {
            throw new UnsupportedOperationException("Sin procesador para: " + tipoDetectado);
        }

        // El procesador añade al mapa "DS_PAGOS", "DS_NOMINA", etc.
        procesador.procesar(comprobante, parametrosJasper);

        // 5. Preparar cadena original y URL para validación de CFDI 4.0
        String urlValidacion = "https://verificacfdi.facturaelectronica.sat.gob.mx/?id=" + tfd.getUUID()
            + "&re=" + comprobante.getEmisor().getRfc()
            + "&rr=" + comprobante.getReceptor().getRfc()
            + "&tt=" + comprobante.getTotal()
            + "&fe=" + comprobante.getSello().substring(comprobante.getSello().length() - 8);
        parametrosJasper.put("URL_VALIDACION", urlValidacion);
        
        String cadenaOriginal = "||" + tfd.getVersion() + "|" + tfd.getUUID() + "|" + tfd.getFechaTimbrado() + "|" + tfd.getRfcProvCertif() + comprobante.getSello() + "|" + tfd.getNoCertificadoSAT() + "||";
        parametrosJasper.put("CADENA_ORIGINAL", cadenaOriginal);


        // 6. Retornar el paquete completo listo para imprimir
        return new CFDIPrintWrapper(comprobante, parametrosJasper);
    }
}
