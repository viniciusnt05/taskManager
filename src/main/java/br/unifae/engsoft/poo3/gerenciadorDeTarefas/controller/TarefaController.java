package br.unifae.engsoft.poo3.gerenciadorDeTarefas.controller;

import br.unifae.engsoft.poo3.gerenciadorDeTarefas.model.GerenciadorDeTarefas;
import br.unifae.engsoft.poo3.gerenciadorDeTarefas.model.Tarefa;
import br.unifae.engsoft.poo3.gerenciadorDeTarefas.model.TarefaComPrazo;
import br.unifae.engsoft.poo3.gerenciadorDeTarefas.model.TarefaSimples;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

public class TarefaController {

    private GerenciadorDeTarefas<Tarefa> gerenciador; // Tarefa como tipo genérico

    public TarefaController(GerenciadorDeTarefas<Tarefa> gerenciador) {
        this.gerenciador = gerenciador;
    }

    // Construtor sem parâmetros pra inicializar GerenciadorDeTarefas
    public TarefaController() {
        this.gerenciador = new GerenciadorDeTarefas<>();
    }
    
    public GerenciadorDeTarefas<Tarefa> getGerenciador() {
        return this.gerenciador;
    }

    public List<Tarefa> listarTarefas() {
        return gerenciador.listarTarefas();
    }
    
    public DefaultTableModel getTabelaTarefas(List<Tarefa> tarefas) {
        String[] colunas = {"Descrição", "Data Criação", "Prioridade", "Prazo", "Concluída"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class; // Descrição
                    case 1: return String.class; // Data de Criação
                    case 2: return Integer.class; // Prioridade
                    case 3: return String.class; // Prazo
                    case 4: return String.class; // Concluída (que será Sim ou Não)
                    default: return Object.class;
                }
            }
        };

        for (Tarefa t : tarefas) {
            Object[] linha = {
                t.getDescricao(),
                t.getDataCriacao().toString(),
                t.getPrioridade(),
                (t instanceof TarefaComPrazo) ? ((TarefaComPrazo) t).getPrazo().toString() : "Sem Prazo",
                t.isConcluida() ? "Sim" : "Não"
            };
            modelo.addRow(linha);
        }

        return modelo;
    }
    
    public boolean criarTarefa(String descricao, int prioridade, String dataCriacao, String prazoString) {
        try {
            // Validação para a descrição não poder ser nula
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição da tarefa não pode ser vazia.");
            }

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Puxa a data atual pra já vir preenchida na data de criação, mas pode ser editada, se necessário
            LocalDate dataCriacaoLocal = LocalDate.parse(dataCriacao, formatador);

            // Verifica se o prazo já foi preenchido antes de fazer o try/catch, pois ele é nullable
            LocalDate prazoLocal = null;
            final String prazoChecker = prazoString.replaceAll(" ", "").replaceAll("/", "");
            if (!prazoChecker.isEmpty()) {
                try {
                    prazoLocal = LocalDate.parse(prazoString, formatador);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("O prazo deve estar no formato dd/MM/yyyy.");
                }
            }

            // Cria a tarefa
            if (prazoLocal != null) {
                return gerenciador.adicionarTarefa(new TarefaComPrazo(descricao, prioridade, dataCriacaoLocal, prazoLocal));
            } else {
                return gerenciador.adicionarTarefa(new TarefaSimples(descricao, prioridade, dataCriacaoLocal));
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
        } catch (IllegalArgumentException e) {
            throw e; // Repassa a exceção para a View
        }
    }
    
    public boolean atualizarTarefa(Tarefa tarefaAntiga, Tarefa tarefaNova) {
        return gerenciador.atualizarTarefa(tarefaAntiga, tarefaNova);
    }
    
    public boolean atualizarTarefa(Tarefa tarefaSelecionada, String descricao, int prioridade, String dataCriacao, String prazoString, boolean concluida) {
        try {
            // Validação para a descrição não poder ser nula
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição da tarefa não pode ser vazia.");
            }

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate dataCriacaoLocal = LocalDate.parse(dataCriacao, formatador);
            LocalDate prazoLocal = null;

            // Verifica se o prazo já foi preenchido antes de fazer o try/catch, pois ele é nullable
            final String prazoChecker = prazoString.replaceAll(" ", "").replaceAll("/", "");
            if (!prazoChecker.isEmpty()) {
                try {
                    prazoLocal = LocalDate.parse(prazoString, formatador);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("O prazo deve estar no formato dd/MM/yyyy.");
                }
            }

            // Nova instância de Tarefa (ou TarefaComPrazo)
            Tarefa novaTarefa;
            if (prazoLocal != null) {
                novaTarefa = new TarefaComPrazo(descricao, prioridade, dataCriacaoLocal, prazoLocal);
            } else {
                novaTarefa = new TarefaSimples(descricao, prioridade, dataCriacaoLocal);
            }
            novaTarefa.setConcluida(concluida);

            // Atualiza a tarefa no gerenciador
            return gerenciador.atualizarTarefa(tarefaSelecionada, novaTarefa);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy.");
        } catch (IllegalArgumentException e) {
            throw e; // Repassa a exceção para a View
        }
    }
    
    public boolean removerTarefa(Tarefa tarefa) {
        return gerenciador.removerTarefa(tarefa);
    }
    
    public boolean removerTarefaPorDescricao(String descricao) {
        Tarefa tarefaParaRemover = gerenciador.buscarTarefaPorDescricao(descricao);
        if (tarefaParaRemover != null) {
            return gerenciador.removerTarefa(tarefaParaRemover);
        }
        return false; // Retorna false se a tarefa não for encontrada
    }

    public List<Tarefa> filtrarPorPrioridade(String filtroSelecionado) {
        List<Tarefa> tarefas = gerenciador.listarTarefas();

        switch (filtroSelecionado) {
            case "Maior Prioridade":
                tarefas.sort((t1, t2) -> Integer.compare(t2.getPrioridade(), t1.getPrioridade())); // Do maior pro menor
                break;
            case "Menor Prioridade":
                tarefas.sort((t1, t2) -> Integer.compare(t1.getPrioridade(), t2.getPrioridade())); // Do menor pro maior
                break;
            default:
                int prioridadeFiltro = Integer.parseInt(filtroSelecionado);
                tarefas = tarefas.stream()
                    .filter(t -> t.getPrioridade() == prioridadeFiltro)
                    .collect(Collectors.toList());
                break;
        }

        return tarefas;
    }

    public List<Tarefa> filtrarPorPrazo(String filtroSelecionado) {
        List<Tarefa> tarefas = gerenciador.listarTarefas();

        switch (filtroSelecionado) {
            case "Maior Prazo":
                tarefas = tarefas.stream()
                    .filter(t -> t instanceof TarefaComPrazo)
                    .sorted((t1, t2) -> ((TarefaComPrazo) t2).getPrazo().compareTo(((TarefaComPrazo) t1).getPrazo()))
                    .collect(Collectors.toList());
                break;
            case "Menor Prazo":
                tarefas = tarefas.stream()
                    .filter(t -> t instanceof TarefaComPrazo)
                    .sorted((t1, t2) -> ((TarefaComPrazo) t1).getPrazo().compareTo(((TarefaComPrazo) t2).getPrazo()))
                    .collect(Collectors.toList());
                break;
            case "Hoje":
                LocalDate hoje = LocalDate.now();
                tarefas = tarefas.stream()
                    .filter(t -> t instanceof TarefaComPrazo && ((TarefaComPrazo) t).getPrazo().isEqual(hoje))
                    .collect(Collectors.toList());
                break;
            case "Últimos 7 dias":
                LocalDate now = LocalDate.now();
                LocalDate seteDiasAtras = now.minusDays(7);
                tarefas = tarefas.stream()
                    .filter(t -> t instanceof TarefaComPrazo && ((TarefaComPrazo) t).getPrazo().isAfter(seteDiasAtras) && ((TarefaComPrazo) t).getPrazo().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());
                break;
            case "Sem Prazo":
                tarefas = tarefas.stream()
                    .filter(t -> !(t instanceof TarefaComPrazo))
                    .collect(Collectors.toList());
                break;
            default:
                break;
        }

        return tarefas;
    }
}