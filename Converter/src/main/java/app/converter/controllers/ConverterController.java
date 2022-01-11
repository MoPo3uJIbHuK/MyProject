package app.converter.controllers;

import app.converter.dto.CurrencyCourse;
import app.converter.models.Currency;
import app.converter.models.User;
import app.converter.security.UserDetailsImpl;
import app.converter.services.ConverterService;
import app.converter.services.CurrencyGeneralService;
import app.converter.services.OperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/converters")
public class ConverterController {
    private final ConverterService converterService;
    private final CurrencyGeneralService currencyGeneralService;
    private final OperationService operationService;
    private boolean isShowDetailsCurrency;

    @Autowired
    public ConverterController(ConverterService converterService, CurrencyGeneralService currencyGeneralService, OperationService operationService) {
        this.converterService = converterService;
        this.currencyGeneralService = currencyGeneralService;
        this.operationService = operationService;
    }

    @GetMapping
    public String getPageConverter(@RequestParam(value = "sourceCharCode", required = false) String source,
                                   @RequestParam(value = "destinationCharCode", required = false) String destination,
                                   @RequestParam(value = "result", required = false) BigDecimal result,
                                   @RequestParam(value = "date", required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                   @RequestParam(value = "value", required = false, defaultValue = "0") BigDecimal value, Model model) {
        fillModel(source, destination, result, date, value, model);
        return "converters";
    }

    @PostMapping
    public String makeConverter(@RequestParam(value = "sourceCharCode", required = false) String source,
                                @RequestParam(value = "destinationCharCode", required = false) String destination,
                                @RequestParam(value = "result", required = false) BigDecimal result,
                                @RequestParam(value = "date", required = false)
                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                @RequestParam(value = "value", required = false, defaultValue = "0") BigDecimal value, Model model) {
        value = value.abs();
        date = validateDate(date);
        result = converterService.getCalculate(source, destination, value, date);
        saveOperation(source, value, destination, result, date);
        return getPageConverter(source, destination, result, date, value, model);
    }

    @PostMapping("/changeState")
    public String showHideCurrencies(@RequestParam(value = "sourceCharCode", required = false) String source,
                                     @RequestParam(value = "destinationCharCode", required = false) String destination,
                                     @RequestParam(value = "result", required = false) BigDecimal result,
                                     @RequestParam(value = "date", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     @RequestParam(value = "value", required = false, defaultValue = "0") BigDecimal value, Model model) {
        isShowDetailsCurrency = !isShowDetailsCurrency;
        return getPageConverter(source, destination, result, date, value, model);
    }

    @GetMapping("/history")
    public String getHistoryOperations(Model model) {
        model.addAttribute("operations", operationService.getOperations());
        return "history";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    private void saveOperation(String fromCurrency, BigDecimal value, String toCurrency,
                               BigDecimal result, LocalDate date) {
        Optional<Currency> sourceCurrency = currencyGeneralService.getCurrencyFromCharCode(fromCurrency);
        Optional<Currency> destinationCurrency = currencyGeneralService.getCurrencyFromCharCode(toCurrency);
        operationService.saveOperation(sourceCurrency.get(), value, destinationCurrency.get(), result, getCurrentUser(), date);
    }

    private LocalDate validateDate(LocalDate date) {
        date = date == null ? LocalDate.now() : date;
        if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.of(2000, 01, 01))) {
            return LocalDate.now();
        }
        return date;
    }

    private void fillModel(String source, String destination, BigDecimal result, LocalDate date, BigDecimal value, Model model) {
        date = validateDate(date);
        Set<CurrencyCourse> actualCurrencyCourse = converterService.getCurrency(date);
        List<String> actualCharCode = actualCurrencyCourse.stream().map(CurrencyCourse::getCharCode).toList();
        Set<Currency> dateCurrency = currencyGeneralService.getCurrencyGeneral();
        if (actualCharCode.size() != dateCurrency.size()) {
            dateCurrency.removeIf(currencyCourse -> !actualCharCode.contains(currencyCourse.getCharCode()));
        }
        model.addAttribute("currencyCourseList", actualCurrencyCourse);
        model.addAttribute("currencyList", dateCurrency);
        model.addAttribute("result", result);
        model.addAttribute("value", value);
        model.addAttribute("sourceCharCode", source);
        model.addAttribute("destinationCharCode", destination);
        model.addAttribute("date", date);
        model.addAttribute("user", getCurrentUser());
        model.addAttribute("isShow", isShowDetailsCurrency);
    }
}
