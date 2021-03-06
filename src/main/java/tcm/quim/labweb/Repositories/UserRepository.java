package tcm.quim.labweb.Repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tcm.quim.labweb.Domain.Friend_web;
import tcm.quim.labweb.Domain.User_web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    JdbcTemplate jdbcTemplate;

    private final String INSERT_NEW_FRIEND = "INSERT INTO friend_web (username1, username2) VALUES (?, ?)";

    private final String UPDATE_USER = "UPDATE user_web SET username = ?, name = ?, surname = ?, mail = ?, phone = ?, date_edit = ?, date_birth = ? WHERE id = ?";

    private final String QUERY_BY_USERNAME = "SELECT * FROM user_web WHERE username = ?";

    private final String QUERY_USERS_I_AM_FRIEND = "SELECT * FROM friend_web WHERE username2 = ?";
    private final String QUERY_MY_FRIEND = "SELECT * FROM friend_web WHERE username1 = ?";
    private final String QUERY_RELATION_FRIEND = "SELECT * FROM friend_web WHERE username1 = ? AND username2 = ?";
    private final String QUERY_RELATION_FRIEND_EXIST = "SELECT COUNT (*) FROM friend_web WHERE username1 = ? AND username2 = ?";
    private final String QUERY_USER_EXIST = "SELECT COUNT (*) FROM user_web WHERE username = ?";

    private final String DELETE_FRIEND = "DELETE FROM friend_web WHERE id = ?";



    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User_web getUserByUserName(String name) {
        return jdbcTemplate.queryForObject(QUERY_BY_USERNAME, new Object[]{name}, mapper);
    }

    public int updateUser(User_web user_web) {
        return jdbcTemplate.update(UPDATE_USER, user_web.getUsername(), user_web.getName(), user_web.getSurname(), user_web.getMail(),
                user_web.getPhone(), Timestamp.valueOf(user_web.getDate_edit()),
                Timestamp.valueOf(user_web.getDate_birth()), user_web.getId());
    }

    private RowMapper<User_web> mapper = (resultSet, i) -> {

        User_web user_web = new User_web(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("name"),
                resultSet.getString("surname"), resultSet.getString("mail"), Integer.parseInt(resultSet.getString("phone")),
                resultSet.getTimestamp("date_birth").toLocalDateTime());
        return user_web;

    };

    public List<User_web> getUsersThatImFriend(User_web user_web) {
        List<Friend_web> friend_webList = jdbcTemplate.query(QUERY_USERS_I_AM_FRIEND, new FriendUserWebMapper(), user_web.getUsername());

        List<User_web> usersThatImFriend = new ArrayList<>();

        for (Friend_web friend_web: friend_webList) {
            usersThatImFriend.add(this.getUserByUserName(friend_web.getUsername1()));
        }

        return usersThatImFriend;

    }

    public List<User_web> getMyFriends(User_web user_web) {
        List<Friend_web> friend_webList = jdbcTemplate.query(QUERY_MY_FRIEND, new FriendUserWebMapper(), user_web.getUsername());

        List<User_web> myFriends = new ArrayList<>();

        for (Friend_web friend_web: friend_webList) {
            myFriends.add(this.getUserByUserName(friend_web.getUsername2()));
        }
        return myFriends;
    }

    public int addNewFriend(User_web user_web, User_web user_web1) {

        return jdbcTemplate.update(INSERT_NEW_FRIEND, user_web.getUsername(), user_web1.getUsername());

    }

    public Friend_web getRelationFriend (User_web user_web, User_web user_web1) {

        return jdbcTemplate.queryForObject(QUERY_RELATION_FRIEND, new Object[] { user_web.getUsername(), user_web1.getUsername() }, mapperFriend);
    }

    public Boolean existRelationFriend (User_web user_web, User_web user_web1){

        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_RELATION_FRIEND_EXIST, new Object[] { user_web.getUsername(), user_web1.getUsername() }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }

    public Boolean existUserByUsername (String username){

        Boolean result = false;

        int count = jdbcTemplate.queryForObject(QUERY_USER_EXIST, new Object[] { username }, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }

    public void deleteFriend(Friend_web friend_web) {
        jdbcTemplate.update(DELETE_FRIEND, friend_web.getId());
    }


    private final class FriendUserWebMapper implements RowMapper<Friend_web> {
        @Override
        public Friend_web mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Friend_web(resultSet.getInt("id"), resultSet.getString("username1"), resultSet.getString("username2"));
        }
    }

    private RowMapper<Friend_web> mapperFriend = (resultSet, i) -> {

        Friend_web friend_web = new Friend_web(resultSet.getInt("id"), resultSet.getString("username1"), resultSet.getString("username2"));
        return friend_web;

    };

}
