package com.kodilla.finalproject_fe;

import com.kodilla.finalproject_fe.domain.*;
import com.kodilla.finalproject_fe.service.Service;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Component
@Route
public class MainView extends VerticalLayout {
    private User choosenUser = new User();
    private CreateUserForm createUserForm;
    private CurrencyForm currencyForm;
    private Service service;
    private Grid<RateOfExchange> gridRate = new Grid<>(RateOfExchange.class);
    private Grid<Currency> gridCurrency = new Grid<>(Currency.class);
    private Grid<User> grid = new Grid<>(User.class);
    private TextField choiceUser = new TextField();
    private TextField descriptionUser = new TextField();
    private TextArea info = new TextArea();
    private Button editUserButton = new Button("Edycja użytkowników");
    private Button showRatesButton = new Button("Pokaż kursy walut");
    private Button showCurrencyButton = new Button("Pokaż konta walutowe użytkownika");
    private Button infoButton = new Button("Informacje o programie");

    public MainView() {

        service = Service.getInstance();
        createUserForm = new CreateUserForm(service.getUsers(),this);
        createUserForm.setVisible(false);
        currencyForm = new CurrencyForm( this, service.getRates());
        currencyForm.setVisible(false);
        descriptionUser.setValue("Wybrany użytkownik:");
        descriptionUser.setReadOnly(true);
        choiceUser.setReadOnly(true);
        info();
        configureGrids();
        VerticalLayout userContent = new VerticalLayout(grid, createUserForm);
        VerticalLayout currencyContent = new VerticalLayout(gridCurrency, currencyForm);
        HorizontalLayout userBar = firstBar();

        add(new H1("Kantor internetowy"), userBar, info, userContent, gridRate, currencyContent);
        refresh();

    }
    public void refresh() {
        gridRate.setItems(service.getRates());
        grid.setItems(service.getUsers());
        choiceUser.setValue(choosenUser.getUserName());
        if(choiceUser.getValue() == null || choiceUser.getValue().equals("")){
            showCurrencyButton.setVisible(false);
        }else{
            showCurrencyButton.setVisible(true);
            gridCurrency.setItems(service.getCurrency(choosenUser.getUserID()));
            currencyForm.setCurrencyList(service.getCurrency(choosenUser.getUserID()));
        }
    }
    private void configureGrids() {
        grid.setColumns("userID", "userName");
        grid.setItems(service.getUsers());
        grid.setVisible(false);
        grid.setHeight(300, Unit.PIXELS);
        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));

        gridRate.setColumns("currencyName", "currencyCode", "bid", "ask");
        gridRate.setVisible(false);

        gridCurrency.setColumns("currencyName", "currencyCode", "account");
        gridCurrency.setVisible(false);
        gridCurrency.setHeight(300, Unit.PIXELS);
        gridCurrency.asSingleSelect().addValueChangeListener(event -> editCurrency(event.getValue()));
    }
    public void setChoosenUser(User choosenUser){
        this.choosenUser = choosenUser;
    }
    public User getChoosenUser(){return this.choosenUser;}
    private HorizontalLayout firstBar() {
        editUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editUserButton.addClickListener(click -> userAction());

        showRatesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        showRatesButton.addClickListener(click -> showRatesAction());

        showCurrencyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        showCurrencyButton.addClickListener(click -> currencyAction());
        showCurrencyButton.setVisible(false);

        infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        infoButton.addClickShortcut(Key.F1);
        infoButton.addClickListener(click -> infoAction());

        return new HorizontalLayout(descriptionUser , choiceUser, editUserButton, showRatesButton, showCurrencyButton, infoButton);
    }
    private void userAction(){
        if(editUserButton.getText().equals("Edycja użytkowników")){
            editUserButton.setText("Rezygnacja z edycji użytkowników");
            grid.setVisible(true);
            createUserForm.setVisible(true);
        }else{
            editUserButton.setText("Edycja użytkowników");
            grid.setVisible(false);
            createUserForm.setVisible(false);
        }
        refresh();
    }
    private void showRatesAction(){
        if(showRatesButton.getText().equals("Pokaż kursy walut")){
            gridRate.setVisible(true);
            showRatesButton.setText("Ukryj kursy walut");
        }else{
            gridRate.setVisible(false);
            showRatesButton.setText("Pokaż kursy walut");
        }
        refresh();
    }
    private void currencyAction(){
        if(showCurrencyButton.getText().equals("Pokaż konta walutowe użytkownika")){
            showCurrencyButton.setText("Ukryj konta walutowe użytkownika");
            gridCurrency.setVisible(true);
            currencyForm.setVisible(true);
        }else{
            showCurrencyButton.setText("Pokaż konta walutowe użytkownika");
            gridCurrency.setVisible(false);
            currencyForm.setVisible(false);
        }
        refresh();
    }
    private void infoAction(){
        if(infoButton.getText().equals("Informacje o programie")) {
            info.setVisible(true);
            infoButton.setText("Ukryj informacje");
        } else{
            info.setVisible(false);
            infoButton.setText("Informacje o programie");
        }
    }
    public void editContact(User user) {

        if (user == null) {
            createUserForm.setUserID(null);
            createUserForm.setUserName("");
            removeClassName("editing");
        } else {
            createUserForm.setUserID(user.getUserID());
            createUserForm.setUserName(user.getUserName());
            createUserForm.setVisible(true);
            addClassName("editing");
        }
    }
    public void editCurrency(Currency currency){
        if(currency == null){
            currencyForm.setCurrency(new Currency());
        }else{
            currencyForm.setCurrency(currency);
            currencyForm.setVisible(true);
        }
    }
    public void setGridUserVisible(boolean visible){
        grid.setVisible(visible);
    }
    public void setGridCurrencyVisible(boolean visible) {
        gridCurrency.setVisible(visible);
    }
    public void setTextEditUserButton(){
        editUserButton.setText("Edycja użytkowników");
    }
    public void setTextShowCurrencyButton(){
        showCurrencyButton.setText("Pokaż konta walutowe użytkownika");
    }
    private void info(){
        String text ="Aplikacja \"Kantor internetowy\" służy do wirtualnej wymiany walut. Wymiana następuje miedzy walutą obcą a polską i odwrotnie. \n" +
                "Aby skorzystać z tych możliwości programu należy w pierwszej kolejności wybrać użytkownika. Jeżeli jeszcze nie ma użytkowników należy utworzyć nowego użytkownika. A nastepnie go wybrać. \n" +
                "Można to wykonać po kliknieciu przycisku: \"Edycja użytkowników\". Informacje o aktualnych kursach walut można prześledzić klikając na przycisk: \"Pokaż kursy walut\". \n" +
                "Przycisk: \"Pokaż konta walutowe użytkownika\" jest odsłoniety po wybraniu użytkownika. Umożliwia on operacje na kontach użytkownika. Aby była możliwa wymiana walut użytkownik musi \n" +
                "posiadać co najmniej dwa konta walutowe, w tym jedno z nich musi być z polską walutą (PLN).   ";
        info.setValue(text);
        info.setHelperText("Informacje o programie");
        info.setSizeFull();
        info.setReadOnly(true);
        info.setVisible(false);
    }
}
