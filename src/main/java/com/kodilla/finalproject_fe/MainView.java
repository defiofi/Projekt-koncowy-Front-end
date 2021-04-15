package com.kodilla.finalproject_fe;

import com.kodilla.finalproject_fe.domain.CreateUserForm;
import com.kodilla.finalproject_fe.domain.RateOfExchangeDTO;
import com.kodilla.finalproject_fe.domain.UserDTO;
import com.kodilla.finalproject_fe.service.Service;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.stereotype.Component;

@Component
@Route
public class MainView extends VerticalLayout {
    private UserDTO choosenUser = new UserDTO();
    private CreateUserForm createUserForm;
    private Service service;
    private Grid<RateOfExchangeDTO> gridRate = new Grid<>(RateOfExchangeDTO.class);
    private Grid<UserDTO> gridUser = new Grid<>(UserDTO.class);
    private TextField choiceUser = new TextField();
    private TextField descriptionUser = new TextField();
    private Button createUserButton = new Button("Edycja użytkowników");

    public MainView() {

        service = Service.getInstance();
        createUserForm = new CreateUserForm(service.getUsers(),this);
        createUserForm.setVisible(false);
        descriptionUser.setPlaceholder("Wybrany użytkownik:");
        VerticalLayout userContent = new VerticalLayout(gridUser, createUserForm);
        HorizontalLayout userBar = createUserBar();
        configureGridUser();
        add(new H1("Kantor internetowy"), userBar , userContent);
        refresh();

    }
    public void refresh() {
        //gridRate.setItems(rateOfExchangeService.getRates());
       gridUser.setItems(service.getUsers());
       choiceUser.setPlaceholder(choosenUser.getUserName());
    }
    private void configureGridUser() {
        gridUser.setColumns("userID", "userName");
        gridUser.setVisible(false);
        gridUser.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }
    private void configureGridRate() {
        gridRate.setColumns("currencyName", "currencyCode", "bid", "ask");
    }
    /*private List<String> findListOfUsers(){
        List<UserDTO> dtoList = rateOfExchangeService.getUsers();
        List<String> list = dtoList.stream()
                .map(UserDTO::getUserName)
                .collect(Collectors.toList());
        return list;
    }*/
    public void setChoosenUser(UserDTO choosenUser){
        this.choosenUser = choosenUser;
    }
    private HorizontalLayout createUserBar() {
        createUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createUserButton.addClickListener(click -> createAction());

        return new HorizontalLayout(descriptionUser , choiceUser, createUserButton);
    }

    private void createAction(){
        gridUser.setVisible(true);
        createUserForm.setVisible(true);
        refresh();
    }
    public void editContact(UserDTO userDTO) {

        if (userDTO == null) {
            createUserForm.setUserID(null);
            createUserForm.setUserName("");
            createUserForm.setVisible(false);
            removeClassName("editing");
        } else {
            createUserForm.setUserID(userDTO.getUserID());
            createUserForm.setUserName(userDTO.getUserName());
            createUserForm.setVisible(true);
            addClassName("editing");
        }
    }
    public void setGridUserVisible(boolean visible){
        gridUser.setVisible(visible);
    }
}
