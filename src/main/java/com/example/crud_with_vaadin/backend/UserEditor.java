package com.example.crud_with_vaadin.backend;

import com.example.crud_with_vaadin.backend.entities.AppUser;
import com.example.crud_with_vaadin.backend.repositories.UserRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {

    private final UserRepository repository;

    private AppUser user;

    /* Fields to edit properties in Customer entity */
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField address = new TextField("Address");
    TextField job = new TextField("Job");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<AppUser> binder = new Binder<>(AppUser.class);
    private ChangeHandler changeHandler;

    @Autowired
    public UserEditor(UserRepository repository) {
        this.repository = repository;

        add(firstName, lastName, address, job, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // wire action buttons and enter key to save, delete and reset
        addKeyPressListener(Key.ENTER, e -> {
            try {
                saveEntity();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to save user");
            }
        });

        save.addClickListener(e -> {
            try {
                saveEntity();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to save user");
            }
        });

        delete.addClickListener(e -> deleteEntity());
        cancel.addClickListener(e -> editUser(user, "cancel"));
        setVisible(false);
    }

    void deleteEntity() {
        if (user == null) {
            return;
        }
        repository.delete(user);
        changeHandler.onChange();
    }

    void saveEntity() throws Exception {
        if (user == null) {
            throw new Exception("Failed to get user : Null");
        }
        repository.save(user);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
        // fetch all user
    }

    public final void editUser(AppUser c, String action) {
        if (c == null) {
            setVisible(false);
            return;
        }
        //Separate function by action command
        if (action.equalsIgnoreCase("Edit")) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            user = repository.findById(c.getId());
        }
        else if (action.equalsIgnoreCase("Cancel")){
            setVisible(false);
        } else {
            user = c;
        }
        // Bind user properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        binder.setBean(user);

        setVisible(true);

        // Focus first name field initially
        firstName.focus();

        //Binding textField to selected user
        firstName.setValue(c.getFirstName());
        lastName.setValue(c.getLastName());
        address.setValue(c.getAddress());
        job.setValue(c.getJob());
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;

    }

}
