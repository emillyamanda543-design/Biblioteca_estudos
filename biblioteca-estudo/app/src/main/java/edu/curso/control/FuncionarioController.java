package edu.curso.control;
import edu.curso.model.Funcionario;
import javafx.beans.property.*;
import javafx.collections.*;
import java.time.LocalDate;
public class FuncionarioController {
    private final FuncionarioDAO dao;
    private ObservableList<Funcionario> lista = FXCollections.observableArrayList();
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty cpf = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");
    private StringProperty cargo = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataAdmissao = new SimpleObjectProperty<>(null);

    public FuncionarioController(FuncionarioDAO dao) { this.dao = dao; load(); }
    public void load() { lista.setAll(dao.findAll()); }
    public ObservableList<Funcionario> getLista() { return lista; }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cpfProperty() { return cpf; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty cargoProperty() { return cargo; }
    public ObjectProperty<LocalDate> dataAdmissaoProperty() { return dataAdmissao; }
    public void clearFields() {
        id.set(0);
        nome.set("");
        cpf.set("");
        telefone.set("");
        cargo.set("");
        dataAdmissao.set(null);
    }
    public void fromEntity(Funcionario item) {
        if (item == null) return;
        id.set(item.getId());
        nome.set(item.getNome());
        cpf.set(item.getCpf());
        telefone.set(item.getTelefone());
        cargo.set(item.getCargo());
        dataAdmissao.set(item.getDataAdmissao());
    }
    public Funcionario toEntity() {
        Funcionario item = new Funcionario();
        item.setId(id.get());
        item.setNome(nome.get());
        item.setCpf(cpf.get());
        item.setTelefone(telefone.get());
        item.setCargo(cargo.get());
        item.setDataAdmissao(dataAdmissao.get());
        return item;
    }
    private void validate(Funcionario item) {
        if (item.getNome() == null || item.getNome().isBlank()) throw new IllegalArgumentException("Preencha nome.");
        if (item.getCpf() == null || item.getCpf().isBlank()) throw new IllegalArgumentException("Preencha cpf.");
        if (!item.getCpf().matches("[0-9]{11}")) throw new IllegalArgumentException("CPF deve ter 11 números.");
        if (item.getTelefone() == null || item.getTelefone().isBlank()) throw new IllegalArgumentException("Preencha telefone.");
        if (!item.getTelefone().matches("[0-9]{10,11}")) throw new IllegalArgumentException("Telefone deve ter 10 ou 11 números.");
        if (item.getCargo() == null || item.getCargo().isBlank()) throw new IllegalArgumentException("Preencha cargo.");
        if (item.getDataAdmissao() == null || item.getDataAdmissao().isAfter(LocalDate.now())) throw new IllegalArgumentException("dataAdmissao: informe uma data válida, não futura.");
    }
    public void save() {
        if (id.get() != 0) throw new IllegalArgumentException("Use Atualizar para editar um cadastro.");
        Funcionario item = toEntity(); validate(item);
        dao.save(item); load(); clearFields();
    }
    public void update() {
        if (id.get() == 0 || dao.searchById(id.get()) == null) throw new IllegalArgumentException("Selecione um cadastro existente.");
        Funcionario item = toEntity(); validate(item);
        dao.update(id.get(), item); load(); clearFields();
    }
    public void delete(Funcionario item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto));
    }
}
