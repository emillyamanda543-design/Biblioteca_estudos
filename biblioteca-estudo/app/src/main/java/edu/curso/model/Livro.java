package edu.curso.model;

import java.time.LocalDate;
import java.math.BigDecimal;

/** Entidade: representa os dados de Livro, sem acesso à interface ou ao banco. */
public class Livro {
    private long id;
    private String titulo;
    private String autor;
    private String codigo;
    private String editora;
    private LocalDate dataCadastro;
    private String genero;

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
    public String getGenero() { return genero; }
    public void setGenero(String valor) { genero = valor; }

    /** Facilita a leitura dos dados durante estudo e depuração. */
    @Override
    public String toString() {
        return "Livro [id=" + id
            + ", titulo=" + titulo
            + ", autor=" + autor
            + ", codigo=" + codigo
            + ", editora=" + editora
            + ", dataCadastro=" + dataCadastro
            + ", genero=" + genero + "]";
    }
}
