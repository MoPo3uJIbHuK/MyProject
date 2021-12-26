package app.converter.controllers;

import app.converter.models.Currency;
import app.converter.models.User;
import app.converter.security.UserDetailsImpl;
import app.converter.services.AdminService;
import app.converter.services.ConverterService;
import app.converter.services.CurrencyGeneralService;
import app.converter.services.OperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/converters")
public class ConverterController {
    private final AdminService adminService;
    private final ConverterService converterService;
    private final CurrencyGeneralService currencyGeneralService;
    private final OperationService operationService;
    private boolean isShowDetailsCurrency;

    @Autowired
    public ConverterController(AdminService adminService, ConverterService converterService, CurrencyGeneralService currencyGeneralService, OperationService operationService) {
        this.adminService = adminService;
        this.converterService = converterService;
        this.currencyGeneralService = currencyGeneralService;
        this.operationService = operationService;
    }

    @GetMapping
    public String getPageConverter(Model model) {
        model.addAttribute("currencyCourseList", converterService.getCurrencyToday());
        model.addAttribute("currencyList", currencyGeneralService.getCurrencyGeneral());
        model.addAttribute("user", getCurrentUser());
        return "converters";
    }

    @PostMapping
    public String makeConverter(@RequestParam(value = "sourceCharCode") String source,
                                @RequestParam(value = "destinationCharCode") String destination,
                                @RequestParam(value = "value", required = false, defaultValue = "0") long value, Model model) {
        value = Math.abs(value);
        BigDecimal result = converterService.getCalculate(source, destination, value);
        model.addAttribute("result", result);
        model.addAttribute("value", value);
        model.addAttribute("sourceCharCode", source);
        model.addAttribute("destinationCharCode", destination);
        saveOperation(source, value, destination, result);
        return getPageConverter(model);
    }

    @PostMapping("/changeState")
    public String showHideCurrencies(Model model) {
        isShowDetailsCurrency = !isShowDetailsCurrency;
        model.addAttribute("isShow", isShowDetailsCurrency);
        return getPageConverter(model);
    }

    @GetMapping("/history")
    public String getHistoryOperations(Model model) {
        model.addAttribute("operations", operationService.getOperations());
        return "history";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.user();
    }

    private void saveOperation(String fromCurrency, long value, String toCurrency,
                               BigDecimal result) {
        Optional<Currency> sourceCurrency = currencyGeneralService.getCurrencyFromCharCode(fromCurrency);
        Optional<Currency> destinationCurrency = currencyGeneralService.getCurrencyFromCharCode(toCurrency);
        operationService.saveOperation(sourceCurrency.get(), value, destinationCurrency.get(), result, getCurrentUser());
    }
}
