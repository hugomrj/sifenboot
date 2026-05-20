package org.sifenboot.core.shared;


public enum TipoPago {

    EFECTIVO(1, "Efectivo"),
    CHEQUE(2, "Cheque"),
    TARJETA_CREDITO(3, "Tarjeta de crédito"),
    TARJETA_DEBITO(4, "Tarjeta de débito"),
    TRANSFERENCIA(5, "Transferencia"),
    GIRO(6, "Giro"),
    BILLETERA_ELECTRONICA(7, "Billetera electrónica"),
    TARJETA_EMPRESARIAL(8, "Tarjeta empresarial"),
    VALE(9, "Vale"),
    RETENCION(10, "Retención"),
    PAGO_ANTICIPO(11, "Pago por anticipo"),
    VALOR_FISCAL(12, "Valor fiscal"),
    VALOR_COMERCIAL(13, "Valor comercial"),
    COMPENSACION(14, "Compensación"),
    PERMUTA(15, "Permuta"),
    PAGO_BANCARIO(16, "Pago bancario (Informar solo si E011=5)"),
    PAGO_MOVIL(17, "Pago Móvil"),
    DONACION(18, "Donación"),
    PROMOCION(19, "Promoción"),
    CONSUMO_INTERNO(20, "Consumo Interno"),
    PAGO_ELECTRONICO(21, "Pago Electrónico"),
    OTRO(99, "Otro");

    private final int codigo;
    private final String descripcion;

    TipoPago(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String getDescripcionPorCodigo(int codigo) {
        for (TipoPago tipo : TipoPago.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo.getDescripcion();
            }
        }
        return EFECTIVO.getDescripcion(); // Por defecto (1)
    }
}