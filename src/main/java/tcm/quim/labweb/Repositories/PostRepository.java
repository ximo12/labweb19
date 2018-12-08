package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.User_web;

import java.sql.Timestamp;
import java.util.List;


@Repository
public class PostRepository {

    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    private final String INSERT_POST = "INSERT INTO post_web (title, text, is_public, date_create, date_edit) VALUES (?, ? , ?, ?)";
    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_ALL   = "SELECT * FROM post_web";


    public Post_web getPostById(int id) {
        return jdbcTemplate.queryForObject(QUERY_BY_ID, new Object[]{id}, mapper);
    }




    public int savePost(Post_web post_web) {
        return jdbcTemplate.update(INSERT_POST, post_web.getTitle(), post_web.getText(), post_web.getIs_public(),
                Timestamp.valueOf(post_web.getDate_create()), Timestamp.valueOf(post_web.getDate_edit()));
    }

    public List<Post_web> getAllPosts(){
        return jdbcTemplate.query(QUERY_ALL, mapper);
    }


    private RowMapper<Post_web> mapper = (resultSet, i) -> {
        int idUser = resultSet.getInt("owner");
        User_web user_web = userRepository.getUserById(idUser);
        Post_web post_web = new Post_web(resultSet.getString("title"), resultSet.getString("text"),
                user_web, resultSet.getBoolean("is_public"));
        post_web.setDate_create(resultSet.getTimestamp("date_creation").toLocalDateTime());
        post_web.setDate_edit(resultSet.getTimestamp("date_edit").toLocalDateTime());

        return post_web;
    };



}
