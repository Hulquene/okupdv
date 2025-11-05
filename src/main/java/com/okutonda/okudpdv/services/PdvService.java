package com.okutonda.okudpdv.services;

import com.okutonda.okudpdv.controllers.*;
import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.data.entities.*;
import com.okutonda.okudpdv.helpers.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsável por toda a lógica de negócio do PDV Centraliza operações
 * complexas envolvendo múltiplas entidades
 */
public class PdvService {

    private final ProductController productController;
    private final OrderController orderController;
    private final ShiftController shiftController;
    private final ClientController clientController;
    private final PaymentController paymentController;
    private final StockMovementDao stockMovementDao;

    private final UserSession userSession;
    private final ShiftSession shiftSession;

    public PdvService() {
        this.productController = new ProductController();
        this.orderController = new OrderController();
        this.shiftController = new ShiftController();
        this.clientController = new ClientController();
        this.paymentController = new PaymentController();
        this.stockMovementDao = new StockMovementDao();

        this.userSession = UserSession.getInstance();
        this.shiftSession = ShiftSession.getInstance();
    }

    // ==========================================================
    // 🔹 VALIDAÇÕES E VERIFICAÇÕES
    // ==========================================================
    /**
     * Verifica se o sistema está pronto para vendas
     */
    public boolean sistemaProntoParaVendas() {
        try {
            // 1. Verificar turno aberto
            if (!shiftController.temTurnoAberto()) {
                throw new PdvException("Nenhum turno aberto. Abra um turno antes de iniciar vendas.");
            }

            // 2. Verificar usuário logado
            if (userSession.getUser() == null) {
                throw new PdvException("Nenhum usuário logado.");
            }

            // 3. Verificar cliente padrão
            Clients clientePadrao = clientController.getDefaultClient();
            if (clientePadrao == null) {
                throw new PdvException("Cliente padrão não configurado.");
            }

            return true;

        } catch (PdvException e) {
            throw e;
        } catch (Exception e) {
            throw new PdvException("Erro ao verificar sistema: " + e.getMessage());
        }
    }

    /**
     * Verifica se pode processar venda
     */
    public void validarVendaAntesProcessamento(Order order) {
        if (order == null) {
            throw new PdvException("Pedido não pode ser nulo");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new PdvException("Carrinho vazio. Adicione produtos antes de finalizar.");
        }

        // Validar cada item do carrinho
        for (ProductOrder item : order.getProducts()) {
            validarItemVenda(item);
        }

        // Validar cliente
        if (order.getClient() == null) {
            throw new PdvException("Cliente não selecionado.");
        }

        // Validar vendedor
        if (order.getSeller() == null) {
            throw new PdvException("Vendedor não identificado.");
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES DE PRODUTO
    // ==========================================================
    /**
     * Busca e valida produto para venda
     */
    public Product buscarEValidarProduto(String codigoBarras, int quantidade) {
        if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
            throw new PdvException("Código de barras não informado.");
        }

        if (quantidade <= 0) {
            throw new PdvException("Quantidade deve ser maior que zero.");
        }

        Product produto = productController.getByBarcode(codigoBarras.trim());
        if (produto == null) {
            throw new PdvException("Produto não encontrado para o código: " + codigoBarras);
        }

        validarProdutoParaVenda(produto, quantidade);
        return produto;
    }

    /**
     * Valida se produto pode ser vendido
     */
    private void validarProdutoParaVenda(Product produto, int quantidade) {
        // Status
        if (produto.getStatus() != ProductStatus.ACTIVE) {
            throw new PdvException("Produto inativo: " + produto.getDescription());
        }

        // Preço
        if (produto.getPrice() == null || produto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PdvException("Preço do produto não definido: " + produto.getDescription());
        }

        // Imposto
        if (produto.getTaxe() == null) {
            throw new PdvException("Imposto não definido no produto: " + produto.getDescription());
        }

        // Razão para isento
        if (produto.getTaxe().getPercetage().compareTo(BigDecimal.ZERO) == 0) {
            if (produto.getReasonTaxe() == null) {
                throw new PdvException("Produto isento sem razão de imposto: " + produto.getDescription());
            }
        }

        // Estoque
        int stockAtual = stockMovementDao.getStockAtual(produto.getId());
        if (stockAtual < quantidade) {
            throw new PdvException("Estoque insuficiente para " + produto.getDescription()
                    + ". Disponível: " + stockAtual);
        }
    }

    /**
     * Valida item individual da venda
     */
    private void validarItemVenda(ProductOrder item) {
        if (item == null || item.getProduct() == null) {
            throw new PdvException("Item de venda inválido.");
        }

        if (item.getQty() <= 0) {
            throw new PdvException("Quantidade inválida para: " + item.getDescription());
        }

        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PdvException("Preço inválido para: " + item.getDescription());
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES DE CARRINHO
    // ==========================================================
    /**
     * Cria item do pedido com todas as informações necessárias
     */
    public ProductOrder criarItemPedido(Product produto, int quantidade) {
        ProductOrder item = new ProductOrder();
        item.setProduct(produto);
        item.setDescription(produto.getDescription());
        item.setCode(produto.getCode());
        item.setPrice(produto.getPrice());
        item.setQty(quantidade);

        // Configurar impostos
        item.setTaxePercentage(produto.getTaxe().getPercetage());
        item.setTaxeCode(produto.getTaxe().getCode());
        item.setTaxeName(produto.getTaxe().getName());

        // Configurar razão para isento
        if (produto.getTaxe().getPercetage().compareTo(BigDecimal.ZERO) == 0) {
            item.setReasonTax(produto.getReasonTaxe().getReason());
            item.setReasonCode(produto.getReasonTaxe().getCode());
        }

        return item;
    }

    /**
     * Adiciona ou atualiza item no carrinho
     */
    public void adicionarOuAtualizarCarrinho(List<ProductOrder> carrinho, ProductOrder novoItem) {
        // Verificar se já existe no carrinho
        for (int i = 0; i < carrinho.size(); i++) {
            ProductOrder itemExistente = carrinho.get(i);
            if (itemExistente.getProduct().getId().equals(novoItem.getProduct().getId())) {
                // Atualizar quantidade
                int novaQuantidade = itemExistente.getQty() + novoItem.getQty();
                itemExistente.setQty(novaQuantidade);

                // Revalidar estoque
                validarProdutoParaVenda(novoItem.getProduct(), novaQuantidade);

                carrinho.set(i, itemExistente);
                return;
            }
        }

        // Adicionar novo item
        carrinho.add(novoItem);
    }

    // ==========================================================
    // 🔹 CÁLCULOS DE TOTAIS
    // ==========================================================
    /**
     * Calcula totais da venda com IVA
     */
    public Totais calcularTotaisComIVA(List<ProductOrder> itens) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal impostos = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        if (itens != null) {
            for (ProductOrder item : itens) {
                if (item == null || item.getProduct() == null) {
                    continue;
                }

                BigDecimal qty = BigDecimal.valueOf(item.getQty());
                BigDecimal precoUnitario = item.getPrice();
                BigDecimal totalItem = precoUnitario.multiply(qty);

                BigDecimal percImposto = item.getTaxePercentage() != null
                        ? item.getTaxePercentage() : BigDecimal.ZERO;

                BigDecimal linhaImposto, linhaSubtotal;

                if (percImposto.compareTo(BigDecimal.ZERO) > 0) {
                    // Cálculo com IVA embutido
                    BigDecimal base = new BigDecimal("100").add(percImposto);
                    linhaImposto = totalItem.multiply(percImposto)
                            .divide(base, 2, RoundingMode.HALF_UP);
                    linhaSubtotal = totalItem.subtract(linhaImposto);
                } else {
                    // Isento
                    linhaImposto = BigDecimal.ZERO;
                    linhaSubtotal = totalItem;
                }

                subtotal = subtotal.add(linhaSubtotal);
                impostos = impostos.add(linhaImposto);
                total = total.add(totalItem);
            }
        }

        // Arredondamento final
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        impostos = impostos.setScale(2, RoundingMode.HALF_UP);
        total = total.setScale(2, RoundingMode.HALF_UP);

        return new Totais(subtotal, impostos, total);
    }

    // ==========================================================
    // 🔹 PROCESSAMENTO DE VENDA
    // ==========================================================
    /**
     * Processa venda completa - método principal do PDV
     */
    public ResultadoVenda processarVendaCompleta(Order pedido, List<Payment> pagamentos) {
        try {
            // 1. Validações iniciais
            sistemaProntoParaVendas();
            validarVendaAntesProcessamento(pedido);

            // 2. Calcular totais
            Totais totais = calcularTotaisComIVA(pedido.getProducts());
            pedido.setSubTotal(totais.getSubtotal());
            pedido.setTotalTaxe(totais.getImpostos());
            pedido.setTotal(totais.getTotal());

            // 3. Garantir vendedor da sessão
            if (pedido.getSeller() == null) {
                pedido.setSeller(shiftSession.getSeller());
            }

            // 4. Garantir cliente padrão se não selecionado
            if (pedido.getClient() == null) {
                pedido.setClient(clientController.getDefaultClient());
            }

            // 5. Processar venda no controller
            Order vendaProcessada = orderController.criarComPagamentos(pedido, pagamentos);

            if (vendaProcessada == null) {
                throw new PdvException("Falha ao processar venda no sistema.");
            }

            // 6. Atualizar turno com o valor da venda
            shiftController.adicionarValorTurno(vendaProcessada.getTotal());

            // 7. Retornar resultado
            return new ResultadoVenda(
                    vendaProcessada,
                    "Venda processada com sucesso! Nº: " + vendaProcessada.getNumber(),
                    true
            );

        } catch (PdvException e) {
            throw e;
        } catch (Exception e) {
            throw new PdvException("Erro ao processar venda: " + e.getMessage());
        }
    }

    /**
     * Cria pedido básico a partir do carrinho
     */
    public Order criarPedidoFromCarrinho(List<ProductOrder> carrinho, Clients cliente) {
        Order pedido = new Order();
        pedido.setProducts(new ArrayList<>(carrinho));
        pedido.setClient(cliente);
        pedido.setSeller(shiftSession.getSeller());
        pedido.setStatus(1); // Pendente
        return pedido;
    }

    // ==========================================================
    // 🔹 CONSULTAS E RELATÓRIOS
    // ==========================================================
    /**
     * Obtém cliente padrão
     */
    public Clients obterClientePadrao() {
        return clientController.getDefaultClient();
    }

    /**
     * Busca cliente por NIF
     */
    public Clients buscarClientePorNif(String nif) {
        return clientController.getByNif(nif);
    }

    /**
     * Lista produtos para PDV
     */
    public List<Product> listarProdutosPDV(String filtro) {
        return productController.listForPDV(filtro);
    }

    /**
     * Obtém informações do turno atual
     */
    public InformacoesTurno obterInformacoesTurno() {
        Shift turnoAtual = shiftController.buscarTurnoAtual();
        if (turnoAtual == null) {
            return new InformacoesTurno(null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        return new InformacoesTurno(
                turnoAtual,
                turnoAtual.getGrantedAmount(),
                turnoAtual.getIncurredAmount(),
                turnoAtual.getCurrentBalance()
        );
    }

    // ==========================================================
    // 🔹 CLASSES INTERNAS
    // ==========================================================
    /**
     * Classe para resultado da venda
     */
    public static class ResultadoVenda {

        private final Order pedido;
        private final String mensagem;
        private final boolean sucesso;

        public ResultadoVenda(Order pedido, String mensagem, boolean sucesso) {
            this.pedido = pedido;
            this.mensagem = mensagem;
            this.sucesso = sucesso;
        }

        // Getters
        public Order getPedido() {
            return pedido;
        }

        public String getMensagem() {
            return mensagem;
        }

        public boolean isSucesso() {
            return sucesso;
        }
    }

    /**
     * Classe para totais
     */
    public static class Totais {

        private final BigDecimal subtotal;
        private final BigDecimal impostos;
        private final BigDecimal total;

        public Totais(BigDecimal subtotal, BigDecimal impostos, BigDecimal total) {
            this.subtotal = subtotal;
            this.impostos = impostos;
            this.total = total;
        }

        // Getters
        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public BigDecimal getImpostos() {
            return impostos;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }

    /**
     * Classe para informações do turno
     */
    public static class InformacoesTurno {

        private final Shift turno;
        private final BigDecimal valorAbertura;
        private final BigDecimal valorVendas;
        private final BigDecimal saldoAtual;

        public InformacoesTurno(Shift turno, BigDecimal valorAbertura, BigDecimal valorVendas, BigDecimal saldoAtual) {
            this.turno = turno;
            this.valorAbertura = valorAbertura;
            this.valorVendas = valorVendas;
            this.saldoAtual = saldoAtual;
        }

        // Getters
        public Shift getTurno() {
            return turno;
        }

        public BigDecimal getValorAbertura() {
            return valorAbertura;
        }

        public BigDecimal getValorVendas() {
            return valorVendas;
        }

        public BigDecimal getSaldoAtual() {
            return saldoAtual;
        }

        public boolean isTurnoAberto() {
            return turno != null;
        }
    }
}

/**
 * Exceção personalizada para o PDV
 */
class PdvException extends RuntimeException {

    public PdvException(String message) {
        super(message);
    }

    public PdvException(String message, Throwable cause) {
        super(message, cause);
    }
}
