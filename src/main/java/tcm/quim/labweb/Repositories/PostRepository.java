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

    private final String INSERT_POST = "INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES (?, ? , ?, ?, ?, ?)";
    private final String SAVE_POST = "UPDATE post_web SET title = ?, text = ?, date_edit = ? WHERE id = ?";
    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_BY_ID_ONLY_PUBLIC = "SELECT * FROM post_web WHERE id = ? AND is_public = ?";
    private final String QUERY_ALL   = "SELECT * FROM post_web";
    private final String QUERY_ALL_SHARED_POSTS_USER = "SELECT * FROM shared_post WHERE username = ?";
    private final String QUERY_ALL_OWNER_POSTS_USER = "SELECT * FROM post_web WHERE owner = ?";
    private final String QUERY_ALL_POSTS_USER = "SELECT * FROM post_web WHERE owner = ? OR id = ?";



    public PostRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public Post_web getPostById(int id) {
        return jdbcTemplate.queryForObject(QUERY_BY_ID, new Object[]{id}, mapper);
    }




    public int savePost(Post_web post_web) {
        return jdbcTemplate.update(SAVE_POST, post_web.getTitle(), post_web.getText(), post_web.getDate_edit(), post_web.getId());
    }

    public int addNewPost(Post_web post_web, User_web owner) {
        return jdbcTemplate.update(INSERT_POST, post_web.getTitle(), post_web.getText(), post_web.getIs_public(),
                Timestamp.valueOf(post_web.getDate_create()), Timestamp.valueOf(post_web.getDate_edit()), owner.getUsername());
    }



    public List<Post_web> getAllPosts(User_web user_web){
        //List SHared_Post_Web contains lists relation username-postId
        List<Shared_Post_web> shared_post_webs = jdbcTemplate.query(QUERY_ALL_SHARED_POSTS_USER, new SharedPostWebLabMapper(), user_web.getUsername());
        
        //List all Posts
        List<Post_web> post_web_total = new ArrayList<>();

        //Get List Share Posts
        for (Shared_Post_web shared_post_web: shared_post_webs) {
            int postId = shared_post_web.getPost_id();
            Post_web post_web = this.getPostById(postId);
            post_web_total.add(post_web);
        }

        List<Post_web> posts_web_owner = jdbcTemplate.query(QUERY_ALL_OWNER_POSTS_USER, new Object[]{user_web.getUsername()}, mapper);

        //Compare posts shared - owner
        for (Post_web post_web: posts_web_owner) {

            if (!this.compareIfPostIsInArray(post_web_total, post_web)){
                post_web_total.add(post_web);
            }

        }

        return post_web_total;

    }

    public List<Post_web>  getMyPosts(User_web user_web) {
        return jdbcTemplate.query(QUERY_ALL_OWNER_POSTS_USER, new Object[]{user_web.getUsername()}, mapper);
    }

    private boolean compareIfPostIsInArray (List<Post_web> arrayPost, Post_web post){

        for (Post_web post1: arrayPost) {
                if (post1.getId() == post.getId()){
                    return true;
                }
        }
        return false;

    }




    private RowMapper<Post_web> mapper = (resultSet, i) -> {
        String username = resultSet.getString("owner");
        User_web user_web = userRepository.getUserByUserName(username);

        Post_web post_web = new Post_web();
        post_web.setId(Integer.parseInt(resultSet.getString("id")));
        post_web.setTitle(resultSet.getString("title"));
        post_web.setText(resultSet.getString("text"));
        post_web.setDate_create(resultSet.getTimestamp("date_create").toLocalDateTime());
        post_web.setDate_create(resultSet.getTimestamp("date_edit").toLocalDateTime());
        post_web.setIs_public(resultSet.getBoolean("is_public"));
        post_web.setOwner(user_web);


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
