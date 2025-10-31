package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.FinanceDao;
import com.okutonda.okudpdv.data.entities.*;
import com.okutonda.okudpdv.helpers.UserSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller de gestão financeira: - Contas a Pagar / Receber - Fluxo de Caixa
 * - Receitas e Despesas - Relatórios Financeiros
 *
 * Responsável por aplicar lógica de negócio sobre o DAO.
 *
 * @author Hulquene
 */
public class FinanceController {

    private final FinanceDao dao;
    private final UserSession session = UserSession.getInstance();

    public FinanceController() {
        this.dao = new FinanceDao();
    }

    // ==========================================================
    // 🔹 CONTAS A RECEBER
    // ==========================================================
    public List<Order> getContasAReceber() {
        try {
            List<Order> contas = dao.listContasAReceber();
            System.out.println("📊 Contas a receber carregadas: " + contas.size() + " registos");
            return contas;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar contas a receber: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar contas a receber", e);
        }
    }

    public BigDecimal getTotalContasAReceber() {
        try {
            List<Order> contas = getContasAReceber();
            return contas.stream()
                    .map(order -> BigDecimal.valueOf(order.getTotal() - order.getPayTotal()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de contas a receber: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 CONTAS A PAGAR
    // ==========================================================
    public List<Purchase> getContasAPagar() {
        try {
            List<Purchase> contas = dao.listContasAPagar();
            System.out.println("📊 Contas a pagar carregadas: " + contas.size() + " registos");
            return contas;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar contas a pagar: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar contas a pagar", e);
        }
    }

    public BigDecimal getTotalContasAPagar() {
        try {
            List<Purchase> contas = getContasAPagar();
            return contas.stream()
                    .map(purchase -> {
                        BigDecimal saldo = purchase.getTotal().subtract(purchase.getTotal_pago());
                        return saldo != null ? saldo : BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de contas a pagar: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 HISTÓRICO DE VENDAS
    // ==========================================================
    public List<Order> getHistoricoVendas() {
        try {
            List<Order> vendas = dao.listHistoricoVendas();
            System.out.println("📊 Histórico de vendas carregado: " + vendas.size() + " registos");
            return vendas;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar histórico de vendas: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar histórico de vendas", e);
        }
    }

    public List<Order> getHistoricoVendasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        try {
            if (dataInicio == null || dataFim == null) {
                throw new IllegalArgumentException("Datas de período são obrigatórias");
            }

            if (dataInicio.isAfter(dataFim)) {
                throw new IllegalArgumentException("Data inicial não pode ser após data final");
            }

            List<Order> todasVendas = getHistoricoVendas();
            return todasVendas.stream()
                    .filter(venda -> {
                        if (venda.getDatecreate() == null) {
                            return false;
                        }
                        try {
                            LocalDate dataVenda = LocalDate.parse(venda.getDatecreate().substring(0, 10));
                            return !dataVenda.isBefore(dataInicio) && !dataVenda.isAfter(dataFim);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .toList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao filtrar histórico de vendas por período: " + e.getMessage());
            throw new RuntimeException("Erro ao filtrar histórico de vendas", e);
        }
    }

    public BigDecimal getTotalVendasPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        try {
            List<Order> vendasPeriodo = getHistoricoVendasPorPeriodo(dataInicio, dataFim);
            return vendasPeriodo.stream()
                    .map(venda -> BigDecimal.valueOf(venda.getTotal()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de vendas do período: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 FLUXO DE CAIXA
    // ==========================================================
    public List<Payment> getFluxoCaixa(String from, String to) {
        try {
            validarPeriodo(from, to);
            List<Payment> fluxo = dao.listFluxoCaixa(from, to);
            System.out.println("📊 Fluxo de caixa carregado: " + fluxo.size() + " dias");
            return fluxo;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar fluxo de caixa: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar fluxo de caixa", e);
        }
    }

    public List<Payment> getFluxoCaixa(LocalDate dataInicio, LocalDate dataFim) {
        return getFluxoCaixa(dataInicio.toString(), dataFim.toString());
    }

    public BigDecimal getTotalFluxoCaixa(String from, String to) {
        try {
            List<Payment> fluxo = getFluxoCaixa(from, to);
            return fluxo.stream()
                    .map(Payment::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total do fluxo de caixa: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 RECEITAS
    // ==========================================================
    public List<Payment> getReceitas(String from, String to) {
        try {
            validarPeriodo(from, to);
            List<Payment> receitas = dao.listReceitas(from, to);
            System.out.println("📊 Receitas carregadas: " + receitas.size() + " registos");
            return receitas;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar receitas: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar receitas", e);
        }
    }

    public List<Payment> getReceitas(LocalDate dataInicio, LocalDate dataFim) {
        return getReceitas(dataInicio.toString(), dataFim.toString());
    }

    public double getTotalReceitas(String from, String to) {
        try {
            validarPeriodo(from, to);
            double total = dao.getTotalReceitas(from, to);
            System.out.println("💰 Total de receitas: " + total + " AOA");
            return total;
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de receitas: " + e.getMessage());
            return 0.0;
        }
    }

    public BigDecimal getTotalReceitasBigDecimal(String from, String to) {
        return BigDecimal.valueOf(getTotalReceitas(from, to));
    }

    public BigDecimal getTotalReceitas(LocalDate dataInicio, LocalDate dataFim) {
        return getTotalReceitasBigDecimal(dataInicio.toString(), dataFim.toString());
    }

    // ==========================================================
    // 🔹 DESPESAS
    // ==========================================================
    public List<Expense> getDespesas(String from, String to) {
        try {
            validarPeriodo(from, to);
            List<Expense> despesas = dao.listDespesas(from, to);
            System.out.println("📊 Despesas carregadas: " + despesas.size() + " registos");
            return despesas;
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar despesas: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar despesas", e);
        }
    }

    public List<Expense> getDespesas(LocalDate dataInicio, LocalDate dataFim) {
        return getDespesas(dataInicio.toString(), dataFim.toString());
    }

    public BigDecimal getTotalDespesas(String from, String to) {
        try {
            List<Expense> despesas = getDespesas(from, to);
            return despesas.stream()
                    .map(Expense::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular total de despesas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalDespesas(LocalDate dataInicio, LocalDate dataFim) {
        return getTotalDespesas(dataInicio.toString(), dataFim.toString());
    }

    // ==========================================================
    // 🔹 RELATÓRIOS CONSOLIDADOS
    // ==========================================================
    /**
     * Relatório financeiro consolidado por período
     */
    public RelatorioFinanceiro getRelatorioConsolidado(LocalDate dataInicio, LocalDate dataFim) {
        try {
            if (dataInicio == null || dataFim == null) {
                throw new IllegalArgumentException("Datas de período são obrigatórias");
            }

            BigDecimal totalReceitas = getTotalReceitas(dataInicio, dataFim);
            BigDecimal totalDespesas = getTotalDespesas(dataInicio, dataFim);
            BigDecimal saldo = totalReceitas.subtract(totalDespesas);
            BigDecimal totalVendas = getTotalVendasPeriodo(dataInicio, dataFim);

            RelatorioFinanceiro relatorio = new RelatorioFinanceiro();
            relatorio.setPeriodo(dataInicio + " a " + dataFim);
            relatorio.setTotalReceitas(totalReceitas);
            relatorio.setTotalDespesas(totalDespesas);
            relatorio.setSaldo(saldo);
            relatorio.setTotalVendas(totalVendas);
            relatorio.setContasReceber(getTotalContasAReceber());
            relatorio.setContasPagar(getTotalContasAPagar());

            System.out.println("📈 Relatório consolidado gerado: " + relatorio.getSaldo() + " AOA");

            return relatorio;

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório consolidado: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar relatório financeiro", e);
        }
    }

    /**
     * Calcula o lucro líquido do período (Receitas - Despesas)
     */
    public BigDecimal getLucroLiquido(LocalDate dataInicio, LocalDate dataFim) {
        try {
            BigDecimal receitas = getTotalReceitas(dataInicio, dataFim);
            BigDecimal despesas = getTotalDespesas(dataInicio, dataFim);
            return receitas.subtract(despesas);
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular lucro líquido: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcula a margem de lucro em percentagem
     */
    public BigDecimal getMargemLucro(LocalDate dataInicio, LocalDate dataFim) {
        try {
            BigDecimal receitas = getTotalReceitas(dataInicio, dataFim);
            BigDecimal lucro = getLucroLiquido(dataInicio, dataFim);

            if (receitas.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            return lucro.divide(receitas, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } catch (Exception e) {
            System.err.println("❌ Erro ao calcular margem de lucro: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES E UTILITÁRIOS
    // ==========================================================
    private void validarPeriodo(String from, String to) {
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("Data inicial é obrigatória");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Data final é obrigatória");
        }
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }
    }

    /**
     * Classe interna para relatório financeiro
     */
    public static class RelatorioFinanceiro {

        private String periodo;
        private BigDecimal totalReceitas;
        private BigDecimal totalDespesas;
        private BigDecimal saldo;
        private BigDecimal totalVendas;
        private BigDecimal contasReceber;
        private BigDecimal contasPagar;

        // Getters e Setters
        public String getPeriodo() {
            return periodo;
        }

        public void setPeriodo(String periodo) {
            this.periodo = periodo;
        }

        public BigDecimal getTotalReceitas() {
            return totalReceitas;
        }

        public void setTotalReceitas(BigDecimal totalReceitas) {
            this.totalReceitas = totalReceitas;
        }

        public BigDecimal getTotalDespesas() {
            return totalDespesas;
        }

        public void setTotalDespesas(BigDecimal totalDespesas) {
            this.totalDespesas = totalDespesas;
        }

        public BigDecimal getSaldo() {
            return saldo;
        }

        public void setSaldo(BigDecimal saldo) {
            this.saldo = saldo;
        }

        public BigDecimal getTotalVendas() {
            return totalVendas;
        }

        public void setTotalVendas(BigDecimal totalVendas) {
            this.totalVendas = totalVendas;
        }

        public BigDecimal getContasReceber() {
            return contasReceber;
        }

        public void setContasReceber(BigDecimal contasReceber) {
            this.contasReceber = contasReceber;
        }

        public BigDecimal getContasPagar() {
            return contasPagar;
        }

        public void setContasPagar(BigDecimal contasPagar) {
            this.contasPagar = contasPagar;
        }

        @Override
        public String toString() {
            return String.format(
                    "RelatórioFinanceiro{periodo='%s', receitas=%s, despesas=%s, saldo=%s, vendas=%s}",
                    periodo, totalReceitas, totalDespesas, saldo, totalVendas
            );
        }
    }
}
