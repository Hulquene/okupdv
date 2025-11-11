package com.okutonda.okudpdv.data.entities;

public enum StockStatus {
    PENDENTE("Pendente", "Stock pendente de entrada", "warning"),
    DISPONIVEL("Disponível", "Stock disponível para venda", "success"),
    PROCESSADO("Processado", "Stock totalmente processado", "info"),
    PARCIAL("Parcial", "Stock parcialmente processado", "warning"),
    CANCELADO("Cancelado", "Entrada de stock cancelada", "error"),
    BLOQUEADO("Bloqueado", "Stock bloqueado por inventário", "error");

    private final String descricao;
    private final String observacao;
    private final String cor; // Para interface gráfica

    StockStatus(String descricao, String observacao, String cor) {
        this.descricao = descricao;
        this.observacao = observacao;
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public String getCor() {
        return cor;
    }

    // Métodos utilitários
    public boolean isAtivo() {
        return this == DISPONIVEL || this == PROCESSADO || this == PARCIAL;
    }

    public boolean isPendente() {
        return this == PENDENTE || this == PARCIAL;
    }

    public boolean isBloqueado() {
        return this == CANCELADO || this == BLOQUEADO;
    }

    public static StockStatus fromDescricao(String descricao) {
        for (StockStatus status : values()) {
            if (status.getDescricao().equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        return PENDENTE;
    }
}
