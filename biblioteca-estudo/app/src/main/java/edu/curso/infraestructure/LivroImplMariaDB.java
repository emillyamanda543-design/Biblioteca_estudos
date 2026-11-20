package edu.curso.infraestructure;
import edu.curso.control.LivroDAO;
import edu.curso.model.Livro;
import java.sql.*;
import java.util.*;
public class LivroImplMariaDB implements LivroDAO {
    private Livro ler(ResultSet rs) throws SQLException {
        Livro item = new Livro();
        item.setId(rs.getLong("id"));
        item.setTitulo(rs.getString("titulo"));
        item.setAutor(rs.getString("autor"));
        item.setCodigo(rs.getString("codigo"));
        item.setEditora(rs.getString("editora"));
        item.setDataCadastro(rs.getDate("data_cadastro") == null ? null : rs.getDate("data_cadastro").toLocalDate());
        return item;
    }
    public void save(Livro item) {
        String sql = "INSERT INTO livros (titulo, autor, codigo, editora, data_cadastro) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getTitulo());
            ps.setString(2, item.getAutor());
            ps.setString(3, item.getCodigo());
            ps.setString(4, item.getEditora());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataCadastro()));
            ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao salvar Livro: " + e.getMessage(), e); }
    }
    public void update(long id, Livro item) {
        String sql = "UPDATE livros SET titulo=?, autor=?, codigo=?, editora=?, data_cadastro=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getTitulo());
            ps.setString(2, item.getAutor());
            ps.setString(3, item.getCodigo());
            ps.setString(4, item.getEditora());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataCadastro()));
            ps.setLong(6, id);
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Cadastro não encontrado.");
        } catch (SQLException e) { throw new IllegalStateException("Erro ao atualizar Livro: " + e.getMessage(), e); }
    }
    public void delete(Livro item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM livros WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao excluir Livro: " + e.getMessage(), e); }
    }
    public List<Livro> findAll() {
        List<Livro> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM livros ORDER BY id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(ler(rs));
        } catch (SQLException e) { throw new IllegalStateException("Erro ao listar Livro: " + e.getMessage(), e); }
        return lista;
    }
    public Livro searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM livros WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Livro: " + e.getMessage(), e); }
    }
    public List<Livro> searchByName(String texto) {
        List<Livro> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM livros WHERE titulo LIKE ? ORDER BY id")) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Livro: " + e.getMessage(), e); }
        return lista;
    }
}
