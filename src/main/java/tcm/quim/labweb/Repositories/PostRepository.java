package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.Post_web;
import tcm.quim.labweb.Domain.Shared_Post_web;
import tcm.quim.labweb.Domain.User_web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PostRepository {

    JdbcTemplate jdbcTemplate;
    UserRepository userRepository;

    private final String INSERT_POST = "INSERT INTO post_web (title, text, is_public, date_create, date_edit) VALUES (?, ? , ?, ?)";
    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_ALL   = "SELECT * FROM post_web";
    private final String QUERY_ALL_PERMIT_USER = "SELECT * FROM shared_post WHERE username = ?";

    public PostRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public Post_web getPostById(int id) {
        return jdbcTemplate.queryForObject(QUERY_BY_ID, new Object[]{id}, mapper);
    }




    public int savePost(Post_web post_web) {
        return jdbcTemplate.update(INSERT_POST, post_web.getTitle(), post_web.getText(), post_web.getIs_public(),
                Timestamp.valueOf(post_web.getDate_create()), Timestamp.valueOf(post_web.getDate_edit()));
    }



    public List<Post_web> getAllPosts(User_web user_web){
        List<Shared_Post_web> shared_post_webs = jdbcTemplate.query(QUERY_ALL_PERMIT_USER, new SharedPostWebLabMapper(), user_web.getUsername());
        List<Post_web> post_webs = new ArrayList<>();
        for (Shared_Post_web shared_post_web: shared_post_webs) {
            int postId = shared_post_web.getPost_id();
            Post_web post_web = this.getPostById(postId);
            post_webs.add(post_web);
        }

        return post_webs;

    }


    private RowMapper<Post_web> mapper = (resultSet, i) -> {
        int idUser = resultSet.getInt("owner");
        User_web user_web = userRepository.getUserById(idUser);
        Post_web post_web = new Post_web(resultSet.getString("title"), resultSet.getString("text"),
                user_web, resultSet.getBoolean("is_public"));

        //post_web.setDate_create(resultSet.getTimestamp("date_creation").toLocalDateTime());
        //post_web.setDate_edit(resultSet.getTimestamp("date_edit").toLocalDateTime());

        return post_web;
    };

    private RowMapper<Shared_Post_web> mapperShared = (resultSet, i) -> {
        Shared_Post_web shared_post_web = new Shared_Post_web(resultSet.getString("username"), resultSet.getInt("post_id"));



        //post_web.setDate_create(resultSet.getTimestamp("date_creation").toLocalDateTime());
        //post_web.setDate_edit(resultSet.getTimestamp("date_edit").toLocalDateTime());

        return shared_post_web;
    };


    private final class SharedPostWebLabMapper implements RowMapper<Shared_Post_web> {
        @Override
        public Shared_Post_web mapRow(ResultSet resultSet, int i) throws SQLException {
            Shared_Post_web shared_post_web = new Shared_Post_web(resultSet.getString("username"), resultSet.getInt("post_id"));
            return shared_post_web;
        }
    }



}
