package edu.curso.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {
    @Override
    public void start(Stage stage) {
        TabPane abas = new TabPane();
        abas.getTabs().add(new Tab("Livros", new LivroView()));
        abas.getTabs().add(new Tab("Leitores", new LeitorView()));
        abas.getTabs().add(new Tab("Funcionários", new FuncionarioView()));
        abas.getTabs().add(new Tab("Editoras", new EditoraView()));
        for (Tab aba : abas.getTabs()) aba.setClosable(false);
        stage.setTitle("Sistema Biblioteca");
        stage.setScene(new Scene(abas, 1100, 650));
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
