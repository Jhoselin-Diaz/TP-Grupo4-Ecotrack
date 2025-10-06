package com.example.tpgrupo4ecotrack.Controller;




import com.example.tpgrupo4ecotrack.DTO.AuthRequest;
import com.example.tpgrupo4ecotrack.DTO.AuthResponse;
import com.example.tpgrupo4ecotrack.Security.JwtTokenUtil;
import com.example.tpgrupo4ecotrack.Security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class JwtAuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest authRequest) {
        if (userService.existsByUsername(authRequest.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso");
        }
        userService.registerNewUser(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
