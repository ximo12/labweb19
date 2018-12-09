package tcm.quim.labweb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class BaseSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/getPosts").authenticated()
                .antMatchers("/newPost").hasRole("USER")


                .anyRequest().authenticated()
                .and()
            .formLogin() //to use forms (web)
                .and()
            .httpBasic() //to use http headers REST
                .and()
            .rememberMe()
                .tokenValiditySeconds(2419200)
                .key("tecnocampus")
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //needed only when csrf is enabled (as by default is post)
                .logoutSuccessUrl("/logout_page.html"); //where to go when logout is successful
                //.logoutUrl("logoutpage"); // default is "/logout""

            http
                    .csrf().disable()
                    .headers()
                    .frameOptions().disable();


    }
}
