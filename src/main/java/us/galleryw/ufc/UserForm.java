package us.galleryw.ufc;

import us.galleryw.ufc.backend.User;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class UserForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField phone = new TextField("Phone");
    TextField email = new TextField("Email");
    DateField registrationDate = new DateField("Registration date");

    User user;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<User> formFieldBindings;

    public UserForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        /* Highlight primary actions.
         *
         * With Vaadin built-in styles you can highlight the primary save button
         * and give it a keyboard shortcut for a better UX.
         */
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);

		addComponents(actions, firstName, lastName, phone, email, registrationDate);
    }

    public void save(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();
            //TODO:uncomment
           // getUI().service.save(user);

            String msg = String.format("Saved '%s %s'.",
                    user.getFirstName(),
                    user.getLastName());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            //TODO:uncomment
            //getUI().refreshList();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        //TODO:uncomment
        //getUI().userList.select(null);
    }

    void edit(User contact) {
        this.user = contact;
        if(contact != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(contact, this);
            firstName.focus();
        }
        setVisible(contact != null);
    }

    void refreshList() {
      //  refreshList(filter.getValue());
    }

    private void refreshList(String stringFilter) {
//        userList.setContainerDataSource(new BeanItemContainer<>(User.class, service.findAll(stringFilter)));
//        LOG.info("selectedRow=" + userList.getSelectedRow());
//        System.out.println("system.out:selectedRow=" + userList.getSelectedRow());
//        deleteUser.setVisible(userList.getSelectedRow() != null);
    }


}
