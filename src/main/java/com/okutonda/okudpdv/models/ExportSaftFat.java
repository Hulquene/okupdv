/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author hr
 */
public class ExportSaftFat {
    // Status convenientes

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    private long id;
    private LocalDate periodStart;     // saft_exports.period_start (DATE)
    private LocalDate periodEnd;       // saft_exports.period_end (DATE)
    private String filePath;           // saft_exports.file_path
    private String status;             // saft_exports.status
    private String notes;              // saft_exports.notes
    private LocalDateTime createdAt;   // saft_exports.created_at (TIMESTAMP)
    private User user;      // saft_exports.exported_by (FK opcional para User.id)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public String getExportedByName() {
//        return exportedByName;
//    }
//    public void setExportedByName(String exportedByName) {
//        this.exportedByName = exportedByName;
//    }
    // Helpers opcionais
    public String getFileName() {
        if (filePath == null) {
            return "";
        }
        int i = filePath.lastIndexOf(java.io.File.separator);
        return i >= 0 ? filePath.substring(i + 1) : filePath;
    }

    public String getPeriodLabel() {
        String s = (periodStart == null ? "" : periodStart.toString());
        String e = (periodEnd == null ? "" : periodEnd.toString());
        return s + " a " + e;
    }

    @Override
    public String toString() {
        return "ExportSaftFat{"
                + "id=" + id
                + ", periodStart=" + periodStart
                + ", periodEnd=" + periodEnd
                + ", filePath='" + filePath + '\''
                + ", status='" + status + '\''
                + ", notes='" + notes + '\''
                + ", createdAt=" + createdAt
                + 
                '}';
    }
}
