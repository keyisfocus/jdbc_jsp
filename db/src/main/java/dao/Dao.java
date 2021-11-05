package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class Dao<T> {
    protected final DataSource ds;

    Dao() {
        this("jdbc:postgresql://localhost:5432/jdbc_jsp", "root", "5131");
    }

    Dao(String URL, String user, String pw) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(user);
        config.setPassword(pw);
        ds = new HikariDataSource(config);
    }

    public abstract Optional<T> get(int id) throws SQLException;

    public abstract List<T> list() throws SQLException;

    public abstract int save(T t) throws SQLException;

    public abstract boolean update(T t) throws SQLException;

    public abstract boolean delete(int id) throws SQLException;
}
