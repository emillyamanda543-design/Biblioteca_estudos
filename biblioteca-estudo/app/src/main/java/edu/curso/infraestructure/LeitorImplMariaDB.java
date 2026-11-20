package edu.curso.infraestructure;

import edu.curso.control.LeitorDAO;
import edu.curso.model.Leitor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementação JDBC: cada operação fecha conexão e recursos automaticamente. */
public class LeitorImplMariaDB implements LeitorDAO {
    private Leitor ler(ResultSet rs) throws SQLException {
        Leitor item = new Leitor();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setCpf(rs.getString("cpf"));
        item.setTelefone(rs.getString("telefone"));
        item.setEmail(rs.getString("email"));
        item.setDataNascimento(rs.getDate("data_nascimento") == null ? null : rs.getDate("data_nascimento").toLocalDate());
        item.setEndereco(rs.getString("endereco"));
        return item;
    }
    /** Define os parâmetros na mesma ordem do INSERT e UPDATE. */
    private void preencher(PreparedStatement ps, Leitor item) throws SQLException {
        ps.setString(1, item.getNome());
        ps.setString(2, item.getCpf());
        ps.setString(3, item.getTelefone());
        ps.setString(4, item.getEmail());
        ps.setDate(5, java.sql.Date.valueOf(item.getDataNascimento()));
        ps.setString(6, item.getEndereco());
    }
    @Override
    public void save(Leitor item) {
        String sql = "INSERT INTO leitores (nome, cpf, telefone, email, data_nascimento, endereco) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void update(long id, Leitor item) {
        String sql = "UPDATE leitores SET nome=?, cpf=?, telefone=?, email=?, data_nascimento=?, endereco=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.setLong(7, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void delete(Leitor item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM leitores WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Leitor> findAll() { return consultar("SELECT * FROM leitores ORDER BY id", null); }
    @Override
    public Leitor searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM leitores WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Leitor> searchByName(String texto) {
        return consultar("SELECT * FROM leitores WHERE nome LIKE ? ORDER BY id", texto);
    }
    private List<Leitor> consultar(String sql, String texto) {
        List<Leitor> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (texto != null) {
                ps.setString(1, "%" + texto + "%");
            }
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
        return lista;
    }
}
