package edu.curso.model;
import java.time.LocalDate;
public class Livro {
    private long id;
    private String titulo;
    private String autor;
    private String codigo;
    private String editora;
    private LocalDate dataCadastro;
    public Livro() {}
    public long getId() { return id; }
    public void setId(long valor) { id = valor; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String valor) { titulo = valor; }
    public String getAutor() { return autor; }
    public void setAutor(String valor) { autor = valor; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String valor) { codigo = valor; }
    public String getEditora() { return editora; }
    public void setEditora(String valor) { editora = valor; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate valor) { dataCadastro = valor; }
    @Override public String toString() { return String.format("[%s, %s, %s, %s, %s, %s]", id, titulo, autor, codigo, editora, dataCadastro); }
}
