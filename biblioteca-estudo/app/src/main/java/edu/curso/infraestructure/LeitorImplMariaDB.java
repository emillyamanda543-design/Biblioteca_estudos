package edu.curso.infraestructure;
import edu.curso.control.LeitorDAO;
import edu.curso.model.Leitor;
import java.sql.*;
import java.util.*;
public class LeitorImplMariaDB implements LeitorDAO {
    private Leitor ler(ResultSet rs) throws SQLException {
        Leitor item = new Leitor();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setCpf(rs.getString("cpf"));
        item.setTelefone(rs.getString("telefone"));
        item.setEmail(rs.getString("email"));
        item.setDataNascimento(rs.getDate("data_nascimento") == null ? null : rs.getDate("data_nascimento").toLocalDate());
        return item;
    }
    public void save(Leitor item) {
        String sql = "INSERT INTO leitores (nome, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getCpf());
            ps.setString(3, item.getTelefone());
            ps.setString(4, item.getEmail());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataNascimento()));
            ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao salvar Leitor: " + e.getMessage(), e); }
    }
    public void update(long id, Leitor item) {
        String sql = "UPDATE leitores SET nome=?, cpf=?, telefone=?, email=?, data_nascimento=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getCpf());
            ps.setString(3, item.getTelefone());
            ps.setString(4, item.getEmail());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataNascimento()));
            ps.setLong(6, id);
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Cadastro não encontrado.");
        } catch (SQLException e) { throw new IllegalStateException("Erro ao atualizar Leitor: " + e.getMessage(), e); }
    }
    public void delete(Leitor item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM leitores WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao excluir Leitor: " + e.getMessage(), e); }
    }
    public List<Leitor> findAll() {
        List<Leitor> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM leitores ORDER BY id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(ler(rs));
        } catch (SQLException e) { throw new IllegalStateException("Erro ao listar Leitor: " + e.getMessage(), e); }
        return lista;
    }
    public Leitor searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM leitores WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Leitor: " + e.getMessage(), e); }
    }
    public List<Leitor> searchByName(String texto) {
        List<Leitor> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM leitores WHERE nome LIKE ? ORDER BY id")) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Leitor: " + e.getMessage(), e); }
        return lista;
    }
}
