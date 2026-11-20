package edu.curso.control;

import edu.curso.model.Funcionario;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.property.*;
import javafx.collections.*;

/** Controle: mantém propriedades ligadas à tela e valida antes de gravar. */
public class FuncionarioController {
    private final FuncionarioDAO dao;
    private final ObservableList<Funcionario> lista = FXCollections.observableArrayList();
    private final LongProperty id = new SimpleLongProperty(0);
    private final Map<String, StringProperty> erros = new LinkedHashMap<>();
    private final StringProperty nome = new SimpleStringProperty("");
    private final StringProperty cpf = new SimpleStringProperty("");
    private final StringProperty telefone = new SimpleStringProperty("");
    private final StringProperty cargo = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataAdmissao = new SimpleObjectProperty<>();
    private final StringProperty salario = new SimpleStringProperty("");

    public FuncionarioController(FuncionarioDAO dao) {
        this.dao = dao;
        erros.put("nome", new SimpleStringProperty(""));
        erros.put("cpf", new SimpleStringProperty(""));
        erros.put("telefone", new SimpleStringProperty(""));
        erros.put("cargo", new SimpleStringProperty(""));
        erros.put("dataAdmissao", new SimpleStringProperty(""));
        erros.put("salario", new SimpleStringProperty(""));
        load();
    }
    public StringProperty erroProperty(String campo) { return erros.get(campo); }
    public LongProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cpfProperty() { return cpf; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty cargoProperty() { return cargo; }
    public ObjectProperty<LocalDate> dataAdmissaoProperty() { return dataAdmissao; }
    public StringProperty salarioProperty() { return salario; }
    public ObservableList<Funcionario> getLista() { return lista; }
    public void load() { lista.setAll(dao.findAll()); }

    /** Limpa os campos e as mensagens, preparando um novo cadastro. */
    public void clearFields() {
        id.set(0);
        erros.values().forEach(erro -> erro.set(""));
        nome.set("");
        cpf.set("");
        telefone.set("");
        cargo.set("");
        dataAdmissao.set(null);
        salario.set("");
    }

    /** Copia o registro escolhido na tabela para o formulário. */
    public void fromEntity(Funcionario item) {
        if (item == null) return;
        erros.values().forEach(erro -> erro.set(""));
        id.set(item.getId());
        nome.set(item.getNome());
        cpf.set(item.getCpf());
        telefone.set(item.getTelefone());
        cargo.set(item.getCargo());
        dataAdmissao.set(item.getDataAdmissao());
        salario.set(item.getSalario() == null ? "" : item.getSalario().toPlainString());
    }

    /** Cria a entidade com os valores das propriedades do controle. */
    public Funcionario toEntity() {
        Funcionario item = new Funcionario();
        item.setId(id.get());
        item.setNome(texto(nome.get()));
        item.setCpf(texto(cpf.get()));
        item.setTelefone(texto(telefone.get()));
        item.setCargo(texto(cargo.get()));
        item.setDataAdmissao(dataAdmissao.get());
        item.setSalario(new BigDecimal(texto(salario.get()).replace(",", ".")));
        return item;
    }
    private String texto(String valor) { return valor == null ? "" : valor.trim(); }

    /** Apresenta todos os erros simultaneamente, ao lado de cada campo. */
    private void validate() {
        erros.values().forEach(erro -> erro.set(""));
        if (texto(nome.get()).isBlank() || texto(nome.get()).length() > 255) erros.get("nome").set("Nome: preencha com até 255 caracteres.");
        if (!Validacoes.cpf(texto(cpf.get()))) erros.get("cpf").set("CPF: informe 11 números e dígitos verificadores válidos.");
        if (!texto(telefone.get()).matches("[0-9]{10,11}")) erros.get("telefone").set("Telefone: informe DDD e 10 ou 11 números.");
        if (texto(cargo.get()).isBlank() || texto(cargo.get()).length() > 100) erros.get("cargo").set("Cargo: preencha com até 100 caracteres.");
        if (dataAdmissao.get() == null || dataAdmissao.get().isAfter(LocalDate.now())) erros.get("dataAdmissao").set("Data de admissão: informe uma data válida, não futura.");
        try {
            BigDecimal valor = new BigDecimal(texto(salario.get()).replace(",", "."));
            if (valor.signum() <= 0 || valor.scale() > 2 || valor.compareTo(new BigDecimal("9999999999.99")) > 0)
                erros.get("salario").set("Salário: informe um valor positivo com até duas casas decimais.");
        } catch (NumberFormatException ex) { erros.get("salario").set("Salário: informe um número válido."); }
        // Na alteração, ignora o próprio registro ao verificar duplicidade.
        for (Funcionario outro : dao.findAll()) {
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
    public void delete(Funcionario item) {
        if (item == null) throw new IllegalArgumentException("Selecione um cadastro.");
        dao.delete(item); load(); clearFields();
    }
    public void searchByName(String texto) {
        if (texto == null || texto.isBlank()) { load(); return; }
        lista.setAll(dao.searchByName(texto.trim()));
    }
}
