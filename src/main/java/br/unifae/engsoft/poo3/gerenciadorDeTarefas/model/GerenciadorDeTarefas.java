package br.unifae.engsoft.poo3.gerenciadorDeTarefas.model;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeTarefas<T extends Tarefa> {
    private List<Tarefa> listaDeTarefas = new ArrayList<>();

    public GerenciadorDeTarefas() {
        this.listaDeTarefas = new ArrayList<>();
    }

    public boolean adicionarTarefa(T tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }

        if (tarefa.getPrioridade() < 1 || tarefa.getPrioridade() > 5) {
            throw new IllegalArgumentException("A prioridade precisa estar entre 1 e 5.");
        }

        if (tarefa.getDataCriacao() == null) {
            throw new IllegalArgumentException("A data de criação não pode ser nula.");
        }

        if (tarefa instanceof TarefaComPrazo tarefaComPrazo) {
            if (tarefaComPrazo.getPrazo() == null) {
                throw new IllegalArgumentException("É necessário que a tarefa (com prazo) tenha um prazo definido.");
            }
        }

        boolean adicionada = listaDeTarefas.add(tarefa);

        return adicionada;
    }

    public boolean removerTarefa(T tarefa) {
        return listaDeTarefas.removeIf(t -> t.equals(tarefa));
    }

    public boolean atualizarTarefa(T tarefaAntiga, T tarefaNova) {
        int index = listaDeTarefas.indexOf(tarefaAntiga);
        if (index == -1) {
            throw new IllegalArgumentException("Tarefa não encontrada.");
        }
        listaDeTarefas.set(index, tarefaNova);
        return true;
    }

    public List<Tarefa> listarTarefas() {
        return new ArrayList<>(listaDeTarefas); // Cópia da lista
    }
     
    
    public Tarefa buscarTarefaPorDescricao(String descricao) {
    for (Tarefa t : listaDeTarefas) {
        if (t.getDescricao().equals(descricao)) {
            return t;
        }
    }
    return null;
}


}
