/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ShiftDao;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.utilities.ShiftSession;
import com.okutonda.okudpdv.utilities.UserSession;
import com.okutonda.okudpdv.utilities.Util;
import com.okutonda.okudpdv.utilities.UtilDate;
import java.util.List;

/**
 *
 * @author kenny
 */
public class ShiftController {

    ShiftDao dao;
    UserSession session = UserSession.getInstance();
    ShiftSession shiftSession = ShiftSession.getInstance();
//    ProductOrderDao prodOrderDao;

    public ShiftController() {
        this.dao = new ShiftDao();
    }

    public Shift getId(int id) {
        return dao.searchFromId(id);
    }

    public Shift getCode(String code) {
        return dao.getFromCode(code);
    }

    public Shift getHash(String hash) {
        return dao.getFromHash(hash);
    }

    public void getShiftSession() {
        Shift shift = dao.getLastShiftUser(session.getUser().getId());
        if (shift != null) {
            shiftSession.setSeller(session.getUser());
            shiftSession.setShift(shift);
        }
    }

    public List<Shift> filter(String txt) {
        return dao.filter(txt);
    }

    public List<Shift> get(String where) {
        if (session.getUser().getProfile().equals("admin") || session.getUser().getProfile().equals("manager")) {
            return dao.list(where);
        }
        return dao.list("WHERE user_id =" + session.getUser().getId());
    }

    public Boolean add(Shift shift, int id) {
        boolean status;
        if (id == 0) {
            String hash = Util.generateRandomHash();
            shift.setHash(hash);
            shift.setCode(Util.generateRandomHash());
            shift.setStatus("open");
            status = dao.add(shift);
            if (status == true) {
                shift = getHash(hash);
            }
        } else {
            status = dao.edit(shift, id);
            if (status == true) {
                shift = getHash(shift.getHash());
            }
        }
        if (shift != null) {
            shiftSession.setSeller(session.getUser());
            shiftSession.setShift(shift);
            return true;
        }
        return false;
    }

    public Boolean updateIncurredAmount(Double value, int id) {
        boolean status;
        Shift shift = this.getId(id);
        Double currentValor = shift.getIncurredAmount() + value;
        status = dao.updateIncurredAmount(currentValor, id);
        if (status == true) {
            shiftSession.setShift(shift);
            return true;
        }
        return false;
    }

    public Boolean closeShift(Shift closeShift) {
        boolean status;
        closeShift.setStatus("close");
        closeShift.setDateClose(UtilDate.getDateTimeNow());
        closeShift.setClosingAmount(closeShift.getIncurredAmount() + closeShift.getGrantedAmount());

        status = dao.closeShift(closeShift);
        if (status == true) {
            shiftSession.setSeller(session.getUser());
            shiftSession.setShift(closeShift);
            return true;
        }
        return false;
    }

    public Boolean deleteId(int id) {
        return dao.delete(id);
    }

    public Double CalculateValueClose(Shift shift) {
        return null;
    }
}
