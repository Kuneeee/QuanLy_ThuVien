package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .and()
                .withUser("giaovien")
                .password(passwordEncoder().encode("giaovien123"))
                .roles("TEACHER")
                .and()
                .withUser("sinhvien")
                .password(passwordEncoder().encode("sinhvien123"))
                .roles("STUDENT");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/about", "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.GET, "/taiLieu/**", "/docGia/**", "/muonTra/**", "/nhapTaiLieu/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.POST, "/taiLieu/**", "/muonTra/**", "/nhapTaiLieu/**").hasAnyRole("ADMIN", "TEACHER")
                .antMatchers(HttpMethod.POST, "/docGia/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin();
    }
}