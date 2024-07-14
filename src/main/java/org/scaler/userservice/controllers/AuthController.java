package org.scaler.userservice.controllers;


import org.scaler.userservice.Repository.SessionRepository;
import org.scaler.userservice.dtos.*;
import org.scaler.userservice.exceptions.UserAlreadyExistsException;
import org.scaler.userservice.exceptions.UserDoesNotException;

import org.scaler.userservice.models.SessionStatus;
import org.scaler.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionRepository sessionRepository;

    public AuthController(AuthService authService, SessionRepository sessionRepository) {
        this.authService = authService;
        this.sessionRepository = sessionRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) throws UserDoesNotException {

        return authService.login(request.getEmail(),request.getPassword());





    }

    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(),request.getUserId());
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExistsException {


        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());

        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto request){

        SessionStatus sessionStatus = authService.validate(request.getToken(),request.getUserId());

        return new ResponseEntity<>(sessionStatus,HttpStatus.OK);


    }
}
