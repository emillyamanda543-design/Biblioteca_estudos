package edu.curso.view;

import edu.curso.control.LivroController;
import edu.curso.model.Livro;
import edu.curso.infraestructure.LivroImplMariaDB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Tela de cadastro simples: selecione uma linha para editar ou excluir. */
// Os eventos JavaFX exigem parametros mesmo quando nao utilizados.
@SuppressWarnings("unused")
public class LivroView extends BorderPane {
    private final LivroController controle = new LivroController(new LivroImplMariaDB());
    private final TableView<Livro> tabela = new TableView<>();

    public LivroView() {
        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(8);
        formulario.setPadding(new Insets(12));

        TextField id = new TextField();
        id.setEditable(false);
        id.textProperty().bind(controle.idProperty().asString());
        formulario.addRow(0, new Label("ID:"), id);
        TextField campoTitulo = new TextField();
        campoTitulo.textProperty().bindBidirectional(controle.tituloProperty());
        formulario.addRow(1, new Label("Título:"), campoTitulo);
        TextField campoAutor = new TextField();
        campoAutor.textProperty().bindBidirectional(controle.autorProperty());
        formulario.addRow(2, new Label("Autor:"), campoAutor);
        TextField campoCodigo = new TextField();
        campoCodigo.textProperty().bindBidirectional(controle.codigoProperty());
        formulario.addRow(3, new Label("Código:"), campoCodigo);
        TextField campoEditora = new TextField();
        campoEditora.textProperty().bindBidirectional(controle.editoraProperty());
        formulario.addRow(4, new Label("Editora:"), campoEditora);
        DatePicker campoDataCadastro = new DatePicker();
        campoDataCadastro.valueProperty().bindBidirectional(controle.dataCadastroProperty());
        formulario.addRow(5, new Label("Data de cadastro:"), campoDataCadastro);

        Button salvar = new Button("Salvar");
        Button atualizar = new Button("Atualizar");
        Button excluir = new Button("Excluir selecionado");
        Button limpar = new Button("Limpar");
        TextField busca = new TextField();
        busca.setPromptText("Pesquisar por título");
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
        adicionarColuna("Título", item -> String.valueOf(item.getTitulo()));
        adicionarColuna("Autor", item -> String.valueOf(item.getAutor()));
        adicionarColuna("Código", item -> String.valueOf(item.getCodigo()));
        adicionarColuna("Editora", item -> String.valueOf(item.getEditora()));
        adicionarColuna("Data de cadastro", item -> String.valueOf(item.getDataCadastro()));

        tabela.setItems(controle.getLista());
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, selecionado) -> {
                if (selecionado != null) controle.fromEntity(selecionado);
            });
        setTop(formulario);
        setCenter(tabela);
    }

    // Uma coluna da tabela para cada atributo do cadastro.
    private void adicionarColuna(String titulo, java.util.function.Function<Livro, String> valor) {
        TableColumn<Livro, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(d -> new ReadOnlyStringWrapper(valor.apply(d.getValue())));
        tabela.getColumns().add(coluna);
    }

    private void excluirSelecionado() {
        Livro selecionado = tabela.getSelectionModel().getSelectedItem();
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
