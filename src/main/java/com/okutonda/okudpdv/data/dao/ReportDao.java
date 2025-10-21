/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.data.dao;

import com.okutonda.okudpdv.jdbc.ConnectionDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author kenny
 */
public class ReportDao {
    private final Connection conn;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ReportDao() {
        this.conn = ConnectionDatabase.getConnect();
    }
}
