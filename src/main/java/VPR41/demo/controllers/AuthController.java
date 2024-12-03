package VPR41.demo.controllers;

import VPR41.demo.models.User;
import VPR41.demo.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "API для аутентификации и управления пользователями")
public class AuthController {

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int TOKEN_LENGTH = 32;

    @Autowired
    private AuthService authService;

    /*@PostMapping("/login")
    public ResponseEntity<User> login(@RequestHeader("Authorization") String idToken) {
        try {
            User user = authService.authenticate(idToken);
            return ResponseEntity.ok(user);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body(null);
        }
    }*/

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(randomIndex));
        }

        return token.toString();
    }

    @Operation(summary = "Сгенерировать код подтверждения", description = "Возвращает случайный 5-значный код")
    @GetMapping("/getCode") // Полный маршрут: /auth/getCode
    public Map<String, String> getCode() {
        Random random = new Random();
        String code = Integer.toString(10000 + random.nextInt(90000));
        Map<String, String> response = new HashMap<>();
        response.put("code", code);
        return response;
    }


    public static class PhoneNumberDTO {
        private String phoneNumber;

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    @Data
    public static class NotificationSoundRequest {
        private Boolean isSoundEnabled;
    }

    @Operation(summary = "Сохранить пользователя", description = "Сохраняет нового пользователя по номеру телефона или возвращает данные существующего")
    @PostMapping("/saveUser") // Полный маршрут: /auth/saveUser
    public ResponseEntity<User> saveUser(@RequestBody PhoneNumberDTO phoneNumberDTO) {
        String phoneNumber = phoneNumberDTO.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = authService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            String token = generateToken();
            User newUser = new User();
            newUser.setFirebaseUid(token);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setAllowNotificationSound(true);

            authService.saveUser(newUser);
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @Operation(summary = "Обновить настройки уведомлений", description = "Обновляет параметр разрешения звука уведомлений у пользователя")
    @PutMapping("/updateSound") // Полный маршрут: /auth/updateSound
    public ResponseEntity<User> updateAllowNotificationSound(
            @RequestHeader("Authorization") String token,
            @RequestBody NotificationSoundRequest request) {

        User user = authService.findByToken(token);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setAllowNotificationSound(request.getIsSoundEnabled());
        User updatedUser = authService.saveUser(user);

        return ResponseEntity.ok(updatedUser);
    }
}