package Security.com.example.SecurityDemo.exception;

import Security.com.example.SecurityDemo.dto.ApiResponseDTO;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Handle custom business exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleCustomException(CustomException ex) {
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    // ✅ Handle validation errors (@Valid failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleValidationError(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                false,
                errorMessage,
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ✅ Handle unauthorized access (403)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // ✅ Handle 404 (endpoint not found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleNotFound(NoHandlerFoundException ex) {
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                false,
                "Endpoint not found: " + ex.getRequestURL(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ✅ Handle all uncaught exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<?>> handleGenericException(Exception ex) {
        ApiResponseDTO<?> response = new ApiResponseDTO<>(
                false,
                "Internal server error: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


