package br.unifae.engsoft.poo3.gerenciadorDeTarefas.model;

import java.time.LocalDate;
import java.util.Objects;


public abstract class Tarefa implements Prioritizavel{
    String descricao;
    private boolean concluida;
    private int prioridade;
    private LocalDate dataCriacao;

    // Construtor util para realizar consultas
    public Tarefa() {

    }

    // Sobrecarregamento com dados obrigatórios
    public Tarefa(String descricao, int prioridade, LocalDate dataCriacao) {
        this.descricao = descricao;
        this.concluida = false;
        this.prioridade = prioridade;
        this.dataCriacao = dataCriacao;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    @Override
    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("A prioridade deve estar entre 1 e 5.");
        }
        this.prioridade = prioridade;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    

    @Override
    public String toString() {
        return "Tarefa{" + 
               "descricao='" + descricao + '\'' +
               ", concluida=" + concluida +
               ", prioridade=" + prioridade +
               ", dataCriacao=" + dataCriacao +
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        
        // Verifica pelo *objeto* se são iguais, pra não ter que usar ID
        if (this == obj) {
            return true;
        }
        
         // Verifica se o objeto é null OU se vem de outra classe (diferente)
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        // Comparação dos atributos em si
        Tarefa other = (Tarefa) obj;
        return Objects.equals(this.descricao, other.descricao) &&
               this.prioridade == other.prioridade &&
               this.concluida == other.concluida &&
               Objects.equals(this.dataCriacao, other.dataCriacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, prioridade, concluida, dataCriacao);
    }


}   

