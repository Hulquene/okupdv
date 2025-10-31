package com.okutonda.okudpdv.controllers;

import com.okutonda.okudpdv.data.dao.ShiftDao;
import com.okutonda.okudpdv.data.entities.Shift;
import com.okutonda.okudpdv.data.entities.User;
import com.okutonda.okudpdv.helpers.UserSession;
import com.okutonda.okudpdv.helpers.ShiftSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 * Controller para gestão de turnos (shifts)
 */
public class ShiftController {

    private final ShiftDao dao;
    private final UserSession userSession = UserSession.getInstance();
    private final ShiftSession shiftSession = ShiftSession.getInstance();

    public ShiftController() {
        this.dao = new ShiftDao();
        // 🔹 Verifica se já existe turno aberto ao inicializar
        verificarTurnoAbertoAoIniciar();
    }

    // ==========================================================
    // 🔹 MÉTODOS DE ATUALIZAÇÃO DA SESSÃO
    // ==========================================================
    /**
     * Verifica se o usuário tem turno aberto ao inicializar o controller
     */
    private void verificarTurnoAbertoAoIniciar() {
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado != null) {
            Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());
            if (turnoAberto.isPresent()) {
                atualizarSessaoTurno(turnoAberto.get(), usuarioLogado);
            } else {
                limparSessaoTurno();
            }
        }
    }

    /**
     * Atualiza a sessão com o turno e usuário atual
     */
    private void atualizarSessaoTurno(Shift turno, User usuario) {
        shiftSession.setShift(turno);
        shiftSession.setSeller(usuario);
        System.out.println("🔹 Sessão atualizada - Turno: " + turno.getCode() + ", Vendedor: " + usuario.getName());
    }

    /**
     * Limpa a sessão do turno
     */
    private void limparSessaoTurno() {
        shiftSession.clearSession();
        System.out.println("🔹 Sessão do turno limpa");
    }

    // ==========================================================
    // 🔹 OPERAÇÕES PRINCIPAIS (ATUALIZADAS)
    // ==========================================================
    /**
     * Abre um novo turno para o usuário logado
     */
    public Shift abrirTurno(BigDecimal valorAbertura) {
        try {
            User usuarioLogado = userSession.getUser();
            if (usuarioLogado == null) {
                throw new IllegalStateException("Nenhum usuário logado");
            }

            // Verifica se já existe turno aberto para este usuário
            Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());
            if (turnoAberto.isPresent()) {
                throw new IllegalStateException("Já existe um turno aberto para este usuário: " + turnoAberto.get().getCode());
            }

            // Cria novo turno
            Shift turno = new Shift(usuarioLogado, valorAbertura);
            turno.setCode(dao.generateShiftCode());
            turno.setHash(gerarHashTurno());
            turno.setDateOpen(LocalDateTime.now());

            Shift turnoSalvo = dao.save(turno);

            // 🔹 ATUALIZA A SESSÃO COM O TURNO ABERTO
            atualizarSessaoTurno(turnoSalvo, usuarioLogado);

            JOptionPane.showMessageDialog(null,
                    "✅ Turno aberto com sucesso!\nCódigo: " + turnoSalvo.getCode()
                    + "\nValor abertura: " + valorAbertura + " AOA",
                    "Turno Aberto", JOptionPane.INFORMATION_MESSAGE);

            return turnoSalvo;

        } catch (Exception e) {
            System.err.println("Erro ao abrir turno: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao abrir turno: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Erro ao abrir turno", e);
        }
    }

    /**
     * Fecha o turno atual do usuário logado
     */
    public Shift fecharTurno(BigDecimal valorFechamento) {
        try {
            User usuarioLogado = userSession.getUser();
            if (usuarioLogado == null) {
                throw new IllegalStateException("Nenhum usuário logado");
            }

            // Busca turno aberto do usuário
            Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());
            if (turnoAberto.isEmpty()) {
                throw new IllegalStateException("Nenhum turno aberto encontrado para este usuário");
            }

            Shift turno = turnoAberto.get();

            // Atualiza dados de fechamento
            turno.setClosingAmount(valorFechamento);
            turno.setStatus("closed");
            turno.setDateClose(LocalDateTime.now());

            Shift turnoFechado = dao.update(turno);

            // 🔹 LIMPA A SESSÃO DO TURNO
            limparSessaoTurno();

            // Calcula diferença
            BigDecimal diferenca = turnoFechado.getDifference();
            String mensagemDiferenca = diferenca.compareTo(BigDecimal.ZERO) >= 0
                    ? "✅ Sobra: " + diferenca + " AOA"
                    : "❌ Falta: " + diferenca.abs() + " AOA";

            JOptionPane.showMessageDialog(null,
                    "✅ Turno fechado com sucesso!\nCódigo: " + turnoFechado.getCode()
                    + "\nValor esperado: " + turnoFechado.getCurrentBalance() + " AOA"
                    + "\nValor fechamento: " + valorFechamento + " AOA"
                    + "\n" + mensagemDiferenca,
                    "Turno Fechado", JOptionPane.INFORMATION_MESSAGE);

            return turnoFechado;

        } catch (Exception e) {
            System.err.println("Erro ao fechar turno: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "❌ Erro ao fechar turno: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Erro ao fechar turno", e);
        }
    }

    /**
     * Adiciona valor ao incurred_amount do turno atual (vendas, etc)
     */
    public void adicionarValorTurno(BigDecimal valor) {
        try {
            User usuarioLogado = userSession.getUser();
            if (usuarioLogado == null) {
                throw new IllegalStateException("Nenhum usuário logado");
            }

            Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());
            if (turnoAberto.isEmpty()) {
                throw new IllegalStateException("Nenhum turno aberto encontrado");
            }

            Shift turno = turnoAberto.get();
            BigDecimal novoValor = turno.getIncurredAmount().add(valor);
            turno.setIncurredAmount(novoValor);

            Shift turnoAtualizado = dao.update(turno);

            // 🔹 ATUALIZA A SESSÃO COM O TURNO ATUALIZADO
            atualizarSessaoTurno(turnoAtualizado, usuarioLogado);

        } catch (Exception e) {
            System.err.println("Erro ao adicionar valor ao turno: " + e.getMessage());
            throw new RuntimeException("Erro ao adicionar valor ao turno", e);
        }
    }

    // ==========================================================
    // 🔹 CONSULTAS (ATUALIZADAS)
    // ==========================================================
    public Shift buscarTurnoAtual() {
        User usuarioLogado = userSession.getUser();
        if (usuarioLogado == null) {
            return null;
        }

        Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());

        // 🔹 ATUALIZA A SESSÃO SE ENCONTRAR TURNO ABERTO
        if (turnoAberto.isPresent()) {
            atualizarSessaoTurno(turnoAberto.get(), usuarioLogado);
            return turnoAberto.get();
        } else {
            limparSessaoTurno();
            return null;
        }
    }

    public boolean temTurnoAberto() {
        Shift turnoAtual = buscarTurnoAtual();
        return turnoAtual != null;
    }

    /**
     * Cancela o turno atual (em caso de erro)
     */
    public void cancelarTurnoAtual() {
        try {
            User usuarioLogado = userSession.getUser();
            if (usuarioLogado == null) {
                return;
            }

            Optional<Shift> turnoAberto = dao.findLastOpenShiftByUser(usuarioLogado.getId());
            if (turnoAberto.isPresent()) {
                Shift turno = turnoAberto.get();
                turno.setStatus("cancelled");
                turno.setDateClose(LocalDateTime.now());
                dao.update(turno);

                // 🔹 LIMPA A SESSÃO DO TURNO
                limparSessaoTurno();

                System.out.println("Turno cancelado: " + turno.getCode());
            }

        } catch (Exception e) {
            System.err.println("Erro ao cancelar turno: " + e.getMessage());
        }
    }

    // ==========================================================
    // 🔹 MÉTODOS UTILITÁRIOS (MANTIDOS)
    // ==========================================================
    private String gerarHashTurno() {
        User usuarioLogado = userSession.getUser();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String userInfo = usuarioLogado != null ? usuarioLogado.getId().toString() : "unknown";
        return "shift_" + userInfo + "_" + timestamp;
    }

    public BigDecimal obterSaldoAtual() {
        Shift turnoAtual = buscarTurnoAtual();
        if (turnoAtual == null) {
            return BigDecimal.ZERO;
        }
        return turnoAtual.getCurrentBalance();
    }

    public BigDecimal obterValorAbertura() {
        Shift turnoAtual = buscarTurnoAtual();
        if (turnoAtual == null) {
            return BigDecimal.ZERO;
        }
        return turnoAtual.getGrantedAmount();
    }

    public BigDecimal obterValorVendas() {
        Shift turnoAtual = buscarTurnoAtual();
        if (turnoAtual == null) {
            return BigDecimal.ZERO;
        }
        return turnoAtual.getIncurredAmount();
    }

    // 🔹 MÉTODOS DE CONSULTA (MANTIDOS SEM ALTERAÇÕES)
    public Shift buscarPorId(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return dao.findById(id).orElse(null);
    }

    public List<Shift> listarTodos() {
        return dao.findAll();
    }

    public List<Shift> listarPorUsuario(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido");
        }
        return dao.findByUser(userId);
    }

    public List<Shift> filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listarTodos();
        }
        return dao.filter(texto.trim());
    }

    public List<Shift> listarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
        return dao.findByStatus(status.trim());
    }
}
