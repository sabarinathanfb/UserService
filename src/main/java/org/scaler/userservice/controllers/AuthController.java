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
    public ResponseEntity<ValidateTokenResponseDto> validateToken(@RequestBody ValidateTokenRequestDto request){

        Optional<UserDto> userDto = authService.validate(request.getToken(),request.getUserId());

        if(userDto.isEmpty()){
            ValidateTokenResponseDto response = new ValidateTokenResponseDto();
            response.setSessionStatus(SessionStatus.INVALID);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        ValidateTokenResponseDto response = new ValidateTokenResponseDto();
        response.setSessionStatus(SessionStatus.ACTIVE);
        response.setUserDto(userDto.get());

        return new ResponseEntity<>(response,HttpStatus.OK);




    }
}
