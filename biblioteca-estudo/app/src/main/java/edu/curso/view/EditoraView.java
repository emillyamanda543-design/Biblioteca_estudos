package edu.curso.view;

import edu.curso.control.EditoraController;
import edu.curso.model.Editora;
import edu.curso.infraestructure.EditoraImplMariaDB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Fronteira: formulário com bindings e tabela com ações por registro, sem FXML. */
public class EditoraView extends BorderPane {
    private final EditoraController controle = new EditoraController(new EditoraImplMariaDB());
    private final TableView<Editora> tabela = new TableView<>();

    public EditoraView() {
        setPadding(new Insets(16));
        Label titulo = new Label("Cadastro de Editora");
        titulo.setStyle("-fx-font-size: 22px;");
        GridPane formulario = new GridPane();
        formulario.setHgap(12); formulario.setVgap(6);
        TextField id = new TextField(); id.setEditable(false);
        id.textProperty().bind(controle.idProperty().asString());
        formulario.addRow(0, new Label("ID:"), id);
        TextField campoNome = new TextField();
        campoNome.textProperty().bindBidirectional(controle.nomeProperty());
        Label erroNome = new Label();
        erroNome.textProperty().bind(controle.erroProperty("nome"));
        erroNome.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(1, new Label("Nome:"), campoNome, erroNome);
        TextField campoTelefone = new TextField();
        campoTelefone.textProperty().bindBidirectional(controle.telefoneProperty());
        Label erroTelefone = new Label();
        erroTelefone.textProperty().bind(controle.erroProperty("telefone"));
        erroTelefone.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(2, new Label("Telefone:"), campoTelefone, erroTelefone);
        TextField campoEmail = new TextField();
        campoEmail.textProperty().bindBidirectional(controle.emailProperty());
        Label erroEmail = new Label();
        erroEmail.textProperty().bind(controle.erroProperty("email"));
        erroEmail.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(3, new Label("E-mail:"), campoEmail, erroEmail);
        DatePicker campoDataFundacao = new DatePicker();
        campoDataFundacao.setEditable(false);
        campoDataFundacao.valueProperty().bindBidirectional(controle.dataFundacaoProperty());
        Label erroDataFundacao = new Label();
        erroDataFundacao.textProperty().bind(controle.erroProperty("dataFundacao"));
        erroDataFundacao.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(4, new Label("Data de fundação:"), campoDataFundacao, erroDataFundacao);
        TextField campoCnpj = new TextField();
        campoCnpj.textProperty().bindBidirectional(controle.cnpjProperty());
        Label erroCnpj = new Label();
        erroCnpj.textProperty().bind(controle.erroProperty("cnpj"));
        erroCnpj.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(5, new Label("CNPJ:"), campoCnpj, erroCnpj);
        TextField campoCidade = new TextField();
        campoCidade.textProperty().bindBidirectional(controle.cidadeProperty());
        Label erroCidade = new Label();
        erroCidade.textProperty().bind(controle.erroProperty("cidade"));
        erroCidade.setStyle("-fx-text-fill: #b00020;");
        formulario.addRow(6, new Label("Cidade:"), campoCidade, erroCidade);
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
        busca.setPromptText("Pesquisar por nome");
        Button pesquisar = new Button("Pesquisar");
        pesquisar.setOnAction(evento -> { evento.consume(); executar(() -> controle.searchByName(busca.getText())); });
        busca.setOnAction(evento -> { evento.consume(); pesquisar.fire(); });
        HBox pesquisa = new HBox(8, busca, pesquisar); HBox.setHgrow(busca, Priority.ALWAYS);
        adicionarColuna("ID", item -> String.valueOf(item.getId()));
        adicionarColuna("Nome", item -> String.valueOf(item.getNome()));
        adicionarColuna("Telefone", item -> String.valueOf(item.getTelefone()));
        adicionarColuna("E-mail", item -> String.valueOf(item.getEmail()));
        adicionarColuna("Data de fundação", item -> String.valueOf(item.getDataFundacao()));
        adicionarColuna("CNPJ", item -> String.valueOf(item.getCnpj()));
        adicionarColuna("Cidade", item -> String.valueOf(item.getCidade()));
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
    private void adicionarColuna(String titulo, java.util.function.Function<Editora, String> valor) {
        TableColumn<Editora, String> coluna = new TableColumn<>(titulo);
        coluna.setCellValueFactory(dado -> new ReadOnlyStringWrapper(valor.apply(dado.getValue())));
        tabela.getColumns().add(coluna);
    }
    /** Os botões usam o item da própria linha, inclusive após pesquisa e ordenação. */
    private void adicionarAcoes() {
        TableColumn<Editora, Void> acoes = new TableColumn<>("Ações");
        acoes.setMinWidth(170); acoes.setSortable(false);
        acoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button editar = new Button("Editar");
            private final Button excluir = new Button("Excluir");
            private final HBox botoes = new HBox(6, editar, excluir);
            {
                editar.setOnAction(evento -> {
                    evento.consume();
                    Editora item = getTableRow().getItem();
                    if (item != null) controle.fromEntity(item);
                });
                excluir.setOnAction(evento -> {
                    evento.consume();
                    Editora item = getTableRow().getItem();
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
