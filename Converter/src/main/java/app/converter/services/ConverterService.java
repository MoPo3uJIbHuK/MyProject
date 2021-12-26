package app.converter.services;

import app.converter.dto.CurrencyCourse;
import app.converter.utils.CbrXmlMappingToCurrency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public record ConverterService(CbrXmlMappingToCurrency currencyContext) {
    public List<CurrencyCourse> getCurrencyToday() {
        return currencyContext.getCurrenciesCourse().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .toList();
    }

    public BigDecimal getCalculate(String from, String to, long value) {
        if (from.equals(to)) {
            return BigDecimal.valueOf(value);
        }
        CurrencyCourse fromCurrency = currencyContext.getCurrenciesCourse().get(from);
        CurrencyCourse toCurrency = currencyContext.getCurrenciesCourse().get(to);
        return BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(toCurrency.getNominal()))
                .multiply(fromCurrency.getValue()).divide(BigDecimal.valueOf(fromCurrency.getNominal()))
                .divide(toCurrency.getValue(), 2, RoundingMode.CEILING);
    }
}
