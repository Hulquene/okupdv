package com.okutonda.okudpdv.data.entities;

public enum ProductStatus {
    INACTIVE(0, "Inativo"), // ORDINAL 0
    ACTIVE(1, "Ativo"),  // ORDINAL 1  
    DISCONTINUED(2, "Descontinuado"),// ORDINAL 2
    OUT_OF_STOCK(3, "Fora de Stock"),
    PROMOTIONAL(4, "Promocional");

    private final int code;
    private final String description;

    ProductStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductStatus fromCode(int code) {
        for (ProductStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return ACTIVE; // Valor padrão
    }

    public static ProductStatus fromDescription(String description) {
        for (ProductStatus status : values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        return ACTIVE; // Valor padrão
    }

    public boolean isActive() {
        return this == ACTIVE || this == PROMOTIONAL;
    }
}
