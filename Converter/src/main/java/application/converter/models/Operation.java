package application.converter.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "operations")
@RequiredArgsConstructor
@Data
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private BigDecimal value;
    @OneToOne
    private Currency fromCurrency;
    @OneToOne
    private Currency toCurrency;

}