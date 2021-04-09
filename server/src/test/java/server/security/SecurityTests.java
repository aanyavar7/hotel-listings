package server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.jpa.properties.hibernate.show_sql=false",
        "spring.config.name=flight-test-h2",
        "flight.datasource.url=jdbc:h2:mem:trxServiceStatus"
})
@AutoConfigureMockMvc
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //@Test
    public void testDefaultRequireAuthentication() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/" + UUID.randomUUID().toString()))
                .andExpect(status().is3xxRedirection());
    }

    public void testRequireAuthenticationForAccountUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/account" ))
                .andExpect(status().is3xxRedirection());
    }

    //@Test
    @WithMockUser(username="user",roles={"NOTUSER"})
    public void testRequireUserRoleForAccountUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/account" ))
                .andExpect(status().isForbidden());
    }

    //@Test
    @WithMockUser(username="user",roles={"USER"})
    public void testAccountUser() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.get("/account" ))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDetailsImpl user = objectMapper.readValue(result, UserDetailsImpl.class);

        Assertions.assertEquals("user", user.getUsername());
        Assertions.assertTrue(user.isAccountNonExpired());
        Assertions.assertTrue(user.isAccountNonLocked());
        Assertions.assertTrue(user.isCredentialsNonExpired());
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertEquals(1, user.getAuthorities().size());
        Assertions.assertEquals("ROLE_USER", user.getAuthorities().iterator().next().getAuthority());
    }

    //@Test
    @WithMockUser(username="admin",roles={"USER"})
    public void testRequireAdminRoleForAccountCreation() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/account" )
                        .with(csrf())
                        .queryParam("username","bob")
                        .queryParam("password","pass"))
                .andExpect(status().isForbidden());
    }

    //@Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void testAccountCreation() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.post("/account")
                        .with(csrf())
                        .queryParam("username", "bob")
                        .queryParam("password", "pass"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDetailsImpl user = objectMapper.readValue(result, UserDetailsImpl.class);

        Assertions.assertEquals("bob", user.getUsername());
        Assertions.assertTrue(user.isAccountNonExpired());
        Assertions.assertTrue(user.isAccountNonLocked());
        Assertions.assertTrue(user.isCredentialsNonExpired());
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertEquals(1, user.getAuthorities().size());
        Assertions.assertEquals("ROLE_USER", user.getAuthorities().iterator().next().getAuthority());

        mockMvc.perform(formLogin().user("bob").password("passWRD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(formLogin().user("bob").password("pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        result = mockMvc.perform(
                MockMvcRequestBuilders.get("/account" ).with(user("bob")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        user = objectMapper.readValue(result, UserDetailsImpl.class);

        Assertions.assertEquals("bob", user.getUsername());
        Assertions.assertTrue(user.isAccountNonExpired());
        Assertions.assertTrue(user.isAccountNonLocked());
        Assertions.assertTrue(user.isCredentialsNonExpired());
        Assertions.assertTrue(user.isEnabled());
        Assertions.assertEquals(1, user.getAuthorities().size());
        Assertions.assertEquals("ROLE_USER", user.getAuthorities().iterator().next().getAuthority());
    }

    private static class SimplerGrantedAuthority implements GrantedAuthority {

        private String authority;

        @Override
        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }

    private static class UserDetailsImpl implements UserDetails {
        private String username;
        private String password;
        private List<GrantedAuthority> authorities = new ArrayList<>();
        private boolean isAccountNonExpired;
        private boolean isCredentialsNonExpired;
        private boolean isAccountNonLocked;
        private boolean isEnabled;

        public UserDetailsImpl(){}

        @Override
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public List<GrantedAuthority> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<SimplerGrantedAuthority> authorities) {
            this.authorities.addAll(authorities);
        }

        @Override
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean isAccountNonExpired() {
            return isAccountNonExpired;
        }

        public void setAccountNonExpired(boolean accountNonExpired) {
            isAccountNonExpired = accountNonExpired;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return isCredentialsNonExpired;
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
            isCredentialsNonExpired = credentialsNonExpired;
        }

        @Override
        public boolean isAccountNonLocked() {
            return isAccountNonLocked;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
            isAccountNonLocked = accountNonLocked;
        }

        @Override
        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }
    }
}
