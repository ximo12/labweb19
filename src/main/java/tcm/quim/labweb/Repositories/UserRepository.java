package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.User_web;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class UserRepository {

    JdbcTemplate jdbcTemplate;

    private final String INSERT_USER = "INSERT INTO user_web (username, name, surname, mail, phone, date_create, date_edit, date_birth) VALUES (?, ? , ?, ?, ?, ?, ?, ?)";
    private final String QUERY_BY_ID = "SELECT * FROM user_web WHERE id = ?";
    private final String QUERY_BY_USERNAME = "SELECT * FROM user_web WHERE username = ?";
    private final String QUERY_ALL   = "SELECT * FROM user_web";

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User_web getUserById(int idUser) {
        return jdbcTemplate.queryForObject(QUERY_BY_ID, new Object[]{idUser}, mapper);
    }

    public User_web getUserByUserName(String name) {
        return jdbcTemplate.queryForObject(QUERY_BY_USERNAME, new Object[]{name}, mapper);
    }

    public int saveUser(User_web user_web) {
        return jdbcTemplate.update(INSERT_USER, user_web.getUsername(), user_web.getName(), user_web.getSurname(), user_web.getMail(),
                user_web.getPhone(), Timestamp.valueOf(user_web.getDate_create()), Timestamp.valueOf(user_web.getDate_edit()),
                Timestamp.valueOf(user_web.getDate_birth()));
    }



    private RowMapper<User_web> mapper = (resultSet, i) -> {

        String date_birth = resultSet.getString("date_birth");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeBirth = LocalDateTime.parse(date_birth, formatter);

        User_web user_web = new User_web(resultSet.getString("username"),resultSet.getString("name"),
                resultSet.getString("surname"), resultSet.getString("mail"), Integer.parseInt(resultSet.getString("phone")),
                dateTimeBirth);

        /*user_web.setDate_create(LocalDateTime.parse(resultSet.getString("date_create")));
        user_web.setDate_create(LocalDateTime.parse(resultSet.getString("date_edit")));
        user_web.setId(Integer.parseInt(resultSet.getString("id")));*/

        return user_web;
    };


}
