package tcm.quim.labweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.sql.DataSource;

@EnableWebSecurity
public class JDBCSecurityConfiguration extends BaseSecurityConfiguration {
    private DataSource dataSource;

    private static final String USERS_QUERY = "select username, password, enabled from user_web where username = ?";

    private static final String AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public JDBCSecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Configure authentication manager
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_QUERY);
    }

}
