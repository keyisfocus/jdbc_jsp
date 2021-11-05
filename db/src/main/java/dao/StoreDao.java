package dao;

import models.Store;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoreDao extends Dao<Store> {

    public StoreDao() {
    }

    public StoreDao(String URL, String user, String pw) {
        super(URL, user, pw);
    }

    @Override
    public Optional<Store> get(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from store WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new Store(rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("location")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Store> list() throws SQLException {
        var list = new ArrayList<Store>();
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from store";
            var statement = connection.prepareStatement(query);
            var rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new Store(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")));
            }
        }
        return list;
    }

    @Override
    public int save(Store store) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "INSERT INTO store (name, location) VALUES (?, ?)";
            var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, store.getName());
            statement.setString(2, store.getLocation());

            if (statement.executeUpdate() == 1) {
                var rs = statement.getGeneratedKeys();
                return rs.next() ? rs.getInt("id") : -1;
            }
            return -1;
        }
    }

    @Override
    public boolean update(Store store) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "UPDATE store SET name = ?, location = ? WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, store.getName());
            statement.setString(2, store.getLocation());
            statement.setInt(3, store.getId());
            var count = statement.executeUpdate();

            return count == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "DELETE FROM store WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var count = statement.executeUpdate();

            return count == 1;
        }
    }
}
