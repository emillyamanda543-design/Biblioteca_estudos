package edu.curso.control;

import edu.curso.model.Editora;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.property.*;
import javafx.collections.*;

/** Controle: mantém propriedades ligadas à tela e valida antes de gravar. */
public class EditoraController {
    private final EditoraDAO dao;
    private final ObservableList<Editora> lista = FXCollections.observableArrayList();
    private final LongProperty id = new SimpleLongProperty(0);
    private final Map<String, StringProperty> erros = new LinkedHashMap<>();
    private final StringProperty nome = new SimpleStringProperty("");
    private final StringProperty telefone = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataFundacao = new SimpleObjectProperty<>();
    private final StringProperty cnpj = new SimpleStringProperty("");
    private final StringProperty cidade = new SimpleStringProperty("");

    public EditoraController(EditoraDAO dao) {
        this.dao = dao;
        erros.put("nome", new SimpleStringProperty(""));
        erros.put("telefone", new SimpleStringProperty(""));
        erros.put("email", new SimpleStringProperty(""));
        erros.put("dataFundacao", new SimpleStringProperty(""));
        erros.put("cnpj", new SimpleStringProperty(""));
        erros.put("cidade", new SimpleStringProperty(""));
        load();
    }
    public StringProperty erroProperty(String campo) { return erros.get(campo); }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty emailProperty() { return email; }
    public ObjectProperty<LocalDate> dataFundacaoProperty() { return dataFundacao; }
    public StringProperty cnpjProperty() { return cnpj; }
    public StringProperty cidadeProperty() { return cidade; }
    public ObservableList<Editora> getLista() { return lista; }
    public void load() { lista.setAll(dao.findAll()); }

    /** Limpa os campos e as mensagens, preparando um novo cadastro. */
    public void clearFields() {
        id.set(0);
        erros.values().forEach(erro -> erro.set(""));
        nome.set("");
        telefone.set("");
        email.set("");
        dataFundacao.set(null);
        cnpj.set("");
        cidade.set("");
    }

    /** Copia o registro escolhido na tabela para o formulário. */
    public void fromEntity(Editora item) {
        if (item == null) return;
        erros.values().forEach(erro -> erro.set(""));
        id.set(item.getId());
        nome.set(item.getNome());
        telefone.set(item.getTelefone());
        email.set(item.getEmail());
        dataFundacao.set(item.getDataFundacao());
        cnpj.set(item.getCnpj());
        cidade.set(item.getCidade());
    }

    /** Cria a entidade com os valores das propriedades do controle. */
    public Editora toEntity() {
        Editora item = new Editora();
        item.setId(id.get());
        item.setNome(texto(nome.get()));
        item.setTelefone(texto(telefone.get()));
        item.setEmail(texto(email.get()));
        item.setDataFundacao(dataFundacao.get());
        item.setCnpj(texto(cnpj.get()));
        item.setCidade(texto(cidade.get()));
        return item;
    }
    private String texto(String valor) { return valor == null ? "" : valor.trim(); }

    /** Apresenta todos os erros simultaneamente, ao lado de cada campo. */
    private void validate() {
        erros.values().forEach(erro -> erro.set(""));
        if (texto(nome.get()).isBlank() || texto(nome.get()).length() > 255) erros.get("nome").set("Nome: preencha com até 255 caracteres.");
        if (!texto(telefone.get()).matches("[0-9]{10,11}")) erros.get("telefone").set("Telefone: informe DDD e 10 ou 11 números.");
        if (!texto(email.get()).matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+") || texto(email.get()).length() > 255) erros.get("email").set("E-mail: informe um endereço válido.");
        if (dataFundacao.get() == null || dataFundacao.get().isAfter(LocalDate.now())) erros.get("dataFundacao").set("Data de fundação: informe uma data válida, não futura.");
        if (!Validacoes.cnpj(texto(cnpj.get()))) erros.get("cnpj").set("CNPJ: informe 14 números e dígitos verificadores válidos.");
        if (texto(cidade.get()).isBlank() || texto(cidade.get()).length() > 255) erros.get("cidade").set("Cidade: preencha com até 255 caracteres.");
        // Na alteração, ignora o próprio registro ao verificar duplicidade.
        for (Editora outro : dao.findAll()) {
            if (outro.getId() == id.get()) continue;
            if (texto(telefone.get()).equalsIgnoreCase(texto(outro.getTelefone()))) erros.get("telefone").set("Já existe cadastro com este valor.");
            if (texto(cnpj.get()).equalsIgnoreCase(texto(outro.getCnpj()))) erros.get("cnpj").set("Já existe cadastro com este valor.");
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
    public void delete(Editora item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto.trim()));
    }
}
