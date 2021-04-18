package com.kodilla.finalproject_fe.domain;

import com.kodilla.finalproject_fe.MainView;
import com.kodilla.finalproject_fe.service.Service;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.List;

public class CreateUserForm extends FormLayout {
    private MainView mainView;
    private List<User> userList;
    private Service service;
    TextField userID = new TextField("userID");
    TextField userName = new TextField("Nazwa");

    Button createButton = new Button("Stwórz nowego użytkownika");
    Button changeButton = new Button("Zmień nazwę użytkownika");
    Button resignationButton = new Button("Anuluj");
    Button chooseButton = new Button("Wybierz użytkownika");
    Button deleteButton = new Button("Usuń użytkownika");

    public CreateUserForm(List<User> userList, MainView mainView) {
        this.mainView = mainView;
        this.userList = userList;
        service = Service.getInstance();
        add(userID, userName, createButtonsLayoutOne(), createButtonsLayoutTwo());
    }
    private HorizontalLayout createButtonsLayoutOne() {
        createButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        changeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        chooseButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        chooseButton.addClickShortcut(Key.ENTER);

        createButton.addClickListener(click -> createAction());
        changeButton.addClickListener(click -> changeAction());
        chooseButton.addClickListener(click -> chooseAction());

        return new HorizontalLayout(chooseButton, createButton, changeButton);
    }
    private HorizontalLayout createButtonsLayoutTwo() {
        resignationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resignationButton.addClickShortcut(Key.ESCAPE);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        resignationButton.addClickListener(click -> resignationAction());
        deleteButton.addClickListener(click -> deleteAction());

        return new HorizontalLayout(resignationButton, deleteButton);
    }
    private void createAction(){
        if(userName.getValue().length()>0) {
            service.createUser(new User(userName.getValue()));
            mainView.refresh();
        }
    }
    private void resignationAction(){
        setVisible(false);
        mainView.setGridUserVisible(false);
        mainView.setTextEditUserButton();
        mainView.refresh();
    }
    private void changeAction(){
        if(Long.parseLong(userID.getValue()) >0 && userName.getValue().length()>0) {
            service.changeUserName(new User(Long.parseLong(userID.getValue()), userName.getValue()));
            mainView.refresh();
        }
    }
    private void chooseAction(){
        mainView.setChoosenUser(new User(Long.parseLong(userID.getValue()), userName.getValue()));
        setVisible(false);
        mainView.setGridUserVisible(false);
        mainView.setTextEditUserButton();
        mainView.refresh();
    }
    private void deleteAction(){
        service.deleteUser(Long.parseLong(userID.getValue()));
        mainView.refresh();
    }
    public void setUserID(Long id){
        if(id == null){
            userID.setValue("");
        } else{
            userID.setValue(""+id);
        }
    }
    public void setUserName(String name){
        if(name == null){
            userName.setValue("");
        }else {
            userName.setValue(name);
        }
    }
}
