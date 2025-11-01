package Security.com.example.SecurityDemo.controller;

import Security.com.example.SecurityDemo.dto.*;
import Security.com.example.SecurityDemo.model.User;
import Security.com.example.SecurityDemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    // ✅ SIGNUP
    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<?>> signup(@RequestBody SignupRequest request) {
        User user = authService.signup(request);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Signup successful!", user);
        return ResponseEntity.ok(response);
    }

    // ✅ LOGIN
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Login successful!", token);
        return ResponseEntity.ok(response);
    }

    // ✅ PROFILE
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<?>> getProfile() {
        UserProfileDTO profile = authService.getProfile();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Profile fetched successfully!", profile);
        return ResponseEntity.ok(response);
    }

    // ✅ CHANGE ROLE
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-role")
    public ResponseEntity<ApiResponseDTO<?>> changeRole(@RequestParam String newRole) {
        authService.changeUserRole(newRole);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Role changed successfully to " + newRole, null);
        return ResponseEntity.ok(response);
    }

    // ✅ CUSTOMER HOME
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customer-home")
    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome() {
        CustomerHomeDTO dto = authService.getCustomerHomeData();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Customer home data fetched!", dto);
        return ResponseEntity.ok(response);
    }

    // ✅ SELLER HOME
    @PreAuthorize("hasAuthority('SELLER')")
    @GetMapping("/seller-home")
    public ResponseEntity<ApiResponseDTO<?>> getSellerHome() {
        CustomerHomeDTO dto = authService.getSellerHomeData();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Seller home data fetched!", dto);
        return ResponseEntity.ok(response);
    }

    // ✅ PUBLIC HOME
    @PreAuthorize("permitAll()")
    @GetMapping("/home")
    public ResponseEntity<ApiResponseDTO<?>> getHomePage() {
        HomeContentDTO dto = authService.getHomeContent();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Home page fetched!", dto);
        return ResponseEntity.ok(response);
    }
}












