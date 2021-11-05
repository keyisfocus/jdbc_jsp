package dao;

import models.Store;
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
class StoreDaoTest {

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

    private static StoreDao dao;

    @BeforeAll
    static void setup() {
        postgresContainer.start();
        var jdbcURL = postgresContainer.getJdbcUrl();
        dao = new StoreDao(jdbcURL, USERNAME, PASSWORD);
    }

    @AfterAll
    static void close() {
        postgresContainer.stop();
    }

    @Test
    void get() throws SQLException {
        var store = dao.get(1);
        assertTrue(store.isPresent());

        store = dao.get(312312313);
        assertTrue(store.isEmpty());
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
        var store = new Store(0, "JUNIT_TEST_CREATE", "JUNIT_TEST_CREATE");
        var id = dao.save(store);
        store.setId(id);

        var storeFromDBOpt = dao.get(id);
        assertTrue(storeFromDBOpt.isPresent());
        var storeFromDB = storeFromDBOpt.get();
        assertEquals(store, storeFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);

        dao.delete(id);
    }

    @Test
    void update() throws SQLException {
        var store = new Store(1, "JUNIT_TEST_UPDATE", "JUNIT_TEST_UPDATE");
        dao.update(store);

        var storeFromDBOpt = dao.get(1);
        assertTrue(storeFromDBOpt.isPresent());
        var storeFromDB = storeFromDBOpt.get();
        assertEquals(store, storeFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void delete() throws SQLException {
        assertThrows(SQLException.class, () -> dao.delete(1));
        var id = dao.save(new Store(0, "JUNIT_TEST_DELETE", "JUNIT_TEST_DELETE"));
        assertTrue(dao.delete(id));
    }
}