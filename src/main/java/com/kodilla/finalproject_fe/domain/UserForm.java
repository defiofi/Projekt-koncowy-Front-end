package com.kodilla.finalproject_fe.domain;

import com.kodilla.finalproject_fe.MainView;
import com.kodilla.finalproject_fe.service.Service;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;
import java.util.stream.Collectors;

public class UserForm extends FormLayout {
    private Service service;

    private MainView mainView;
    private List<UserDTO> userDTOList;
    ComboBox<String> comboBox = new ComboBox<>("Nazwa użytkownika");
    Button chooseButton = new Button("Wybierz użytkownika");
    Button deleteButton = new Button("Usuń użytkownika");
    Button resignationButton = new Button("Anuluj");

    public UserForm(List<UserDTO> userDTOList, MainView mainView) {
        this.mainView = mainView;
        this.userDTOList = userDTOList;
        service = Service.getInstance();
        List<String> list = userDTOList.stream()
                .map(UserDTO::getUserName)
                .collect(Collectors.toList());
        comboBox.setItems(list);
        add(comboBox, createButtonsLayout());
    }
    private HorizontalLayout createButtonsLayout() {
        chooseButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        chooseButton.addClickShortcut(Key.ENTER);
        resignationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resignationButton.addClickShortcut(Key.ESCAPE);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        chooseButton.addClickListener(click -> chooseAction());
        resignationButton.addClickListener(click -> resignationAction());
        deleteButton.addClickListener(click -> deleteAction());
        return new HorizontalLayout(chooseButton, resignationButton, deleteButton);
    }
    private void chooseAction(){
        mainView.setChoosenUser(userDTOList.stream()
                .filter(userDTO -> userDTO.getUserName().equals(comboBox.getValue()))
                .collect(Collectors.toList()).get(0));
        mainView.refresh();
    }
    private void resignationAction(){
        setUserForm(false);
        mainView.refresh();
    }
    private void deleteAction(){
        /**kasowanie użytkownika w serwisie */
        UserDTO user = userDTOList.stream()
                .filter(userDTO -> userDTO.getUserName().equals(comboBox.getValue()))
                .collect(Collectors.toList()).get(0);
        service.deleteUser(user.getUserID());
        mainView.refresh();
    }
    public void setUserForm(boolean visible) {
        setVisible(visible);

    }
}
