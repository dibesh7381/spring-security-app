//package Security.com.example.SecurityDemo.controller;
//
//import Security.com.example.SecurityDemo.dto.*;
//import Security.com.example.SecurityDemo.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
//public class AuthController {
//
//    private final AuthService authService;
//
//    // ✅ SIGNUP
//    @PostMapping("/signup")
//    public ResponseEntity<ApiResponseDTO<?>> signup(@RequestBody SignupRequest request) {
//        ApiResponseDTO<?> response = authService.signup(request);
//        return ResponseEntity.ok(response);
//    }
//
//    // ✅ LOGIN (JWT token set in ResponseCookie)
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody LoginRequest request) {
//        ApiResponseDTO<?> response = authService.login(request);
//
//        if (response.isStatus() && response.getData() != null) {
//            String token = response.getData().toString();
//
//            // ✅ Build cookie using ResponseCookie
//            ResponseCookie cookie = ResponseCookie.from("jwt", token)
//                    .httpOnly(true)
//                    .secure(false) // set true in production (for HTTPS)
//                    .path("/")
//                    .maxAge(60 * 60) // 1 hour
//                    .sameSite("Lax") // for local dev; use "None" if HTTPS + different domain
//                    .build();
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                    .body(new ApiResponseDTO<>(true, "Login successful!", "JWT token set in cookie"));
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
//
//    // ✅ LOGOUT (clear cookie)
//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponseDTO<?>> logout() {
//        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .maxAge(0)
//                .sameSite("Lax")
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
//                .body(new ApiResponseDTO<>(true, "Logged out successfully!", null));
//    }
//
////    // ✅ PROFILE (protected)
////    @PreAuthorize("isAuthenticated()")
////    @GetMapping("/profile")
////    public ResponseEntity<ApiResponseDTO<?>> getProfile(
////            @CookieValue(name = "jwt", required = false) String token
////    ) {
////        if (token == null || token.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body(new ApiResponseDTO<>(false, "Unauthorized: No token found", null));
////        }
////
////        ApiResponseDTO<?> response = authService.getProfile(token);
////        return ResponseEntity.ok(response);
////    }
////
////    // ✅ CHANGE ROLE (CUSTOMER → SELLER)
////    @PreAuthorize("isAuthenticated()") // only logged-in users can access
////    @PostMapping("/change-role")
////    public ResponseEntity<ApiResponseDTO<?>> changeUserRole(
////            @CookieValue(name = "jwt", required = false) String token,
////            @RequestParam String newRole
////    ) {
////        if (token == null || token.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body(new ApiResponseDTO<>(false, "Unauthorized: No token found", null));
////        }
////
////        ApiResponseDTO<?> response = authService.changeUserRole(token, newRole);
////        return ResponseEntity.ok(response);
////    }
//
//    // ✅ PROFILE (protected)
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/profile")
//    public ResponseEntity<ApiResponseDTO<?>> getProfile(
//            @CookieValue(name = "jwt", required = false) String token
//    ) {
//        ApiResponseDTO<?> response = authService.getProfile(token);
//        return ResponseEntity.ok(response);
//    }
//
//    // ✅ CHANGE ROLE (CUSTOMER → SELLER)
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/change-role")
//    public ResponseEntity<ApiResponseDTO<?>> changeUserRole(
//            @CookieValue(name = "jwt", required = false) String token,
//            @RequestParam String newRole
//    ) {
//        ApiResponseDTO<?> response = authService.changeUserRole(token, newRole);
//        return ResponseEntity.ok(response);
//    }
//
//
////    // ✅ CUSTOMER HOME (only CUSTOMER)
////    @PreAuthorize("hasAuthority('CUSTOMER')")
////    @GetMapping("/customer-home")
////    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome(
////            @CookieValue(name = "jwt", required = false) String token
////    ) {
////        if (token == null || token.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body(new ApiResponseDTO<>(false, "Unauthorized: No token found", null));
////        }
////
////        ApiResponseDTO<?> response = authService.getCustomerHomeData(token);
////        return ResponseEntity.ok(response);
////    }
////
////    // ✅ SELLER HOME (only SELLER)
////    @PreAuthorize("hasAuthority('SELLER')")
////    @GetMapping("/seller-home")
////    public ResponseEntity<ApiResponseDTO<?>> getSellerHome(
////            @CookieValue(name = "jwt", required = false) String token
////    ) {
////        if (token == null || token.isEmpty()) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body(new ApiResponseDTO<>(false, "Unauthorized: No token found", null));
////        }
////
////        ApiResponseDTO<?> response = authService.getSellerHomeData(token);
////        return ResponseEntity.ok(response);
////    }
//
//    @PreAuthorize("hasAuthority('CUSTOMER')")
//    @GetMapping("/customer-home")
//    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome(
//            @CookieValue(name = "jwt", required = false) String token
//    ) {
//        ApiResponseDTO<?> response = authService.getCustomerHomeData(token);
//        return ResponseEntity.ok(response);
//    }
//
//    @PreAuthorize("hasAuthority('SELLER')")
//    @GetMapping("/seller-home")
//    public ResponseEntity<ApiResponseDTO<?>> getSellerHome(
//            @CookieValue(name = "jwt", required = false) String token
//    ) {
//        ApiResponseDTO<?> response = authService.getSellerHomeData(token);
//        return ResponseEntity.ok(response);
//    }
//
//}

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

    // ✅ SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDTO<?>> signup(@RequestBody SignupRequest request) {
        ApiResponseDTO<?> response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    // ✅ LOGIN (JWT token set in ResponseCookie)
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> login(@RequestBody LoginRequest request) {
        ApiResponseDTO<?> response = authService.login(request);

        if (response.isStatus() && response.getData() != null) {
            String token = response.getData().toString();

            // ✅ Build cookie using ResponseCookie
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false) // set true in production (for HTTPS)
                    .path("/")
                    .maxAge(60 * 60) // 1 hour
                    .sameSite("Lax") // use "None" for HTTPS + cross-domain
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new ApiResponseDTO<>(true, "Login successful!", "JWT token set in cookie"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // ✅ LOGOUT (clear cookie)
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

    // ✅ PROFILE (protected)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<?>> getProfile() {
        ApiResponseDTO<?> response = authService.getProfile();
        return ResponseEntity.ok(response);
    }

    // ✅ CHANGE ROLE (CUSTOMER → SELLER)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-role")
    public ResponseEntity<ApiResponseDTO<?>> changeUserRole(@RequestParam String newRole) {
        ApiResponseDTO<?> response = authService.changeUserRole(newRole);
        return ResponseEntity.ok(response);
    }

    // ✅ CUSTOMER HOME (only CUSTOMER)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customer-home")
    public ResponseEntity<ApiResponseDTO<?>> getCustomerHome() {
        ApiResponseDTO<?> response = authService.getCustomerHomeData();
        return ResponseEntity.ok(response);
    }

    // ✅ SELLER HOME (only SELLER)
    @PreAuthorize("hasAuthority('SELLER')")
    @GetMapping("/seller-home")
    public ResponseEntity<ApiResponseDTO<?>> getSellerHome() {
        ApiResponseDTO<?> response = authService.getSellerHomeData();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/home")
    public ResponseEntity<ApiResponseDTO<?>> getHomeContent() {
        try {
            HomeContentDTO data = authService.getHomeContent();
            ApiResponseDTO<HomeContentDTO> response = new ApiResponseDTO<>(true, "Home content fetched successfully!", data);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ApiResponseDTO<String> error = new ApiResponseDTO<>(false, "Failed to fetch home content!", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}



