package app.converter.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import app.converter.dto.CurrencyCourse;
import app.converter.models.Currency;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class CbrXmlMappingToCurrency {
    private Map<LocalDate, Map<String, CurrencyCourse>> cashCurrencies;
    private final String mainUrl = "https://cbr.ru/scripts/";
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final CurrencyCourse RUB = CurrencyCourse.builder().charCode("RUB").name("Российский рубль")
            .nominal(1).value(BigDecimal.ONE).build();

    public Map<String, CurrencyCourse> getCurrenciesCourse(LocalDate date) {
        date = date.isAfter(LocalDate.now()) ? LocalDate.now() : date;
        if (cashCurrencies == null) {
            cashCurrencies = new HashMap();
        } else if (cashCurrencies.containsKey(date)) {
            return cashCurrencies.get(date);
        }
        cashCurrencies.put(date, getCurrenciesCourseCertainDate(date));
        return cashCurrencies.get(date);
    }

    public Set<Currency> getCurrencyGeneral() {
        Set<Currency> currencies = new LinkedHashSet<>();
        Currency rub = new Currency("R00000", "RUB", "Российский рубль");
        currencies.add(rub);
        Map<String, CurrencyCourse> currencyCourseMap = getCurrenciesCourse(LocalDate.now()).values()
                .stream().collect(Collectors.toMap(CurrencyCourse::getId, Function.identity()));
        NodeList nodeList = getDocument(null, TypeRequest.CURRENCY_GENERAL).getElementsByTagName("Item");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String id = element.getAttribute("ID");
            if (currencyCourseMap.containsKey(id)) {
                String name = element.getElementsByTagName("Name").item(0).getTextContent();
                Currency currency = new Currency(id, currencyCourseMap.get(id).getCharCode(), name);
                currencies.add(currency);
            }
        }
        return currencies;
    }

    public Map<String, CurrencyCourse> getCurrenciesCourse() {
        return getCurrenciesCourse(LocalDate.now());
    }

    private Map<String, CurrencyCourse> getCurrenciesCourseCertainDate(LocalDate date) {
        Map<String, CurrencyCourse> currencies = new LinkedHashMap<>();
        currencies.put(RUB.getCharCode(), RUB);
        NodeList nList = getDocument(date, TypeRequest.CURRENCY_COURSE).getElementsByTagName("Valute");
        for (int i = 0; i < nList.getLength(); ++i) {
            Element element = (Element) nList.item(i);
            CurrencyCourse currencyCourse = CurrencyCourse.builder()
                    .name(getStringForCurrency(element, "Name"))
                    .charCode(getStringForCurrency(element, "CharCode"))
                    .nominal(Integer.parseInt(getStringForCurrency(element, "Nominal")))
                    .id(element.getAttribute("ID"))
                    .value(getValueForCurrency(element)).build();
            currencies.put(currencyCourse.getCharCode(), currencyCourse);
        }
        return currencies;
    }

    private Document getDocument(LocalDate date, TypeRequest typeRequest) {
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            return documentBuilder.parse(mainUrl + getLinkForRequest(date, typeRequest));
        } catch (ParserConfigurationException | SAXException | IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private String getLinkForRequest(LocalDate date, TypeRequest typeRequest) {
        if (typeRequest == TypeRequest.CURRENCY_GENERAL) {
            return "XML_val.asp?d=0";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String requestDate = date.format(formatter);
        return "XML_daily.asp?date_req=" + requestDate;
    }

    private String getStringForCurrency(Element element, String text) {
        return element.getElementsByTagName(text).item(0).getTextContent();
    }

    private BigDecimal getValueForCurrency(Element element) {
        return new BigDecimal(this.getStringForCurrency(element, "Value").replace(",", "."));
    }

    private enum TypeRequest {
        CURRENCY_COURSE,
        CURRENCY_GENERAL
    }
}

