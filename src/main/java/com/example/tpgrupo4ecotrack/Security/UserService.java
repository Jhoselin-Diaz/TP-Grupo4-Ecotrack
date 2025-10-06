package com.example.tpgrupo4ecotrack.Security;

import com.example.tpgrupo4ecotrack.Entity.Role;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
    public String encodePassword(String rawPassword) {

        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    public boolean existsByUsername(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    public void registerNewUser(String username, String rawPassword) {
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(encodePassword(rawPassword));

        Role defaultRole = new Role();
        defaultRole.setName("USER");
        defaultRole.setName("ADMIN");

        user.setRoles(List.of(defaultRole));
        userRepository.save(user);
    }
}


