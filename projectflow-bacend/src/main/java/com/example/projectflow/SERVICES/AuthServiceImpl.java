package com.example.projectflow.SERVICES;

import com.example.projectflow.DTOs.*;
import com.example.projectflow.ENTITIES.User;
import com.example.projectflow.REPOSITORIES.UserRepository;
import com.example.projectflow.SECURITY.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        return LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }
    @Override
    public LoginResponse register(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .nom(registerDTO.getNom())
                .email(registerDTO.getEmail())
                .pasword(passwordEncoder.encode(registerDTO.getPassword()))
                .role(User.Role.valueOf(registerDTO.getRole())) // par exemple "Professeur" ou "Etudiant"
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        return LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }

}
