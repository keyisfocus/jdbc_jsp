package dao;

import models.CarBrand;
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
class CarBrandDaoTest {

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

    private static CarBrandDao dao;

    @BeforeAll
    static void setup() {
        postgresContainer.start();
        var jdbcURL = postgresContainer.getJdbcUrl();
        dao = new CarBrandDao(jdbcURL, USERNAME, PASSWORD);
    }

    @AfterAll
    static void close() {
        postgresContainer.stop();
    }

    @Test
    void get() throws SQLException {
        var brand = dao.get(1);
        assertTrue(brand.isPresent());

        brand = dao.get(312312313);
        assertTrue(brand.isEmpty());
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
        var brand = new CarBrand(0, "JUNIT_TEST_CREATE", "JUNIT_TEST_CREATE");
        var id = dao.save(brand);
        brand.setId(id);

        var brandFromDBOpt = dao.get(id);
        assertTrue(brandFromDBOpt.isPresent());
        var brandFromDB = brandFromDBOpt.get();
        assertEquals(brand, brandFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);

        dao.delete(id);
    }

    @Test
    void update() throws SQLException {
        var brand = new CarBrand(1, "JUNIT_TEST_UPDATE2", "JUNIT_TEST_UPDATE2");
        dao.update(brand);

        var brandFromDBOpt = dao.get(1);
        assertTrue(brandFromDBOpt.isPresent());
        var brandFromDB = brandFromDBOpt.get();
        assertEquals(brand, brandFromDB);
        assertDoesNotThrow((ThrowingSupplier<SQLException>) SQLException::new);
    }

    @Test
    void delete() throws SQLException {
        assertThrows(SQLException.class, () -> dao.delete(1));
        var id = dao.save(new CarBrand(0, "JUNIT_TEST_DELETE", "JUNIT_TEST_DELETE"));
        assertTrue(dao.delete(id));
    }
}