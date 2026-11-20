package edu.curso.model;
import java.time.LocalDate;
public class Editora {
    private long id;
    private String nome;
    private String telefone;
    private String email;
    private LocalDate dataFundacao;
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
    @Override public String toString() { return String.format("[%s, %s, %s, %s, %s]", id, nome, telefone, email, dataFundacao); }
}
