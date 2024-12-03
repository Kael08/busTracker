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
@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает null-поля из JSON
public class StopInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String idStop; // Уникальный идентификатор остановки
    private double lat; // Широта
    private double lng; // Долгота
    private String stops; // Связанные остановки
    private String name; // Название остановки
}
