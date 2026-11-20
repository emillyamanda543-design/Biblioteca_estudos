package edu.curso.infraestructure;

import edu.curso.control.FuncionarioDAO;
import edu.curso.model.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Implementação JDBC: cada operação fecha conexão e recursos automaticamente. */
public class FuncionarioImplMariaDB implements FuncionarioDAO {
    private Funcionario ler(ResultSet rs) throws SQLException {
        Funcionario item = new Funcionario();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setCpf(rs.getString("cpf"));
        item.setTelefone(rs.getString("telefone"));
        item.setCargo(rs.getString("cargo"));
        item.setDataAdmissao(rs.getDate("data_admissao") == null ? null : rs.getDate("data_admissao").toLocalDate());
        item.setSalario(rs.getBigDecimal("salario"));
        return item;
    }
    /** Define os parâmetros na mesma ordem do INSERT e UPDATE. */
    private void preencher(PreparedStatement ps, Funcionario item) throws SQLException {
        ps.setString(1, item.getNome());
        ps.setString(2, item.getCpf());
        ps.setString(3, item.getTelefone());
        ps.setString(4, item.getCargo());
        ps.setDate(5, java.sql.Date.valueOf(item.getDataAdmissao()));
        ps.setBigDecimal(6, item.getSalario());
    }
    @Override
    public void save(Funcionario item) {
        String sql = "INSERT INTO funcionarios (nome, cpf, telefone, cargo, data_admissao, salario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void update(long id, Funcionario item) {
        String sql = "UPDATE funcionarios SET nome=?, cpf=?, telefone=?, cargo=?, data_admissao=?, salario=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, item);
            ps.setLong(7, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public void delete(Funcionario item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM funcionarios WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Funcionario> findAll() { return consultar("SELECT * FROM funcionarios ORDER BY id", null); }
    @Override
    public Funcionario searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM funcionarios WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
    }
    @Override
    public List<Funcionario> searchByName(String texto) {
        return consultar("SELECT * FROM funcionarios WHERE nome LIKE ? ORDER BY id", texto);
    }
    private List<Funcionario> consultar(String sql, String texto) {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (texto != null) {
                ps.setString(1, "%" + texto + "%");
            }
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException ex) { throw Conexao.erro(ex); }
        return lista;
    }
}
