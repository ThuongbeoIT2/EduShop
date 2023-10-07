package com.example.ttversion1.login.configuration;



import com.example.ttversion1.login.serviceImpl.MyAccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();

    @Autowired
    private MyAccountDetailsService accountDetailsService;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .userDetailsService(accountDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";

        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(loginPage).permitAll()
                .antMatchers("/account/confirmotp").permitAll()
                .antMatchers("/product/search/{name}").permitAll()
                .antMatchers("/product/detailproduct/{productname}").permitAll()
                .antMatchers("/forgotpassword").permitAll()
                .antMatchers("/resendRegistrationToken").permitAll()
              .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/admin/").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage)
                .loginPage("/")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl(loginPage).and().exceptionHandling()
                .and()
                .sessionManagement() // Cấu hình quản lý phiên
                .sessionFixation().migrateSession() // Đảm bảo phiên mới được tạo sau khi đăng nhập thành công
                .maximumSessions(100000) // Chỉ cho phép một phiên hoạt động tại một thời điểm
                .expiredUrl(loginPage) // Đường dẫn chuyển hướng khi phiên hết hạn
                .and()
                .invalidSessionUrl(loginPage); // Đường dẫn chuyển hướng khi phiên không hợp lệ
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
