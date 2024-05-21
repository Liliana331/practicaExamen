package backend.dh.dao.impl;

import backend.dh.dao.IDao;
import backend.dh.db.H2Connection;
import backend.dh.model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioDAOH2 implements IDao<Domicilio> {

    public static Logger LOGGER = Logger.getLogger(DomicilioDAOH2.class);

    public static String SELECT_ID = "SELECT * FROM DOMICILIOS WHERE ID = ?";

    public static String SELECT_CAMPO = "SELECT * FROM DOMICILIOS WHERE CALLE = ?";

    public static String SELECT_ALL = "SELECT * FROM DOMICILIOS";

    public static String INSERT = "INSERT INTO DOMICILIOS VALUES(DEFAULT, ?, ?, ?, ?)";

    public static String UPDATE_ID = "UPDATE DOMICILIOS SET ID = ?";

    public static String DELETE_ID = "DELETE FROM DOMICILIOS WHERE ID = ?";

    @Override
    public Domicilio insertar(Domicilio domicilio) {
        Connection connection = null;
        Domicilio domicilioADevolver = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, domicilio.getCalle());
            preparedStatement.setInt(2, domicilio.getNumero());
            preparedStatement.setString(3, domicilio.getLocalidad());
            preparedStatement.setString(4, domicilio.getProvincia());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String calle = domicilio.getCalle();
                int numero = domicilio.getNumero();
                String localidad = domicilio.getLocalidad();
                String provincia = domicilio.getProvincia();

                domicilioADevolver = new Domicilio(id, calle, numero, localidad, provincia); //se crea dentro del while para no devolver algo sin ID
            }

            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Se ha insertado el domicilio: " + domicilioADevolver);
        }catch (Exception e) {
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error al realizar rollback en Domicilio: " + ex.getMessage());
                    e.printStackTrace();
                }
            }
            LOGGER.error("Error al insertar Domicilio: " + e.getMessage());
            e.printStackTrace();
        }finally {

                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }

        }

        return domicilioADevolver;
    }

    @Override
    public Domicilio buscarPorCampo(String campo) {
        Connection connection = null;
        Domicilio domicilioADevolver = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CAMPO);
            preparedStatement.setString(1, campo);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String calle = resultSet.getString(2);
                Integer numero = resultSet.getInt(3);
                String localidad = resultSet.getString(4);
                String provincia = resultSet.getString(5);
                domicilioADevolver = new Domicilio(id, calle, numero, localidad, provincia);
            }
            LOGGER.info("Domicilio por campo encontrado: " + domicilioADevolver);
        }catch (Exception e){
            LOGGER.error("Error al buscar domicilio por campo: " + e.getMessage());
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
        return domicilioADevolver;
    }

    @Override
    public Domicilio buscarPorId(Integer id) {
        Connection connection = null;
        Domicilio domicilioADevolver = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer idBD = resultSet.getInt(1);
                String calle = resultSet.getString(2);
                int numero = resultSet.getInt(3);
                String localidad = resultSet.getString(4);
                String provincia = resultSet.getString(5);
                domicilioADevolver = new Domicilio(idBD, calle, numero, localidad, provincia);
            }
            LOGGER.info("Domicilio por Id encontrado: " + domicilioADevolver);
        }catch (Exception e){
            LOGGER.error("Error al buscar domicilio por Id: " + e.getMessage());
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
        return domicilioADevolver;
    }

    @Override
    public Domicilio eliminarPorId(Integer id) {
        Connection connection = null;
        Domicilio domicilioADevolver = null;
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ID);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer idBD = resultSet.getInt(1);
                String calle = resultSet.getString(2);
                Integer numero = resultSet.getInt(3);
                String localidad = resultSet.getString(4);
                String provincia = resultSet.getString(5);
                domicilioADevolver = new Domicilio(idBD, calle, numero, localidad, provincia);
            }
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Se ha eliminado el Domicilio: " + domicilioADevolver);
        }catch (Exception e){
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.error("Error al realizar rollback en Paciente: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            LOGGER.error("Error al eliminar domicilio por Id: " + e.getMessage());
            e.printStackTrace();
        }finally {

                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error de conexión con la BD H2: " + e.getMessage());
                    e.printStackTrace();
                }

        }
        return domicilioADevolver;
    }

    //lista de domicilios realmente no se necesita pq el paciente solo tiene un domicilio
    @Override
    public List<Domicilio> buscarTodos() {
        Connection connection = null;
        Domicilio domicilioBd = null;
        List<Domicilio> domicilios = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String calle = resultSet.getString(2);
                Integer numero = resultSet.getInt(3);
                String localidad = resultSet.getString(4);
                String provincia = resultSet.getString(5);
                domicilioBd = new Domicilio(id, calle, numero, localidad, provincia);
                domicilios.add(domicilioBd);
            }
            LOGGER.info("Lista de Domicilio: " + domicilioBd);
        }catch (Exception e){
            LOGGER.error("Error al traer listado de domicilio: " + e.getMessage());
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
        return domicilios;
    }
}
