package org.sifenboot.core.shared;

public enum TipoTransaccion {

    VENTA_MERCADERIA(1, "Venta de mercadería"),
    PRESTACION_SERVICIOS(2, "Prestación de servicios"),
    MIXTO(3, "Mixto (Venta de mercadería y servicios)"),
    VENTA_ACTIVO_FIJO(4, "Venta de activo fijo"),
    VENTA_DIVISAS(5, "Venta de divisas"),
    COMPRA_DIVISAS(6, "Compra de divisas"),
    PROMOCION_MUESTRAS(7, "Promoción o entrega de muestras"),
    DONACION(8, "Donación"),
    ANTICIPO(9, "Anticipo"),
    COMPRA_PRODUCTOS(10, "Compra de productos"),
    COMPRA_SERVICIOS(11, "Compra de servicios"),
    VENTA_CREDITO_FISCAL(12, "Venta de crédito fiscal"),
    MUESTRAS_MEDICAS(13, "Muestras médicas (Art. 3 RG 24/2014)");

    private final int codigo;
    private final String descripcion;

    TipoTransaccion(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Recibe el código y devuelve la descripción.
     * Si el código no existe, retorna la de Venta de mercadería por defecto.
     */
    public static String getDescripcionPorCodigo(int codigo) {
        for (TipoTransaccion tipo : TipoTransaccion.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo.getDescripcion();
            }
        }
        return VENTA_MERCADERIA.getDescripcion();
    }
}