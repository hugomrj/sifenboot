package org.sifenboot.core.shared;


public enum MonedaISO {

    PYG("Guarani"),
    USD("Dolar americano"),
    EUR("Euro"),
    ARS("Peso argentino"),
    BRL("Real brasileño"),
    CLP("Peso chileno"),
    BOB("Boliviano"),
    UYU("Peso uruguayo"),
    PEN("Sol peruano"),
    COP("Peso colombiano"),
    MXN("Peso mexicano"),
    GBP("Libra esterlina"),
    CHF("Franco suizo"),
    JPY("Yen japonés"),
    CNY("Yuan chino"),
    CAD("Dolar canadiense"),
    AUD("Dolar australiano");

    private final String descripcion;

    MonedaISO(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Valida si el código existe en el enum. Si no existe o es inválido,
     * devuelve falso para poder aplicar el comportamiento por defecto.
     */
    public static boolean isValid(String code) {
        if (code == null) return false;
        try {
            MonedaISO.valueOf(code.toUpperCase().trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene la descripción basada en el código. Si no se encuentra,
     * por defecto asume la descripción del Guaraní.
     */
    public static String getDescripcionOrDefault(String code) {
        if (code == null) return PYG.getDescripcion();
        try {
            return MonedaISO.valueOf(code.toUpperCase().trim()).getDescripcion();
        } catch (IllegalArgumentException e) {
            return PYG.getDescripcion();
        }
    }
}