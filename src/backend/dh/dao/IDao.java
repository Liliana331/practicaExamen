package backend.dh.dao;

import java.util.List;

public interface IDao<T> {
    T insertar(T t);
    T buscarPorCampo(String campo);
    T buscarPorId(Integer id);
    T eliminarPorId(Integer id);
    List<T> buscarTodos();

}
