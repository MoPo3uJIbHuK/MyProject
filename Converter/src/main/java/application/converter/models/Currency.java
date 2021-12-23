package application.converter.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencies")
@SuperBuilder
@RequiredArgsConstructor
@Getter@Setter
@ToString
public class Currency {
    @Id
    @Column(name = "id")
    private String charCode;
    private Integer nominal;
    private String name;
    private BigDecimal value;
}

