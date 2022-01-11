package app.converter.services;

import app.converter.models.Currency;
import app.converter.repositories.CurrencyRepository;
import app.converter.utils.CbrXmlMappingToCurrency;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CurrencyGeneralService {
    private final CurrencyRepository currencyRepository;
    private final CbrXmlMappingToCurrency currencyContext;

    @Autowired
    public CurrencyGeneralService(CurrencyRepository currencyRepository, CbrXmlMappingToCurrency currencyContext) {
        this.currencyRepository = currencyRepository;
        this.currencyContext = currencyContext;
    }

    public Set<Currency> getCurrencyGeneral() {
        Set<Currency> currencySet = new LinkedHashSet<>(currencyRepository.findAll());
        if (currencySet.isEmpty()) {
            fillStorage(currencySet);
            return new LinkedHashSet<>(currencyRepository.findAll());
        }
        return currencySet;
    }

    public void fillStorage(Set currencyList) {
        Set<Currency> currencySet = currencyContext.getCurrencyGeneral();
        currencySet.stream().filter(currency -> !currencyList.contains(currency))
                .forEach(currencyRepository::save);
    }

    public Optional<Currency> getCurrencyFromCharCode(String charCode){
        return getCurrencyGeneral().stream()
                .filter(currency -> currency.getCharCode().equals(charCode)).findAny();
    }
}
