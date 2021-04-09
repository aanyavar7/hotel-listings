package server.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.security.SecureRandom;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // In the FlightIntegrationTests, make sure and note that the
        // post(...) methods are all followed by a call to .with(csrf()).
        // Without this extra call, the appropriate CSRF token will not
        // be generated in the request and it will fail. The project has
        // the Spring Security test dependency added to provide the csrf()
        // method.

        httpSecurity
                .authorizeRequests()
                // We specifically allow certain paths for all users, but the default
                // behavior is to force authentication for anything that isn't explicitly
                // allowed here.
                .anyRequest().authenticated()
//                .hasRole("USER")
                .and()
                .httpBasic();
//                .antMatchers(
//                        "/" + Constants.EndPoint.AIRPORTS,
//                        "/" + Constants.EndPoint.BEST_PRICE,
//                        "/" + Constants.EndPoint.FLIGHT_DATES,
//                        "/" + Constants.EndPoint.FLIGHTS,
//                        "/" + Constants.EndPoint.RATE
//                )
//                .permitAll()
//                // We default to requiring every request to at least come from
//                // an authorized user
//                .anyRequest()
//                .hasRole("USER")
//                .and()
//                .formLogin();
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // This example preconfigures two users for the system and generates
        // random passwords for them. The passwords are printed to the console.
        //
        // This should not be used in any form of production system but is used
        // solely for informational purposes.
        //
        //
        //  INSECURE, INSECURE, SECURITY RISK, SECURITY RISK
        //
        //

        String user = "user";
        String userPassword = "pass";
        String admin = "admin";
        String adminPassword =
                RandomStringUtils.random(24, 0, 0, true, true, null, new SecureRandom());

        System.out.println("Admin User: " + admin);
        System.out.println("Admin Pass: " + adminPassword);

        System.out.println("Test User: " + user);
        System.out.println("User Pass: " + userPassword);

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(User.withUsername(admin)
                        // We always need to salt & hash the passwords before they are stored
                        // in the database.
                        .password(passwordEncoder().encode(adminPassword))
                        .roles("ADMIN", "USER"))
                .withUser(User.withUsername(user)
                        // We always need to salt & hash the passwords before they are stored
                        // in the database.
                        .password(passwordEncoder().encode(userPassword))
                        .roles("USER"));
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
        // We need to be able to get access to a JdbcUserDetailsManager bean
        // so that the AccountController can add user accounts in the demo.
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Passwords need to be salted & hashed before being stored in the database.
        // This bean takes care of both.
        return new BCryptPasswordEncoder();
    }

}
