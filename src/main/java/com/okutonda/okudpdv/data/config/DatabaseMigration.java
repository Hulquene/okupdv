///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.okutonda.okudpdv.data.config;
//
///**
// *
// * @author hr
// */
//public class DatabaseMigration {
//
//    public static void checkAndApplyMigrations() {
//        try {
//            // Verificar se precisa de migração
//            if (!columnExists("users", "phone_verified")) {
//                System.out.println("🔄 Aplicando migração: adicionando phone_verified");
//                executeSQL("ALTER TABLE users ADD COLUMN phone_verified BOOLEAN DEFAULT FALSE");
//            }
//
//            if (!tableExists("product_categories")) {
//                System.out.println("🔄 Aplicando migração: criando product_categories");
//                executeSQL("CREATE TABLE product_categories (...)");
//            }
//
//        } catch (Exception e) {
//            System.err.println("❌ Erro na migração: " + e.getMessage());
//        }
//    }
//}
