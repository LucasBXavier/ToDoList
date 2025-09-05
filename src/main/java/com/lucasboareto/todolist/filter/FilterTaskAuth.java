package com.lucasboareto.todolist.filter;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.lucasboareto.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Log
@Component
@AllArgsConstructor
public class FilterTaskAuth extends OncePerRequestFilter {

    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {

            var authorization = request.getHeader("Authorization");

            var userPassword = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(userPassword);

            var newString = new String(authDecode);
            String[] credentials = newString.split(":");
            String username = credentials[0];
            String password = credentials[1];
            var user = this.userRepository.findByUserName(username);

            if (user == null) {
                response.sendError(401);
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Unauthorized");
                }
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }
}
