package server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthStatefulSecurityConfig.class)
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDefaultRequireAuthentication() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/" + UUID.randomUUID()))
                .andExpect(status().is4xxClientError());
    }

    public void testRequireAuthenticationForAccountUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/account"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user0", roles = {"USER"})
    public void testAccountUser() throws Exception {
        String result = mockMvc.perform(
                MockMvcRequestBuilders.get("/account"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDetailsImpl user = objectMapper.readValue(result, UserDetailsImpl.class);

        assertThat(user.getUsername()).isEqualTo("user0");
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getAuthorities().size()).isEqualTo(1);
        assertThat(user.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
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

        public UserDetailsImpl() {
        }

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
