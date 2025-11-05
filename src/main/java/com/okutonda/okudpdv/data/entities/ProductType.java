package com.okutonda.okudpdv.data.entities;

public enum ProductType {
    PRODUCT("product", "Produto"),
    SERVICE("service", "Serviço"),
    KIT("kit", "Kit"),
    RAW_MATERIAL("raw_material", "Matéria Prima"),
    FINISHED_GOOD("finished_good", "Produto Acabado"),
    COMPOSITE("composite", "Composto");

    private final String code;
    private final String description;

    ProductType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductType fromCode(String code) {
        for (ProductType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return PRODUCT; // Valor padrão
    }

    public static ProductType fromDescription(String description) {
        for (ProductType type : values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        return PRODUCT; // Valor padrão
    }
}
