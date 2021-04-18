package com.kodilla.finalproject_fe.domain;

import com.kodilla.finalproject_fe.MainView;
import com.kodilla.finalproject_fe.service.Service;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyForm extends FormLayout {
    private Service service;
    private MainView mainView;
    List<RateOfExchange> list;
    protected final Logger LOGGER = LoggerFactory.getLogger(CurrencyForm.class);
    private List<Currency> currencyList;
    private Map<String, String> currencyMap;
    ComboBox currencyName = new ComboBox("nazwa waluty");
    ComboBox currencyCode = new ComboBox("kod waluty");
    TextField account = new TextField("środki na koncie");
    TextField value = new TextField("kwota doładowania/pobrania");

    Button createCurrencyButton = new Button("Stworzenie nowego konta walutowego");
    Button deleteCurrencyButton = new Button("Skasowanie wybranego konta");
    Button topUpCurrencyButton = new Button("Doładowanie wybranego konta");
    Button payOutCurrencyButton = new Button("Wypłacenie z wybranego konta");
    Button resignationButton = new Button("Anuluj");
    Button buyCurrencyButton = new Button("Kupno waluty za złotówki");
    Button sellCurrencyButton = new Button("Sprzedaż waluty na złotówki");

    public CurrencyForm( MainView mainView, List<RateOfExchange> list){
        this.currencyList = new ArrayList<Currency>();
        this.mainView = mainView;
        this.list = list;
        service = Service.getInstance();
        account.setReadOnly(true);
        configurationComboBoxes();
        add(currencyName ,
                currencyCode ,
                account, value,
                createButtonsLayoutOne(),
                createButtonsLayoutTwo(),
                createButtonsLayoutThree());
        CurrencyMapper();
    }
    private void configurationComboBoxes(){
        List<String> codeList = list.stream().map(RateOfExchange::getCurrencyCode).collect(Collectors.toList());
        codeList.add("PLN");
        currencyCode.setItems(codeList);
        currencyCode.addValueChangeListener(event -> CodeEventAction());

        List<String> nameList = list.stream().map(RateOfExchange::getCurrencyName).collect(Collectors.toList());
        nameList.add("nowy złoty polski");
        currencyName.setItems(nameList);
        currencyName.addValueChangeListener(event -> NameEventAction());
    }

    private HorizontalLayout createButtonsLayoutOne(){
        createCurrencyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        createCurrencyButton.addClickShortcut(Key.ENTER);
        createCurrencyButton.addClickListener(click -> createAction());

        deleteCurrencyButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteCurrencyButton.addClickShortcut(Key.DELETE);
        deleteCurrencyButton.addClickListener(click -> deleteAction());

        return new HorizontalLayout(createCurrencyButton, deleteCurrencyButton);
    }
    private HorizontalLayout createButtonsLayoutTwo(){
        topUpCurrencyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        topUpCurrencyButton.addClickListener(click -> topUpAction());

        payOutCurrencyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        payOutCurrencyButton.addClickListener(click -> payOutAction());

        resignationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resignationButton.addClickShortcut(Key.ESCAPE);
        resignationButton.addClickListener(click -> resignationAction());

        return new HorizontalLayout(topUpCurrencyButton, payOutCurrencyButton, resignationButton);
    }
    private HorizontalLayout createButtonsLayoutThree(){
        buyCurrencyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buyCurrencyButton.addClickListener(click -> buyAction());

        sellCurrencyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sellCurrencyButton.addClickListener(click -> sellAction());

        return new HorizontalLayout(buyCurrencyButton, sellCurrencyButton);
    }
    private void createAction(){
        if(currencyCode.getValue() != null && !currencyCode.getValue().equals("")) {
            service.newCurrency(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString());
            mainView.refresh();
        }
    }
    private void deleteAction(){
        if(currencyCode.getValue() != null && !currencyCode.getValue().equals("")) {
            service.deleteCurrency(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString());
            mainView.refresh();
        }
    }
    private void topUpAction(){
        if(currencyCode.getValue() != null && !currencyCode.getValue().equals("")) {
            try {
                Double dValue = Double.parseDouble(value.getValue());
                service.topUpAccount(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString(), dValue);
                mainView.refresh();
            }catch(NumberFormatException e){
                LOGGER.error("Nie można zamienić tekstu na liczbę - wyjątek: NumberFormatException");
            }
        }
    }
    private void payOutAction(){
        if(currencyCode.getValue() != null && !currencyCode.getValue().equals("")) {
            try {
                Double dValue = Double.parseDouble(value.getValue());
                service.payOutAccount(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString(), dValue);
                mainView.refresh();
            }catch(NumberFormatException e){
                LOGGER.error("Nie można zamienić tekstu na liczbę - wyjątek: NumberFormatException");
            }
        }
    }
    private void resignationAction(){
        setVisible(false);
        mainView.setGridCurrencyVisible(false);
        mainView.setTextShowCurrencyButton();
        mainView.refresh();
    }
    public void setCurrencyList(List<Currency> currencyList){
        this.currencyList = currencyList;
    }
    public void setCurrency(Currency currency){
        currencyName.setValue(currency.getCurrencyName());
        currencyCode.setValue(currency.getCurrencyCode());
        if(currency.getAccount() != null) {
            account.setValue(String.valueOf(currency.getAccount()));
        } else{
            account.setValue("");
        }
    }
    private void CodeEventAction(){
        if (currencyCode.getValue() != null && !currencyCode.getValue().equals("") )
        {
            currencyName.setValue(currencyMap.get(currencyCode.getValue()));
        }
    }
    private void NameEventAction(){
        if (currencyName.getValue() != null && !currencyName.getValue().equals("") )
        {
            currencyCode.setValue(currencyMap.get(currencyName.getValue()));
        }
    }
    private void CurrencyMapper(){
        this.currencyMap = new HashMap<>();
        for(int i = 0 ; i< list.size() ; i++ ){
            currencyMap.put(list.get(i).getCurrencyCode(), list.get(i).getCurrencyName());
            currencyMap.put(list.get(i).getCurrencyName(), list.get(i).getCurrencyCode());
        }
        currencyMap.put("PLN" , "nowy złoty polski");
        currencyMap.put("nowy złoty polski", "PLN");
    }
    private void buyAction(){
        try{
            Double dValue = Double.parseDouble(value.getValue());
            service.buyCurrency(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString(), dValue);
            mainView.refresh();
        }catch(NumberFormatException e){
            LOGGER.error("Nie można zamienić tekstu na liczbę - wyjątek: NumberFormatException");
        }
    }
    private void sellAction(){
        try{
            Double dValue = Double.parseDouble(value.getValue());
            service.sellCurrency(mainView.getChoosenUser().getUserID(), currencyCode.getValue().toString(), dValue);
            mainView.refresh();
        }catch(NumberFormatException e){
            LOGGER.error("Nie można zamienić tekstu na liczbę - wyjątek: NumberFormatException");
        }
    }
}

