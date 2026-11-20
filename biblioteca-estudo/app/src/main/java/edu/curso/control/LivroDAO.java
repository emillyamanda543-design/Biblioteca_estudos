package edu.curso.control;
import edu.curso.model.Livro;
import java.util.List;
public interface LivroDAO {
    void save(Livro item);
    void update(long id, Livro item);
    void delete(Livro item);
    List<Livro> findAll();
    Livro searchById(long id);
    List<Livro> searchByName(String texto);
}
