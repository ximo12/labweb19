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
    private final String INSERT_SHARED = "INSERT INTO shared_post (username, post_id) VALUES (?, ?)";

    private final String SAVE_POST = "UPDATE post_web SET title = ?, text = ?, date_edit = ? WHERE id = ?";


    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_BY_ID_COUNT = "SELECT COUNT (*) FROM post_web WHERE id = ?";
    private final String QUERY_SHARED_EXIST = "SELECT COUNT (*) FROM shared_post WHERE username = ? AND post_id = ?";


    private final String QUERY_BY_ID_ONLY_PUBLIC = "SELECT * FROM post_web WHERE id = ? AND is_public = ?";
    private final String QUERY_ALL   = "SELECT * FROM post_web";
    private final String QUERY_ALL_SHARED_POSTS_USER = "SELECT * FROM shared_post WHERE username = ?";
    private final String QUERY_ALL_OWNER_POSTS_USER = "SELECT * FROM post_web WHERE owner = ?";
    private final String QUERY_ALL_POSTS_USER = "SELECT * FROM post_web WHERE owner = ? OR id = ?";
    private final String QUERY_SHARED_POST = "SELECT * FROM shared_post WHERE username = ? AND post_id = ?";

    private final String DELETE_SHARED_POST = "DELETE FROM shared_post WHERE post_user_id = ?";




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


    public List<Post_web> getAllPostsSharedWithUser(User_web user_web){
        //List SHared_Post_Web contains lists relation username-postId
        List<Shared_Post_web> shared_post_webs = jdbcTemplate.query(QUERY_ALL_SHARED_POSTS_USER, new SharedPostWebLabMapper(), user_web.getUsername());

        //List all Posts
        List<Post_web> post_shared_with_user = new ArrayList<>();

        //Get List Share Posts
        for (Shared_Post_web shared_post_web: shared_post_webs) {
            int postId = shared_post_web.getPost_id();
            Post_web post_web = this.getPostById(postId);
            post_shared_with_user.add(post_web);
        }

        return post_shared_with_user;
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

        return new Post_web(resultSet.getInt("id"), resultSet.getString("title"),
                resultSet.getString("text"), resultSet.getBoolean("is_public"),
                resultSet.getTimestamp("date_create").toLocalDateTime(),
                resultSet.getTimestamp("date_edit").toLocalDateTime(),
                user_web);

    };


    private RowMapper<Shared_Post_web> mapperShared = (resultSet, i) -> {
        Shared_Post_web shared_post_web = new Shared_Post_web(resultSet.getString("username"), resultSet.getInt("post_id"));



        //post_web.setDate_create(resultSet.getTimestamp("date_creation").toLocalDateTime());
        //post_web.setDate_edit(resultSet.getTimestamp("date_edit").toLocalDateTime());

        return shared_post_web;
    };

    public int addShare(Shared_Post_web shared_post_web) {
        return jdbcTemplate.update(INSERT_SHARED, shared_post_web.getUsername(), shared_post_web.getPost_id());
    }

    public Shared_Post_web getSharedPostWeb(User_web user_web1, Post_web post_web) {
        return jdbcTemplate.queryForObject(QUERY_SHARED_POST, new SharedPostWebLabMapper(), user_web1.getUsername(), post_web.getId());
    }

    public void deleteSharedPostWeb(Shared_Post_web shared_post_web) {
        jdbcTemplate.update(DELETE_SHARED_POST, shared_post_web.getPost_user_id());
    }

    public boolean existPostById(String id) {

        int idToUse = Integer.parseInt(id);

        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_BY_ID_COUNT, new Object[] { idToUse }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;

    }

    public boolean existPostShared(User_web user_web, Post_web post_web) {
        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_SHARED_EXIST, new Object[] { user_web.getUsername(), post_web.getId() }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }


    private final class SharedPostWebLabMapper implements RowMapper<Shared_Post_web> {
        @Override
        public Shared_Post_web mapRow(ResultSet resultSet, int i) throws SQLException {
            Shared_Post_web shared_post_web = new Shared_Post_web(resultSet.getString("username"), resultSet.getInt("post_id"));
            return shared_post_web;
        }
    }



}
