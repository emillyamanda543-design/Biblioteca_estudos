package edu.curso.control;
import edu.curso.model.Leitor;
import javafx.beans.property.*;
import javafx.collections.*;
import java.time.LocalDate;
public class LeitorController {
    private final LeitorDAO dao;
    private ObservableList<Leitor> lista = FXCollections.observableArrayList();
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty cpf = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>(null);

    public LeitorController(LeitorDAO dao) { this.dao = dao; load(); }
    public void load() { lista.setAll(dao.findAll()); }
    public ObservableList<Leitor> getLista() { return lista; }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cpfProperty() { return cpf; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> dataNascimentoProperty() { return dataNascimento; }
    public void clearFields() {
        id.set(0);
        nome.set("");
        cpf.set("");
        telefone.set("");
        email.set("");
        dataNascimento.set(null);
    }
    public void fromEntity(Leitor item) {
        if (item == null) return;
        id.set(item.getId());
        nome.set(item.getNome());
        cpf.set(item.getCpf());
        telefone.set(item.getTelefone());
        email.set(item.getEmail());
        dataNascimento.set(item.getDataNascimento());
    }
    public Leitor toEntity() {
        Leitor item = new Leitor();
        item.setId(id.get());
        item.setNome(nome.get());
        item.setCpf(cpf.get());
        item.setTelefone(telefone.get());
        item.setEmail(email.get());
        item.setDataNascimento(dataNascimento.get());
        return item;
    }
    private void validate(Leitor item) {
        if (item.getNome() == null || item.getNome().isBlank()) throw new IllegalArgumentException("Preencha nome.");
        if (item.getCpf() == null || item.getCpf().isBlank()) throw new IllegalArgumentException("Preencha cpf.");
        if (!item.getCpf().matches("[0-9]{11}")) throw new IllegalArgumentException("CPF deve ter 11 números.");
        if (item.getTelefone() == null || item.getTelefone().isBlank()) throw new IllegalArgumentException("Preencha telefone.");
        if (!item.getTelefone().matches("[0-9]{10,11}")) throw new IllegalArgumentException("Telefone deve ter 10 ou 11 números.");
        if (item.getEmail() == null || item.getEmail().isBlank()) throw new IllegalArgumentException("Preencha email.");
        if (!item.getEmail().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) throw new IllegalArgumentException("E-mail inválido.");
        if (item.getDataNascimento() == null || item.getDataNascimento().isAfter(LocalDate.now())) throw new IllegalArgumentException("dataNascimento: informe uma data válida, não futura.");
    }
    public void save() {
        if (id.get() != 0) throw new IllegalArgumentException("Use Atualizar para editar um cadastro.");
        Leitor item = toEntity(); validate(item);
        dao.save(item); load(); clearFields();
    }
    public void update() {
        if (id.get() == 0 || dao.searchById(id.get()) == null) throw new IllegalArgumentException("Selecione um cadastro existente.");
        Leitor item = toEntity(); validate(item);
        dao.update(id.get(), item); load(); clearFields();
    }
    public void delete(Leitor item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto));
    }
}
