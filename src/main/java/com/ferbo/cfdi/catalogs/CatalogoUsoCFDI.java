package com.ferbo.cfdi.catalogs;

/**
 * Clase de utilidad para obtener las descripciones del catálogo c_UsoCFDI del SAT.
 */
public class CatalogoUsoCFDI {

    /**
     * Devuelve el nombre oficial del Uso del CFDI correspondiente a la clave del SAT.
     * 
     * @param clave Clave de tres o cuatro caracteres del uso de CFDI (ej. "G01", "D01", "CP01")
     * @return String con la descripción oficial o un mensaje por defecto si no existe.
     */
    public static String obtenerLeyenda(String clave) {
        if (clave == null) {
            return "No especificado";
        }

        switch (clave.trim().toUpperCase()) {
            // --- GRUPO G: Gastos ---
            case "G01":
                return "G01 - Adquisición de mercancías";
            case "G02":
                return "G02 - Devoluciones, descuentos o bonificaciones";
            case "G03":
                return "G03 - Gastos en general";

            // --- GRUPO I: Inversiones ---
            case "I01":
                return "I01 - Construcciones";
            case "I02":
                return "I02 - Mobiliario y equipo de oficina por inversiones";
            case "I03":
                return "I03 - Equipo de transporte";
            case "I04":
                return "I04 - Equipo de cómputo y accesorios";
            case "I05":
                return "I05 - Dados, troqueles, moldes, matrices y herramental";
            case "I06":
                return "I06 - Comunicaciones telefónicas";
            case "I07":
                return "I07 - Comunicaciones satelitales";
            case "I08":
                return "I08 - Otra maquinaria y equipo";

            // --- GRUPO D: Deducciones Personales (Personas Físicas) ---
            case "D01":
                return "D01 - Honorarios médicos, dentales y gastos hospitalarios";
            case "D02":
                return "D02 - Gastos médicos por incapacidad o discapacidad";
            case "D03":
                return "D03 - Gastos funerales";
            case "D04":
                return "D04 - Donativos";
            case "D05":
                return "D05 - Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación)";
            case "D06":
                return "D06 - Aportaciones voluntarias al SAR";
            case "D07":
                return "D07 - Primas por seguros de gastos médicos";
            case "D08":
                return "D08 - Gastos de transportación escolar obligatoria";
            case "D09":
                return "D09 - Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones";
            case "D10":
                return "D10 - Pagos por servicios educativos (colegiaturas)";

            // --- GRUPO S, CP, CN: Otros usos agregados / modificados en CFDI 4.0 ---
            case "S01":
                return "S01 - Sin efectos fiscales";
            case "CP01":
                return "CP01 - Pagos";
            case "CN01":
                return "CN01 - Nómina";

            default:
                return "Uso de CFDI no válido (" + clave + ")";
        }
    }
}
