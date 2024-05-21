package backend.dh.dao.impl;

import backend.dh.dao.IDao;
import backend.dh.db.H2Connection;
import backend.dh.model.Domicilio;
import backend.dh.model.Paciente;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOH2 implements IDao<Paciente> {

    public static Logger LOGGER = Logger.getLogger(PacienteDAOH2.class);

    public static String SELECT_ID = "SELECT * FROM PACIENTES WHERE ID = ?";

    public static String SELECT_CAMPO = "SELECT * FROM PACIENTES WHERE NOMBRE = ?";

    public static String SELECT_ALL = "SELECT * FROM PACIENTES";

    public static String INSERT = "INSERT INTO PACIENTES VALUES(DEFAULT, ?, ?, ?, ?, ?)";

    public static String UPDATE_ID = "UPDATE PACIENTES SET ID = ?";

    public static String DELETE_ID = "DELETE FROM PACIENTES WHERE ID = ?";

    @Override
    public Paciente insertar(Paciente paciente) {
        Connection connection = null;
        Paciente pacienteADevolver = null;
        DomicilioDAOH2 domicilioDao = new DomicilioDAOH2();
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paciente.getApellido());
            preparedStatement.setString(2, paciente.getNombre());
            preparedStatement.setString(3, paciente.getDni());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso()));

            //Obtener id del Domicilio
            Domicilio domicilioBd = paciente.getDomicilio(); //la que viene del parametro
            Domicilio domicilio = domicilioDao.insertar(domicilioBd); //este contiene toda la informacion del domicilio insertado
            Integer idDomicilio = domicilio.getId();

            preparedStatement.setInt(5, idDomicilio);

            preparedStatement.executeUpdate(); //sin esto no sse guarda la consulta

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String apellido = paciente.getApellido();
                String nombre = paciente.getNombre();
                String dni = paciente.getDni();
                LocalDate fechaIngreso = paciente.getFechaIngreso();

                pacienteADevolver = new Paciente(id, apellido, nombre, dni, fechaIngreso, domicilio);
            }

            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Se ha insertado el paciente: " + pacienteADevolver);
        }catch (Exception e){
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error al realizar rollback en Paciente: " + ex.getMessage());
                    e.printStackTrace();
                }
            }
            LOGGER.error("Error al insertar Paciente: " + e.getMessage());
            e.printStackTrace();
        }finally {

                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }

        }

        return pacienteADevolver;
    }

    @Override
    public Paciente buscarPorCampo(String campo) {
        Connection connection = null;
        Paciente pacienteADevolver = null;
        IDao domicilioDao = new DomicilioDAOH2();
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CAMPO);
            preparedStatement.setString(1, campo);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fechaIngreso = resultSet.getDate(5).toLocalDate();
                Integer idDomicilio = resultSet.getInt(6);

                Domicilio domicilioBd = (Domicilio) domicilioDao.buscarPorId(idDomicilio);

                pacienteADevolver = new Paciente(id, apellido, nombre, dni, fechaIngreso, domicilioBd);
            }
            LOGGER.info("Paciente por campo encontrado: " + pacienteADevolver);
        }catch (Exception e){
            LOGGER.error("Error al buscar paciente por campo: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return pacienteADevolver;
    }

    @Override
    public Paciente buscarPorId(Integer id) {
        Connection connection = null;
        Paciente pacienteADevolver = null;
        DomicilioDAOH2 domicilioDao = new DomicilioDAOH2();
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer idBd = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fechaIngreso = resultSet.getDate(5).toLocalDate();
                Integer idDomicilio = resultSet.getInt(6);

                Domicilio domicilioBd = domicilioDao.buscarPorId(idDomicilio);

                pacienteADevolver = new Paciente(idBd, apellido, nombre, dni, fechaIngreso, domicilioBd);
            }
            LOGGER.info("Paciente por Id encontrado: " + pacienteADevolver);
        }catch (Exception e){
            LOGGER.error("Error al buscar paciente por Id: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return pacienteADevolver;
    }

    @Override
    public Paciente eliminarPorId(Integer id) {
        Connection connection = null;
        Paciente pacienteADevolver = null;
        IDao domicilioDao = new DomicilioDAOH2();
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ID);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer idBD = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fechaIngreso = resultSet.getDate(5).toLocalDate();
                Integer idDomicilio = resultSet.getInt(6);

                Domicilio domicilioBd = (Domicilio) domicilioDao.buscarPorId(idDomicilio);

                pacienteADevolver = new Paciente(id, apellido, nombre, dni, fechaIngreso, domicilioBd);
            }
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Paciente eliminado de la BD: " + pacienteADevolver);
        }catch (Exception e){
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.error("Error al realizar rollback en Paciente: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            LOGGER.error("Error al buscar paciente por Id: " + e.getMessage());
            e.printStackTrace();
        }finally {

                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        return pacienteADevolver;
    }

    @Override
    public List<Paciente> buscarTodos() {
        Connection connection = null;
        Paciente pacienteBd = null;
        DomicilioDAOH2 domicilioDao = new DomicilioDAOH2();
        List<Paciente> pacientes = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()){ //para hacer recorrido por cada paciente
                Integer id = resultSet.getInt(1);
                String apellido = resultSet.getString(2);
                String nombre = resultSet.getString(3);
                String dni = resultSet.getString(4);
                LocalDate fechaIngreso = resultSet.getDate(5).toLocalDate();
                Integer idDomicilio = resultSet.getInt(6);

                Domicilio domicilioBd = domicilioDao.buscarPorId(idDomicilio);

                pacienteBd = new Paciente(id, apellido, nombre, dni, fechaIngreso, domicilioBd);

                LOGGER.info("Lista de pacientes: " + pacienteBd);
                pacientes.add(pacienteBd);
            }

        }catch (Exception e){
            LOGGER.error("Error al traer listado de pacientes: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return pacientes;
    }
}
