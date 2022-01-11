package app.converter.services;

import app.converter.dto.CurrencyCourse;
import app.converter.utils.CbrXmlMappingToCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConverterService {
    private final CbrXmlMappingToCurrency currencyContext;
    public Set<CurrencyCourse> getCurrency() {
        return currencyContext.getCurrenciesCourse().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<CurrencyCourse> getCurrency(LocalDate date) {
        return currencyContext.getCurrenciesCourse(date).entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public BigDecimal getCalculate(String from, String to, BigDecimal value,LocalDate date) {
        if (from.equals(to)) {
            return value;
        }
        CurrencyCourse fromCurrency = currencyContext.getCurrenciesCourse(date).get(from);
        CurrencyCourse toCurrency = currencyContext.getCurrenciesCourse(date).get(to);
        return value.multiply(BigDecimal.valueOf(toCurrency.getNominal()))
                .multiply(fromCurrency.getValue()).divide(BigDecimal.valueOf(fromCurrency.getNominal()))
                .divide(toCurrency.getValue(), 2, RoundingMode.CEILING);
    }
}
