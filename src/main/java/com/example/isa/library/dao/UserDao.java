package com.example.isa.library.dao;

import com.example.isa.library.entity.Users;
import com.example.isa.library.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.tags.shaded.org.apache.bcel.classfile.InnerClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao implements Dao<Long, Users> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String SAVE_SQL = """
            INSERT INTO users (name, surname, email, password, phone) VALUES (?, ?, ?, ?, ?)
            """;
    private static final String FIND_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT * FROM users
            WHERE email = ? AND password = ?
            """;

    @SneakyThrows
    public Optional<Users> findByEmailAndPassword(String email, String password) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            Users users = null;
            if (resultSet.next()) {
                users = buildEntity(resultSet);
            }
            return Optional.ofNullable(users);
        }
    }

    private Users buildEntity(ResultSet resultSet) throws SQLException {
        return Users.builder()
                .id(resultSet.getObject("id", Long.class))
                .name(resultSet.getObject("name", String.class))
                .surname(resultSet.getObject("surname", String.class))
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .phone(resultSet.getObject("phone", String.class))
                .build();
    }

    @Override
    public List<Users> findAll() {
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = prepareStatement.executeQuery();

            List<Users> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Users> findUserById(Long id) {
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            ResultSet resultSet = prepareStatement.executeQuery();

            prepareStatement.setObject(1, id);
            List<Users> usersById = new ArrayList<>();
            while (resultSet.next()) {
                usersById.add(buildUser(resultSet));
            }

            return usersById;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Users buildUser(ResultSet resultSet) {
        return new Users(
                resultSet.getObject("id", Long.class),
                resultSet.getObject("name", String.class),
                resultSet.getObject("surname", String.class),
                resultSet.getObject("email", String.class),
                resultSet.getObject("password", String.class),
                resultSet.getObject("phone", String.class)
        );
    }

    @Override
    public Optional<Users> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public void update(Users entity) {

    }

    @SneakyThrows
    @Override
    public Users save(Users entity) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getSurname());
            preparedStatement.setObject(3, entity.getEmail());
            preparedStatement.setObject(4, entity.getPassword());
            preparedStatement.setObject(5, entity.getPhone());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            entity.setId(generatedKeys.getObject("id", Long.class));
        }
        return null;
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}