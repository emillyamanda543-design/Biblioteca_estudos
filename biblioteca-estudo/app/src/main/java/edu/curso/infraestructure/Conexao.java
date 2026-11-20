package edu.curso.infraestructure;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexao {
    public static Connection abrir() throws SQLException {
        String url = System.getenv().getOrDefault("BIBLIOTECA_DB_URL", "jdbc:mariadb://localhost:3306/biblioteca");
        String usuario = System.getenv().getOrDefault("BIBLIOTECA_DB_USER", "root");
        String senha = System.getenv().getOrDefault("BIBLIOTECA_DB_PASSWORD", "alunofatec");
        return DriverManager.getConnection(url, usuario, senha);
    }
}
