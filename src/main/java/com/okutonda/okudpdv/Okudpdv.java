/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.okutonda.okudpdv;

import com.formdev.flatlaf.FlatLightLaf;
import com.okutonda.okudpdv.controllers.SupplierController;
import com.okutonda.okudpdv.data.config.HibernateConfig;
import com.okutonda.okudpdv.data.dao.ClientDao;
import com.okutonda.okudpdv.views.install.ScreenInstall;
import com.okutonda.okudpdv.views.login.ScreenLogin;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.okutonda.okudpdv.data.dao.GroupsProductDao;
import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.dao.TaxeDao;
import com.okutonda.okudpdv.data.dao.TaxeReasonDao;
import com.okutonda.okudpdv.data.dao.UserDao;
import com.okutonda.okudpdv.data.entities.Clients;
import com.okutonda.okudpdv.data.entities.Countries;
import com.okutonda.okudpdv.data.entities.GroupsProduct;
import com.okutonda.okudpdv.data.entities.Options;
import com.okutonda.okudpdv.data.entities.ReasonTaxes;
import com.okutonda.okudpdv.data.entities.Supplier;
import com.okutonda.okudpdv.data.entities.Taxes;
import com.okutonda.okudpdv.data.entities.User;
import java.time.LocalDateTime;
import java.util.Optional;

public class Okudpdv {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        // 1) Look & Feel
        try {
            FlatLightLaf.setup();
            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
        } catch (Exception ex) {
            System.err.println("Erro no Look & Feel: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // 🔥 SIMPLIFICAÇÃO: Testa apenas se Hibernate funciona
                boolean hibernateOk = testHibernateConnection();

                if (hibernateOk) {
                    System.out.println("✅ Sistema pronto - abrindo login");

                    // 🔥 INICIALIZAÇÃO AUTOMÁTICA: Sempre tenta carregar dados essenciais
                    initializeEssentialData();

                    new ScreenLogin().setVisible(true);
                } else {
                    // Se Hibernate falhar, tenta instalação
                    JOptionPane.showMessageDialog(null,
                            "Não foi possível conectar ao banco de dados.\nVerifique se o MySQL está rodando.",
                            "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                    new ScreenInstall().setVisible(true);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro inesperado: " + e.getMessage(),
                        "Erro Fatal", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            HibernateConfig.shutdown();
        }));
    }

    private static boolean testHibernateConnection() {
        try {
            return HibernateConfig.testConnection();
        } catch (Exception e) {
            System.err.println("❌ Hibernate connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🔥 INICIALIZAÇÃO AUTOMÁTICA: Sempre tenta carregar/criar dados essenciais
     */
    private static boolean initializeEssentialData() {
        try {
            System.out.println("🔄 Inicializando dados essenciais do sistema...");

            // 1. Carregar cache de países
            Countries.loadCache();
            System.out.println("✅ Países carregados");

            // 2. Inicializar outras tabelas essenciais
            boolean usersInitialized = initializeUsers();
            boolean taxesInitialized = initializeTaxes();

            boolean clientInitialized = initializeDefaultClients();
            boolean suppliersInitialized = initializeDefaultSuppliers();

            boolean reasonTaxesInitialized = initializeReasonTaxes(); // 🔥 NOVO
            boolean groupsInitialized = initializeProductGroups();
            boolean optionsInitialized = initializeSystemOptions();

            System.out.println("📊 Resumo da inicialização:");
            System.out.println("   - Users: " + (usersInitialized ? "✅" : "⚠️"));
            System.out.println("   - Taxes: " + (taxesInitialized ? "✅" : "⚠️"));
            System.out.println("   - Reason Taxes: " + (reasonTaxesInitialized ? "✅" : "⚠️")); // 🔥 NOVO
            System.out.println("   - Product Groups: " + (groupsInitialized ? "✅" : "⚠️"));
            System.out.println("   - System Options: " + (optionsInitialized ? "✅" : "⚠️"));
            System.out.println("   - System Clients: " + (clientInitialized ? "✅" : "⚠️"));
            System.out.println("   - System Suppliers: " + (suppliersInitialized ? "✅" : "⚠️"));

            // Considera sucesso se os dados mais críticos foram inicializados
            return usersInitialized && taxesInitialized && optionsInitialized;

        } catch (Exception e) {
            System.err.println("❌ Erro durante inicialização automática: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean initializeUsers() {
        try {
            UserDao userDao = new UserDao();
            var users = userDao.findAll();

            if (users.isEmpty()) {
                System.out.println("👤 Criando usuário administrador...");
                User admin = new User();
                admin.setName("Administrador");
                admin.setEmail("admin@empresa.com");
                admin.setPassword("admin123");
                admin.setProfile("ADMIN");
                admin.setStatus(1);
                admin.setCreatedAt(LocalDateTime.now());
                userDao.save(admin);

                System.out.println("✅ Usuário administrador criado com sucesso");
                return true;
            }

            System.out.println("✅ Usuários já existem: " + users.size());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar usuário admin: " + e.getMessage());
            return false;
        }
    }

    private static boolean initializeDefaultClients() {
        try {
            ClientDao clientDao = new ClientDao();

            // 🔹 CLIENTE CONSUMIDOR FINAL
            Optional<Clients> consumidorFinal = clientDao.findByName("Consumidor Final");
            if (consumidorFinal.isEmpty()) {
                System.out.println("👤 Criando cliente Consumidor Final...");

                Clients cliente = new Clients();
                cliente.setName("Consumidor Final");
//                cliente.setCompany("Consumidor Final");
                cliente.setNif("999999999"); // NIF genérico para consumidor final
                cliente.setAddress("Não especificado");
                cliente.setCity("Não especificado");
                cliente.setPhone("Não especificado");
                cliente.setEmail("consumidor@final.com");
                cliente.setIsDefault(1); // 🔥 Cliente padrão
                cliente.setStatus(1);

                clientDao.save(cliente);
                System.out.println("✅ Cliente 'Consumidor Final' criado como padrão");
            } else {
                System.out.println("✅ Cliente 'Consumidor Final' já existe");

                // Garante que é o cliente padrão
                if (consumidorFinal.get().getIsDefault() != 1) {
                    clientDao.setDefaultClient(consumidorFinal.get().getId());
                    System.out.println("✅ Cliente 'Consumidor Final' definido como padrão");
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar cliente padrão: " + e.getMessage());
            return false;
        }
    }

    private static boolean initializeDefaultSuppliers() {
        try {
            SupplierController supplierController = new SupplierController();

            // 🔹 FORNECEDOR PADRÃO - SEM FORNECEDOR
            Supplier semFornecedor = supplierController.getByName("Sem Fornecedor");
            if (semFornecedor == null) {
                System.out.println("🏭 Criando fornecedor 'Sem Fornecedor'...");

                Supplier supplier = new Supplier();
                supplier.setName("Sem Fornecedor");
                supplier.setNif("000000000"); // NIF genérico
                supplier.setAddress("Não especificado");
                supplier.setCity("Não especificado");
                supplier.setPhone("Não especificado");
                supplier.setEmail("sem@fornecedor.com");
                supplier.setStatus(1);
                supplier.setIsDefault(1); // 🔥 Fornecedor padrão

                // Valida antes de salvar
                if (supplierController.validarSupplier(supplier)) {
                    supplierController.save(supplier);
                    System.out.println("✅ Fornecedor 'Sem Fornecedor' criado como padrão");
                } else {
                    System.err.println("❌ Validação falhou para fornecedor padrão");
                    return false;
                }
            } else {
                System.out.println("✅ Fornecedor 'Sem Fornecedor' já existe");

                // Garante que é o fornecedor padrão
                if (semFornecedor.getIsDefault() != 1) {
                    semFornecedor.setIsDefault(1);
                    supplierController.save(semFornecedor);
                    System.out.println("✅ Fornecedor 'Sem Fornecedor' definido como padrão");
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar fornecedor padrão: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean initializeTaxes() {
        try {
            // 🔥 CORREÇÃO: Use TaxDao (singular) se for o nome correto
            TaxeDao taxDao = new TaxeDao(); // Ou TaxesDao, dependendo da sua classe
            var taxes = taxDao.findAll();

            if (taxes.isEmpty()) {
                System.out.println("💰 Criando impostos padrão...");

                // IVA 7% (mais comum em Angola atualmente)
                Taxes iva7 = new Taxes();
                iva7.setName("IVA 7%");
                iva7.setCode("IVA7");
                iva7.setPercentage(new java.math.BigDecimal("7.00"));
                iva7.setIsDefault(1); // 🔥 Padrão
                taxDao.save(iva7);

                // IVA 14% 
                Taxes iva14 = new Taxes();
                iva14.setName("IVA 14%");
                iva14.setCode("IVA14");
                iva14.setPercentage(new java.math.BigDecimal("14.00"));
                iva14.setIsDefault(0);
                taxDao.save(iva14);

                // Isento
                Taxes isento = new Taxes();
                isento.setName("Isento");
                isento.setCode("ISENTO");
                isento.setPercentage(java.math.BigDecimal.ZERO);
                isento.setIsDefault(0);
                taxDao.save(isento);

                System.out.println("✅ 3 impostos padrão criados");
                return true;
            }
            System.out.println("✅ Impostos já existem: " + taxes.size());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar impostos: " + e.getMessage());
            return false;
        }
    }

    private static boolean initializeProductGroups() {
        try {
            // Usar seu GroupProductDao existente
            GroupsProductDao groupDao = new GroupsProductDao();
            var groups = groupDao.findAll();

            if (groups.isEmpty()) {
                System.out.println("📦 Criando grupos de produtos padrão...");

                GroupsProduct geral = new GroupsProduct();
                geral.setName("Geral");
                geral.setCode("GERAL");
                groupDao.save(geral);

                System.out.println("✅ Grupos de produtos criados");
                return true;
            }
            System.out.println("✅ Grupos de produtos já existem: " + groups.size());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar grupos de produtos: " + e.getMessage());
            return false;
        }
    }

    private static boolean initializeReasonTaxes() {
        try {
            TaxeReasonDao reasonTaxDao = new TaxeReasonDao();
            var reasonTaxes = reasonTaxDao.findAll();

            if (reasonTaxes.isEmpty()) {
                System.out.println("📋 Criando motivos fiscais padrão de Angola...");

                // 🔹 Dados dos motivos fiscais baseados na sua tabela
                String[][] reasonTaxData = {
                    // código, motivo, padrão, descrição
                    {"M00", "Regime transitório", "SAF-T", "Regime transitório"},
                    {"M02", "Transmissão de bens e serviço não sujeita", "SAF-T", "Transmissão de bens e serviço não sujeita"},
                    {"M04", "IVA – Regime de não sujeição", "SAF-T", "IVA – Regime de não sujeição"},
                    {"M10", "Isento nos termos da alínea b) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Transmissão de bens alimentares"},
                    {"M11", "Isento nos termos da alínea b) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Transmissões de medicamentos"},
                    {"M12", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Cadeiras de rodas e veículos especiais"},
                    {"M13", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Livros em formato digital ou físico"},
                    {"M14", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Locação de bens imóveis habitacionais"},
                    {"M15", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Operações sujeitas ao imposto de SISA"},
                    {"M16", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Exploração de jogos de fortuna ou azar"},
                    {"M17", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Transporte colectivo de passageiros"},
                    {"M18", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Operações de intermediação financeira"},
                    {"M19", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Seguro de saúde e prestação de seguros"},
                    {"M20", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Transmissões de produtos petrolíferos"},
                    {"M21", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Serviços de ensino"},
                    {"M22", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Prestações de serviço médico sanitário"},
                    {"M23", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Transporte de doentes em ambulâncias"},
                    {"M24", "Isento nos termos da alínea c) do nº1 do artigo 12", "CIVA", "Artigo 12.º do CIVA - Equipamentos médicos"},
                    {"M30", "Isento nos termos da alínea h) do artigo 15.º do CIVA", "CIVA", "Artigo 15.º do CIVA - Transmissões de bens expedidos ou transportados"},
                    {"M31", "Isento nos termos da alínea h) do artigo 15.º do CIVA", "CIVA", "Artigo 15.º do CIVA - Transmissões de bens de abastecimento"},
                    {"NOR", "Normal", "SAF-T", "Regime normal de IVA"},
                    {"RET", "Retenção na fonte", "SAF-T", "Sujeito a retenção na fonte"},
                    {"ISE", "Isento", "SAF-T", "Isento de IVA"},
                    {"OUT", "Outro", "SAF-T", "Outro motivo fiscal"}
                };

                int createdCount = 0;
                for (String[] data : reasonTaxData) {
                    try {
                        // Verificar se já existe pelo código
                        var existing = reasonTaxDao.findByCode(data[0]);
                        if (existing.isEmpty()) {
                            ReasonTaxes reasonTax = new ReasonTaxes();
                            reasonTax.setCode(data[0]);
                            reasonTax.setReason(data[1]);
                            reasonTax.setStandard(data[2]);
                            reasonTax.setDescription(data[3]);
                            reasonTaxDao.save(reasonTax);
                            createdCount++;
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️  Erro ao criar motivo fiscal " + data[0] + ": " + e.getMessage());
                    }
                }

                System.out.println("✅ " + createdCount + " motivos fiscais criados");
                return true;
            }

            System.out.println("✅ Motivos fiscais já existem: " + reasonTaxes.size());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar motivos fiscais: " + e.getMessage());
            return false;
        }
    }

    private static boolean initializeSystemOptions() {
        try {
            // Usar seu OptionDao existente
            OptionsDao optionDao = new OptionsDao();

            // Configurações essenciais
            String[][] essentialOptions = {
                {"companyName", "ACTIVE", "Minha Empresa"},
                {"companyNif", "ACTIVE", "000000000"},
                {"companyAddress", "ACTIVE", "Endereço da empresa"},
                {"companyPhone", "ACTIVE", "+244 900 000 000"},
                {"companyEmail", "ACTIVE", "empresa@email.com"},
                {"systemCurrency", "ACTIVE", "AOA"}
            };

            int created = 0;
            for (String[] optionData : essentialOptions) {
                var existing = optionDao.findByName(optionData[0]);
                if (existing.isEmpty()) {
                    Options option = new Options();
                    option.setName(optionData[0]);
                    option.setStatus(optionData[1]);
                    option.setValue(optionData[2]);
                    optionDao.save(option);
                    created++;
                }
            }

            if (created > 0) {
                System.out.println("✅ " + created + " opções do sistema criadas");
            } else {
                System.out.println("✅ Opções do sistema já existem");
            }
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar opções do sistema: " + e.getMessage());
            return false;
        }
    }
}

///**
// *
// * @author kenny
// */
//public class Okudpdv {
//
//    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        Countries.loadCache();
//        // 1) Look & Feel (FlatLaf)
//        try {
//            FlatLightLaf.setup();
//        } catch (Exception ex) {
//            System.err.println("Falhou inicializar o Look & Feel FlatLaf: " + ex.getMessage());
//        }
//
//        // 2) Aplicar tema global
//        try {
//            com.okutonda.okudpdv.ui.TemaLookAndFeel.aplicarUIManagerBasico();
//        } catch (Exception ex) {
//            System.err.println("Falhou aplicar TemaLookAndFeel: " + ex.getMessage());
//        }
//
//        // Garante que a UI Swing inicia na thread de interface
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // 🔍 Diagnóstico inicial do banco (SEU CÓDIGO ATUAL)
//                DatabaseBootProbe.AppState state = DatabaseBootProbe.detect();
//                System.out.println("DEBUG → Estado do BD: " + state);
//
//                switch (state) {
//                    case DB_UNREACHABLE -> {
//                        JOptionPane.showMessageDialog(null, "❌ Não foi possível comunicar com o servidor de base de dados.", "Base de dados inativa", JOptionPane.ERROR_MESSAGE);
//                        new ScreenLogin().setVisible(true);
//                    }
//
//                    case DB_NOT_FOUND, SCHEMA_MISSING, INSTALL_INCOMPLETE -> {
//                        JOptionPane.showMessageDialog(null, "⚙️ O sistema precisa ser instalado ou atualizado.", "Instalação necessária", JOptionPane.INFORMATION_MESSAGE);
//                        new ScreenInstall().setVisible(true);
//                    }
//
//                    case READY -> {
//                        // 🔥 NOVA VERIFICAÇÃO: Testar Hibernate também
//                        boolean hibernateOk = testHibernateConnection();
//
//                        if (hibernateOk) {
//                            System.out.println("✅ [Hibernate] Conexão validada com sucesso!");
//                            new ScreenLogin().setVisible(true);
//                        } else {
//                            JOptionPane.showMessageDialog(null,
//                                    "Hibernate não conseguiu conectar. Usando fallback para JDBC.",
//                                    "Aviso", JOptionPane.WARNING_MESSAGE);
//                            // Fallback para seu sistema atual
//                            try (var conn = DatabaseProvider.getConnection()) {
//                                new ScreenLogin().setVisible(true);
//                            } catch (Exception e) {
//                                JOptionPane.showMessageDialog(null, "Erro ao conectar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//                                System.exit(1);
//                            }
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), "Erro Fatal", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace();
//                System.exit(1);
//            }
//        });
//
//        // Shutdown hook para fechar Hibernate adequadamente
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("🔄 Encerrando aplicação...");
//            HibernateConfig.shutdown();
//        }));
//    }
//
//    private static boolean testHibernateConnection() {
//        try {
//            return HibernateConfig.testConnection();
//        } catch (Exception e) {
//            System.err.println("❌ Hibernate connection test failed: " + e.getMessage());
//            return false;
//        }
//    }
//}
