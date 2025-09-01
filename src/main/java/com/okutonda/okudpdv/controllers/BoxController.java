/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.dao.BoxDao;
import com.okutonda.okudpdv.models.Box;
import com.okutonda.okudpdv.utilities.UserSession;
import java.util.List;

/**
 *
 * @author kenny
 */
public class BoxController {

    BoxDao dao;
    UserSession session = UserSession.getInstance();
//    ShiftSession shiftSession = ShiftSession.getInstance();
//    ProductOrderDao prodOrderDao;

    public BoxController() {
        this.dao = new BoxDao();
    }

    public Box getId(int id) {
        return dao.getId(id);
    }

//    public Box getCode(String code) {
//        return dao.getFromCode(code);
//    }
//    public Shift getHash(String hash) {
//        return dao.getFromHash(hash);
//    }
//    public void getShiftSession() {
//        Shift shift = dao.getLastShiftUser(session.getUser().getId());
//        if (shift != null) {
//            shiftSession.setSeller(session.getUser());
//            shiftSession.setShift(shift);
//        }
//    }
//    public List<Box> filter(String txt) {
//        return dao.filter(txt);
//    }
    public List<Box> get(String where) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.list(where);
        }
        return dao.list("WHERE user_id =" + session.getUser().getId());
    }

    public Boolean add(Box box, int id) {
        boolean status = false;
        if (id == 0) {
            status = dao.add(box);
        } else {
            status = dao.edit(box, id);
        }
        return status;
    }

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
    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

//    public Double CalculateValueClose(Shift shift) {
//        return null;
//    }
}
