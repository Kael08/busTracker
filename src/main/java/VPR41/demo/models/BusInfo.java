package VPR41.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String idRoute;

    private String busNumber;

    private double latitude;

    private double longitude;

    private Integer speed;

    private Integer azimuth;

    private String regNumber;    // Регистрационный номер

    private Integer estimatedArrivalTime; // Время прибытия в минутах
}
