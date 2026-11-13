package com.ferbo.cfdi.tools;

import java.util.List;
import java.util.Objects;

import mx.gob.sat.cfdi.v40.Comprobante;

public class InformacionAduaneraTool {

    public static String formatearInformacionAduanera(List<Comprobante.Conceptos.Concepto.InformacionAduanera> informacionAduaneraList, String prefijo) {
        if (informacionAduaneraList == null || informacionAduaneraList.isEmpty()) {
            return null;
        }

        String prefijoEfectivo = (prefijo != null) ? prefijo : "";
        return informacionAduaneraList.stream()
                .filter(Objects::nonNull)
                .map(ia -> prefijoEfectivo + ia.getNumeroPedimento())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
