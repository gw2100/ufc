package us.galleryw.ufc.view.users;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.User;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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
    Button saveButton = new Button("Save", this::save);
    Button cancelButton = new Button("Cancel", this::cancel);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField phone = new TextField("Phone");
    TextField email = new TextField("Email");
    DateField registrationDate = new DateField("Registration date");

    User user;
    UsersView parent;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<User> beanFieldGroup;

    public UserForm(UsersView parent) {
        this.parent = parent;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        /*
         * Highlight primary actions.
         * 
         * With Vaadin built-in styles you can highlight the primary save button
         * and give it a keyboard shortcut for a better UX.
         */
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        actions.setSpacing(true);

        addComponents(actions, firstName, lastName, phone, email, registrationDate);
    }

    public void save(Button.ClickEvent event) {
        try {
            beanFieldGroup.commit();
            UfcUI.getUserService().save(user);
            String msg = String.format("Saved '%s %s'.", user.getFirstName(), user.getLastName());
            Notification.show(msg, Type.TRAY_NOTIFICATION);
            parent.refreshList();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        parent.userList.select(null);
    }

    public void edit(User user) {
        this.user = user;
        if (user != null) {
            beanFieldGroup = BeanFieldGroup.bindFieldsBuffered(user, this);
            firstName.focus();
        }
        setVisible(user != null);
    }

}
