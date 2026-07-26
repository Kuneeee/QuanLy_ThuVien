package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import service.LibraryUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_SYSTEM_ADMIN = "SYSTEM_ADMIN";

    @Autowired
    private LibraryUserDetailsService libraryUserDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
            .roles(ROLE_SYSTEM_ADMIN)
                .build();
        UserDetails librarian = User.withUsername("thuthu")
                .password(passwordEncoder().encode("thuthu123"))
                .roles(ROLE_ADMIN)
                .build();
        UserDetails teacher = User.withUsername("giaovien")
                .password(passwordEncoder().encode("giaovien123"))
                .roles("TEACHER")
                .build();
        UserDetails student = User.withUsername("sinhvien")
                .password(passwordEncoder().encode("sinhvien123"))
                .roles("STUDENT")
                .build();
        return new InMemoryUserDetailsManager(admin, librarian, teacher, student);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(libraryUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/doi-mat-khau").authenticated()
            .antMatchers(HttpMethod.GET, "/dashboard", "/heThong", "/heThong/**").hasRole(ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/taiLieu/new", "/taiLieu/*/edit", "/nhapTaiLieu/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/taiLieu/search", "/taiLieu/*").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.GET, "/taiLieu/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/docGia/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/muonTra/search", "/muonTra").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.GET, "/phong/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.GET, "/thongBao").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.GET, "/thongBao/chinh-sua/**", "/thongBao/xuat-bao-cao").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/muonTra/new", "/muonTra/*/edit").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.GET, "/muonTra/*").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.POST, "/muonTra/dat-truoc").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.POST, "/taiLieu/**", "/nhapTaiLieu/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.POST, "/muonTra/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.POST, "/phong/dat-phong").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN, "TEACHER", "STUDENT")
            .antMatchers(HttpMethod.POST, "/phong/lich-trong", "/phong/huy/**", "/phong/gia-han/**", "/phong/vi-pham/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.POST, "/thongBao/mau", "/thongBao/xoa/**", "/thongBao/xuat-bao-cao").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.POST, "/docGia/**").hasAnyRole(ROLE_ADMIN, ROLE_SYSTEM_ADMIN)
            .antMatchers(HttpMethod.POST, "/heThong/**").hasRole(ROLE_SYSTEM_ADMIN)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
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