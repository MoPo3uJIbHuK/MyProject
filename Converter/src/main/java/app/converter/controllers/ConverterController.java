package app.converter.controllers;

import app.converter.models.User;
import app.converter.security.UserDetailsImpl;
import app.converter.services.AdminService;
import app.converter.services.ConverterService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
public class ConverterController {
    private final AdminService adminService;
    private final ConverterService converterService;

    @Autowired
    public ConverterController(AdminService adminService, ConverterService converterService) {
        this.adminService = adminService;
        this.converterService = converterService;
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
                                @RequestParam(value = "value") long value, Model model) {
        model.addAttribute("result", converterService.getCalculate(from,to,value).toString());
        return "redirect:/converters";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.user();
    }
}
