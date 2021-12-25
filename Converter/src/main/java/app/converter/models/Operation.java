package app.converter.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "operations")
@RequiredArgsConstructor
@Data
@SuperBuilder
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String fromCurrency;
    private long value;
    private String toCurrency;
    private BigDecimal result;
    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private LocalDate date;

}