package org.scaler.userservice.services;



import org.apache.commons.lang3.RandomStringUtils;
import org.scaler.userservice.Repository.SessionRepository;
import org.scaler.userservice.dtos.UserDto;
import org.scaler.userservice.exceptions.UserAlreadyExistsException;
import org.scaler.userservice.exceptions.UserDoesNotException;
import org.scaler.userservice.models.Session;
import org.scaler.userservice.models.SessionStatus;
import org.scaler.userservice.models.User;
import org.scaler.userservice.Repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository,
                       SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public UserDto signUp(String email, String password) throws UserAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isEmpty()){
            throw new UserAlreadyExistsException("User with" + email + "already Exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);

    }

    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            throw new UserDoesNotException("User with email:" + email + "does not exist");
        }

        User user = userOptional.get();

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>(HttpStatus.NON_AUTHORITATIVE_INFORMATION);

        }

        RandomStringUtils randomStringUtils = new RandomStringUtils();
        String token = randomStringUtils.randomAlphabetic(20);

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>(new HashMap<>());
        headers.add("AUTHTOKEN",token);

        Session session = new Session();
        session.setSessionstatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);


        UserDto userDto = UserDto.from(user);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto,headers, HttpStatus.OK);


        return response;

    }

    public SessionStatus validate(String token,Long userId)  {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token,userId);

        if(sessionOptional.isEmpty()){
            return SessionStatus.INVALID;

        }
        Session session = sessionOptional.get();

        if(!session.getSessionstatus().equals(SessionStatus.ACTIVE)){
            return SessionStatus.EXPIRED;
        }



        return SessionStatus.ACTIVE;


    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token,userId);

        if(sessionOptional.isEmpty()){
            return null;
        }

        Session session = sessionOptional.get();
        session.setSessionstatus(SessionStatus.LOGGED_OUT);
        sessionRepository.save(session);

        return ResponseEntity.ok().build();

    }


}
