package com.okutonda.okudpdv.controllers;

// ❌ REMOVER ESTE IMPORT
// import com.okutonda.okudpdv.jdbc.ConnectionDatabase;

// ✅ ADICIONAR ESTES IMPORTS
import com.okutonda.okudpdv.data.config.HibernateUtil;
import com.okutonda.okudpdv.data.dao.OptionsDao;
import com.okutonda.okudpdv.data.entities.Options;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Controlador de Instalação do Sistema
 * 
 * @author kenny
 */
public class Install {
    
    private final OptionController optionController;
    
    public Install() {
        this.optionController = new OptionController();
    }
    
    /**
     * Executa a instalação inicial do sistema
     */
    public boolean runInitialInstallation() {
        Session session = HibernateUtil.getCurrentSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // 🔹 Configurações básicas da empresa
            saveDefaultOption("company_name", "Minha Empresa", "active");
            saveDefaultOption("company_nif", "999999999", "active");
            saveDefaultOption("company_address", "Endereço da Empresa", "active");
            saveDefaultOption("company_city", "Cidade", "active");
            saveDefaultOption("company_postal_code", "0000-000", "active");
            saveDefaultOption("company_country", "Angola", "active");
            saveDefaultOption("company_phone", "+244 900 000 000", "active");
            saveDefaultOption("company_email", "empresa@exemplo.com", "active");
            saveDefaultOption("company_website", "www.empresa.com", "active");
            
            // 🔹 Configurações do sistema
            saveDefaultOption("currency_code", "AOA", "active");
            saveDefaultOption("tax_accounting_basis", "F", "active"); // F=Faturação
            saveDefaultOption("tax_entity", "AO", "active");
            saveDefaultOption("product_id", "Okudpdv/Okutonda", "active");
            saveDefaultOption("product_version", "1.0.0", "active");
            saveDefaultOption("file_type", "N", "active"); // N=Normal
            
            // 🔹 Marcar instalação como completa
            saveDefaultOption("install_complete", "true", "active");
            
            tx.commit();
            System.out.println("✅ Instalação concluída com sucesso!");
            return true;
            
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println("❌ Erro durante a instalação: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Salva uma opção padrão apenas se não existir
     */
    private void saveDefaultOption(String name, String value, String status) {
        // Verifica se a opção já existe
        String existingValue = optionController.getOptionValue(name);
        if (existingValue.isEmpty()) {
            // Só salva se não existir
            optionController.saveOption(name, value, status);
            System.out.println("🔹 Opção criada: " + name);
        } else {
            System.out.println("🔹 Opção já existe: " + name);
        }
    }
    
    /**
     * Verifica se o sistema já foi instalado
     */
    public boolean isSystemInstalled() {
        return optionController.isInstallationComplete();
    }
    
    /**
     * Executa migrações ou atualizações do sistema
     */
    public boolean runSystemUpdates() {
        // Aqui você pode adicionar lógica para migrações futuras
        System.out.println("🔹 Verificando atualizações do sistema...");
        
        // Exemplo: Adicionar novas opções em versões futuras
        saveDefaultOption("software_validation_number", "", "active");
        saveDefaultOption("company_country_code", "AO", "active");
        saveDefaultOption("company_province", "", "active");
        saveDefaultOption("company_state", "", "active");
        saveDefaultOption("company_address_detail", "", "active");
        
        return true;
    }
}