//package com.okutonda.okudpdv.controllers;
//
//import com.okutonda.okudpdv.data.dao.BoxDao;
//import com.okutonda.okudpdv.data.entities.Box;
//import com.okutonda.okudpdv.helpers.UserSession;
//import java.util.List;
//
///**
// * Controller responsável pela lógica de negócio de caixas (Box).
// *
// * Garante regras, permissões e ações específicas, enquanto o DAO trata apenas
// * da persistência.
// *
// * @author Hulquene
// */
//public class BoxController {
//
//    private final BoxDao dao;
//    private final UserSession session = UserSession.getInstance();
//
//    public BoxController() {
//        this.dao = new BoxDao();
//    }
//
//    // ==========================================================
//    // 🔹 CRUD / Lógica de Negócio
//    // ==========================================================
//    public boolean save(Box box) {
//        if (box == null) {
//            return false;
//        }
//        if (box.getId() == 0) {
//            return dao.add(box);
//        } else {
//            return dao.update(box);
//        }
//    }
//
//    public boolean delete(int id) {
//        if (id <= 0) {
//            return false;
//        }
//        return dao.delete(id);
//    }
//
//    public Box findById(int id) {
//        return dao.findById(id);
//    }
//
//    public List<Box> findAll() {
//        if (isAdmin()) {
//            return dao.findAll();
//        }
//        // Em sistemas multiusuário, poderias filtrar:
//        // return dao.executeQuery("SELECT * FROM box WHERE user_id=?", this::map, session.getUser().getId());
//        return dao.findAll();
//    }
//
//    public List<Box> filter(String txt) {
//        return dao.filter(txt);
//    }
//
//    public Box getDefaultBox() {
//        return dao.getDefaultBox();
//    }
//
//    // ==========================================================
//    // 🔹 Lógica auxiliar
//    // ==========================================================
//    private boolean isAdmin() {
//        String profile = session.getUser().getProfile();
//        return profile.equalsIgnoreCase("admin") || profile.equalsIgnoreCase("manager");
//    }
//}
//
/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package com.okutonda.okudpdv.controllers;
////
////import com.okutonda.okudpdv.data.dao.BoxDao;
////import com.okutonda.okudpdv.data.entities.Box;
////import com.okutonda.okudpdv.helpers.UserSession;
////import java.util.List;
////
/////**
//// *
//// * @author kenny
//// */
////public class BoxController {
////
////    BoxDao dao;
////    UserSession session = UserSession.getInstance();
//////    ShiftSession shiftSession = ShiftSession.getInstance();
//////    ProductOrderDao prodOrderDao;
////
////    public BoxController() {
////        this.dao = new BoxDao();
////    }
////
////    public Box getId(int id) {
////        return dao.getId(id);
////    }
////
//////    public Box getCode(String code) {
//////        return dao.getFromCode(code);
//////    }
//////    public Shift getHash(String hash) {
//////        return dao.getFromHash(hash);
//////    }
//////    public void getShiftSession() {
//////        Shift shift = dao.getLastShiftUser(session.getUser().getId());
//////        if (shift != null) {
//////            shiftSession.setSeller(session.getUser());
//////            shiftSession.setShift(shift);
//////        }
//////    }
//////    public List<Box> filter(String txt) {
//////        return dao.filter(txt);
//////    }
////    public List<Box> get(String where) {
////        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
////            return dao.list(where);
////        }
////        return dao.list("WHERE user_id =" + session.getUser().getId());
////    }
////
////    public Boolean add(Box box, int id) {
////        boolean status = false;
////        if (id == 0) {
////            status = dao.add(box);
////        } else {
////            status = dao.edit(box, id);
////        }
////        return status;
////    }
////
//////    public Boolean updateIncurredAmount(Double value, int id) {
//////        boolean status;
//////        Shift shift = this.getId(id);
//////        Double currentValor = shift.getIncurredAmount() + value;
//////        status = dao.updateIncurredAmount(currentValor, id);
//////        if (status == true) {
//////            shiftSession.setShift(shift);
//////            return true;
//////        }
//////        return false;
//////    }
//////    public Boolean closeShift(Shift closeShift) {
//////        boolean status;
//////        closeShift.setStatus("close");
//////        closeShift.setDateClose(UtilDate.getDateTimeNow());
//////        closeShift.setClosingAmount(closeShift.getIncurredAmount() + closeShift.getGrantedAmount());
//////
//////        status = dao.closeShift(closeShift);
//////        if (status == true) {
//////            shiftSession.setSeller(session.getUser());
//////            shiftSession.setShift(closeShift);
//////            return true;
//////        }
//////        return false;
//////    }
////    public Boolean deleteId(int id) {
////        return dao.delete(id);
////    }
////
//////    public Double CalculateValueClose(Shift shift) {
//////        return null;
//////    }
////}
