package com.ferbo.cfdi.tools.currency.interfaces;

public interface Divisa {
    String getTextoPorCantidad(long cantidad);
    String getSufijo();
    String getNombre();
    String getCodigo();
    int getDecimales();
}
