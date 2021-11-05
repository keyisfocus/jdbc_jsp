package dao;

import models.Stock;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockDao extends Dao<Stock> {

    public StockDao() {
    }

    StockDao(String URL, String user, String pw) {
        super(URL, user, pw);
    }

    @Override
    public Optional<Stock> get(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from stock WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new Stock(rs.getInt("id"),
                                rs.getInt("store"),
                                rs.getInt("car_model"),
                                rs.getInt("price"),
                                rs.getInt("quantity")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Stock> list() throws SQLException {
        var list = new ArrayList<Stock>();
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from stock";
            var statement = connection.prepareStatement(query);
            var rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new Stock(rs.getInt("id"),
                        rs.getInt("store"),
                        rs.getInt("car_model"),
                        rs.getInt("price"),
                        rs.getInt("quantity")));
            }
        }
        return list;
    }

    @Override
    public int save(Stock stock) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "INSERT INTO stock (store, car_model, price, quantity) VALUES (?, ?, ?, ?)";
            var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, stock.getStore());
            statement.setInt(2, stock.getCarModel());
            statement.setInt(3, stock.getPrice());
            statement.setInt(4, stock.getQuantity());

            if (statement.executeUpdate() == 1) {
                var rs = statement.getGeneratedKeys();
                return rs.next() ? rs.getInt("id") : -1;
            }
            return -1;
        }
    }

    @Override
    public boolean update(Stock stock) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "UPDATE stock SET store = ?, car_model = ?, price = ?, quantity = ? WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, stock.getStore());
            statement.setInt(2, stock.getCarModel());
            statement.setInt(3, stock.getPrice());
            statement.setInt(4, stock.getQuantity());
            statement.setInt(5, stock.getId());
            var count = statement.executeUpdate();

            return count == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "DELETE FROM stock WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var count = statement.executeUpdate();

            return count == 1;
        }
    }
}
