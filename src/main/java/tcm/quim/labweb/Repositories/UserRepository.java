package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.User_web;

import java.time.LocalDateTime;

@Repository
public class UserRepository {

    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    private final String INSERT_POST = "INSERT INTO user_web (title, text, is_public, date_create, date_edit) VALUES (?, ? , ?, ?)";
    private final String QUERY_BY_ID = "SELECT * FROM user_web WHERE id = ?";
    private final String QUERY_ALL   = "SELECT * FROM user_web";


    public User_web getUserById(int idUser) {
        return jdbcTemplate.query(QUERY_BY_ID, new Object[]{idUser}, mapper);

        return null;
    }


    private RowMapper<User_web> mapper = (resultSet, i) -> {
        User_web user_web = new User_web(resultSet.getString("username"),resultSet.getString("name"),
                resultSet.getString("surname"), resultSet.getString("mail"), Integer.parseInt(resultSet.getString("phone")),
                resultSet.getTimestamp("date_birth").toLocalDateTime());

        user_web.setDate_create(LocalDateTime.parse(resultSet.getString("date_create")));
        user_web.setDate_create(LocalDateTime.parse(resultSet.getString("date_edit")));
        user_web.setId(Integer.parseInt(resultSet.getString("id")));

        return user_web;
    };


}
