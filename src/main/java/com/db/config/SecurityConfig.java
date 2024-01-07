package com.db.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.db.security.AuthEntryPointJwt;
import com.db.security.AuthTokenFilter;
import com.db.service.UserDetailsServiceImpl;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(
				// requests -> requests.antMatchers("/swagger-resources/*", "*.html",
				// "/api/v1/swagger.json")
				requests -> requests.antMatchers("/")
						// .hasAuthority("SWAGGER")
						// .anyRequest()
						.permitAll())
				.httpBasic(withDefaults()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions().disable());
	}
	
//	 @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	        http.cors().and().csrf().disable()
//	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//	                .authorizeRequests().antMatchers("/api/auth/**", "/api/lots/**", "/api/lot/**").permitAll()
//	                .antMatchers("/api/test/**", "/images/**", "/").permitAll()
//	                .anyRequest().authenticated();
//
//	        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("SWAGGER");
	}

}