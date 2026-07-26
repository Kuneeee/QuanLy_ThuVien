package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class DemoAccountBootstrap implements ApplicationRunner {

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        upsertUser("admin", "admin123", "ADMIN");
        upsertUser("giaovien", "giaovien123", "TEACHER");
        upsertUser("sinhvien", "sinhvien123", "STUDENT");
    }

    private void upsertUser(String username, String rawPassword, String role) {
        UserDetails user = User.withUsername(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(role)
                .build();

        if (userDetailsManager.userExists(username)) {
            userDetailsManager.updateUser(user);
        } else {
            userDetailsManager.createUser(user);
        }
    }
}