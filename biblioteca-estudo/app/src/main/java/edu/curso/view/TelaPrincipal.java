package edu.curso.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/** Janela principal: início, menus de cadastros e encerramento. */
public class TelaPrincipal extends Application {
    private final BorderPane principal = new BorderPane();

    @Override
    public void start(Stage stage) {
        Label titulo = new Label("Minha Biblioteca");
        titulo.setStyle("-fx-font-size: 32px;");
        FlowPane atalhos = new FlowPane(12, 12);
        atalhos.setAlignment(Pos.CENTER);
        VBox inicio = new VBox(20, titulo, new Label("Selecione um cadastro para começar."), atalhos);
        inicio.setAlignment(Pos.CENTER);
        Menu arquivo = new Menu("Arquivo");
        MenuItem inicial = new MenuItem("Início");
        inicial.setOnAction(evento -> { evento.consume(); principal.setCenter(inicio); });
        MenuItem sair = new MenuItem("Sair");
        sair.setOnAction(evento -> { evento.consume(); stage.close(); });
        arquivo.getItems().addAll(inicial, sair);
        Menu cadastros = new Menu("Cadastros");
        for (String nome : new String[]{"Livros", "Leitores", "Funcionários", "Editoras"}) {
            Button botao = new Button(nome);
            botao.setOnAction(evento -> { evento.consume(); abrir(nome); });
            atalhos.getChildren().add(botao);
            MenuItem item = new MenuItem(nome);
            item.setOnAction(evento -> { evento.consume(); abrir(nome); });
            cadastros.getItems().add(item);
        }
        principal.setTop(new MenuBar(arquivo, cadastros));
        principal.setCenter(inicio);
        stage.setTitle("Minha Biblioteca");
        stage.setScene(new Scene(principal, 1200, 760));
        stage.show();
    }

    /** Carrega o cadastro ao abrir; falhas de conexão são apresentadas ao usuário. */
    private void abrir(String nome) {
        try {
            Pane tela = switch (nome) {
                case "Livros" -> new LivroView();
                case "Leitores" -> new LeitorView();
                case "Funcionários" -> new FuncionarioView();
                case "Editoras" -> new EditoraView();
                default -> throw new IllegalArgumentException("Cadastro desconhecido.");
            };
            principal.setCenter(tela);
        } catch (RuntimeException erro) {
            new Alert(Alert.AlertType.ERROR, erro.getMessage(), ButtonType.OK).showAndWait();
        }
    }
    public static void main(String[] args) { launch(args); }
}
