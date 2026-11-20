package edu.curso.control;

import edu.curso.model.Livro;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.property.*;
import javafx.collections.*;

/** Controle: mantém propriedades ligadas à tela e valida antes de gravar. */
public class LivroController {
    private final LivroDAO dao;
    private final ObservableList<Livro> lista = FXCollections.observableArrayList();
    private final LongProperty id = new SimpleLongProperty(0);
    private final Map<String, StringProperty> erros = new LinkedHashMap<>();
    private final StringProperty titulo = new SimpleStringProperty("");
    private final StringProperty autor = new SimpleStringProperty("");
    private final StringProperty codigo = new SimpleStringProperty("");
    private final StringProperty editora = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataCadastro = new SimpleObjectProperty<>();
    private final StringProperty genero = new SimpleStringProperty("");

    public LivroController(LivroDAO dao) {
        this.dao = dao;
        erros.put("titulo", new SimpleStringProperty(""));
        erros.put("autor", new SimpleStringProperty(""));
        erros.put("codigo", new SimpleStringProperty(""));
        erros.put("editora", new SimpleStringProperty(""));
        erros.put("dataCadastro", new SimpleStringProperty(""));
        erros.put("genero", new SimpleStringProperty(""));
        load();
    }
    public StringProperty erroProperty(String campo) { return erros.get(campo); }
    public LongProperty idProperty() { return id; }
    public StringProperty tituloProperty() { return titulo; }
    public StringProperty autorProperty() { return autor; }
    public StringProperty codigoProperty() { return codigo; }
    public StringProperty editoraProperty() { return editora; }
    public ObjectProperty<LocalDate> dataCadastroProperty() { return dataCadastro; }
    public StringProperty generoProperty() { return genero; }
    public ObservableList<Livro> getLista() { return lista; }
    public void load() { lista.setAll(dao.findAll()); }

    /** Limpa os campos e as mensagens, preparando um novo cadastro. */
    public void clearFields() {
        id.set(0);
        erros.values().forEach(erro -> erro.set(""));
        titulo.set("");
        autor.set("");
        codigo.set("");
        editora.set("");
        dataCadastro.set(null);
        genero.set("");
    }

    /** Copia o registro escolhido na tabela para o formulário. */
    public void fromEntity(Livro item) {
        if (item == null) return;
        erros.values().forEach(erro -> erro.set(""));
        id.set(item.getId());
        titulo.set(item.getTitulo());
        autor.set(item.getAutor());
        codigo.set(item.getCodigo());
        editora.set(item.getEditora());
        dataCadastro.set(item.getDataCadastro());
        genero.set(item.getGenero());
    }

    /** Cria a entidade com os valores das propriedades do controle. */
    public Livro toEntity() {
        Livro item = new Livro();
        item.setId(id.get());
        item.setTitulo(texto(titulo.get()));
        item.setAutor(texto(autor.get()));
        item.setCodigo(texto(codigo.get()));
        item.setEditora(texto(editora.get()));
        item.setDataCadastro(dataCadastro.get());
        item.setGenero(texto(genero.get()));
        return item;
    }
    private String texto(String valor) { return valor == null ? "" : valor.trim(); }

    /** Apresenta todos os erros simultaneamente, ao lado de cada campo. */
    private void validate() {
        erros.values().forEach(erro -> erro.set(""));
        if (texto(titulo.get()).isBlank() || texto(titulo.get()).length() > 255) erros.get("titulo").set("Título: preencha com até 255 caracteres.");
        if (texto(autor.get()).isBlank() || texto(autor.get()).length() > 255) erros.get("autor").set("Autor: preencha com até 255 caracteres.");
        if (texto(codigo.get()).isBlank() || texto(codigo.get()).length() > 100) erros.get("codigo").set("Código: preencha com até 100 caracteres.");
        if (texto(editora.get()).isBlank() || texto(editora.get()).length() > 255) erros.get("editora").set("Editora: preencha com até 255 caracteres.");
        if (dataCadastro.get() == null || dataCadastro.get().isAfter(LocalDate.now())) erros.get("dataCadastro").set("Data de cadastro: informe uma data válida, não futura.");
        if (texto(genero.get()).isBlank() || texto(genero.get()).length() > 255) erros.get("genero").set("Gênero: preencha com até 255 caracteres.");
        // Na alteração, ignora o próprio registro ao verificar duplicidade.
        for (Livro outro : dao.findAll()) {
            if (outro.getId() == id.get()) continue;
            if (texto(codigo.get()).equalsIgnoreCase(texto(outro.getCodigo()))) erros.get("codigo").set("Já existe cadastro com este valor.");
        }
        if (erros.values().stream().anyMatch(erro -> !erro.get().isEmpty()))
            throw new IllegalArgumentException("Confira as mensagens ao lado dos campos.");
    }

    public void save() {
        if (id.get() != 0) throw new IllegalArgumentException("Use Salvar alterações ou limpe o formulário para cadastrar.");
        validate();
        dao.save(toEntity());
        load(); clearFields();
    }
    public void update() {
        if (id.get() == 0 || dao.searchById(id.get()) == null)
            throw new IllegalArgumentException("Selecione um cadastro existente.");
        validate();
        dao.update(id.get(), toEntity());
        load(); clearFields();
    }
    public void delete(Livro item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto.trim()));
    }
}
