package VPR41.demo.repository;


import VPR41.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    User findByFirebaseUid(String firebaseUID);

    User getUserByPhoneNumber(String phoneNumber);

    Optional<User> getUserById(long id);


}

