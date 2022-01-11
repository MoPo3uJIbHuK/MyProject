package app.converter.services;

import app.converter.models.Currency;
import app.converter.models.Operation;
import app.converter.models.User;
import app.converter.repositories.OperationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationService {
    private final OperationsRepository operationsRepository;
    public List<Operation> getOperations() {
        return operationsRepository.findAll();
    }
    public void saveOperation(Currency fromCurrency, BigDecimal value, Currency toCurrency,
                              BigDecimal result, User user, LocalDate date){
        Operation operation = Operation.builder().fromCurrency(fromCurrency)
                        .value(value).toCurrency(toCurrency).result(result)
                        .user(user).date(date).build();
        operationsRepository.save(operation);
    }

}
