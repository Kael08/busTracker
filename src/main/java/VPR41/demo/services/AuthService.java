package VPR41.demo.services;

import VPR41.demo.repository.UserRepository;
import VPR41.demo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String idToken) throws FirebaseAuthException {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();

            UserRecord userRecord = FirebaseAuth.getInstance().getUser(firebaseUid);
            String phoneNumber = userRecord.getPhoneNumber();

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("Phone number is missing in Firebase user record.");
            }

            return userRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> {
                User newUser = new User();
                newUser.setFirebaseUid(firebaseUid);
                newUser.setPhoneNumber(phoneNumber);

                return userRepository.save(newUser);
            });
        } catch (FirebaseAuthException e) {
            System.err.println("Firebase Authentication failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw e;
        }
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User findByToken(String token)
    {
        return userRepository.findByFirebaseUid(token);
    }

    public User findByPhoneNumber(String phoneNumber)
    {
        return userRepository.getUserByPhoneNumber(phoneNumber);
    }
}
