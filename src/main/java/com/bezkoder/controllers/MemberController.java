package com.bezkoder.controllers;

import com.bezkoder.models.JWTToken;
import com.bezkoder.models.Member;
import com.bezkoder.payload.response.MessageResponse;
import com.bezkoder.repository.MemberRepository;
import com.bezkoder.repository.TokenRepository;
import com.bezkoder.repository.UserRepository;
import com.bezkoder.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
     private final JwtUtils jwtUtils;
     private final MemberRepository memberRepository;
     private final UserRepository userRepository;
     private final TokenRepository tokenRepository;

     @Autowired
     public MemberController(JwtUtils jwtUtils, MemberRepository memberRepository, UserRepository userRepository, TokenRepository tokenRepository) {
          this.jwtUtils = jwtUtils;
          this.memberRepository = memberRepository;
          this.userRepository = userRepository;
          this.tokenRepository = tokenRepository;
     }

     @GetMapping("/{username}")
     @PreAuthorize("hasRole('MEMBER') or hasRole('AGENT')")
     public ResponseEntity<?> getMemberDetails(@PathVariable String username) {

          try {

               if(!userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Member Found !!\n" + "Requested Member Username : " + username);

               Member member = memberRepository.findByUsername(username);

               if(member == null)
                    return ResponseEntity.internalServerError().build();
               else
                    return ResponseEntity.ok(member);
          }
          catch(Exception e) {
               logger.error("Exception : " + e.getMessage());
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse(e.getMessage()));
          }
     }

     @PostMapping()
     @PreAuthorize("hasRole('MEMBER')")
     public ResponseEntity<?> registerMember( @RequestBody Member member) {
          try {
               String username = member.getUsername();

               if(memberRepository.existsByUsername(username))
                    throw new Exception("Member With Same Username already Exists !!\n" + " Member Username : " + username);

               Member newMember = memberRepository.save(member);

               if (newMember != null)
                    return ResponseEntity.ok(newMember);
               else
                    return ResponseEntity.internalServerError().build();
          }
          catch(Exception e) {
               logger.error("Exception : " + e.getMessage());
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse(e.getMessage()));
          }
     }
     @PutMapping("/{username}")
     @PreAuthorize("hasRole('MEMBER')")
     public ResponseEntity<?> updateMemberDetails(@PathVariable String username, @CookieValue(value = "token") String token, @Valid @RequestBody Member reqMember) {

          try {
               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized User Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Member Found !!\n" + "Requested Member Username : " + username);

               Member existingMember = memberRepository.findByUsername(username);

               existingMember.setName(reqMember.getName());
               existingMember.setPhoneNo(reqMember.getPhoneNo());

               Member newMember = memberRepository.save(existingMember);

               if (newMember != null)
                    return ResponseEntity.ok(newMember);
               else
                    return ResponseEntity.internalServerError().build();
          }
          catch(Exception e) {
               logger.error("Exception : " + e.getMessage());
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse(e.getMessage()));
          }
     }
     @DeleteMapping("/{username}")
     @PreAuthorize("hasRole('MEMBER')")
     public ResponseEntity<?> deleteMemeberDetails(@PathVariable String username, @CookieValue(value = "token") String token) {

          try {
               if(!userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized User Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Member Found !!\n" + "Requested Member Username : " + username);

               memberRepository.deleteByUsername(username);

               if(memberRepository.existsByUsername(username)) {

                    JWTToken userToken = tokenRepository.findByToken(token);
                    userToken.setValid(false);

                    tokenRepository.save(userToken);

                    return ResponseEntity.internalServerError().build();
               }
               else
                    return ResponseEntity.ok().build();
          }
          catch(Exception e) {
               logger.error("Exception : " + e.getMessage());
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse(e.getMessage()));
          }
     }

}
