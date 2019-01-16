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

    private final String SAVE_POST = "UPDATE post_web SET title = ?, text = ?, is_public = ?, date_edit = ? WHERE id = ?";


    private final String QUERY_BY_ID = "SELECT * FROM post_web WHERE id = ?";
    private final String QUERY_BY_ID_COUNT = "SELECT COUNT (*) FROM post_web WHERE id = ?";
    private final String QUERY_SHARED_EXIST = "SELECT COUNT (*) FROM shared_post WHERE username = ? AND post_id = ?";
    private final String QUERY_EXIST_POST_SHARED = "SELECT COUNT (*) FROM shared_post WHERE post_id = ?";



    private final String QUERY_ALL_SHARED_POSTS_USER = "SELECT * FROM shared_post WHERE username = ?";
    private final String QUERY_ALL_OWNER_POSTS_USER = "SELECT * FROM post_web WHERE owner = ?";
    private final String QUERY_ALL_OWNER_POSTS_USER_PUBLIC = "SELECT * FROM post_web WHERE owner = ? AND is_public = ?";
    private final String QUERY_SHARED_POST = "SELECT * FROM shared_post WHERE username = ? AND post_id = ?";
    private final String QUERY_SHARED_POST_BY_POST = "SELECT * FROM shared_post WHERE post_id = ?";
    private final String DELETE_SHARED_POST = "DELETE FROM shared_post WHERE post_user_id = ?";


    private final String DELETE_POST = "DELETE FROM post_web WHERE id = ?";





    public PostRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public Post_web getPostById(int id) {
        return jdbcTemplate.queryForObject(QUERY_BY_ID, new Object[]{id}, mapper);
    }




    public int savePost(Post_web post_web) {
        return jdbcTemplate.update(SAVE_POST, post_web.getTitle(), post_web.getText(), post_web.getIs_public(), post_web.getDate_edit(), post_web.getId());
    }

    public int addNewPost(Post_web post_web, User_web owner) {
        return jdbcTemplate.update(INSERT_POST, post_web.getTitle(), post_web.getText(), post_web.getIs_public(),
                Timestamp.valueOf(post_web.getDate_create()), Timestamp.valueOf(post_web.getDate_edit()), owner.getUsername());
    }




    public List<Post_web>  getMyPosts(User_web user_web) {
        return jdbcTemplate.query(QUERY_ALL_OWNER_POSTS_USER, new Object[]{user_web.getUsername()}, mapper);
    }

    public List<Post_web>  getMyPostsForOtherUser(User_web user_web) {
        return jdbcTemplate.query(QUERY_ALL_OWNER_POSTS_USER_PUBLIC, new Object[]{user_web.getUsername(), true}, mapper);
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


    public int addShare(Shared_Post_web shared_post_web) {
        return jdbcTemplate.update(INSERT_SHARED, shared_post_web.getUsername(), shared_post_web.getPost_id());
    }

    public Shared_Post_web getSharedPostWeb(User_web user_web1, Post_web post_web) {
        return jdbcTemplate.queryForObject(QUERY_SHARED_POST, new SharedPostWebLabMapper(), user_web1.getUsername(), post_web.getId());
    }

    public void deleteSharedPostWeb(Shared_Post_web shared_post_web) {
        jdbcTemplate.update(DELETE_SHARED_POST, shared_post_web.getPost_user_id());
    }

    public boolean existPostById(int idToUse) {

        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_BY_ID_COUNT, new Object[] { idToUse }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;

    }

    public boolean existPostSharedUserPost(User_web user_web, Post_web post_web) {
        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_SHARED_EXIST, new Object[] { user_web.getUsername(), post_web.getId() }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }

    public void deletePost(Post_web post_web) {
        jdbcTemplate.update(DELETE_POST, post_web.getId());
    }

    public Boolean existPostShared(Post_web post_web) {
        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_EXIST_POST_SHARED, new Object[] {post_web.getId() }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }

    public List<Shared_Post_web> getAllSharedPostByPost(Post_web post_web) {
        List<Shared_Post_web> shared_post_webs = jdbcTemplate.query(QUERY_SHARED_POST_BY_POST, new SharedPostWebLabMapper(), post_web.getId());
        return  shared_post_webs;
    }


    private final class SharedPostWebLabMapper implements RowMapper<Shared_Post_web> {
        @Override
        public Shared_Post_web mapRow(ResultSet resultSet, int i) throws SQLException {
            Shared_Post_web shared_post_web = new Shared_Post_web(resultSet.getInt("post_user_id"), resultSet.getString("username"), resultSet.getInt("post_id"));
            return shared_post_web;
        }
    }



}
