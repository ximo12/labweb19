package tcm.quim.labweb.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("security_memory")
@EnableWebSecurity
public class MemorySecurityConfiguration extends BaseSecurityConfiguration {

    //The {noop} is used in Spring Security 5.0. It substitutes the NoOpPasswordEncoder, an encoder that does nothing
    //There are other encoders that encrypt using other algorithms
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER").and()
                .withUser("quim").password("{noop}quim").roles("ADMIN").and()
                .withUser("both").password("{noop}both").roles("USER,ADMIN");
    }

}
