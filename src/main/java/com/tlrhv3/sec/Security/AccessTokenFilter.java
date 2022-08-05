package com.tlrhv3.sec.Security;

import com.tlrhv3.sec.Entity.User;
import com.tlrhv3.sec.Jwt.JwtHelper;
import com.tlrhv3.sec.Service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
public class AccessTokenFilter extends OncePerRequestFilter {

    private JwtHelper jwtHelper;
    public UserService userService;

    public AccessTokenFilter(JwtHelper jwtHelper,UserService userService) {
        this.jwtHelper = jwtHelper;
        this.userService =userService;
    }

    public AccessTokenFilter() {

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            Optional<String> accesstoken = parseAccessToken(request);
            if (accesstoken.isPresent() && jwtHelper.validateAccessToken(accesstoken.get()) ){
                Long userId = Long.valueOf(jwtHelper.getUserIdFromAccessToken(accesstoken.get()));
                User user =userService.findById(userId);
                UsernamePasswordAuthenticationToken upat = new
                        UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }

        }catch (Exception e){
            log.error("Cannot set authentication",e);

        }
        filterChain.doFilter(request,response);

    }

    private Optional<String > parseAccessToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")){
             String jwt = authHeader.substring(7);
             return Optional.of(authHeader.replace("Bearer",""));

        }
        return Optional.empty();

    }

}
