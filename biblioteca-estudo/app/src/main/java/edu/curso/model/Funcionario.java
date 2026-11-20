package edu.curso.model;
import java.time.LocalDate;
public class Funcionario {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String cargo;
    private LocalDate dataAdmissao;
    public Funcionario() {}
    public long getId() { return id; }
    public void setId(long valor) { id = valor; }
    public String getNome() { return nome; }
    public void setNome(String valor) { nome = valor; }
    public String getCpf() { return cpf; }
    public void setCpf(String valor) { cpf = valor; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String valor) { telefone = valor; }
    public String getCargo() { return cargo; }
    public void setCargo(String valor) { cargo = valor; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate valor) { dataAdmissao = valor; }
    @Override public String toString() { return String.format("[%s, %s, %s, %s, %s, %s]", id, nome, cpf, telefone, cargo, dataAdmissao); }
}
