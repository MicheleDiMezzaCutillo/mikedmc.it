package it.mikedmc.enums;

public enum AofRarity {
    Comune("cmn"),
    Non_Comune("ncm"),
    Raro("rar"),
    Epico("epi"),
    Leggendario("leg");

    private final String codice;

    AofRarity(String codice) {
        this.codice = codice;
    }

    public String getCodice() {
        return codice;
    }

    // Metodo statico per ottenere l'enum dal codice
    public static AofRarity fromCodice(String codice) {
        for (AofRarity rarity : values()) {
            if (rarity.getCodice().equalsIgnoreCase(codice)) {
                return rarity;
            }
        }
        throw new IllegalArgumentException("Codice di rarità non valido: " + codice);
    }
}