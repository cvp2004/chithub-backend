package com.bezkoder.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bezkoder.models.JWTToken;
import com.bezkoder.repository.TokenRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.models.ERole;
import com.bezkoder.models.Role;
import com.bezkoder.models.User;
import com.bezkoder.payload.request.LoginRequest;
import com.bezkoder.payload.request.SignupRequest;
import com.bezkoder.payload.response.UserInfoResponse;
import com.bezkoder.payload.response.MessageResponse;
import com.bezkoder.repository.RoleRepository;
import com.bezkoder.repository.UserRepository;
import com.bezkoder.security.jwt.JwtUtils;
import com.bezkoder.security.services.UserDetailsImpl;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  final AuthenticationManager authenticationManager;
  final UserRepository userRepository;
  final RoleRepository roleRepository;
  final PasswordEncoder encoder;
  final JwtUtils jwtUtils;

  @Value("${bezkoder.app.jwtCookieName}")
  private String jwtCookie;
  private final TokenRepository tokenRepository;

  public AuthController(
          AuthenticationManager authenticationManager,
          UserRepository userRepository,
          RoleRepository roleRepository,
          PasswordEncoder encoder,
          JwtUtils jwtUtils,
          TokenRepository tokenRepository)
  {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
    this.tokenRepository = tokenRepository;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = getRoles(signUpRequest);

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }


  @PostMapping("/logout")
  public ResponseEntity<?> logOut(@CookieValue(value = "token") String jwt) {
    JWTToken existingJwtToken = tokenRepository.findTokenByToken(jwt);

    existingJwtToken.setValid(false);

    JWTToken newJwtToken = tokenRepository.save(existingJwtToken);

    return ResponseEntity.ok(new MessageResponse("User Successfully Logged Out !!"));
  }

  private Set<Role> getRoles(SignupRequest signUpRequest) {
    Set<String> strRoles = signUpRequest.getRoles();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
          throw new RuntimeException("Error: Role is not found.");
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "agent":
            Role adminRole = roleRepository.findByName(ERole.ROLE_AGENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "member":
            Role userRole = roleRepository.findByName(ERole.ROLE_MEMBER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            break;
            default:
              throw new RuntimeException("Error: Role is not found.");
        }
      });
    }
    return roles;
  }
}
