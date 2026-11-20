package edu.curso.control;
import edu.curso.model.Editora;
import java.util.List;
public interface EditoraDAO {
    void save(Editora item);
    void update(long id, Editora item);
    void delete(Editora item);
    List<Editora> findAll();
    Editora searchById(long id);
    List<Editora> searchByName(String texto);
}
