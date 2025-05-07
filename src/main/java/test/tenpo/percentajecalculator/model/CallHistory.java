package test.tenpo.percentajecalculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "call_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime callDate;

    @Column(nullable = false)
    private String endpoint;

    @Column(length = 1000)
    private String parameters;

    @Column(length = 10000)
    private String response;
}