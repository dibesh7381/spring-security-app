package Security.com.example.SecurityDemo.service;

import Security.com.example.SecurityDemo.config.JwtUtil;
import Security.com.example.SecurityDemo.dto.*;
import Security.com.example.SecurityDemo.exception.CustomException;
import Security.com.example.SecurityDemo.model.User;
import Security.com.example.SecurityDemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ✅ SIGNUP
    public User signup(SignupRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new CustomException("Email already registered!", HttpStatus.BAD_REQUEST.value());
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");

        return userRepository.save(user);
    }

    // ✅ LOGIN → only authenticate + return JWT token
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid email or password!", HttpStatus.UNAUTHORIZED.value()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid email or password!", HttpStatus.UNAUTHORIZED.value());
        }

        // ✅ Generate and return JWT
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    // ✅ Get current user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new CustomException("User not authenticated!", HttpStatus.UNAUTHORIZED.value());
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException("User not found!", HttpStatus.NOT_FOUND.value()));
    }

    // ✅ PROFILE
    public UserProfileDTO getProfile() {
        User user = getCurrentUser();
        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // ✅ CHANGE ROLE
    public void changeUserRole(String newRole) {
        User user = getCurrentUser();

        if (!newRole.equalsIgnoreCase("SELLER") && !newRole.equalsIgnoreCase("CUSTOMER")) {
            throw new CustomException("Invalid role! Allowed: CUSTOMER or SELLER", HttpStatus.BAD_REQUEST.value());
        }

        user.setRole(newRole.toUpperCase());
        userRepository.save(user);
    }

    // ✅ CUSTOMER HOME
    public CustomerHomeDTO getCustomerHomeData() {
        User user = getCurrentUser();

        CustomerHomeDTO dto = new CustomerHomeDTO();
        dto.setTitle("Welcome " + user.getUsername() + "!");
        dto.setContent("This is your exclusive customer home page 🎉");
        return dto;
    }

    // ✅ SELLER HOME
    public CustomerHomeDTO getSellerHomeData() {
        User user = getCurrentUser();

        CustomerHomeDTO dto = new CustomerHomeDTO();
        dto.setTitle("Welcome Seller " + user.getUsername() + "!");
        dto.setContent("Manage your products, orders, and earnings here 💼");
        return dto;
    }

    // ✅ PUBLIC HOME
    public HomeContentDTO getHomeContent() {
        return new HomeContentDTO(
                "Welcome to Security Demo App",
                "This is a demo homepage with hardcoded title and content using DTO structure."
        );
    }

    // ✅ PUBLIC HOME
    public AboutContentDTO getAboutContent() {
        return new AboutContentDTO(
                "This is the about page for this application",
                "This is a demo aboutpage with hardcoded title and content using DTO structure."
        );
    }
}











