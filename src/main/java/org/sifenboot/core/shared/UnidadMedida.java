package org.sifenboot.core.shared;

public enum UnidadMedida {
    M(87, "m", "Metros"),
    CPM(2366, "CPM", "Costo por Mil"),
    UI(2329, "UI", "Unidad Internacional"),
    M3(110, "M3", "Metros cubicos"),
    UNI(77, "UNI", "Unidad"),
    G(86, "g", "Gramos"),
    LT(89, "LT", "Litros"),
    MG(90, "MG", "Miligramo"),
    CM(91, "CM", "Centimetros"),
    CM2(92, "CM2", "Centimetros cuadrados"),
    CM3(93, "CM3", "Centimetros cubicos"),
    PUL(94, "PUL", "Pulgadas"),
    MM2(96, "MM2", "Milimetros cuadrados"),
    KG_M2(79, "kg/m2", "Kiligramos s/ metro cuadrado"),
    AA(97, "AA", "Año"),
    ME(98, "ME", "Mes"),
    TN(99, "TN", "Tonelada"),
    HS(100, "Hs", "Hora"),
    MI(101, "Mi", "Minuto"),
    DTE(103, "DTE", "Determinación"),
    YA(104, "Ya", "YardasMetros"),
    MT(108, "MT", "Metros"),
    M2(109, "M2", "Metros cuadrados"),
    MM(95, "MM", "Milimetros"),
    SE(666, "Se", "Segundo"),
    DI(102, "Di", "Día"),
    KG(83, "Kg", "Kilogramos"),
    ML(88, "ML", "Mililitros"),
    A4(111, "4A", "Bovinas"),
    CI(112, "Ci", "Curie"),
    DOC(113, "DOC", "Docena"),
    GLL(114, "GLL", "Galones (US)(3,7843 LT)"),
    GRO(115, "GRO", "Gruesas"),
    E4(116, "E4", "Kilogramo Bruto"),
    KT(117, "KT", "Kits"),
    M5(118, "M5", "Microcurie"),
    MCU(119, "MCU", "Milicurie"),
    MIL(120, "MIL", "Millar"),
    PAR(121, "PAR", "Par"),
    FOTP(122, "FOTP", "Pies"),
    FTKP(123, "FTKP", "Pies Cuadrados"),
    PCE(124, "PCE", "Piezas"),
    KLT(125, "KLT", "Quilate"),
    RM(126, "RM", "Resmas"),
    ROR(127, "ROR", "Rollos"),
    KWH(128, "kWh", "1000 Kiloewatt Hora"),
    U_JGO(129, "U(JGO)", "Mazos"),
    DR(130, "DR", "Tambores"),
    BXC(131, "BXC", "Caja"),
    SET(132, "SET", "Juego"),
    PK(133, "PK", "Paquete"),
    BGB(134, "BGB", "Bolsa"),
    DPC(135, "DPC", "Docena Par"),
    JRP(136, "JRP", "Pote"),
    BL(137, "BL", "Fardos"),
    ABB(138, "ABB", "Bulto"),
    BK(139, "BK", "Cesta"),
    BWP(140, "BWP", "Peso Base");

    private final int codigo;
    private final String representante;
    private final String descripcion;

    UnidadMedida(int codigo, String representante, String descripcion) {
        this.codigo = codigo;
        this.representante = representante;
        this.descripcion = descripcion;
    }

    public int getCodigo() { return codigo; }
    //public String getRepresentante() { return representante; }

    public static UnidadMedida obtenerCodigo(String rep) {
        if (rep == null) {
            throw new IllegalArgumentException("El representante de unidad de medida no puede ser nulo");
        }
        for (UnidadMedida um : values()) {
            if (um.representante.equalsIgnoreCase(rep.trim())) {
                return um;
            }
        }
        throw new IllegalArgumentException("Unidad de medida SIFEN no reconocida: " + rep);
    }
}