package backend.dh.test;

import backend.dh.dao.IDao;
import backend.dh.dao.impl.PacienteDAOH2;
import backend.dh.model.Domicilio;
import backend.dh.model.Paciente;
import backend.dh.service.PacienteService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PacienteServiceTest {

    private static Logger LOGGER = Logger.getLogger(PacienteServiceTest.class);

    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/clase11_integradora;INIT=RUNSCRIPT FROM 'C:/Digital House/B5/Backend/Ejercicios_Backend/Clase11/practicaIntegradora/create.sql'", "sa", "sa");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Paciente guardado en BD")
    void test1(){
        Domicilio domicilio = new Domicilio("calle prueba", 45, "localidad prueba", "provincia prueba");
        Paciente paciente = new Paciente("Quiroz", "Manuel", "87564", LocalDate.of(2024, 1, 25), domicilio);
        IDao<Paciente> pacienteIDao = new PacienteDAOH2();
        PacienteService pacienteService = new PacienteService(pacienteIDao);
        Paciente pacienteDesdeBD = pacienteService.iDao.insertar(paciente);

        assertNotNull(pacienteDesdeBD);

    }
}