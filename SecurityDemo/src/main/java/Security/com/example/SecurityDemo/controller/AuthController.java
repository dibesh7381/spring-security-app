
package Security.com.example.SecurityDemo.controller;

import Security.com.example.SecurityDemo.dto.*;
import Security.com.example.SecurityDemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    // ✅ Anyone can signup
    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<?>> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    // ✅ Anyone can login
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody LoginRequest request) {
        ApiResponseDTO<?> response = authService.login(request);
        if (response.isStatus() && response.getData() != null) {
            String token = response.getData().toString();

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new ApiResponseDTO<>(true, "Login successful!", "JWT set in cookie"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // ✅ Logout — only logged-in users
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<?>> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(new ApiResponseDTO<>(true, "Logged out successfully!", null));
    }

    // ✅ Profile (any logged-in user)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<?>> getProfile() {
        return ResponseEntity.ok(authService.getProfile());
    }

    // ✅ Change Role (authenticated user only)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-role")
    public ResponseEntity<ApiResponseDTO<?>> changeUserRole(@RequestParam String newRole) {
        return ResponseEntity.ok(authService.changeUserRole(newRole));
    }

    // ✅ Customer Home (only CUSTOMER)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customer-home")
    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome() {
        return ResponseEntity.ok(authService.getCustomerHomeData());
    }

    // ✅ Seller Home (only SELLER)
    @PreAuthorize("hasAuthority('SELLER')")
    @GetMapping("/seller-home")
    public ResponseEntity<ApiResponseDTO<?>> getSellerHome() {
        return ResponseEntity.ok(authService.getSellerHomeData());
    }

    // ✅ Public Home (no login needed)
    @PreAuthorize("permitAll()")
    @GetMapping("/home")
    public ResponseEntity<ApiResponseDTO<?>> getHomeContent() {
        try {
            HomeContentDTO data = authService.getHomeContent();
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Home content fetched!", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO<>(false, "Failed to fetch home content!", e.getMessage()));
        }
    }
}




