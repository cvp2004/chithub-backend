package com.bezkoder.controllers;

import com.bezkoder.models.Agent;
import com.bezkoder.payload.response.MessageResponse;
import com.bezkoder.repository.MemberRepository;
import com.bezkoder.repository.UserRepository;
import com.bezkoder.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class MemberController {
     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
     private final JwtUtils jwtUtils;
     private final MemberRepository memberRepository;
     private final UserRepository userRepository;

     public MemberController(JwtUtils jwtUtils, MemberRepository memberRepository, UserRepository userRepository) {
          this.jwtUtils = jwtUtils;
          this.memberRepository = memberRepository;
          this.userRepository = userRepository;
     }

     @Autowired
     @GetMapping("/{username}")
     public ResponseEntity<?> getMemberDetails(@PathVariable String username, @CookieValue(value = "token") String token) {

          try {

               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               Agent agent = memberRepository.findByUsername(username);

               if(agent == null)
                    return ResponseEntity.internalServerError().build();
               else
                    return ResponseEntity.ok(agent);
          }
          catch(Exception e) {
               logger.error("Exception : " + e.getMessage());
               return ResponseEntity
                       .badRequest()
                       .body(new MessageResponse(e.getMessage()));
          }
     }

     @GetMapping()
     public ResponseEntity<?> getAll() {
          List<Agent> agents = memberRepository.findAll();
          return ResponseEntity.ok(agents);
     }

     @PutMapping("/{username}")
     public ResponseEntity<?> updateMemberDetails(@PathVariable String username, @CookieValue(value = "token") String token, @RequestBody Agent reqAgent) {

          try {
               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               Agent existingAgent = memberRepository.findByUsername(username);

               existingAgent.setName(reqAgent.getName());
               existingAgent.setPhoneNo(reqAgent.getPhoneNo());

               Agent newAgent = memberRepository.save(existingAgent);

               if (newAgent != null)
                    return ResponseEntity.ok(newAgent);
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
     public ResponseEntity<?> updateMemeberDetails(@PathVariable String username, @CookieValue(value = "token") String token) {

          try {
               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!memberRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               memberRepository.deleteAgentByUsername(username);

               if(!memberRepository.existsByUsername(username))
                    return ResponseEntity.ok().build();
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

}
