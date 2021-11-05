package dao;

import models.CarModel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarModelDao extends Dao<CarModel> {

    public CarModelDao() {
    }

    CarModelDao(String URL, String user, String pw) {
        super(URL, user, pw);
    }

    @Override
    public Optional<CarModel> get(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from car_model WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new CarModel(rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("brand"),
                                rs.getInt("base_price")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<CarModel> list() throws SQLException {
        var list = new ArrayList<CarModel>();
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from car_model";
            var statement = connection.prepareStatement(query);
            var rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new CarModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("brand"),
                        rs.getInt("base_price")));
            }
        }
        return list;
    }

    @Override
    public int save(CarModel carModel) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "INSERT INTO car_model (name, brand, base_price) VALUES (?, ?, ?)";
            var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, carModel.getName());
            statement.setInt(2, carModel.getBrand());
            statement.setInt(3, carModel.getBasePrice());

            if (statement.executeUpdate() == 1) {
                var rs = statement.getGeneratedKeys();
                return rs.next() ? rs.getInt("id") : -1;
            }
            return -1;
        }
    }

    @Override
    public boolean update(CarModel carModel) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "UPDATE car_model SET name = ?, brand = ?, base_price = ? WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, carModel.getName());
            statement.setInt(2, carModel.getBrand());
            statement.setInt(3, carModel.getBasePrice());
            statement.setInt(4, carModel.getId());
            var count = statement.executeUpdate();

            return count == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "DELETE FROM car_model WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var count = statement.executeUpdate();

            return count == 1;
        }
    }
}
