package edu.curso.infraestructure;
import edu.curso.control.FuncionarioDAO;
import edu.curso.model.Funcionario;
import java.sql.*;
import java.util.*;
public class FuncionarioImplMariaDB implements FuncionarioDAO {
    private Funcionario ler(ResultSet rs) throws SQLException {
        Funcionario item = new Funcionario();
        item.setId(rs.getLong("id"));
        item.setNome(rs.getString("nome"));
        item.setCpf(rs.getString("cpf"));
        item.setTelefone(rs.getString("telefone"));
        item.setCargo(rs.getString("cargo"));
        item.setDataAdmissao(rs.getDate("data_admissao") == null ? null : rs.getDate("data_admissao").toLocalDate());
        return item;
    }
    public void save(Funcionario item) {
        String sql = "INSERT INTO funcionarios (nome, cpf, telefone, cargo, data_admissao) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getCpf());
            ps.setString(3, item.getTelefone());
            ps.setString(4, item.getCargo());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataAdmissao()));
            ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao salvar Funcionario: " + e.getMessage(), e); }
    }
    public void update(long id, Funcionario item) {
        String sql = "UPDATE funcionarios SET nome=?, cpf=?, telefone=?, cargo=?, data_admissao=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, item.getNome());
            ps.setString(2, item.getCpf());
            ps.setString(3, item.getTelefone());
            ps.setString(4, item.getCargo());
            ps.setDate(5, java.sql.Date.valueOf(item.getDataAdmissao()));
            ps.setLong(6, id);
            if (ps.executeUpdate() == 0) throw new IllegalStateException("Cadastro não encontrado.");
        } catch (SQLException e) { throw new IllegalStateException("Erro ao atualizar Funcionario: " + e.getMessage(), e); }
    }
    public void delete(Funcionario item) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("DELETE FROM funcionarios WHERE id=?")) {
            ps.setLong(1, item.getId()); ps.executeUpdate();
        } catch (SQLException e) { throw new IllegalStateException("Erro ao excluir Funcionario: " + e.getMessage(), e); }
    }
    public List<Funcionario> findAll() {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM funcionarios ORDER BY id"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(ler(rs));
        } catch (SQLException e) { throw new IllegalStateException("Erro ao listar Funcionario: " + e.getMessage(), e); }
        return lista;
    }
    public Funcionario searchById(long id) {
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM funcionarios WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ler(rs) : null; }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Funcionario: " + e.getMessage(), e); }
    }
    public List<Funcionario> searchByName(String texto) {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection con = Conexao.abrir(); PreparedStatement ps = con.prepareStatement("SELECT * FROM funcionarios WHERE nome LIKE ? ORDER BY id")) {
            ps.setString(1, "%" + texto + "%");
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) lista.add(ler(rs)); }
        } catch (SQLException e) { throw new IllegalStateException("Erro ao pesquisar Funcionario: " + e.getMessage(), e); }
        return lista;
    }
}
