package edu.curso.model;
import java.time.LocalDate;
public class Leitor {
    private long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
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
    @Override public String toString() { return String.format("[%s, %s, %s, %s, %s, %s]", id, nome, cpf, telefone, email, dataNascimento); }
}
