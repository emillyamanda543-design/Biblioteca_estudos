package edu.curso.infraestructure;

import edu.curso.control.LivroDAO;
import edu.curso.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementação JDBC: cada operação fecha conexão e recursos automaticamente. */
public class LivroImplMariaDB implements LivroDAO {
    private Livro ler(ResultSet rs) throws SQLException {
        Livro item = new Livro();
        item.setId(rs.getLong("id"));
        item.setTitulo(rs.getString("titulo"));
        item.setAutor(rs.getString("autor"));
        item.setCodigo(rs.getString("codigo"));
        item.setEditora(rs.getString("editora"));
        item.setDataCadastro(rs.getDate("data_cadastro") == null ? null : rs.getDate("data_cadastro").toLocalDate());
        item.setGenero(rs.getString("genero"));
        return item;
    }
    /** Define os parâmetros na mesma ordem do INSERT e UPDATE. */
    private void preencher(PreparedStatement ps, Livro item) throws SQLException {
        ps.setString(1, item.getTitulo());
        ps.setString(2, item.getAutor());
        ps.setString(3, item.getCodigo());
        ps.setString(4, item.getEditora());
        ps.setDate(5, java.sql.Date.valueOf(item.getDataCadastro()));
        ps.setString(6, item.getGenero());
    }
    @Override
    public void save(Livro item) {
        String sql = "INSERT INTO livros (titulo, autor, codigo, editora, data_cadastro, genero) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void update(long id, Livro item) {
        String sql = "UPDATE livros SET titulo=?, autor=?, codigo=?, editora=?, data_cadastro=?, genero=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.setLong(7, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void delete(Livro item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM livros WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Livro> findAll() { return consultar("SELECT * FROM livros ORDER BY id", null); }
    @Override
    public Livro searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM livros WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Livro> searchByName(String texto) {
        return consultar("SELECT * FROM livros WHERE titulo LIKE ? OR autor LIKE ? OR codigo LIKE ? OR editora LIKE ? ORDER BY id", texto);
    }
    private List<Livro> consultar(String sql, String texto) {
        List<Livro> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (texto != null) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
                ps.setString(3, "%" + texto + "%");
                ps.setString(4, "%" + texto + "%");
            }
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
        return lista;
    }
}
