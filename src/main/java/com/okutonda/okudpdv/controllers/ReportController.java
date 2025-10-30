package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ReportDao;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.UtillFiles;
import java.io.IOException;
import java.util.*;

/**
 * Controller responsável pela geração de relatórios.
 * 
 * Camada intermediária entre a UI e o DAO.
 * Contém apenas lógica de negócio e exportação.
 * 
 * @author Hulquene
 */
public class ReportController {

    private final ReportDao dao;
    private final UserSession session = UserSession.getInstance();

    public ReportController() {
        this.dao = new ReportDao();
    }

    // ==========================================================
    // 🔹 Relatórios de Vendas
    // ==========================================================
    public List<Map<String, Object>> getSalesByProduct(String from, String to) {
        return dao.getSalesByProduct(from, to);
    }

    public List<Map<String, Object>> getSalesBySeller(String from, String to) {
        return dao.getSalesBySeller(from, to);
    }

    // ==========================================================
    // 🔹 Relatórios de Turnos
    // ==========================================================
    public Map<String, Object> getShiftSummary(int shiftId) {
        return dao.getShiftSummary(shiftId);
    }

    public List<Map<String, Object>> listClosedShifts(String from, String to) {
        return dao.listClosedShifts(from, to);
    }

    // ==========================================================
    // 🔹 Exportação para Excel
    // ==========================================================
    public boolean exportToExcel(List<List<String>> data, String filePath) {
        try {
            UtillFiles.convertArrayListToExcel(data, filePath);
            System.out.println("✅ Arquivo Excel criado com sucesso em: " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("[Export] Erro ao gerar Excel: " + e.getMessage());
            return false;
        }
    }

    public boolean exportSalesByProductExcel(String from, String to, String filePath) {
        List<Map<String, Object>> result = dao.getSalesByProduct(from, to);
        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("Produto", "Quantidade", "Total"));
        for (Map<String, Object> r : result) {
            rows.add(List.of(
                    String.valueOf(r.get("produto")),
                    String.valueOf(r.get("quantidade")),
                    String.valueOf(r.get("total"))
            ));
        }
        return exportToExcel(rows, filePath);
    }
}
