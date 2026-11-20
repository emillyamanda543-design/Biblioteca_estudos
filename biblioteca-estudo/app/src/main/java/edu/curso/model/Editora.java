package edu.curso.model;

import java.time.LocalDate;
import java.math.BigDecimal;

/** Entidade: representa os dados de Editora, sem acesso à interface ou ao banco. */
public class Editora {
    private long id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataFundacao;
    private String cnpj;
    private String cidade;

    public Editora() {}
    public long getId() { return id; }
    public void setId(long valor) { id = valor; }
    public String getNome() { return nome; }
    public void setNome(String valor) { nome = valor; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String valor) { telefone = valor; }
    public String getEmail() { return email; }
    public void setEmail(String valor) { email = valor; }
    public LocalDate getDataFundacao() { return dataFundacao; }
    public void setDataFundacao(LocalDate valor) { dataFundacao = valor; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String valor) { cnpj = valor; }
    public String getCidade() { return cidade; }
    public void setCidade(String valor) { cidade = valor; }

    /** Facilita a leitura dos dados durante estudo e depuração. */
    @Override
    public String toString() {
        return "Editora [id=" + id
            + ", nome=" + nome
            + ", telefone=" + telefone
            + ", email=" + email
            + ", dataFundacao=" + dataFundacao
            + ", cnpj=" + cnpj
            + ", cidade=" + cidade + "]";
    }
}
