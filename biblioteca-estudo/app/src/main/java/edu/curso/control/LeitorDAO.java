package edu.curso.control;
import edu.curso.model.Leitor;
import java.util.List;
public interface LeitorDAO {
    void save(Leitor item);
    void update(long id, Leitor item);
    void delete(Leitor item);
    List<Leitor> findAll();
    Leitor searchById(long id);
    List<Leitor> searchByName(String texto);
}
