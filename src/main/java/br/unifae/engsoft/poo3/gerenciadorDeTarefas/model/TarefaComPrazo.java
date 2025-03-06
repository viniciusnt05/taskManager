package br.unifae.engsoft.poo3.gerenciadorDeTarefas.model;

import java.time.LocalDate;
import java.util.Objects;

public class TarefaComPrazo extends Tarefa {

    private LocalDate prazo;

    public TarefaComPrazo() {
    }

    public TarefaComPrazo(String descricao, int prioridade, LocalDate dataCriacao, LocalDate prazo) {
        super(descricao, prioridade, dataCriacao);
        this.prazo = prazo;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    @Override
    public String toString() {
        return super.toString() + ", prazo=" + prazo + '}';
    }
    
    @Override
    public int getPrioridade() {
        return super.getPrioridade();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TarefaComPrazo other = (TarefaComPrazo) obj;
        return super.equals(obj) && prazo.equals(other.prazo); 
    }

    // Perguntar pra Cris do 23 (gerado no insert code)
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.prazo);
        return hash;
    }
}
