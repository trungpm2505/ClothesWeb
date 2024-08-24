package com.web.clothes.ClothesWeb.config;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.web.clothes.ClothesWeb.jwt.JwtAuthenticationFilter;
import com.web.clothes.ClothesWeb.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final UserServiceImpl userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String[] NO_LOG_IN = {
    		
            "/login/checkLogin",
            "/user/sucess",
            "/user/signUp",
            "/register",
            "/register/checkRegister/**",
            "/register/confirm-account/**",
            "/register/resendMail",
            "/product/details/**",
            "/product/user-home/**",
            "/product/getProductPage/**",
            "/product/all-roduct/**",
            "/product/women-fashion/**",
            "/product/men-fashion/**",
            "/product/best-seller/**",
            "/product/getBestSeller/**",
            "/password/reset",
            "/password/sentEmail/**",
            "/password/getTokenReset/**",
            "/password/checkTokenReset",
            "/password/setNewPassView",
            "/password/checkResetPass",
            "/password/resetPassSuccess",
            "/category/getAll",
            "/product/search/**",
            "/product/getProductView",
            "/product/searchByCategory/**",
            "/images/**",
            "/css/**",
            "/js/**",
            "/upload/**",
            "/feedback/user/**",
            "/feedback/user/contact/**",
            "/rate/getRatePage/**"
            //"/response/add/**"
            
    };
    
    private static final String[] ROLE_USER = {
    		
            "/user/accountUser",
            "/cart/**",
            "/order/checkout/**",
            "/order/add",
            "/order/user/getOrderPage",
            "/order/all-order",
            "/order/confirmed-order",
            "/order/new-order",
            "/order/canceled-order",
            "/order/completed-order",
            
            
    };
    
    private static final String[] ROLE_ADMIN = {
    		
            "/user/update/**",
            "/user/delete/**",
            "/user/list",
            "/user/getUserPage/**",
            "/user/getAllRole/**",
            "/user/accountAdmin",
            //"/attributeValue/add",
//            "/attributeValue/update",
//            "/attributeValue/delete",
            "/attributeValue/**",
            "/category/add",
            "/category/update/**",
            "/category/delete/**",
            "/category/getCategoryPage/**",
            
            "/feedback/admin/list",
            "/feedback/viewDetail/**",
            "/feedback/getFeedbackPage/**",
            
            "/order/admin/all",
            "/order/admin/confirmed",
            "/order/admin/new",
            "/order/admin/canceled",
            "/order/admin/completed",
            "/order/getOrderPage/**",
            "/order/updateStatus",
            
            "/product/admin",
            "/product/add/**",
            "/product/update/**",
            "/product/delete/**",
            "/report/**"
            
            
    };

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(NO_LOG_IN);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
       .csrf()
       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
       .and()
       .cors().and()
                .authorizeRequests()
                .antMatchers(NO_LOG_IN).permitAll()
//                .antMatchers("/cart/save/**").authenticated()
                .and()
                .authorizeRequests()
                .antMatchers(ROLE_USER).hasAuthority("USER")
                .and()
                .authorizeRequests()
                .antMatchers(ROLE_ADMIN).hasAuthority("ADMIN")
                .and()
                .authorizeRequests()
                .anyRequest()
                //.permitAll()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutUrl("/logout") // Url của trang logout.
                //.logoutSuccessHandler(new CustomLogoutSuccessHandler()) // Đăng ký LogoutSuccessHandler tùy chỉnh.
                
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                
                //.logoutSuccessUrl("/login")
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.removeAttribute("username");
                    }
                    // Chuyển hướng đến trang đăng nhập
                    response.sendRedirect("/product/user-home");
                });
                ;
        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
    
}
