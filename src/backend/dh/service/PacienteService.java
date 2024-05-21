package backend.dh.service;

import backend.dh.dao.IDao;
import backend.dh.dao.impl.PacienteDAOH2;
import backend.dh.model.Paciente;

import java.util.List;

public class PacienteService {
    public IDao<Paciente> iDao = new PacienteDAOH2();

    public PacienteService(IDao<Paciente> iDao) {
        this.iDao = iDao;
    }

    Paciente insertar(Paciente paciente){
        return iDao.insertar(paciente);
    }
    Paciente buscarPorCampo(String campo){
       return iDao.buscarPorCampo(campo);
    }
    Paciente buscarPorId(Integer id){
        return iDao.buscarPorId(id);
    }
    Paciente eliminarPorId(Integer id){
        return iDao.eliminarPorId(id);
    }
    List<Paciente> buscarTodos(){
        return iDao.buscarTodos();
    }
}
