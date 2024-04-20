package com.bezkoder.controllers;


import com.bezkoder.models.Agent;
import com.bezkoder.payload.response.MessageResponse;
import com.bezkoder.repository.AgentRepository;
import com.bezkoder.repository.UserRepository;
import com.bezkoder.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/agents")
public class AgentController {
     private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
     private final JwtUtils jwtUtils;
     private final AgentRepository agentRepository;
     private final UserRepository userRepository;

     @Autowired
     public AgentController(JwtUtils jwtUtils, AgentRepository agentRepository, UserRepository userRepository) {
          this.jwtUtils = jwtUtils;
          this.agentRepository = agentRepository;
          this.userRepository = userRepository;
     }

     @GetMapping("/{username}")
     @PreAuthorize("hasRole('AGENT')")
     public ResponseEntity<?> getAgent(@PathVariable String username, @CookieValue(value = "token") String token) {

          try {

               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!agentRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               Agent agent = agentRepository.findByUsername(username);

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

//     @GetMapping()
//     public ResponseEntity<?> getAllAgents() {
//         List<Agent> agents = agentRepository.findAll();
//         return ResponseEntity.ok(agents);
//     }

     @PostMapping
     @PreAuthorize("hasRole('AGENT')")
     public ResponseEntity<?> registerAgent(@RequestBody Agent agent, @CookieValue(value = "token") String token) {
          try {
               String username = jwtUtils.getUserNameFromJwtToken(token);

               if(agentRepository.existsByUsername(username))
                    throw new Exception("Agent With Same Username already Exists !!\n" + " Agent Username : " + username);

               agent.setUsername(username);

               Agent newAgent = agentRepository.save(agent);

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

     @PutMapping("/{username}")
     @PreAuthorize("hasRole('AGENT')")
     public ResponseEntity<?> updateAgent(@PathVariable String username, @CookieValue(value = "token") String token, @RequestBody Agent reqAgent) {

          try {
               if(userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!agentRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               Agent existingAgent = agentRepository.findByUsername(username);

               existingAgent.setName(reqAgent.getName());
               existingAgent.setPhoneNo(reqAgent.getPhoneNo());

               Agent newAgent = agentRepository.save(existingAgent);

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
     @PreAuthorize("hasRole('AGENT')")
     public ResponseEntity<?> deleteAgent(@PathVariable String username, @CookieValue(value = "token") String token) {

          try {
               if(!userRepository.existsByUsername(username))
                    throw new Exception("No User Found !!\n" + "Requested Username : " + username);

               String tokenUserName = jwtUtils.getUserNameFromJwtToken(token);

               if(!username.equals(tokenUserName))
                    throw new Exception("Request for unauthorized Agent Details !!\n" + "Requested Username : " + username + "\nCurrent User : " + tokenUserName);

               if(!agentRepository.existsByUsername(username))
                    throw new Exception("No Agent Found !!\n" + "Requested Agent Username : " + username);

               agentRepository.deleteAgentByUsername(username);

               if(!agentRepository.existsByUsername(username))
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
