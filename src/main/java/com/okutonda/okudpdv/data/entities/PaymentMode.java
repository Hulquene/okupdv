package com.okutonda.okudpdv.data.entities;

/**
 * Enum para modos de pagamento compatível com a tabela do banco de dados
 */
public enum PaymentMode {
    OU("OU", "Outros meios aqui não assinalados"),
    CC("CC", "Cartão de Crédito"),
    CD("CD", "Cartão de Débito"),
    CH("CH", "Cheque Bancário"),
    CI("CI", "Crédito"),
    CO("CO", "Cheque ou Cartão Oferta"),
    TB("TB", "Transferência Bancária"),
    NU("NU", "Numerário"),
    MB("MB", "Multicaixa");

    private final String codigo;
    private final String descricao;

    PaymentMode(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    // ==========================================================
    // 🔹 GETTERS
    // ==========================================================
    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    // ==========================================================
    // 🔹 MÉTODOS DE CONVERSÃO
    // ==========================================================
    /**
     * Converte do código do banco (CC, CD, NU, etc.)
     */
    public static PaymentMode fromCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return OU;
        }

        String codigoUpper = codigo.trim().toUpperCase();
        for (PaymentMode mode : values()) {
            if (mode.codigo.equals(codigoUpper)) {
                return mode;
            }
        }
        return OU;
    }

    /**
     * Converte do nome do enum (case-insensitive)
     */
    public static PaymentMode fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OU;
        }

        String valueUpper = value.trim().toUpperCase();

        // Tenta pelo código (CC, CD, etc.)
        PaymentMode byCode = fromCodigo(valueUpper);
        if (byCode != OU) {
            return byCode;
        }

        // Tenta pelo nome do enum
        try {
            return PaymentMode.valueOf(valueUpper);
        } catch (IllegalArgumentException e) {
            // Tenta pela descrição
            for (PaymentMode mode : values()) {
                if (mode.descricao.equalsIgnoreCase(value)
                        || mode.descricao.toUpperCase().contains(valueUpper)) {
                    return mode;
                }
            }
            return OU;
        }
    }

    /**
     * Verifica se o código existe
     */
    public static boolean isValidCodigo(String codigo) {
        return fromCodigo(codigo) != OU;
    }

    /**
     * Lista todos os modos de pagamento ativos
     */
    public static PaymentMode[] getActiveModes() {
        return values();
    }

    /**
     * Lista apenas os modos de pagamento mais comuns
     */
    public static PaymentMode[] getCommonModes() {
        return new PaymentMode[]{NU, MB, CC, CD, TB};
    }

    @Override
    public String toString() {
        return descricao + " (" + codigo + ")";
    }
}
