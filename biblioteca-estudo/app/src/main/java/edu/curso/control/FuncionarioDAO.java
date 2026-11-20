package edu.curso.control;
import edu.curso.model.Funcionario;
import java.util.List;
public interface FuncionarioDAO {
    void save(Funcionario item);
    void update(long id, Funcionario item);
    void delete(Funcionario item);
    List<Funcionario> findAll();
    Funcionario searchById(long id);
    List<Funcionario> searchByName(String texto);
}
