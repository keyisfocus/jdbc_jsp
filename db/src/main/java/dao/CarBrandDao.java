package dao;

import models.CarBrand;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarBrandDao extends Dao<CarBrand> {

    public CarBrandDao() {
    }

    CarBrandDao(String URL, String user, String pw) {
        super(URL, user, pw);
    }

    @Override
    public Optional<CarBrand> get(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from car_brand WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(
                        new CarBrand(rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("value_class")));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<CarBrand> list() throws SQLException {
        var list = new ArrayList<CarBrand>();
        try (var connection = ds.getConnection()) {
            var query = "SELECT * from car_brand";
            var statement = connection.prepareStatement(query);
            var rs = statement.executeQuery();

            while (rs.next()) {
                list.add(new CarBrand(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("value_class")));
            }
        }
        return list;
    }

    @Override
    public int save(CarBrand carBrand) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "INSERT INTO car_brand (name, value_class) VALUES (?, ?)";
            var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, carBrand.getName());
            statement.setString(2, carBrand.getValueClass());
            if (statement.executeUpdate() == 1) {
                var rs = statement.getGeneratedKeys();
                return rs.next() ? rs.getInt("id") : -1;
            }
            return -1;
        }
    }

    @Override
    public boolean update(CarBrand carBrand) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "UPDATE car_brand SET name = ?, value_class = ? WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, carBrand.getName());
            statement.setString(2, carBrand.getValueClass());
            statement.setInt(3, carBrand.getId());
            var count = statement.executeUpdate();

            return count == 1;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (var connection = ds.getConnection()) {
            var query = "DELETE FROM car_brand WHERE id = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var count = statement.executeUpdate();

            return count == 1;
        }
    }
}
