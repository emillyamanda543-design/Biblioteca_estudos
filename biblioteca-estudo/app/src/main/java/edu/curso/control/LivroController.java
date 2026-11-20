package edu.curso.control;
import edu.curso.model.Livro;
import javafx.beans.property.*;
import javafx.collections.*;
import java.time.LocalDate;
public class LivroController {
    private final LivroDAO dao;
    private ObservableList<Livro> lista = FXCollections.observableArrayList();
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty titulo = new SimpleStringProperty("");
    private StringProperty autor = new SimpleStringProperty("");
    private StringProperty codigo = new SimpleStringProperty("");
    private StringProperty editora = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataCadastro = new SimpleObjectProperty<>(null);

    public LivroController(LivroDAO dao) { this.dao = dao; load(); }
    public void load() { lista.setAll(dao.findAll()); }
    public ObservableList<Livro> getLista() { return lista; }
    public LongProperty idProperty() { return id; }
    public StringProperty tituloProperty() { return titulo; }
    public StringProperty autorProperty() { return autor; }
    public StringProperty codigoProperty() { return codigo; }
    public StringProperty editoraProperty() { return editora; }
    public ObjectProperty<LocalDate> dataCadastroProperty() { return dataCadastro; }
    public void clearFields() {
        id.set(0);
        titulo.set("");
        autor.set("");
        codigo.set("");
        editora.set("");
        dataCadastro.set(null);
    }
    public void fromEntity(Livro item) {
        if (item == null) return;
        id.set(item.getId());
        titulo.set(item.getTitulo());
        autor.set(item.getAutor());
        codigo.set(item.getCodigo());
        editora.set(item.getEditora());
        dataCadastro.set(item.getDataCadastro());
    }
    public Livro toEntity() {
        Livro item = new Livro();
        item.setId(id.get());
        item.setTitulo(titulo.get());
        item.setAutor(autor.get());
        item.setCodigo(codigo.get());
        item.setEditora(editora.get());
        item.setDataCadastro(dataCadastro.get());
        return item;
    }
    private void validate(Livro item) {
        if (item.getTitulo() == null || item.getTitulo().isBlank()) throw new IllegalArgumentException("Preencha titulo.");
        if (item.getAutor() == null || item.getAutor().isBlank()) throw new IllegalArgumentException("Preencha autor.");
        if (item.getCodigo() == null || item.getCodigo().isBlank()) throw new IllegalArgumentException("Preencha codigo.");
        if (item.getEditora() == null || item.getEditora().isBlank()) throw new IllegalArgumentException("Preencha editora.");
        if (item.getDataCadastro() == null || item.getDataCadastro().isAfter(LocalDate.now())) throw new IllegalArgumentException("dataCadastro: informe uma data válida, não futura.");
    }
    public void save() {
        if (id.get() != 0) throw new IllegalArgumentException("Use Atualizar para editar um cadastro.");
        Livro item = toEntity(); validate(item);
        dao.save(item); load(); clearFields();
    }
    public void update() {
        if (id.get() == 0 || dao.searchById(id.get()) == null) throw new IllegalArgumentException("Selecione um cadastro existente.");
        Livro item = toEntity(); validate(item);
        dao.update(id.get(), item); load(); clearFields();
    }
    public void delete(Livro item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto));
    }
}
