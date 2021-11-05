package dao;

import models.Stock;
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
class StockDaoTest {

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

    private static StockDao dao;

    @BeforeAll
    static void setup() {
        postgresContainer.start();
        var jdbcURL = postgresContainer.getJdbcUrl();
        dao = new StockDao(jdbcURL, USERNAME, PASSWORD);
    }

    @AfterAll
    static void close() {
        postgresContainer.stop();
    }

    @Test
    void get() throws SQLException {
        var stock = dao.get(1);
        assertTrue(stock.isPresent());

        stock = dao.get(312312313);
        assertTrue(stock.isEmpty());
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
        var stock = dao.get(1).orElseThrow();
        dao.delete(1);
        var id = dao.save(stock);
        stock.setId(id);

        var stockFromDBOpt = dao.get(id);
        assertTrue(stockFromDBOpt.isPresent());
        var stockFromDB = stockFromDBOpt.get();
        assertEquals(stock, stockFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void update() throws SQLException {
        var stock = new Stock(1, 1, 1, 100, 321);
        dao.update(stock);

        var stockFromDBOpt = dao.get(1);
        assertTrue(stockFromDBOpt.isPresent());
        var stockFromDB = stockFromDBOpt.get();
        assertEquals(stock, stockFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void delete() throws SQLException {
        assertFalse(dao.delete(2));
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }
}