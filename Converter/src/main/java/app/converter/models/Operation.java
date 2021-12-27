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
    @OneToOne
    @JoinColumn(name = "from_currency_id")
    private Currency fromCurrency;
    private BigDecimal value;
    @OneToOne
    @JoinColumn(name = "to_currency_id")
    private Currency toCurrency;
    private BigDecimal result;
    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private LocalDate date;

}