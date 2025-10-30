package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ShiftDao;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.helpers.ShiftSession;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.Util;
import com.okutonda.okudpdv.helpers.UtilDate;
import java.util.List;

/**
 * Controller responsável pela gestão dos turnos (Shift).
 *
 * Contém as regras de negócio, cálculos, atualizações de sessão e controle de
 * permissões de acesso.
 *
 * @author Hulquene
 */
public class ShiftController {

    private final ShiftDao dao;
    private final UserSession userSession = UserSession.getInstance();
    private final ShiftSession shiftSession = ShiftSession.getInstance();

    public ShiftController() {
        this.dao = new ShiftDao();
    }

    // ==========================================================
    // 🔹 REGRAS DE NEGÓCIO (abertura, encerramento, etc.)
    // ==========================================================
    /**
     * Abre um novo turno para o utilizador atual.
     */
    public boolean openShift(Shift shift) {
        if (shift == null) {
            return false;
        }

        shift.setHash(Util.generateRandomHash());
        shift.setCode(Util.generateRandomHash());
        shift.setStatus("open");
        shift.setUser(userSession.getUser());

        boolean created = dao.add(shift);
        if (created) {
            Shift novo = dao.findByHash(shift.getHash());
            shiftSession.setSeller(userSession.getUser());
            shiftSession.setShift(novo);
        }
        return created;
    }

    /**
     * Fecha o turno atual com cálculo automático do valor final.
     */
    public boolean closeShift(Shift shift) {
        if (shift == null) {
            return false;
        }

        double closingAmount = shift.getGrantedAmount() + shift.getIncurredAmount();
        shift.setClosingAmount(closingAmount);
        shift.setStatus("close");
        shift.setDateClose(UtilDate.getDateTimeNow());

        boolean ok = dao.updateClosingData(
                shift.getClosingAmount(),
                shift.getStatus(),
                shift.getDateClose(),
                shift.getId());

        if (ok) {
            shiftSession.setSeller(userSession.getUser());
            shiftSession.setShift(shift);
        }
        return ok;
    }

    /**
     * Atualiza o valor total incorrido no turno (ex: vendas realizadas).
     */
    public boolean updateIncurredAmount(double value, int shiftId) {
        Shift shift = dao.findById(shiftId);
        if (shift == null) {
            return false;
        }

        double newValue = shift.getIncurredAmount() + value;
        boolean ok = dao.updateIncurredAmount(newValue, shiftId);

        if (ok) {
            shift.setIncurredAmount(newValue);
            shiftSession.setShift(shift);
        }
        return ok;
    }

    /**
     * Carrega o turno ativo (aberto) do utilizador logado.
     */
    public void loadActiveShift() {
        Shift shift = dao.findLastOpenShiftByUser(userSession.getUser().getId());
        if (shift != null) {
            shiftSession.setSeller(userSession.getUser());
            shiftSession.setShift(shift);
        }
    }

    // ==========================================================
    // 🔹 OPERAÇÕES DE CONSULTA
    // ==========================================================
    public Shift findById(int id) {
        return dao.findById(id);
    }

    public Shift findByCode(String code) {
        return dao.findByCode(code);
    }

    public Shift findByHash(String hash) {
        return dao.findByHash(hash);
    }

    public List<Shift> findAll() {
//        if (isAdmin()) {
        return dao.findAll();
//        }
//        return dao.executeQuery("SELECT * FROM shift WHERE user_id=?",
//                rs -> dao.findById(rs.getInt("id")),
//                userSession.getUser().getId());
    }

    public List<Shift> filter(String text) {
        return dao.filter(text);
    }

    public boolean delete(int id) {
        return dao.delete(id);
    }

    // ==========================================================
    // 🔹 AUXILIARES
    // ==========================================================
    private boolean isAdmin() {
        String profile = userSession.getUser().getProfile();
        return profile.equalsIgnoreCase("admin") || profile.equalsIgnoreCase("manager");
    }
}

///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.ShiftDao;
//import com.okutonda.okudpdv.data.entities.Shift;
//import com.okutonda.okudpdv.utilities.ShiftSession;
//import com.okutonda.okudpdv.utilities.UserSession;
//import com.okutonda.okudpdv.utilities.Util;
//import com.okutonda.okudpdv.utilities.UtilDate;
//import java.util.List;
//
///**
// *
// * @author kenny
// */
//public class ShiftController {
//
//    ShiftDao dao;
//    UserSession session = UserSession.getInstance();
//    ShiftSession shiftSession = ShiftSession.getInstance();
////    ProductOrderDao prodOrderDao;
//
//    public ShiftController() {
//        this.dao = new ShiftDao();
//    }
//
//    public Shift getId(int id) {
//        return dao.searchFromId(id);
//    }
//
//    public Shift getCode(String code) {
//        return dao.getFromCode(code);
//    }
//
//    public Shift getHash(String hash) {
//        return dao.getFromHash(hash);
//    }
//
//    public void getShiftSession() {
//        Shift shift = dao.getLastShiftUser(session.getUser().getId());
//        if (shift != null) {
//            shiftSession.setSeller(session.getUser());
//            shiftSession.setShift(shift);
//        }
//    }
//
//    public List<Shift> filter(String txt) {
//        return dao.filter(txt);
//    }
//
//    public List<Shift> get(String where) {
//        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
//            return dao.list(where);
//        }
//        return dao.list("WHERE user_id =" + session.getUser().getId());
//    }
//
//    public Boolean add(Shift shift, int id) {
//        boolean status;
//        if (id == 0) {
//            String hash = Util.generateRandomHash();
//            shift.setHash(hash);
//            shift.setCode(Util.generateRandomHash());
//            shift.setStatus("open");
//            status = dao.add(shift);
//            if (status == true) {
//                shift = getHash(hash);
//            }
//        } else {
//            status = dao.edit(shift, id);
//            if (status == true) {
//                shift = getHash(shift.getHash());
//            }
//        }
//        if (shift != null) {
//            shiftSession.setSeller(session.getUser());
//            shiftSession.setShift(shift);
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean updateIncurredAmount(Double value, int id) {
//        boolean status;
//        Shift shift = this.getId(id);
//        Double currentValor = shift.getIncurredAmount() + value;
//        status = dao.updateIncurredAmount(currentValor, id);
//        if (status == true) {
//            shiftSession.setShift(shift);
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean closeShift(Shift closeShift) {
//        boolean status;
//        closeShift.setStatus("close");
//        closeShift.setDateClose(UtilDate.getDateTimeNow());
//        closeShift.setClosingAmount(closeShift.getIncurredAmount() + closeShift.getGrantedAmount());
//
//        status = dao.closeShift(closeShift);
//        if (status == true) {
//            shiftSession.setSeller(session.getUser());
//            shiftSession.setShift(closeShift);
//            return true;
//        }
//        return false;
//    }
//
//    public Boolean deleteId(int id) {
//        return dao.delete(id);
//    }
//
//    public Double CalculateValueClose(Shift shift) {
//        return null;
//    }
//}
