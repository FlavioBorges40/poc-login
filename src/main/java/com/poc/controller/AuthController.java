package com.poc.controller;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.poc.enumerator.RoleUserEnum;
import com.poc.model.User;
import com.poc.model.dto.LoginDTO;
import com.poc.model.dto.ResponseDTO;
import com.poc.security.TokenService;
import com.poc.service.UserService;
import com.poc.util.PasswordUtil;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthController {

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @POST
    @Transactional
    @PermitAll
    @Path("/cadastrar")
    public Object signUser(@Valid User user){
        User tempUser = userService.findUser(user);
        if(tempUser != null) {
            return new ResponseDTO("1001", "Email ou Username já cadastrado.");
        } else {
            user.password = PasswordUtil.convertToBcryptPass(user.password);
            user.role = RoleUserEnum.USER;
            userService.insert(user);
            return new ResponseDTO("0", "Usuario cadastrado com sucesso.");
        }
    }

    @POST
    @Transactional
    @PermitAll
    @Path("/logar")
    public Object loginUser(LoginDTO login){
        User tempUser = userService.findByUsername(login.username);
        if(tempUser != null){
            if(PasswordUtil.verifyPassword(login.password, tempUser.password)){
                String token = tokenService.generateUserToken(tempUser.email, tempUser.username, tempUser.role.toString());
                login.token = token;
                tempUser.token = token;
                userService.update(tempUser);
                return login;
            } else {
                return new ResponseDTO("1003", "Senha incorreta.");    
            }
        } else {
            return new ResponseDTO("1002", "Usuario não existe.");
        }
    }
}
