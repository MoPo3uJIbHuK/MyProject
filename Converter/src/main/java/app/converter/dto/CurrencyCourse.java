package app.converter.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@RequiredArgsConstructor
@Getter@Setter
@ToString
public class CurrencyCourse {
    private String charCode;
    private Integer nominal;
    private String name;
    private BigDecimal value;
}

