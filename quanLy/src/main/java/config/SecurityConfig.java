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
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import service.LibraryUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LibraryUserDetailsService libraryUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        UserDetails teacher = User.withUsername("giaovien")
                .password(passwordEncoder().encode("giaovien123"))
                .roles("TEACHER")
                .build();
        UserDetails student = User.withUsername("sinhvien")
                .password(passwordEncoder().encode("sinhvien123"))
                .roles("STUDENT")
                .build();
        return new InMemoryUserDetailsManager(admin, teacher, student);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(libraryUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/about", "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/doi-mat-khau").authenticated()
                .antMatchers(HttpMethod.GET, "/dashboard").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/taiLieu/new", "/taiLieu/*/edit", "/nhapTaiLieu/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/taiLieu/search", "/taiLieu/*").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.GET, "/taiLieu/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/docGia/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/muonTra/search", "/muonTra").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.GET, "/phong/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.GET, "/thongBao").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.GET, "/thongBao/chinh-sua/**", "/thongBao/xuat-bao-cao").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/muonTra/new", "/muonTra/*/edit").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/muonTra/*").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.POST, "/muonTra/dat-truoc").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.POST, "/taiLieu/**", "/nhapTaiLieu/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/muonTra/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/phong/dat-phong").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .antMatchers(HttpMethod.POST, "/phong/lich-trong", "/phong/huy/**", "/phong/gia-han/**", "/phong/vi-pham/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/thongBao/mau", "/thongBao/xoa/**", "/thongBao/xuat-bao-cao").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/docGia/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
                    response.sendRedirect(isAdmin ? "/dashboard" : "/muonTra");
                })
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