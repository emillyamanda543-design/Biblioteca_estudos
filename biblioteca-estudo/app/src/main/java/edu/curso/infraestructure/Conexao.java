package edu.curso.infraestructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza a conexão JDBC; as configurações não precisam ser alteradas nos DAOs. */
public final class Conexao {
    private Conexao() {}

    public static Connection abrir() throws SQLException {
        String url = System.getenv().getOrDefault("BIBLIOTECA_DB_URL", "jdbc:mariadb://localhost:3306/biblioteca");
        String usuario = System.getenv().getOrDefault("BIBLIOTECA_DB_USER", "root");
        String senha = System.getenv("BIBLIOTECA_DB_PASSWORD");
        if (senha == null) throw new SQLException("Configure BIBLIOTECA_DB_PASSWORD antes de executar.");
        return DriverManager.getConnection(url, usuario, senha);
    }

    public static RuntimeException erro(SQLException erro) {
        if (erro.getErrorCode() == 1062)
            return new IllegalStateException("Já existe cadastro com um dos valores informados. Confira os campos únicos.", erro);
        return new IllegalStateException("Falha no banco: " + erro.getMessage(), erro);
    }
}
