package edu.curso.control;

import edu.curso.model.Leitor;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.property.*;
import javafx.collections.*;

/** Controle: mantém propriedades ligadas à tela e valida antes de gravar. */
public class LeitorController {
    private final LeitorDAO dao;
    private final ObservableList<Leitor> lista = FXCollections.observableArrayList();
    private final LongProperty id = new SimpleLongProperty(0);
    private final Map<String, StringProperty> erros = new LinkedHashMap<>();
    private final StringProperty nome = new SimpleStringProperty("");
    private final StringProperty cpf = new SimpleStringProperty("");
    private final StringProperty telefone = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>();
    private final StringProperty endereco = new SimpleStringProperty("");

    public LeitorController(LeitorDAO dao) {
        this.dao = dao;
        erros.put("nome", new SimpleStringProperty(""));
        erros.put("cpf", new SimpleStringProperty(""));
        erros.put("telefone", new SimpleStringProperty(""));
        erros.put("email", new SimpleStringProperty(""));
        erros.put("dataNascimento", new SimpleStringProperty(""));
        erros.put("endereco", new SimpleStringProperty(""));
        load();
    }
    public StringProperty erroProperty(String campo) { return erros.get(campo); }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cpfProperty() { return cpf; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> dataNascimentoProperty() { return dataNascimento; }
    public StringProperty enderecoProperty() { return endereco; }
    public ObservableList<Leitor> getLista() { return lista; }
    public void load() { lista.setAll(dao.findAll()); }

    /** Limpa os campos e as mensagens, preparando um novo cadastro. */
    public void clearFields() {
        id.set(0);
        erros.values().forEach(erro -> erro.set(""));
        nome.set("");
        cpf.set("");
        telefone.set("");
        email.set("");
        dataNascimento.set(null);
        endereco.set("");
    }

    /** Copia o registro escolhido na tabela para o formulário. */
    public void fromEntity(Leitor item) {
        if (item == null) return;
        erros.values().forEach(erro -> erro.set(""));
        id.set(item.getId());
        nome.set(item.getNome());
        cpf.set(item.getCpf());
        telefone.set(item.getTelefone());
        email.set(item.getEmail());
        dataNascimento.set(item.getDataNascimento());
        endereco.set(item.getEndereco());
    }

    /** Cria a entidade com os valores das propriedades do controle. */
    public Leitor toEntity() {
        Leitor item = new Leitor();
        item.setId(id.get());
        item.setNome(texto(nome.get()));
        item.setCpf(texto(cpf.get()));
        item.setTelefone(texto(telefone.get()));
        item.setEmail(texto(email.get()));
        item.setDataNascimento(dataNascimento.get());
        item.setEndereco(texto(endereco.get()));
        return item;
    }
    private String texto(String valor) { return valor == null ? "" : valor.trim(); }

    /** Apresenta todos os erros simultaneamente, ao lado de cada campo. */
    private void validate() {
        erros.values().forEach(erro -> erro.set(""));
        if (texto(nome.get()).isBlank() || texto(nome.get()).length() > 255) erros.get("nome").set("Nome: preencha com até 255 caracteres.");
        if (!Validacoes.cpf(texto(cpf.get()))) erros.get("cpf").set("CPF: informe 11 números e dígitos verificadores válidos.");
        if (!texto(telefone.get()).matches("[0-9]{10,11}")) erros.get("telefone").set("Telefone: informe DDD e 10 ou 11 números.");
        if (!texto(email.get()).matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+") || texto(email.get()).length() > 255) erros.get("email").set("E-mail: informe um endereço válido.");
        if (dataNascimento.get() == null || dataNascimento.get().isAfter(LocalDate.now())) erros.get("dataNascimento").set("Data de nascimento: informe uma data válida, não futura.");
        if (texto(endereco.get()).isBlank() || texto(endereco.get()).length() > 255) erros.get("endereco").set("Endereço: preencha com até 255 caracteres.");
        // Na alteração, ignora o próprio registro ao verificar duplicidade.
        for (Leitor outro : dao.findAll()) {
            if (outro.getId() == id.get()) continue;
            if (texto(nome.get()).equalsIgnoreCase(texto(outro.getNome()))) erros.get("nome").set("Já existe cadastro com este valor.");
            if (texto(cpf.get()).equalsIgnoreCase(texto(outro.getCpf()))) erros.get("cpf").set("Já existe cadastro com este valor.");
            if (texto(telefone.get()).equalsIgnoreCase(texto(outro.getTelefone()))) erros.get("telefone").set("Já existe cadastro com este valor.");
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
    public void delete(Leitor item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto.trim()));
    }
}
