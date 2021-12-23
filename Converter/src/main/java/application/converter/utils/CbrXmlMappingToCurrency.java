package application.converter.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import application.converter.models.Currency;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class CbrXmlMappingToCurrency {
    private Map<LocalDate, Map<String, Currency>> cashCurrencies;
    private String mainUrl = "http://cbr.ru/scripts/XML_daily.asp";
    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    public Map<String, Currency> getCurrencies(LocalDate date) {
        date = date.isAfter(LocalDate.now()) ? LocalDate.now() : date;
        if (this.cashCurrencies == null) {
            this.cashCurrencies = new HashMap();
        } else if (this.cashCurrencies.containsKey(date)) {
            return (Map) this.cashCurrencies.get(date);
        }

        this.cashCurrencies.put(date, this.getCurrenciesCertainDate(date));
        return (Map) this.cashCurrencies.get(date);
    }

    private Map<String, Currency> getCurrenciesCertainDate(LocalDate date) {
        Map<String, Currency> currencies = new HashMap();
        NodeList nList = this.getDocument(date).getElementsByTagName("Valute");
        for (int i = 0; i < nList.getLength(); ++i) {
            Element element = (Element) nList.item(i);
            Currency currency = Currency.builder().name(this.getStringForCurrency(element, "Name")).charCode(this.getStringForCurrency(element, "CharCode")).nominal(Integer.parseInt(this.getStringForCurrency(element, "Nominal"))).value(this.getValueForCurrency(element)).build();
            currencies.put(currency.getCharCode(), currency);
        }
        return currencies;
    }

    private Document getDocument(LocalDate date) {
        try {
            DocumentBuilder var10000 = this.factory.newDocumentBuilder();
            String var10001 = this.mainUrl;
            return var10000.parse(var10001 + this.getDateForRequest(date));
        } catch (ParserConfigurationException | SAXException | IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private String getDateForRequest(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String requestDate = date.format(formatter);
        return "?date_req=" + requestDate;
    }

    private String getStringForCurrency(Element element, String text) {
        return element.getElementsByTagName(text).item(0).getTextContent();
    }

    private BigDecimal getValueForCurrency(Element element) {
        return new BigDecimal(this.getStringForCurrency(element, "Value").replace(",", "."));
    }
}

