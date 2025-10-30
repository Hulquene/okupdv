/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author kenny
 */
public class UtilEvent {

    public static void formatDateNow(Date now) {
        // Defina a data e hora para disparar o evento
        LocalDateTime eventDateTime = LocalDateTime.of(2024, 7, 31, 10, 0);

        // Converta LocalDateTime para Date
        Date eventDate = Date.from(eventDateTime.atZone(ZoneId.from(eventDateTime).systemDefault()).toInstant());

        // Calcule o tempo de atraso até a data do evento
        long delay = eventDate.getTime() - System.currentTimeMillis();

        // Crie um ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Defina a tarefa a ser executada
        Runnable eventTask = new Runnable() {
            @Override
            public void run() {
                System.out.println("Evento disparado em: " + LocalDateTime.now());
                // Coloque aqui o código que deseja executar no evento
            }
        };

        // Agende a tarefa para ser executada após o atraso calculado
        scheduler.schedule(eventTask, delay, TimeUnit.MILLISECONDS);

        System.out.println("Evento agendado para: " + eventDateTime);
    }
}
