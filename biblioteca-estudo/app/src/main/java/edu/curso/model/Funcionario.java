package edu.curso.model;

import java.time.LocalDate;
import java.math.BigDecimal;

/** Entidade: representa os dados de Funcionario, sem acesso à interface ou ao banco. */
public class Funcionario {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String cargo;
    private LocalDate dataAdmissao;
    private BigDecimal salario;

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
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal valor) { salario = valor; }

    /** Facilita a leitura dos dados durante estudo e depuração. */
    @Override
    public String toString() {
        return "Funcionario [id=" + id
            + ", nome=" + nome
            + ", cpf=" + cpf
            + ", telefone=" + telefone
            + ", cargo=" + cargo
            + ", dataAdmissao=" + dataAdmissao
            + ", salario=" + salario + "]";
    }
}
