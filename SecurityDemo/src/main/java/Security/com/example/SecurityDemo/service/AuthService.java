package Security.com.example.SecurityDemo.service;

import Security.com.example.SecurityDemo.config.JwtUtil;
import Security.com.example.SecurityDemo.dto.*;
import Security.com.example.SecurityDemo.model.User;
import Security.com.example.SecurityDemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ✅ SIGNUP
    public ApiResponseDTO<?> signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponseDTO<>(false, "Email already registered!", null);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER"); // default role

        userRepository.save(user);
        return new ApiResponseDTO<>(true, "Signup successful!", null);
    }

    // ✅ LOGIN (Generate JWT with Role)
    public ApiResponseDTO<?> login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return new ApiResponseDTO<>(false, "Invalid email or password!", null);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponseDTO<>(false, "Invalid email or password!", null);
        }

        // ✅ Generate JWT with both email & role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new ApiResponseDTO<>(true, "Login successful!", token);
    }

    // ✅ Get current logged-in user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("User not authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ PROFILE
    public ApiResponseDTO<?> getProfile() {
        try {
            User user = getCurrentUser();

            UserProfileDTO profileDTO = new UserProfileDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );

            return new ApiResponseDTO<>(true, "Profile fetched successfully!", profileDTO);
        } catch (Exception e) {
            return new ApiResponseDTO<>(false, e.getMessage(), null);
        }
    }

    // ✅ CHANGE ROLE
    public ApiResponseDTO<?> changeUserRole(String newRole) {
        try {
            User user = getCurrentUser();

            if (!newRole.equalsIgnoreCase("SELLER") && !newRole.equalsIgnoreCase("CUSTOMER")) {
                return new ApiResponseDTO<>(false, "Invalid role! Allowed: CUSTOMER or SELLER", null);
            }

            user.setRole(newRole.toUpperCase());
            userRepository.save(user);

            return new ApiResponseDTO<>(true, "User role updated to " + newRole.toUpperCase(), null);
        } catch (Exception e) {
            return new ApiResponseDTO<>(false, e.getMessage(), null);
        }
    }

    // ✅ CUSTOMER HOME
    public ApiResponseDTO<?> getCustomerHomeData() {
        try {
            User user = getCurrentUser();

            if (!"CUSTOMER".equalsIgnoreCase(user.getRole())) {
                return new ApiResponseDTO<>(false, "Access denied! Only customers allowed.", null);
            }

            CustomerHomeDTO dto = new CustomerHomeDTO();
            dto.setTitle("Welcome " + user.getUsername() + "!");
            dto.setContent("This is your exclusive customer home page 🎉");

            return new ApiResponseDTO<>(true, "Customer data fetched successfully!", dto);

        } catch (Exception e) {
            return new ApiResponseDTO<>(false, e.getMessage(), null);
        }
    }

    // ✅ SELLER HOME
    public ApiResponseDTO<?> getSellerHomeData() {
        try {
            User user = getCurrentUser();

            if (!"SELLER".equalsIgnoreCase(user.getRole())) {
                return new ApiResponseDTO<>(false, "Access denied! Only sellers allowed.", null);
            }

            CustomerHomeDTO dto = new CustomerHomeDTO();
            dto.setTitle("Welcome Seller " + user.getUsername() + "!");
            dto.setContent("Manage your products, orders, and earnings here 💼");

            return new ApiResponseDTO<>(true, "Seller data fetched successfully!", dto);

        } catch (Exception e) {
            return new ApiResponseDTO<>(false, e.getMessage(), null);
        }
    }

    // ✅ HOME PAGE (PUBLIC)
    public HomeContentDTO getHomeContent() {
        return new HomeContentDTO(
                "Welcome to Security Demo App",
                "This is a demo homepage with hardcoded title and content using DTO structure."
        );
    }
}




