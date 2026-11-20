package edu.curso.control;
import edu.curso.model.Editora;
import javafx.beans.property.*;
import javafx.collections.*;
import java.time.LocalDate;
public class EditoraController {
    private final EditoraDAO dao;
    private ObservableList<Editora> lista = FXCollections.observableArrayList();
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataFundacao = new SimpleObjectProperty<>(null);

    public EditoraController(EditoraDAO dao) { this.dao = dao; load(); }
    public void load() { lista.setAll(dao.findAll()); }
    public ObservableList<Editora> getLista() { return lista; }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> dataFundacaoProperty() { return dataFundacao; }
    public void clearFields() {
        id.set(0);
        nome.set("");
        telefone.set("");
        email.set("");
        dataFundacao.set(null);
    }
    public void fromEntity(Editora item) {
        if (item == null) return;
        id.set(item.getId());
        nome.set(item.getNome());
        telefone.set(item.getTelefone());
        email.set(item.getEmail());
        dataFundacao.set(item.getDataFundacao());
    }
    public Editora toEntity() {
        Editora item = new Editora();
        item.setId(id.get());
        item.setNome(nome.get());
        item.setTelefone(telefone.get());
        item.setEmail(email.get());
        item.setDataFundacao(dataFundacao.get());
        return item;
    }
    private void validate(Editora item) {
        if (item.getNome() == null || item.getNome().isBlank()) throw new IllegalArgumentException("Preencha nome.");
        if (item.getTelefone() == null || item.getTelefone().isBlank()) throw new IllegalArgumentException("Preencha telefone.");
        if (!item.getTelefone().matches("[0-9]{10,11}")) throw new IllegalArgumentException("Telefone deve ter 10 ou 11 números.");
        if (item.getEmail() == null || item.getEmail().isBlank()) throw new IllegalArgumentException("Preencha email.");
        if (!item.getEmail().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) throw new IllegalArgumentException("E-mail inválido.");
        if (item.getDataFundacao() == null || item.getDataFundacao().isAfter(LocalDate.now())) throw new IllegalArgumentException("dataFundacao: informe uma data válida, não futura.");
    }
    public void save() {
        if (id.get() != 0) throw new IllegalArgumentException("Use Atualizar para editar um cadastro.");
        Editora item = toEntity(); validate(item);
        dao.save(item); load(); clearFields();
    }
    public void update() {
        if (id.get() == 0 || dao.searchById(id.get()) == null) throw new IllegalArgumentException("Selecione um cadastro existente.");
        Editora item = toEntity(); validate(item);
        dao.update(id.get(), item); load(); clearFields();
    }
    public void delete(Editora item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto));
    }
}
