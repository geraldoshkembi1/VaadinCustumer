package org.example;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.data.Binder;


public class CustomerForm extends FormLayout {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
    private DateField birthday = new DateField("Birthday");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");


    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MyUI myUI;
    private Binder<Customer> binder = new Binder<> (Customer.class);

    public CustomerForm(MyUI myUI) {
        this.myUI = myUI;
        setSizeUndefined();
        HorizontalLayout buttonsLayout = new HorizontalLayout(save,delete);
        addComponent(firstName);
        addComponent(lastName);
        addComponent(email);
        addComponent(status);
        addComponent(birthday);
        addComponent(buttonsLayout);

        status.setItems(CustomerStatus.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(clickEvent -> save());
        delete.addClickListener(clickEvent -> delete());
    }


    public void setCustomer(Customer customer){
        this.customer=customer;
        binder.setBean(customer);
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void delete(){
        service.delete(customer);
        myUI.updateList();
        setVisible(false);
    }

    private void save(){
        service.save(customer);
        myUI.updateList();
        setVisible(false);
    }
}
