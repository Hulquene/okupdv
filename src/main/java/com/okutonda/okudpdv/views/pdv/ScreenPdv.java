/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.okutonda.okudpdv.views.pdv;

import com.okutonda.okudpdv.views.shift.JDialogCloseShift;
import com.okutonda.okudpdv.views.shift.JDialogOpenShift;
import com.okutonda.okudpdv.controllers.OrderController;
import com.okutonda.okudpdv.controllers.ProductController;
import com.okutonda.okudpdv.controllers.ShiftController;
import com.okutonda.okudpdv.controllers.UserController;
import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.data.dao.StockMovementDao;
import com.okutonda.okudpdv.utilities.Utilities;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Order;
import com.okutonda.okudpdv.data.entities.Product;
import com.okutonda.okudpdv.data.entities.ProductOrder;
import com.okutonda.okudpdv.utilities.CompanySession;
import com.okutonda.okudpdv.utilities.ShiftSession;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.Util;
import com.okutonda.okudpdv.views.ScreenMain;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import com.okutonda.okudpdv.views.sales.JDialogListOrder;
import com.okutonda.okudpdv.views.users.JDialogProfile;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kenny
 */
public class ScreenPdv extends javax.swing.JFrame {

    JDialogOrder jdialogOrder;
    UserSession session;
    CompanySession companySession;
    UserController userController;
    ShiftSession shiftSession;
    OrderController orderController;
    ProductController productController;
    List<ProductOrder> listProductOrder;
    Clients clientSelected;
    ShiftController shiftController = new ShiftController();
    double subTotal, total;

    /**
     * Creates new form ScreenPdv
     */
    public ScreenPdv() {
        initComponents();
        this.setExtendedState(ScreenPdv.MAXIMIZED_BOTH);
        session = UserSession.getInstance();
        shiftSession = ShiftSession.getInstance();
        companySession = CompanySession.getInstance();
        //orderController = new OrderController();
        productController = new ProductController();
        listProductOrder = new ArrayList<>();
        clientSelected = null;//new Clients();
        shiftController.getShiftSession();
        userController = new UserController();

        // Atalhos globais
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // F2 → Ajuda
        im.put(KeyStroke.getKeyStroke("F1"), "ajuda");
        am.put("ajuda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajuda();
            }
        });
        // F2 → Novo Cliente
        im.put(KeyStroke.getKeyStroke("F2"), "novoCliente");
        am.put("novoCliente", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoCliente();
            }
        });

        // F3 → Procurar Produto
        im.put(KeyStroke.getKeyStroke("F3"), "procurarProduto");
        am.put("procurarProduto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procurarProduto();
            }
        });

        // F12 → Finalizar Venda
        im.put(KeyStroke.getKeyStroke("F12"), "finalizarVenda");
        am.put("finalizarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });

        // ESC → Cancelar
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelarVenda");
        am.put("cancelarVenda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarVenda();
            }
        });
    }

    // ================== FUNÇÕES DO PDV ==================
    private void ajuda() {
        JDialogHelpers jdHelp = new JDialogHelpers(this, true);
        jdHelp.setVisible(true);
    }

    private void novoCliente() {
        JOptionPane.showMessageDialog(this, "Função: Novo Cliente");
    }

    private void procurarProduto() {
        JDialogProductSearch jdP = new JDialogProductSearch(this, true);
        jdP.setVisible(true);
    }

    private void finalizarVenda() {
        // Evita finalizar sem itens
        if (listProductOrder == null || listProductOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione produto para fazer a venda", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
//       System.out.println(listProductOrder.toString());
        // Evita duplo clique
        jButtonFinishInvoice.setEnabled(false);

        try {
            // 1) Cliente padrão (se não selecionado)
            if (clientSelected == null) {
                ClientDao clientDao = new ClientDao();
                clientSelected = clientDao.getClientDefault();
                if (clientSelected == null) {
                    JOptionPane.showMessageDialog(this, "Cliente padrão não configurado.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // 2) Sessão/Operador
            if (shiftSession == null || shiftSession.getSeller() == null) {
                JOptionPane.showMessageDialog(this, "Abra um turno (operador não encontrado).", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3) Monta o Order “mínimo” (sem numeração/hash/valores pagos)
            Order order = new Order();
            order.setClient(clientSelected);
            order.setSeller(shiftSession.getSeller());
            order.setProducts(listProductOrder);
            // NÃO setar prefix/number/hash aqui; isso é do controller/service.
            // NÃO usar total/subTotal aqui; isso é recalculado no servidor.

            // 3.1) CALCULAR TOTAIS PARA EXIBIÇÃO NO DIÁLOGO (UI)
// (o controller recalcula novamente ao salvar)
            // 3.1) CALCULAR TOTAIS PARA EXIBIÇÃO (preço COM IVA embutido)
            Totais t = calcularTotaisLocalComIVA(listProductOrder);
            order.setSubTotal(t.subtotal.doubleValue()); // líquido
            order.setTotalTaxe(t.tax.doubleValue());     // IVA
            order.setTotal(t.total.doubleValue());       // bruto

//            System.out.println(order);
            // 4) Abre diálogo de pagamento (o diálogo calcula payTotal, troco, métodos, observações, etc.)
            JDialogOrder jdOrder = new JDialogOrder(this, true);
            jdOrder.setOrder(order);          // passa itens/cliente/seller para o preview no diálogo
            jdOrder.setVisible(true);

            // 5) Verifica se o utilizador confirmou o pagamento
            Boolean confirmado = jdOrder.getResponse();
            if (Boolean.TRUE.equals(confirmado)) {
                // O diálogo deve ter preenchido:
                //   order.setPayTotal(...);
                //   order.setAmountReturned(...);
                //   order.setNote(...);           // se aplicares
                //   // não mexer no prefix/number/hash – isso será feito no controller

                // 6) Finaliza no controller (ele calcula totais, reserva número, gera hash e persiste tudo)
//                Order result = orderController.criarEFinalizar(order);
//                if (result != null && result.getId() > 0) {
//                    JOptionPane.showMessageDialog(this, "Venda efetuada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearOrder();   // limpa carrinho/campos
//                    listProduts();  // recarrega grelha de produtos/stock
//                    // opcional: imprimir/mostrar resumo result
//                } else {
//                    JOptionPane.showMessageDialog(this, "Falha ao gravar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
//                }
            } else {
                // utilizador cancelou; nada a fazer
                // JOptionPane.showMessageDialog(this, "Pagamento cancelado.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            Logger.getLogger(ScreenPdv.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao finalizar a venda:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            jButtonFinishInvoice.setEnabled(true);
        }

    }

    private void cancelarVenda() {
        listProductOrder.clear();
        listProdutsOrder();
        calculTotal();
    }

    private static class Totais {

        BigDecimal subtotal; // líquido (sem IVA)
        BigDecimal tax;      // IVA
        BigDecimal total;    // bruto (com IVA)

        Totais(BigDecimal s, BigDecimal i, BigDecimal t) {
            this.subtotal = s;
            this.tax = i;
            this.total = t;
        }
    }

    /**
     * Calcula totais assumindo que Product.getPrice() é PREÇO COM IVA embutido.
     * Regras: - perc = 0 => tudo líquido, tax = 0 (mas pode ter reason Mxx) -
     * perc > 0 => líquido = preço / (1 + perc/100), imposto = preço - líquido
     */
    private Totais calcularTotaisLocalComIVA(List<ProductOrder> itens) {
        BigDecimal sub = BigDecimal.ZERO; // líquido
        BigDecimal iva = BigDecimal.ZERO; // imposto
        BigDecimal tot = BigDecimal.ZERO; // bruto (soma dos preços com IVA)

        if (itens != null) {
            for (ProductOrder po : itens) {
                if (po == null || po.getProduct() == null) {
                    continue;
                }

                BigDecimal qty = bd(po.getQty());
                BigDecimal priceW = bd(po.getPrice()); // PREÇO UNITÁRIO COM IVA
                BigDecimal gross = priceW.multiply(qty);

//                System.out.println(" qtd " + qty + " priceW " + priceW + " gross " + gross);
                // percentagem de IVA (pode vir null)
//                Double percD = po.getTaxePercentage();
//                if (percD == null) {
//                    percD = 0.0;
//                }
//                BigDecimal perc = new BigDecimal(percD.toString());
                // % IVA (ex.: 7 → 7%)
                BigDecimal perc = bd(po.getTaxePercentage()); // trata null como 0 no helper

                BigDecimal lineNet; // sem IVA
                BigDecimal lineTax; // imposto

//                ⚠️ No teu caso do PDV:
//
//Se o preço já vem com IVA incluído, então o imposto não é 700 mas sim a parte do total (10 000) que corresponde a 7% sobre a base líquida.
//→ Fórmula: taxa = total × perc / (100 + perc)
//→ 10 000 × 7 / 107 ≈ 654,21
//
//Ou seja:
//
//Preço sem IVA (líquido): 9 345,79
//
//IVA embutido (7%): 654,21
//
//Preço com IVA: 10 000
                if (perc.compareTo(BigDecimal.ZERO) > 0) {
                    // taxa = gross * perc / (100 + perc)
                    BigDecimal base = new BigDecimal("100").add(perc);
                    lineTax = gross.multiply(perc).divide(base, 2, RoundingMode.HALF_UP);
                    lineNet = gross.subtract(lineTax);
                } else {
                    // isento/0% → tudo líquido
                    lineTax = BigDecimal.ZERO;
                    lineNet = gross;
                }
                sub = sub.add(lineNet);
                iva = iva.add(lineTax);
                tot = tot.add(gross);
            }
        }

        // Arredondamento final a 2 casas
        sub = sub.setScale(2, RoundingMode.HALF_UP);
        iva = iva.setScale(2, RoundingMode.HALF_UP);
        tot = tot.setScale(2, RoundingMode.HALF_UP);

        // Segurança: tot deve ser ~ sub + iva
        // (devido a arredondamentos pode dar diferença de 0,01; se quiseres, força tot = sub.add(iva))
        return new Totais(sub, iva, tot);
    }

    private static BigDecimal bd(Number n) {
        return (n == null) ? BigDecimal.ZERO : new BigDecimal(n.toString());
    }

    public String[] splitString(String str) {
        // Verifica se a string está vazia
        if (str.isEmpty()) {
            return null; // Retorna null se a string estiver vazia
        }
        // Usar regex para separar a string em 'X' ou 'x'
        String[] parts = str.split("(?i)x", 2); // (?i) torna a regex case-insensitive
        // Retorna o array, que terá a string inteira se o delimitador não for encontrado
        return parts.length == 1 ? new String[]{str} : parts;
    }

    public String[] validateProduto(String str) {
        String[] result = splitString(str);
        if (result == null) {
            JOptionPane.showMessageDialog(null, "Selecione um produto da lista!!");
        } else if (result.length == 1) {
            String[] value = {result[0], jTextFieldQtdProductsSelected.getText()};
            return value;
        } else {
            String[] value = {result[1], result[0]};
            return value;
        }
        return result;
    }

    public boolean AddProdutoToList(String barcode, int qtd) {
        // 0) Validações iniciais
        String bc = (barcode == null) ? "" : barcode.trim();
        if (bc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código de barras vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (qtd <= 0) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Informe um valor maior que zero.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 1) Buscar produto
        Product prod = productController.getFromBarCode(bc);
        if (prod == null || prod.getId() <= 0 || prod.getDescription() == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado para o código informado.", "Atenção", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (prod.getPrice() == null) {
            JOptionPane.showMessageDialog(this, "Preço do produto não definido.", "Atenção", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // (Opcional) bloquear produto inativo
        if (prod.getStatus() == 0) {
            JOptionPane.showMessageDialog(this, "Produto inativo. PRODUTO NAO DISPONIVEL", "Atenção", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // 2) Imposto e razão
        if (prod.getTaxe() == null) {
            JOptionPane.showMessageDialog(this, "Imposto não definido no produto.", "Atenção", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        BigDecimal perc = prod.getTaxe().getPercetage();
        if (perc == null) {
            perc = BigDecimal.ZERO;
        }

        String reasonTax = null;
        String reasonCode = null;

        if (perc == BigDecimal.ZERO) {
            if (prod.getReasonTaxe() == null) {
                JOptionPane.showMessageDialog(this, "Produto isento/0% sem Razão de Imposto (Mxx).", "Atenção", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                reasonTax = prod.getReasonTaxe().getReason();
                reasonCode = prod.getReasonTaxe().getCode();
            }
        }

        // 3) Verificar se já existe no carrinho
        int existingIdx = -1;
        ProductOrder existing = null;
        for (int i = 0; i < listProductOrder.size(); i++) {
            ProductOrder po = listProductOrder.get(i);
            if (po != null && po.getProduct() != null && po.getProduct().getId() == prod.getId()) {
                existingIdx = i;
                existing = po;
                break;
            }
        }

        // 4) Conferir stock com soma
        int qtyAtual = (existing != null) ? existing.getQty() : 0;
        int qtyFinal = qtyAtual + qtd;

        StockMovementDao stockDao = new StockMovementDao();
        int stockAtual = stockDao.getStockAtual(prod.getId());  // consulta somatório IN - OUT
        prod.setCurrentStock(stockAtual); // opcional: atualiza no objeto

        if (stockAtual < qtyFinal) {
            JOptionPane.showMessageDialog(this,
                    "ESTOQUE INSUFICIENTE!\nDISPONÍVEL ATUAL: " + stockAtual,
                    "Atenção", JOptionPane.ERROR_MESSAGE);
            return false;
        }

//        if (prod.getCurrentStock() < qtyFinal) {
////        if (prod.getStockTotal() < qtyFinal) {
//            int disponivel = Math.max(prod.getStockTotal() - qtyAtual, 0);
//            JOptionPane.showMessageDialog(this,
//                    "ESTOQUE INSUFICIENTE!\nDISPONIVEL ATUAL: " + disponivel,
//                    "Atenção", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
        // 5) Criar/Atualizar ProductOrder
        if (existing == null) {
            ProductOrder po = new ProductOrder();
            po.setProduct(prod);
            po.setDescription(prod.getDescription());
            po.setCode(prod.getCode());
            po.setPrice(prod.getPrice());
            po.setQty(qtd);

            // Imposto
            po.setTaxePercentage(perc);
            po.setTaxeCode(prod.getTaxe().getCode());
            po.setTaxeName(prod.getTaxe().getName());

            // Razão (se 0%/isento)
            po.setReasonTax(reasonTax);
            po.setReasonCode(reasonCode);

            listProductOrder.add(po);
        } else {
            // Atualiza apenas a quantidade e (opcionalmente) sincroniza impostos se o produto mudou
            existing.setQty(qtyFinal);

            // Re-sincronizar impostos/razões (caso tenham sido alterados no cadastro)
            existing.setTaxePercentage(perc);
            existing.setTaxeCode(prod.getTaxe().getCode());
            existing.setTaxeName(prod.getTaxe().getName());
            existing.setReasonTax(reasonTax);
            existing.setReasonCode(reasonCode);

            // Garante consistência do preço e descrição
            existing.setPrice(prod.getPrice());
            existing.setDescription(prod.getDescription());
            existing.setCode(prod.getCode());

            listProductOrder.set(existingIdx, existing);
        }

        // 6) Atualizar UI
        listProdutsOrder();
        calculTotal();
        new Utilities().clearScreen(jPanelSelectedProduct);
        jTextFieldQtdProductsSelected.setText("1");
        // jTextFieldBarCodeProductSelect.setText(""); // se quiseres limpar o campo

        return true;
////        if (barcode.isEmpty() != true) {
//        Product prod = productController.getFromBarCode(barcode);
////            System.out.println("product encontrado1:" + prod.getDescription());
//        if (prod.getDescription() != null) {
////                System.out.println("product encontrado2:" + prod.getDescription());
//            if (prod.getStockTotal() >= qtd && qtd > 0) {
//                ProductOrder prodOrder = new ProductOrder();
//                prodOrder.setProduct(prod);
//                prodOrder.setDescription(prod.getDescription());
//
//                if (prod.getTaxe() == null) {
//                    JOptionPane.showMessageDialog(null, "Imposto não definido no Produto", "Atenção", JOptionPane.ERROR_MESSAGE);
//                    return false;
//                } else {
//                    prodOrder.setTaxePercentage(prod.getTaxe().getPercetage());
//                    prodOrder.setTaxeCode(prod.getTaxe().getCode());
//                    prodOrder.setTaxeName(prod.getTaxe().getName());
//
//                    if (prod.getTaxe().getPercetage() == 0) {
//                        if (prod.getReasonTaxe() == null) {
//                            JOptionPane.showMessageDialog(null, "Produto sem Razao de imposto!", "Atenção", JOptionPane.ERROR_MESSAGE);
//                            return false;
//                        } else {
//                            prodOrder.setReasonTax(prod.getReasonTaxe().getReason());
//                            prodOrder.setReasonCode(prod.getReasonTaxe().getCode());
//                        }
//                    } else {
//                        prodOrder.setReasonTax(null);
//                        prodOrder.setReasonCode(null);
//                    }
//                }
//
//                prodOrder.setCode(prod.getCode());
//                prodOrder.setPrice(prod.getPrice());
//                prodOrder.setQty(qtd);
//
//                for (ProductOrder productOrder : listProductOrder) {
//                    if (productOrder.getProduct().getId() == prodOrder.getProduct().getId()) {
//                        prodOrder.setQty(prodOrder.getQty() + productOrder.getQty());
//                        listProductOrder.remove(productOrder);
//                        break;
//                    }
//                }
////                    System.out.println(prodOrder);
//                listProductOrder.add(prodOrder);
//                listProdutsOrder();
//                calculTotal();
//                new Utilities().clearScreen(jPanelSelectedProduct);
//                jTextFieldQtdProductsSelected.setText("1");
////                    jTextFieldBarCodeProductSelect.setText("1X");
//                return true;
//
//            } else if (qtd <= 0) {
//                JOptionPane.showMessageDialog(null, "Quantidade invalida!! Verifica a Quantidade de produto!", "Atenção", JOptionPane.ERROR_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Estoque insuficiente!", "Atenção", JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Não foi possivel adicionar Produto ao carrinho. verifica O Codigo de barra!!", "Atenção", JOptionPane.ERROR_MESSAGE);
//        }
//
////        }
//        return false;
    }

    public void listProduts(List<Product> list) {
        if (list == null) {
            list = productController.getForPDV(null);
        }
        DefaultTableModel data = (DefaultTableModel) jTableProducts.getModel();
        data.setNumRows(0);
        for (Product c : list) {
            data.addRow(new Object[]{
                c.getId(),
                c.getBarcode(),
                c.getDescription(),
                c.getPrice(),
                c.getTaxe().getPercetage(),
                c.getCurrentStock()
            }
            );
        }
    }

    public void listProdutsOrder() {
        DefaultTableModel data = (DefaultTableModel) jTableProductsInvoice.getModel();
        data.setNumRows(0);
        for (ProductOrder c : listProductOrder) {
            data.addRow(new Object[]{
                c.getProduct().getId(),
                c.getCode(),
                c.getDescription(),
                c.getPrice(),
                c.getQty(),
                productController.calculateTotalProduct(c.getProduct(), c.getQty())
//                c.getQty() * c.getPrice()
            }
            );
        }
    }

    public void filterListProducts(String txt) {
        listProduts(productController.getForPDV(txt));
    }

//    public void listClients() {
//        ClientDao dao = new ClientDao();
//        List<Clients> list = dao.list("");
//        jComboBoxClients.removeAllItems();
//        for (Clients item : list) {
//            jComboBoxClients.addItem(item);
//        }
//    }
//    public void seletedClient() {
////        String name = jComboBoxClients.getSelectedItem().toString();
////        ClientDao cDao = new ClientDao();
////        clientSelected = cDao.searchFromName(name);
////        if (clientSelected.getName() != null) {
////            jTextFieldNifClient.setText(clientSelected.getNif());
////            jLabelNameClienteSelected.setText(clientSelected.getName());
////        } else {
////            JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
////        }
//    }
    public void backDashboard() {
        new ScreenMain().setVisible(true);
        this.dispose();
    }

//    public void calculTotal() {
//        total = subTotal = 0;
//        for (ProductOrder productOrder : listProductOrder) {
//            subTotal = productOrder.getProduct().getPrice() * productOrder.getQty();
//            total += productOrder.getProduct().getPrice() * productOrder.getQty();
//        }
//        jTextFieldTotalInvoice.setText(String.valueOf(total));
////        jTextFieldPayClient.setText(String.valueOf(total));
//    }
    public void calculTotal() {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (ProductOrder productOrder : listProductOrder) {
            if (productOrder.getProduct() != null && productOrder.getProduct().getPrice() != null) {
                BigDecimal price = productOrder.getProduct().getPrice();
                BigDecimal qty = BigDecimal.valueOf(productOrder.getQty());

                BigDecimal lineTotal = price.multiply(qty);

                subTotal = subTotal.add(lineTotal);
                total = total.add(lineTotal);
            }
        }

        jTextFieldTotalInvoice.setText(total.toPlainString());
        // jTextFieldPayClient.setText(total.toPlainString());
    }

    public void clearOrder() {
        listProductOrder.clear();
        listProdutsOrder();
        calculTotal();
        clearClient();
    }

    public void clearClient() {
        ClientDao clientDao = new ClientDao();
        clientSelected = clientDao.getClientDefault();
        jTextFieldNifClient.setText(clientSelected.getNif());
        jLabelNameClienteSelected.setText(clientSelected.getName());
    }

    private void iniciarRelogio() {
        Timer timer = new Timer(1000, e -> {
            // Atualiza a cada 1 segundo (1000 ms)
            String horaAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            jLabelDateTime.setText(horaAtual);
        });
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelNameUserSeller = new javax.swing.JLabel();
        jLabelNameCompany = new javax.swing.JLabel();
        jLabelDateTime = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonPainel = new javax.swing.JButton();
        jButtonListSales = new javax.swing.JButton();
        jButtonPainelListProducts = new javax.swing.JButton();
        jButtonListClient = new javax.swing.JButton();
        jButtonCloseShift = new javax.swing.JButton();
        jButtonHelp = new javax.swing.JButton();
        jComboBoxOptions = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldTotalInvoice = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButtonFinishInvoice = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProductsInvoice = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jButtonRemoveAllProd = new javax.swing.JButton();
        jButtonRemoveProd = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldNifClient = new javax.swing.JTextField();
        jButtonPesquisarCompany = new javax.swing.JButton();
        jLabelNameClienteSelected = new javax.swing.JLabel();
        jPanelSelectedProduct = new javax.swing.JPanel();
        jTextFieldSearchProducts = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldBarCodeProductSelect = new javax.swing.JTextField();
        jTextFieldQtdProductsSelected = new javax.swing.JTextField();
        jButtonAddProductInvoice = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextFieldPriceProductSelect = new javax.swing.JTextField();
        jTextFieldNameProductSelected = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableProducts = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ponto de Venda");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabelNameUserSeller.setBackground(new java.awt.Color(255, 255, 255));
        jLabelNameUserSeller.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameUserSeller.setForeground(new java.awt.Color(0, 0, 102));
        jLabelNameUserSeller.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNameUserSeller.setText("Usuario");

        jLabelNameCompany.setBackground(new java.awt.Color(255, 255, 255));
        jLabelNameCompany.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameCompany.setForeground(new java.awt.Color(0, 0, 102));
        jLabelNameCompany.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNameCompany.setText("PDV");

        jLabelDateTime.setBackground(new java.awt.Color(255, 255, 255));
        jLabelDateTime.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDateTime.setForeground(new java.awt.Color(0, 0, 102));
        jLabelDateTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDateTime.setText("Data e hora");

        jToolBar1.setRollover(true);
        jToolBar1.setMargin(new java.awt.Insets(0, 5, 0, 5));

        jButtonPainel.setBackground(new java.awt.Color(51, 51, 255));
        jButtonPainel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPainel.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPainel.setText("Painel");
        jButtonPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPainelActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPainel);

        jButtonListSales.setBackground(new java.awt.Color(102, 102, 102));
        jButtonListSales.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonListSales.setForeground(new java.awt.Color(255, 255, 255));
        jButtonListSales.setText("Lista de venda");
        jButtonListSales.setFocusable(false);
        jButtonListSales.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonListSales.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonListSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListSalesActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonListSales);

        jButtonPainelListProducts.setBackground(new java.awt.Color(51, 51, 255));
        jButtonPainelListProducts.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPainelListProducts.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPainelListProducts.setText("Produtos");
        jButtonPainelListProducts.setFocusable(false);
        jButtonPainelListProducts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPainelListProducts.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPainelListProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPainelListProductsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPainelListProducts);

        jButtonListClient.setBackground(new java.awt.Color(51, 153, 0));
        jButtonListClient.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonListClient.setForeground(new java.awt.Color(255, 255, 255));
        jButtonListClient.setText("Clientes");
        jButtonListClient.setFocusable(false);
        jButtonListClient.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonListClient.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonListClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListClientActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonListClient);

        jButtonCloseShift.setBackground(new java.awt.Color(204, 0, 0));
        jButtonCloseShift.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonCloseShift.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCloseShift.setText("Fechar caixa");
        jButtonCloseShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseShiftActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonCloseShift);

        jButtonHelp.setBackground(new java.awt.Color(204, 204, 0));
        jButtonHelp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonHelp.setForeground(new java.awt.Color(255, 255, 255));
        jButtonHelp.setText("Ajuda");
        jButtonHelp.setFocusable(false);
        jButtonHelp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonHelp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHelpActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonHelp);

        jComboBoxOptions.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Opcões", "Panel", "Sair" }));
        jComboBoxOptions.setToolTipText("Opcões");
        jComboBoxOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOptionsActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBoxOptions);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNameCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelNameUserSeller, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNameCompany)
                            .addComponent(jLabelNameUserSeller)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDateTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jPanel10.setBackground(new java.awt.Color(0, 0, 51));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTextFieldTotalInvoice.setEditable(false);
        jTextFieldTotalInvoice.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jTextFieldTotalInvoice.setForeground(new java.awt.Color(204, 0, 0));
        jTextFieldTotalInvoice.setText("0.00");
        jTextFieldTotalInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTotalInvoiceActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("Total");

        jButtonFinishInvoice.setBackground(new java.awt.Color(204, 204, 255));
        jButtonFinishInvoice.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButtonFinishInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Done.png"))); // NOI18N
        jButtonFinishInvoice.setText("Pagamento F12");
        jButtonFinishInvoice.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonFinishInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishInvoiceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldTotalInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(jButtonFinishInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTableProductsInvoice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTableProductsInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo", "Produto", "Preço", "Qtd", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableProductsInvoice);
        if (jTableProductsInvoice.getColumnModel().getColumnCount() > 0) {
            jTableProductsInvoice.getColumnModel().getColumn(0).setMinWidth(30);
            jTableProductsInvoice.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTableProductsInvoice.getColumnModel().getColumn(0).setMaxWidth(50);
            jTableProductsInvoice.getColumnModel().getColumn(1).setMinWidth(80);
            jTableProductsInvoice.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTableProductsInvoice.getColumnModel().getColumn(1).setMaxWidth(120);
            jTableProductsInvoice.getColumnModel().getColumn(2).setMinWidth(200);
            jTableProductsInvoice.getColumnModel().getColumn(2).setPreferredWidth(250);
            jTableProductsInvoice.getColumnModel().getColumn(2).setMaxWidth(300);
            jTableProductsInvoice.getColumnModel().getColumn(4).setMinWidth(40);
            jTableProductsInvoice.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTableProductsInvoice.getColumnModel().getColumn(4).setMaxWidth(60);
        }

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/shopping-cart_14572403.png"))); // NOI18N
        jLabel17.setText("Cesta");

        jButtonRemoveAllProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Restart.png"))); // NOI18N
        jButtonRemoveAllProd.setText("Limpar");
        jButtonRemoveAllProd.setBorderPainted(false);
        jButtonRemoveAllProd.setContentAreaFilled(false);
        jButtonRemoveAllProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRemoveAllProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveAllProdActionPerformed(evt);
            }
        });

        jButtonRemoveProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Trash Can.png"))); // NOI18N
        jButtonRemoveProd.setText("Remover");
        jButtonRemoveProd.setBorderPainted(false);
        jButtonRemoveProd.setContentAreaFilled(false);
        jButtonRemoveProd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRemoveProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveProdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRemoveAllProd)
                .addGap(18, 18, 18)
                .addComponent(jButtonRemoveProd)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonRemoveAllProd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonRemoveProd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 0, 102));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Localizar o Cliente por NIF");

        jTextFieldNifClient.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFieldNifClient.setAutoscrolls(false);

        jButtonPesquisarCompany.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPesquisarCompany.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonPesquisarCompany.setToolTipText("Selecionar Cliente");
        jButtonPesquisarCompany.setBorderPainted(false);
        jButtonPesquisarCompany.setContentAreaFilled(false);
        jButtonPesquisarCompany.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPesquisarCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPesquisarCompanyActionPerformed(evt);
            }
        });

        jLabelNameClienteSelected.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelNameClienteSelected.setText("Consumidor final");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jTextFieldNifClient, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPesquisarCompany)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNameClienteSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPesquisarCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNameClienteSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNifClient, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanelSelectedProduct.setBackground(new java.awt.Color(255, 255, 255));

        jTextFieldSearchProducts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchProductsKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Pesquisar");

        jTextFieldBarCodeProductSelect.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTextFieldBarCodeProductSelect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldBarCodeProductSelectKeyPressed(evt);
            }
        });

        jTextFieldQtdProductsSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextFieldQtdProductsSelected.setText("1");
        jTextFieldQtdProductsSelected.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldQtdProductsSelectedKeyReleased(evt);
            }
        });

        jButtonAddProductInvoice.setBackground(new java.awt.Color(204, 204, 255));
        jButtonAddProductInvoice.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonAddProductInvoice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        jButtonAddProductInvoice.setToolTipText("Adicionar Produto");
        jButtonAddProductInvoice.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAddProductInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddProductInvoiceActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("QTD X Codigo de Barra");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("QTD");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jTextFieldPriceProductSelect.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jTextFieldNameProductSelected.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Produto selecionado...");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Preço Unitario");

        javax.swing.GroupLayout jPanelSelectedProductLayout = new javax.swing.GroupLayout(jPanelSelectedProduct);
        jPanelSelectedProduct.setLayout(jPanelSelectedProductLayout);
        jPanelSelectedProductLayout.setHorizontalGroup(
            jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                        .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldSearchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                        .addComponent(jTextFieldBarCodeProductSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldQtdProductsSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(56, 56, 56)
                                        .addComponent(jLabel10)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                                        .addComponent(jTextFieldPriceProductSelect)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonAddProductInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                        .addComponent(jTextFieldNameProductSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88))
        );
        jPanelSelectedProductLayout.setVerticalGroup(
            jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectedProductLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14))
                .addGap(1, 1, 1)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldBarCodeProductSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddProductInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldQtdProductsSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldPriceProductSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSelectedProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNameProductSelected, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSearchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTableProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Codigo de Barra", "Produto", "Preço", "Imposto", "Estoque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableProducts);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelSelectedProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSelectedProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1137, 578));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
//        System.out.println("hhh" + shiftSession.getShift());
        if (shiftSession.getShift() == null) {
            new JDialogOpenShift(this, rootPaneCheckingEnabled).setVisible(true);
            if (shiftSession.getShift() == null) {
                backDashboard();
            }
        } else {
            listProduts(null);
//            listClients();
            iniciarRelogio(); // inicia o relógio
//            String date = UtilDate.getDateNow();
//            jLabelDateTime.setText(date);
            jLabelNameUserSeller.setText(shiftSession.getSeller().getName());
            jLabelNameCompany.setText(companySession.getName());

            int itemCount = jComboBoxOptions.getItemCount();
            String itemValue = jComboBoxOptions.getItemAt(itemCount - 1);

            if (itemValue != shiftSession.getSeller().getName()) {
                jComboBoxOptions.addItem(shiftSession.getSeller().getName());
                jComboBoxOptions.setToolTipText(shiftSession.getSeller().getName());
                jComboBoxOptions.setSelectedItem(shiftSession.getSeller().getName());
            }
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButtonFinishInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishInvoiceActionPerformed
        // TODO add your handling code here:
        finalizarVenda();
        // Evita finalizar sem itens
//        if (listProductOrder == null || listProductOrder.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Adicione produto para fazer a venda", "Atenção", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        // Evita duplo clique
//        jButtonFinishInvoice.setEnabled(false);
//
//        try {
//            // 1) Cliente padrão (se não selecionado)
//            if (clientSelected == null) {
//                ClientDao clientDao = new ClientDao();
//                clientSelected = clientDao.getClientDefault();
//                if (clientSelected == null) {
//                    JOptionPane.showMessageDialog(this, "Cliente padrão não configurado.", "Atenção", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//            }
//
//            // 2) Sessão/Operador
//            if (shiftSession == null || shiftSession.getSeller() == null) {
//                JOptionPane.showMessageDialog(this, "Abra um turno (operador não encontrado).", "Atenção", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            // 3) Monta o Order “mínimo” (sem numeração/hash/valores pagos)
//            Order order = new Order();
//            order.setClient(clientSelected);
//            order.setSeller(shiftSession.getSeller());
//            order.setProducts(listProductOrder);
//            // NÃO setar prefix/number/hash aqui; isso é do controller/service.
//            // NÃO usar total/subTotal aqui; isso é recalculado no servidor.
//
//            // 3.1) CALCULAR TOTAIS PARA EXIBIÇÃO NO DIÁLOGO (UI)
//// (o controller recalcula novamente ao salvar)
//            // 3.1) CALCULAR TOTAIS PARA EXIBIÇÃO (preço COM IVA embutido)
//            Totais t = calcularTotaisLocalComIVA(listProductOrder);
//            order.setSubTotal(t.subtotal.doubleValue()); // líquido
//            order.setTotalTaxe(t.tax.doubleValue());     // IVA
//            order.setTotal(t.total.doubleValue());       // bruto
//
////            System.out.println(order);
//            // 4) Abre diálogo de pagamento (o diálogo calcula payTotal, troco, métodos, observações, etc.)
//            JDialogOrder jdOrder = new JDialogOrder(this, true);
//            jdOrder.setOrder(order);          // passa itens/cliente/seller para o preview no diálogo
//            jdOrder.setVisible(true);
//
//            // 5) Verifica se o utilizador confirmou o pagamento
//            Boolean confirmado = jdOrder.getResponse();
//            if (Boolean.TRUE.equals(confirmado)) {
//                // O diálogo deve ter preenchido:
//                //   order.setPayTotal(...);
//                //   order.setAmountReturned(...);
//                //   order.setNote(...);           // se aplicares
//                //   // não mexer no prefix/number/hash – isso será feito no controller
//
//                // 6) Finaliza no controller (ele calcula totais, reserva número, gera hash e persiste tudo)
////                Order result = orderController.criarEFinalizar(order);
////                if (result != null && result.getId() > 0) {
////                    JOptionPane.showMessageDialog(this, "Venda efetuada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
//                clearOrder();   // limpa carrinho/campos
////                    listProduts();  // recarrega grelha de produtos/stock
////                    // opcional: imprimir/mostrar resumo result
////                } else {
////                    JOptionPane.showMessageDialog(this, "Falha ao gravar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
////                }
//            } else {
//                // utilizador cancelou; nada a fazer
//                // JOptionPane.showMessageDialog(this, "Pagamento cancelado.", "Info", JOptionPane.INFORMATION_MESSAGE);
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(ScreenPdv.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao finalizar a venda:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            jButtonFinishInvoice.setEnabled(true);
//        }
    }//GEN-LAST:event_jButtonFinishInvoiceActionPerformed

    private void jButtonRemoveProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveProdActionPerformed
        // TODO add your handling code here:
        int value = 0;
        try {
            value = (int) jTableProductsInvoice.getValueAt(jTableProductsInvoice.getSelectedRow(), 0);
            System.out.println("jTableProductsInvoice id:" + value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Selecione um Products na tabela!!", "Atencao", JOptionPane.ERROR_MESSAGE);
        } finally {
//            System.out.println("jTableUsers id:" + value);
            if (value > 0) {
                for (ProductOrder productOrder : listProductOrder) {
                    if (productOrder.getProduct().getId() == value) {
                        listProductOrder.remove(productOrder);
                        break;
                    }
                }
                listProdutsOrder();
                calculTotal();
            }
        }
    }//GEN-LAST:event_jButtonRemoveProdActionPerformed

    private void jButtonRemoveAllProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveAllProdActionPerformed
        // TODO add your handling code here:
        cancelarVenda();
//        listProductOrder.clear();
//        listProdutsOrder();
//        calculTotal();
    }//GEN-LAST:event_jButtonRemoveAllProdActionPerformed

    private void jButtonPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPainelActionPerformed
        // TODO add your handling code here:
        int yes = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja ir ao Dashboard??", "Atenção", JOptionPane.YES_NO_OPTION);
        if (yes == JOptionPane.YES_OPTION) {
            backDashboard();
        }
    }//GEN-LAST:event_jButtonPainelActionPerformed

    private void jButtonCloseShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseShiftActionPerformed
        // TODO add your handling code here:
        new JDialogCloseShift(this, rootPaneCheckingEnabled).setVisible(true);
    }//GEN-LAST:event_jButtonCloseShiftActionPerformed

    private void jTextFieldTotalInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTotalInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTotalInvoiceActionPerformed

    private void jButtonListSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListSalesActionPerformed
        // TODO add your handling code here:
        JDialogListOrder jdListorder = new JDialogListOrder(null, true);
        jdListorder.setVisible(true);
    }//GEN-LAST:event_jButtonListSalesActionPerformed

    private void jButtonListClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListClientActionPerformed
        // TODO add your handling code here:
        novoCliente();
    }//GEN-LAST:event_jButtonListClientActionPerformed

    private void jComboBoxOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOptionsActionPerformed
        // TODO add your handling code here:
        String option = (String) jComboBoxOptions.getSelectedItem();

        if ("Panel".equals(option)) {
            int yes = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja ir ao Dashboard??", "Atenção", JOptionPane.YES_NO_OPTION);
            if (yes == JOptionPane.YES_OPTION) {
                backDashboard();
            }
        } else if ("Usuario".equals(option)) {
            new JDialogProfile(this, rootPaneCheckingEnabled).setVisible(true);
        } else if ("Sair".equals(option)) {
            int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (sair == JOptionPane.YES_OPTION) {
                this.dispose();
                if (userController.logout()) {
                    new ScreenLogin().setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxOptionsActionPerformed

    private void jButtonPainelListProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPainelListProductsActionPerformed
        // TODO add your handling code here:
//        JDialogProductSearch jdP = new JDialogProductSearch(this, true);
//        jdP.setVisible(true);
        procurarProduto();
    }//GEN-LAST:event_jButtonPainelListProductsActionPerformed

    private void jTableProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductsMouseClicked
        // TODO add your handling code here:
        jTextFieldBarCodeProductSelect.setText((String) jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 1));
        jTextFieldNameProductSelected.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 2).toString());
        jTextFieldPriceProductSelect.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 3).toString());
        //        jTextFieldStock.setText(jTableProducts.getValueAt(jTableProducts.getSelectedRow(), 4).toString());
    }//GEN-LAST:event_jTableProductsMouseClicked

    private void jButtonAddProductInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddProductInvoiceActionPerformed
        // TODO add your handling code here:
        //        String str = jTextFieldBarCodeProductSelect.getText();
        String[] result = validateProduto(jTextFieldBarCodeProductSelect.getText());

        if (result != null) {
            if (AddProdutoToList(result[0], Integer.parseInt(result[1])) == true) {
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possivel adicionar Produto ao carrinho", "Atenção", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonAddProductInvoiceActionPerformed

    private void jTextFieldQtdProductsSelectedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldQtdProductsSelectedKeyReleased
        // TODO add your handling code here:
//        if (Util.checkIsNumber(jTextFieldQtdProductsSelected.getText()) == false) {
//            JOptionPane.showMessageDialog(null, "Insira um numero valido!");
////            jTextFieldQtdProductsSelected.setText("1");
//        }
        String texto = jTextFieldQtdProductsSelected.getText().trim();
        int quantidade = 1; // valor padrão

        // 🔹 1️⃣ Se o campo estiver vazio → não forçar nada (permite o utilizador digitar)
        if (texto.isEmpty()) {
            return;
        }

        // 🔹 2️⃣ Verifica se é número válido
        if (!Util.checkIsNumber(texto)) {
            // opcional: alerta visual sem forçar "1"
//            jTextFieldQtdProductsSelected.setBackground(Color.decode("#f8d7da")); // vermelho leve
            jTextFieldQtdProductsSelected.setText(String.valueOf("1"));
            return;
        } else {
//            jTextFieldQtdProductsSelected.setBackground(Color.WHITE);
        }

        try {
            quantidade = Integer.parseInt(texto);

            // 🔹 3️⃣ Garante que nunca seja menor que 1
            if (quantidade < 1) {
                quantidade = 1;
            }

        } catch (NumberFormatException e) {
            quantidade = 1; // fallback de segurança
        }

        // 🔹 4️⃣ Atualiza o campo com o valor ajustado (só se for válido)
        jTextFieldQtdProductsSelected.setText(String.valueOf(quantidade));
        
        
    }//GEN-LAST:event_jTextFieldQtdProductsSelectedKeyReleased

    private void jTextFieldBarCodeProductSelectKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBarCodeProductSelectKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String[] result = validateProduto(jTextFieldBarCodeProductSelect.getText());
            if (result != null) {
                if (AddProdutoToList(result[0], Integer.parseInt(result[1])) == true) {
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possivel adicionar Produto ao carrinho", "Atenção", JOptionPane.ERROR_MESSAGE);
                }
            }
            //            String barCode = jTextFieldBarCodeProductSelect.getText();
            //            Product pModel;
            //            pModel = productController.getFromBarCode(barCode);
            //            if (pModel.getDescription() == null) {
            //                JOptionPane.showMessageDialog(null, "Produto nao encontrado!");
            //            }
            //            System.out.println("clicado!!");
        }
    }//GEN-LAST:event_jTextFieldBarCodeProductSelectKeyPressed

    private void jTextFieldSearchProductsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchProductsKeyReleased
        // TODO add your handling code here:
        String txt = jTextFieldSearchProducts.getText();
        if (!txt.isEmpty()) {
            filterListProducts(txt);
        } else {
            listProduts(null);
        }
    }//GEN-LAST:event_jTextFieldSearchProductsKeyReleased

    private void jButtonPesquisarCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPesquisarCompanyActionPerformed
        // TODO add your handling code here:
        String nif = jTextFieldNifClient.getText();
        ClientDao cDao = new ClientDao();
        clientSelected = cDao.searchFromNif(nif);
        if (clientSelected.getName() != null) {
            jTextFieldNifClient.setText(clientSelected.getNif());
            jLabelNameClienteSelected.setText(clientSelected.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Cliente nao encontrado!");
        }
    }//GEN-LAST:event_jButtonPesquisarCompanyActionPerformed

    private void jButtonHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHelpActionPerformed
        // TODO add your handling code here:
        ajuda();
    }//GEN-LAST:event_jButtonHelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScreenPdv.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScreenPdv().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddProductInvoice;
    private javax.swing.JButton jButtonCloseShift;
    private javax.swing.JButton jButtonFinishInvoice;
    private javax.swing.JButton jButtonHelp;
    private javax.swing.JButton jButtonListClient;
    private javax.swing.JButton jButtonListSales;
    private javax.swing.JButton jButtonPainel;
    private javax.swing.JButton jButtonPainelListProducts;
    private javax.swing.JButton jButtonPesquisarCompany;
    private javax.swing.JButton jButtonRemoveAllProd;
    private javax.swing.JButton jButtonRemoveProd;
    private javax.swing.JComboBox<String> jComboBoxOptions;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDateTime;
    private javax.swing.JLabel jLabelNameClienteSelected;
    private javax.swing.JLabel jLabelNameCompany;
    private javax.swing.JLabel jLabelNameUserSeller;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelSelectedProduct;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableProducts;
    private javax.swing.JTable jTableProductsInvoice;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldBarCodeProductSelect;
    private javax.swing.JTextField jTextFieldNameProductSelected;
    private javax.swing.JTextField jTextFieldNifClient;
    private javax.swing.JTextField jTextFieldPriceProductSelect;
    private javax.swing.JTextField jTextFieldQtdProductsSelected;
    private javax.swing.JTextField jTextFieldSearchProducts;
    private javax.swing.JTextField jTextFieldTotalInvoice;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
