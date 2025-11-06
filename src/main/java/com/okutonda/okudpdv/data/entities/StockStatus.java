package com.okutonda.okudpdv.data.entities;

public enum StockStatus {
    PENDENTE("Pendente", "Stock pendente de entrada"),
    PROCESSADO("Processado", "Stock totalmente processado"),
    PARCIAL("Parcial", "Stock parcialmente processado"),
    CANCELADO("Cancelado", "Entrada de stock cancelada");

    private final String descricao;
    private final String observacao;

    StockStatus(String descricao, String observacao) {
        this.descricao = descricao;
        this.observacao = observacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacao() {
        return observacao;
    }
}
