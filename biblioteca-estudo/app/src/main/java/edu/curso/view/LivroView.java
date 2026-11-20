package edu.curso.view;

import edu.curso.control.LivroController;
import edu.curso.model.Livro;
import edu.curso.infraestructure.LivroImplMariaDB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Fronteira: formulário com bindings e tabela com ações por registro, sem FXML. */
public class LivroView extends BorderPane {
    private final LivroController controle = new LivroController(new LivroImplMariaDB());
    private final TableView<Livro> tabela = new TableView<>();

    public LivroView() {
        setPadding(new Insets(16));
        Label titulo = new Label("Cadastro de Livro");
        titulo.setStyle("-fx-font-size: 22px;");
        GridPane formulario = new GridPane();
        formulario.setHgap(12); formulario.setVgap(6);
        TextField id = new TextField(); id.setEditable(false);
        id.textProperty().bind(controle.idProperty().asString());
        formulario.addRow(0, new Label("ID:"), id);
        TextField campoTitulo = new TextField();
        campoTitulo.textProperty().bindBidirectional(controle.tituloProperty());
        Label erroTitulo = new Label();
        erroTitulo.textProperty().bind(controle.erroProperty("titulo"));
        erroTitulo.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(1, new Label("Título:"), campoTitulo, erroTitulo);
        TextField campoAutor = new TextField();
        campoAutor.textProperty().bindBidirectional(controle.autorProperty());
        Label erroAutor = new Label();
        erroAutor.textProperty().bind(controle.erroProperty("autor"));
        erroAutor.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(2, new Label("Autor:"), campoAutor, erroAutor);
        TextField campoCodigo = new TextField();
        campoCodigo.textProperty().bindBidirectional(controle.codigoProperty());
        Label erroCodigo = new Label();
        erroCodigo.textProperty().bind(controle.erroProperty("codigo"));
        erroCodigo.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(3, new Label("Código:"), campoCodigo, erroCodigo);
        TextField campoEditora = new TextField();
        campoEditora.textProperty().bindBidirectional(controle.editoraProperty());
        Label erroEditora = new Label();
        erroEditora.textProperty().bind(controle.erroProperty("editora"));
        erroEditora.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(4, new Label("Editora:"), campoEditora, erroEditora);
        DatePicker campoDataCadastro = new DatePicker();
        campoDataCadastro.setEditable(false);
        campoDataCadastro.valueProperty().bindBidirectional(controle.dataCadastroProperty());
        Label erroDataCadastro = new Label();
        erroDataCadastro.textProperty().bind(controle.erroProperty("dataCadastro"));
        erroDataCadastro.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(5, new Label("Data de cadastro:"), campoDataCadastro, erroDataCadastro);
        TextField campoGenero = new TextField();
        campoGenero.textProperty().bindBidirectional(controle.generoProperty());
        Label erroGenero = new Label();
        erroGenero.textProperty().bind(controle.erroProperty("genero"));
        erroGenero.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(6, new Label("Gênero:"), campoGenero, erroGenero);
        // Ações de gravação ficam próximas ao formulário.
        Button cadastrar = new Button("Cadastrar");
        cadastrar.setOnAction(evento -> { evento.consume(); executar(controle::save); });
        Button atualizar = new Button("Salvar alterações");
        atualizar.setOnAction(evento -> { evento.consume(); executar(controle::update); });
        HBox gravacao = new HBox(10, cadastrar, atualizar);
        ScrollPane painel = new ScrollPane(new VBox(10, titulo, formulario, gravacao));
        painel.setFitToWidth(true); painel.setPrefViewportHeight(330);
        setTop(painel);

        TextField busca = new TextField();
        busca.setPromptText("Título, autor, código ou editora");
        Button pesquisar = new Button("Pesquisar");
        pesquisar.setOnAction(evento -> { evento.consume(); executar(() -> controle.searchByName(busca.getText())); });
        busca.setOnAction(evento -> { evento.consume(); pesquisar.fire(); });
        HBox pesquisa = new HBox(8, busca, pesquisar); HBox.setHgrow(busca, Priority.ALWAYS);
        adicionarColuna("ID", item -> String.valueOf(item.getId()));
        adicionarColuna("Título", item -> String.valueOf(item.getTitulo()));
        adicionarColuna("Autor", item -> String.valueOf(item.getAutor()));
        adicionarColuna("Código", item -> String.valueOf(item.getCodigo()));
        adicionarColuna("Editora", item -> String.valueOf(item.getEditora()));
        adicionarColuna("Data de cadastro", item -> String.valueOf(item.getDataCadastro()));
        adicionarColuna("Gênero", item -> String.valueOf(item.getGenero()));
        adicionarAcoes();
        tabela.setItems(controle.getLista());
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPlaceholder(new Label("Nenhum cadastro encontrado."));
        VBox centro = new VBox(10, pesquisa, tabela);
        VBox.setVgrow(tabela, Priority.ALWAYS);
        BorderPane.setMargin(centro, new Insets(12, 0, 12, 0)); setCenter(centro);
        Button limpar = new Button("Limpar formulário");
        limpar.setOnAction(evento -> { evento.consume(); tabela.getSelectionModel().clearSelection(); controle.clearFields(); });
        setBottom(limpar);
    }
    private void adicionarColuna(String titulo, java.util.function.Function<Livro, String> valor) {
        TableColumn<Livro, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(dado -> new ReadOnlyStringWrapper(valor.apply(dado.getValue())));
        tabela.getColumns().add(coluna);
    }
    /** Os botões usam o item da própria linha, inclusive após pesquisa e ordenação. */
    private void adicionarAcoes() {
        TableColumn<Livro, Void> acoes = new TableColumn<>("Ações");
        acoes.setMinWidth(170); acoes.setSortable(false);
        acoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button editar = new Button("Editar");
            private final Button excluir = new Button("Excluir");
            private final HBox botoes = new HBox(6, editar, excluir);
            {
                editar.setOnAction(evento -> {
                    evento.consume();
                    Livro item = getTableRow().getItem();
                    if (item != null) controle.fromEntity(item);
                });
                excluir.setOnAction(evento -> {
                    evento.consume();
                    Livro item = getTableRow().getItem();
                    if (item == null) return;
                    Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION,
                        "Excluir o cadastro ID " + item.getId() + "?", ButtonType.YES, ButtonType.NO);
                    if (confirmar.showAndWait().orElse(ButtonType.NO) == ButtonType.YES)
                        executar(() -> controle.delete(item));
                });
            }
            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                setGraphic(vazio ? null : botoes);
            }
        });
        tabela.getColumns().add(acoes);
    }
    private void executar(Runnable tarefa) {
        try { tarefa.run(); }
        catch (RuntimeException erro) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, erro.getMessage(), ButtonType.OK);
            alerta.setHeaderText("Não foi possível concluir a operação"); alerta.showAndWait();
        }
    }
}
