package app.converter.controllers;

import app.converter.models.User;
import app.converter.security.UserDetailsImpl;
import app.converter.services.AdminService;
import app.converter.services.ConverterService;
import app.converter.services.OperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@Log4j2
public class ConverterController {
    private final AdminService adminService;
    private final ConverterService converterService;
    private final OperationService operationService;
    private boolean isShowDetailsCurrency;

    @Autowired
    public ConverterController(AdminService adminService, ConverterService converterService, OperationService operationService) {
        this.adminService = adminService;
        this.converterService = converterService;
        this.operationService = operationService;
    }

    @GetMapping("/converters")
    public String getPageConverter(Model model) {
        model.addAttribute("currencyCourseList", converterService.getCurrencyToday());
        model.addAttribute("currencyCharCodeList", converterService.getCurrencyCharCode());
        model.addAttribute("user", getCurrentUser());
        return "converters";
    }

    @PostMapping("/converters")
    public String makeConverter(@RequestParam(value = "fromCharCode") String from,
                                @RequestParam(value = "toCharCode") String to,
                                @RequestParam(value = "value", required = false, defaultValue = "0") long value, Model model) {
        value = Math.abs(value);
        BigDecimal result = converterService.getCalculate(from, to, value);
        model.addAttribute("result", result);
        model.addAttribute("value", value);
        model.addAttribute("fromCharCode", from);
        model.addAttribute("toCharCode", to);
        saveOperation(from,value,to,result);
        return getPageConverter(model);
    }

    @PostMapping("/converters/changeState")
    public String showHideCurrencies(Model model) {
        isShowDetailsCurrency = !isShowDetailsCurrency;
        model.addAttribute("isShow", isShowDetailsCurrency);
        return getPageConverter(model);
    }

    @GetMapping("/converters/history")
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
        operationService.saveOperation(fromCurrency,value,toCurrency,result,getCurrentUser());
    }
}
