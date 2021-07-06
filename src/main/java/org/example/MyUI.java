package org.example;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    CustomerService service = CustomerService.getInstance();
    Grid<Customer> grid = new Grid<>(Customer.class);
    TextField filterTextField= new TextField();
    CustomerForm customerForm = new CustomerForm(this);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        filterTextField.setPlaceholder("filter by name");
        filterTextField.addValueChangeListener(e-> updateList());
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearTextBtn = new Button(VaadinIcons.CLOSE);
        clearTextBtn.setDescription("clear the filter text");
        clearTextBtn.addClickListener(clickEvent -> filterTextField.clear());

        CssLayout filterLayout = new CssLayout();
        filterLayout.addComponent(filterTextField);
        filterLayout.addComponent(clearTextBtn);
        filterLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        Button update = new Button("add new cuostemer");
        update.addClickListener(clickEvent -> {
           grid.asSingleSelect().clear();
           customerForm.setCustomer(new Customer());
        });

       HorizontalLayout toolbar = new HorizontalLayout(filterLayout,update);

        grid.setColumns("firstName","lastName","email");
        HorizontalLayout main = new HorizontalLayout(grid,customerForm);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid,1);

        layout.addComponent(toolbar);
        layout.addComponent(main);
        updateList();

        setContent(layout);

        customerForm.setVisible(false);
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if(valueChangeEvent.getValue() == null){
                customerForm.setVisible(false);
            }else {
                customerForm.setCustomer(valueChangeEvent.getValue());
            }
        });
    }

    protected void updateList(){
        List<Customer> customers = service.findAll(filterTextField.getValue());
        grid.setItems(customers);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
