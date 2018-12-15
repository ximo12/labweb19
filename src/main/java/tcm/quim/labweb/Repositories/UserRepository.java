package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.User_web;

@Repository
public class UserRepository {

    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    private final String INSERT_POST = "INSERT INTO user_web (title, text, is_public, date_create, date_edit) VALUES (?, ? , ?, ?)";
    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_ALL   = "SELECT * FROM post_web";


    public User_web getUserById(int idUser) {

        return null;
    }


    private RowMapper<User_web> mapper = (resultSet, i) -> {
        int idUser = resultSet.getInt("owner");
        User_web user_web = userRepository.getUserById(idUser);
        User_web post_web = new Post_web(resultSet.getString("title"), resultSet.getString("text"),
                user_web, resultSet.getBoolean("is_public"));
        post_web.setDate_create(resultSet.getTimestamp("date_creation").toLocalDateTime());
        post_web.setDate_edit(resultSet.getTimestamp("date_edit").toLocalDateTime());

        return post_web;
    };


}
