package Security.com.example.SecurityDemo.controller;

import Security.com.example.SecurityDemo.dto.*;
import Security.com.example.SecurityDemo.model.User;
import Security.com.example.SecurityDemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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
        return ResponseEntity.ok().body(response);
    }

    // ✅ LOGIN - sets JWT cookie using ResponseCookie
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request); // service returns token

        // ✅ Create cookie using ResponseCookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // true in production with HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day
                .sameSite("Lax")
                .build();

        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Login successful!", null);
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response);
    }

    // ✅ LOGOUT - clears cookie
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<?>> logout() {
        // ✅ Empty cookie to remove JWT
        ResponseCookie clearCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Logged out successfully!", null);
        return ResponseEntity.ok()
                .header("Set-Cookie", clearCookie.toString())
                .body(response);
    }

    // ✅ PROFILE
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<?>> getProfile() {
        UserProfileDTO profile = authService.getProfile();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Profile fetched successfully!", profile);
        return ResponseEntity.ok().body(response);
    }

    // ✅ CHANGE ROLE
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-role")
    public ResponseEntity<ApiResponseDTO<?>> changeRole(@RequestParam String newRole) {
        authService.changeUserRole(newRole);
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Role changed successfully to " + newRole, null);
        return ResponseEntity.ok().body(response);
    }

    // ✅ CUSTOMER HOME
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customer-home")
    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome() {
        CustomerHomeDTO dto = authService.getCustomerHomeData();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Customer home data fetched!", dto);
        return ResponseEntity.ok().body(response);
    }

    // ✅ SELLER HOME
    @PreAuthorize("hasAuthority('SELLER')")
    @GetMapping("/seller-home")
    public ResponseEntity<ApiResponseDTO<?>> getSellerHome() {
        CustomerHomeDTO dto = authService.getSellerHomeData();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Seller home data fetched!", dto);
        return ResponseEntity.ok().body(response);
    }

    // ✅ PUBLIC HOME
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public ResponseEntity<ApiResponseDTO<?>> getHomePage() {
        HomeContentDTO dto = authService.getHomeContent();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true, "Home page fetched!", dto);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/about")
    public ResponseEntity<ApiResponseDTO<?>> getAboutContent() {
        AboutContentDTO dto = authService.getAboutContent();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(true,"About page fetch sucessfully",dto);
        return ResponseEntity.ok().body(response);
    }
}
