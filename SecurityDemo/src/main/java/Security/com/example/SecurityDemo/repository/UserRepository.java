package Security.com.example.SecurityDemo.repository;

import Security.com.example.SecurityDemo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // ✅ Custom finder method (Spring Data automatically implements it)
    Optional<User> findByEmail(String email);
}

