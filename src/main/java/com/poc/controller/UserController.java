package com.poc.controller;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.poc.model.User;
import com.poc.model.dto.ChangePasswordDTO;
import com.poc.model.dto.FilterDTO;
import com.poc.model.dto.ResponseDTO;
import com.poc.security.TokenService;
import com.poc.service.UserService;
import com.poc.util.PasswordUtil;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserController {

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken JWT;

    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Object listUsers(FilterDTO filterDto) {
        User user = userService.findByUsername(JWT.getName());
        if (user != null && JWT.getRawToken().equals(user.token)) {
            if (filterDto != null) {
                return userService.findFilter(filterDto.column, filterDto.filter);
            } else {
                return userService.findAll();
            }
        } else {
            return new ResponseDTO("1006", "Erro ao validar token do usuário.");
        }
    }

    @POST
    @RolesAllowed("USER")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/change-password")
    public Object changePassword(ChangePasswordDTO changePass) {
        if (tokenService.validateToken(JWT)) {
            User user = userService.findByUsername(JWT.getName());
            if (user != null && PasswordUtil.verifyPassword(changePass.oldPassword, user.password)) {
                user.password = PasswordUtil.convertToBcryptPass(changePass.newPassword);
                userService.update(user);
                return new ResponseDTO("0", "Senha alterada com sucesso.");
            } else {
                return new ResponseDTO("1005", "Senha antiga informada está incorreta.");
            }
        } else {
            return new ResponseDTO("1006", "Erro ao validar token do usuário.");
        }
    }

    @POST
    @RolesAllowed("USER")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/change-role")
    public Object changeRole(ChangePasswordDTO changePass) {
        if (tokenService.validateToken(JWT)) {
            return new ResponseDTO("0", "TODO");
        } else {
            return new ResponseDTO("1006", "Erro ao validar token do usuário.");
        }
    }

}
