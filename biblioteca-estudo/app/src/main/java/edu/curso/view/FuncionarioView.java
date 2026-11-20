package edu.curso.view;

import edu.curso.control.FuncionarioController;
import edu.curso.model.Funcionario;
import edu.curso.infraestructure.FuncionarioImplMariaDB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Tela de cadastro simples: selecione uma linha para editar ou excluir. */
// Os eventos JavaFX exigem parametros mesmo quando nao utilizados.
@SuppressWarnings("unused")
public class FuncionarioView extends BorderPane {
    private final FuncionarioController controle = new FuncionarioController(new FuncionarioImplMariaDB());
    private final TableView<Funcionario> tabela = new TableView<>();

    public FuncionarioView() {
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(8);
        formulario.setPadding(new Insets(12));

        TextField id = new TextField();
        id.setEditable(false);
        id.textProperty().bind(controle.idProperty().asString());
        formulario.addRow(0, new Label("ID:"), id);
        TextField campoNome = new TextField();
        campoNome.textProperty().bindBidirectional(controle.nomeProperty());
        formulario.addRow(1, new Label("Nome:"), campoNome);
        TextField campoCpf = new TextField();
        campoCpf.textProperty().bindBidirectional(controle.cpfProperty());
        formulario.addRow(2, new Label("CPF:"), campoCpf);
        TextField campoTelefone = new TextField();
        campoTelefone.textProperty().bindBidirectional(controle.telefoneProperty());
        formulario.addRow(3, new Label("Telefone:"), campoTelefone);
        TextField campoCargo = new TextField();
        campoCargo.textProperty().bindBidirectional(controle.cargoProperty());
        formulario.addRow(4, new Label("Cargo:"), campoCargo);
        DatePicker campoDataAdmissao = new DatePicker();
        campoDataAdmissao.valueProperty().bindBidirectional(controle.dataAdmissaoProperty());
        formulario.addRow(5, new Label("Data de admissão:"), campoDataAdmissao);

        Button salvar = new Button("Salvar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir selecionado");
        Button limpar = new Button("Limpar");
        TextField busca = new TextField();
        busca.setPromptText("Pesquisar por nome");
        Button pesquisar = new Button("Pesquisar");

        salvar.setOnAction(e -> executar(() -> controle.save()));
        atualizar.setOnAction(e -> executar(() -> controle.update()));
        excluir.setOnAction(e -> excluirSelecionado());
        limpar.setOnAction(e -> { tabela.getSelectionModel().clearSelection(); controle.clearFields(); });
        pesquisar.setOnAction(e -> executar(() -> controle.searchByName(busca.getText())));

        HBox acoes = new HBox(8, salvar, atualizar, excluir, limpar);
        formulario.add(acoes, 0, 6, 2, 1);
        HBox pesquisa = new HBox(8, busca, pesquisar);
        formulario.add(pesquisa, 0, 7, 2, 1);

        adicionarColuna("ID", item -> String.valueOf(item.getId()));
        adicionarColuna("Nome", item -> String.valueOf(item.getNome()));
        adicionarColuna("CPF", item -> String.valueOf(item.getCpf()));
        adicionarColuna("Telefone", item -> String.valueOf(item.getTelefone()));
        adicionarColuna("Cargo", item -> String.valueOf(item.getCargo()));
        adicionarColuna("Data de admissão", item -> String.valueOf(item.getDataAdmissao()));

        tabela.setItems(controle.getLista());
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, selecionado) -> {
                if (selecionado != null) controle.fromEntity(selecionado);
            });
        setTop(formulario);
        setCenter(tabela);
    }

    // Uma coluna da tabela para cada atributo do cadastro.
    private void adicionarColuna(String titulo, java.util.function.Function<Funcionario, String> valor) {
        TableColumn<Funcionario, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(d -> new ReadOnlyStringWrapper(valor.apply(d.getValue())));
        tabela.getColumns().add(coluna);
    }

    private void excluirSelecionado() {
        Funcionario selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mensagem("Selecione uma linha na tabela para excluir.");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
            "Excluir este cadastro?", ButtonType.YES, ButtonType.NO);
        if (confirmacao.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            executar(() -> controle.delete(selecionado));
        }
    }

    private void executar(Runnable tarefa) {
        try { tarefa.run(); }
        catch (RuntimeException erro) { mensagem(erro.getMessage()); }
    }

    private void mensagem(String texto) {
        new Alert(Alert.AlertType.ERROR, texto, ButtonType.OK).showAndWait();
    }
}
