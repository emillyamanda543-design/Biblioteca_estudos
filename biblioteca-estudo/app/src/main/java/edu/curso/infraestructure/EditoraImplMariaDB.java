package edu.curso.infraestructure;
import edu.curso.control.EditoraDAO;
import edu.curso.model.Editora;
import java.sql.*;
import java.util.*;
public class EditoraImplMariaDB implements EditoraDAO {
    private Editora ler(ResultSet rs) throws SQLException {
        Editora item = new Editora();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setTelefone(rs.getString("telefone"));
        item.setEmail(rs.getString("email"));
        item.setDataFundacao(rs.getDate("data_fundacao") == null ? null : rs.getDate("data_fundacao").toLocalDate());
        return item;
    }
    public void save(Editora item) {
        String sql = "INSERT INTO editoras (nome, telefone, email, data_fundacao) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getTelefone());
            ps.setString(3, item.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(item.getDataFundacao()));
            ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao salvar Editora: " + e.getMessage(), e); }
    }
    public void update(long id, Editora item) {
        String sql = "UPDATE editoras SET nome=?, telefone=?, email=?, data_fundacao=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getTelefone());
            ps.setString(3, item.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(item.getDataFundacao()));
            ps.setLong(5, id);
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Cadastro não encontrado.");
        } catch (SQLException e) { throw new IllegalStateException("Erro ao atualizar Editora: " + e.getMessage(), e); }
    }
    public void delete(Editora item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM editoras WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao excluir Editora: " + e.getMessage(), e); }
    }
    public List<Editora> findAll() {
        List<Editora> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM editoras ORDER BY id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(ler(rs));
        } catch (SQLException e) { throw new IllegalStateException("Erro ao listar Editora: " + e.getMessage(), e); }
        return lista;
    }
    public Editora searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM editoras WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Editora: " + e.getMessage(), e); }
    }
    public List<Editora> searchByName(String texto) {
        List<Editora> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM editoras WHERE nome LIKE ? ORDER BY id")) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Editora: " + e.getMessage(), e); }
        return lista;
    }
}
