package edu.curso.model;

import java.time.LocalDate;
import java.math.BigDecimal;

/** Entidade: representa os dados de Leitor, sem acesso à interface ou ao banco. */
public class Leitor {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private String endereco;

    public Leitor() {}
    public long getId() { return id; }
    public void setId(long valor) { id = valor; }
    public String getNome() { return nome; }
    public void setNome(String valor) { nome = valor; }
    public String getCpf() { return cpf; }
    public void setCpf(String valor) { cpf = valor; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String valor) { telefone = valor; }
    public String getEmail() { return email; }
    public void setEmail(String valor) { email = valor; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate valor) { dataNascimento = valor; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String valor) { endereco = valor; }

    /** Facilita a leitura dos dados durante estudo e depuração. */
    @Override
    public String toString() {
        return "Leitor [id=" + id
            + ", nome=" + nome
            + ", cpf=" + cpf
            + ", telefone=" + telefone
            + ", email=" + email
            + ", dataNascimento=" + dataNascimento
            + ", endereco=" + endereco + "]";
    }
}
