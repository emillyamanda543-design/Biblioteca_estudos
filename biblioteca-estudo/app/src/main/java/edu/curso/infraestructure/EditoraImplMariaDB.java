package edu.curso.infraestructure;

import edu.curso.control.EditoraDAO;
import edu.curso.model.Editora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementação JDBC: cada operação fecha conexão e recursos automaticamente. */
public class EditoraImplMariaDB implements EditoraDAO {
    private Editora ler(ResultSet rs) throws SQLException {
        Editora item = new Editora();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setTelefone(rs.getString("telefone"));
        item.setEmail(rs.getString("email"));
        item.setDataFundacao(rs.getDate("data_fundacao") == null ? null : rs.getDate("data_fundacao").toLocalDate());
        item.setCnpj(rs.getString("cnpj"));
        item.setCidade(rs.getString("cidade"));
        return item;
    }
    /** Define os parâmetros na mesma ordem do INSERT e UPDATE. */
    private void preencher(PreparedStatement ps, Editora item) throws SQLException {
        ps.setString(1, item.getNome());
        ps.setString(2, item.getTelefone());
        ps.setString(3, item.getEmail());
        ps.setDate(4, java.sql.Date.valueOf(item.getDataFundacao()));
        ps.setString(5, item.getCnpj());
        ps.setString(6, item.getCidade());
    }
    @Override
    public void save(Editora item) {
        String sql = "INSERT INTO editoras (nome, telefone, email, data_fundacao, cnpj, cidade) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void update(long id, Editora item) {
        String sql = "UPDATE editoras SET nome=?, telefone=?, email=?, data_fundacao=?, cnpj=?, cidade=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.setLong(7, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void delete(Editora item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM editoras WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Editora> findAll() { return consultar("SELECT * FROM editoras ORDER BY id", null); }
    @Override
    public Editora searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM editoras WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Editora> searchByName(String texto) {
        return consultar("SELECT * FROM editoras WHERE nome LIKE ? ORDER BY id", texto);
    }
    private List<Editora> consultar(String sql, String texto) {
        List<Editora> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (texto != null) {
                ps.setString(1, "%" + texto + "%");
            }
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
        return lista;
    }
}
