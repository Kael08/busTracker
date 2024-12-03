package VPR41.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebaseUID", unique = true, nullable = false)
    private String firebaseUid;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "isAllowNotificationSound")
    private boolean isAllowNotificationSound;
}