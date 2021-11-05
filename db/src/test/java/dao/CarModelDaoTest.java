package dao;

import models.CarModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class CarModelDaoTest {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:13");
    private static final String USERNAME = "root";
    private static final String PASSWORD = "5131";

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("jdbc_jsp_test")
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withInitScript("db_init.sql")
            .withExposedPorts(5432);

    private static CarModelDao dao;

    @BeforeAll
    static void setup() {
        postgresContainer.start();
        var jdbcURL = postgresContainer.getJdbcUrl();
        dao = new CarModelDao(jdbcURL, USERNAME, PASSWORD);
    }

    @AfterAll
    static void close() {
        postgresContainer.stop();
    }

    @Test
    void get() throws SQLException {
        var model = dao.get(1);
        assertTrue(model.isPresent());

        model = dao.get(312312313);
        assertTrue(model.isEmpty());
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void list() throws SQLException {
        var list = dao.list();
        assertFalse(list.isEmpty());
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void save() throws SQLException {
        var model = new CarModel(0, "JUNIT_TEST_CREATE", 1, 123);
        var id = dao.save(model);
        model.setId(id);

        var modelFromDBOpt = dao.get(id);
        assertTrue(modelFromDBOpt.isPresent());
        var modelFromDB = modelFromDBOpt.get();
        assertEquals(model, modelFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);

        dao.delete(id);
    }

    @Test
    void update() throws SQLException {
        var model = new CarModel(1, "JUNIT_TEST_UPDATE2", 1, 123);
        dao.update(model);

        var modelFromDBOpt = dao.get(1);
        assertTrue(modelFromDBOpt.isPresent());
        var modelFromDB = modelFromDBOpt.get();
        assertEquals(model, modelFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void delete() throws SQLException {
        var id = dao.save(new CarModel(0, "JUNIT_TEST_DELETE", 1, 123));
        assertTrue(dao.delete(id));
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }
}