package com.ferbo.cfdi.tools.currency.enums;

import java.util.HashMap;
import java.util.Map;

import com.ferbo.cfdi.tools.currency.interfaces.Divisa;

public enum MonedaISO implements Divisa {
    // Divisas nativas de alta transaccionalidad
    MXN("PESO MEXICANO","PESO", "PESOS", 2, true),
    USD("DÓLAR ESTADOUNIDENSE", "DÓLAR", "DÓLARES", 2, false),
    EUR("EURO", "EURO", "EUROS", 2, false),
    GBP("LIBRA ESTERLINA","LIBRA", "LIBRAS", 2, false),
    JPY("YEN JAPONÉS", "YEN", "YENES", 0, false),
    KWD("DINAR KWAITI", "DINAR", "DINARES", 3, false),
    BRL("REAL BRASILEÑO", "REAL", "REALES", 2, false),
    ARS("PESO ARGENTINO", "PESO ARGENTINO", "PESOS ARGENTINOS", 2, false),
    CLP("PESO CHILENO","PESO CHILENO", "PESOS CHILENOS", 0, false),
    COP("PESO COLOMBIANO", "PESO COLOMBIANO", "PESOS COLOMBIANOS", 2, false);

    private final String nombre;
    private final String singular;
    private final String plural;
    private final int decimales;
    private final boolean esNacional;

    private static final Map<String, Divisa> CATALOGO = new HashMap<>();
    private static final Map<String, String[]> GENERICOS_ISO = new HashMap<>();

    static {
        for (MonedaISO m : values()) {
            CATALOGO.put(m.name(), m);
        }

        // Catálogo extendido ISO-4217 compatible con JDK 8
        GENERICOS_ISO.put("CAD", new String[]{"DOLAR CANADIENSE", "DOLAR CANADIENSE", "DOLARES CANADIENSES", "2"});
        GENERICOS_ISO.put("AUD", new String[]{"DOLAR AUSTRALIANO", "DOLAR AUSTRALIANO", "DOLARES AUSTRALIANOS", "2"});
        GENERICOS_ISO.put("CHF", new String[]{"FRANCO SUIZO", "FRANCO SUIZO", "FRANCOS SUIZOS", "2"});
        GENERICOS_ISO.put("CNY", new String[]{"YUAN CHINO", "YUAN", "YUANES", "2"});
        GENERICOS_ISO.put("INR", new String[]{"RUPIA INDIA", "RUPIA", "RUPIAS", "2"});
        GENERICOS_ISO.put("RUB", new String[]{"RUBLO RUSO", "RUBLO", "RUBLOS", "2"});
        GENERICOS_ISO.put("PEN", new String[]{"SOL", "SOL", "SOLES", "2"});
        GENERICOS_ISO.put("UYU", new String[]{"PESO URUGUAYO", "PESO URUGUAYO", "PESOS URUGUAYOS", "2"});
        GENERICOS_ISO.put("CRC", new String[]{"COLÓN COSTARRICENSE", "COLÓN", "COLONES", "2"});
        GENERICOS_ISO.put("DZD", new String[]{"DINAR ARGELINO", "DINAR ARGELINO", "DINARES ARGELINOS", "2"});
        GENERICOS_ISO.put("GTQ", new String[]{"QUETZAL", "QUETZAL", "QUETZALES", "2"});
        GENERICOS_ISO.put("BHD", new String[]{"DINAR BAHREINÍ", "DINAR BAHREINÍ", "DINARES BAHREINÍES", "3"});
        GENERICOS_ISO.put("XXX", new String[]{"XXX", "LOS CODIGOS ASIGNADOS PARA LAS TRANSACCIONES EN QUE INTERVENGA NINGUNA MONEDA" , 
                                              "LOS CODIGOS ASIGNADOS PARA LAS TRANSACCIONES EN QUE INTERVENGA NINGUNA MONEDA", "2"});
    }

    MonedaISO(String nombre, String singular, String plural, int decimales, boolean esNacional) {
        this.nombre = nombre;
        this.singular = singular;
        this.plural = plural;
        this.decimales = decimales;
        this.esNacional = esNacional;
    }

    @Override
    public String getTextoPorCantidad(long cantidad) {
        return (cantidad == 1) ? singular : plural;
    }

    @Override
    public String getSufijo() {
        return esNacional ? "M. N." : "M. E.";
    }

    @Override
    public int getDecimales() {
        return decimales;
    }

    /**
     * Resuelve y devuelve un objeto de tipo Divisa basándose en el estándar ISO-4217.
     */
    public static Divisa desdeCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("El código de moneda no puede ser nulo");
        }
        String iso = codigo.toUpperCase().trim();
        
        // 1. Validar en catálogo principal
        if (CATALOGO.containsKey(iso)) {
            return CATALOGO.get(iso);
        }
        
        // 2. Validar en catálogo extendido
        if (GENERICOS_ISO.containsKey(iso)) {
            String[] meta = GENERICOS_ISO.get(iso);
            return new MonedaDinamica(meta[0], meta[1], meta[2], Integer.parseInt(meta[3]));
        }
        
        // 3. Fallback automático legal para códigos de 3 letras válidos
        if (iso.matches("[A-Z]{3}")) {
            return new MonedaDinamica("UNIDAD DE " + iso, "UNIDAD DE " + iso, "UNIDADES DE " + iso, 2);
        }
        
        throw new IllegalArgumentException("El código provisto no cumple con el formato ISO-4217: " + codigo);
    }

    // Clase contenedora dinámica que implementa el contrato limpio
    private static class MonedaDinamica implements Divisa {
        private final String nombre;
        private final String singular;
        private final String plural;
        private final int decimales;

        MonedaDinamica(String nombre, String singular, String plural, int decimales) {
            this.nombre = nombre;
            this.singular = singular;
            this.plural = plural;
            this.decimales = decimales;
        }

        @Override
        public String getTextoPorCantidad(long cantidad) {
            return (cantidad == 1) ? singular : plural;
        }

        @Override
        public String getSufijo() {
            return "M. E."; // Al ser dinámica y no mapeada en el Enum inicial, se infiere Moneda Extranjera
        }

        @Override
        public int getDecimales() {
            return decimales;
        }

        @Override
        public String getNombre() {
            return nombre;
        }

        @Override
        public String getCodigo() {
            throw new UnsupportedOperationException("Unimplemented method 'getCodigo'");
        }
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public String getCodigo() {
        throw new UnsupportedOperationException("Unimplemented method 'getCodigo'");
    }
}