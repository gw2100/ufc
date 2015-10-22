package us.galleryw.ufc.view.users;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.User;
import us.galleryw.ufc.event.UfcEventBus;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public final class UsersView extends VerticalLayout implements View, UserFormParent {
    private Logger LOG = LoggerFactory.getLogger(UsersView.class);

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city", "theater", "room", "title", "seats" };
    public static final String ID="id";
    public static final String FIRST_NAME="firstName";
    public static final String LAST_NAME="lastName";
    public static final String EMAIL="email";
    public static final String THUMBNAIL="thumbnail";
    public static final String PHONE="phone";
    public static final String ROLES="roles";
 
    TextField filter = new TextField();
    Grid userList = new Grid();
    Button newUser = new Button("New user");
    Button deleteUser = new Button("Delete user");
    private UserForm userForm = new UserForm(this);

    public UsersView() {
        setSizeFull();
        addStyleName("users");
        UfcEventBus.register(this);
        configureComponents();
        buildLayout();
    }
    private void configureComponents() {

        newUser.addClickListener(e -> userForm.edit(new User()));
        deleteUser.addClickListener(e -> {
            UfcUI.getUserService().delete((User) userList.getSelectedRow());
            updateContent();
        });

        filter.setInputPrompt("Filter users...");
        filter.addTextChangeListener(e -> refreshList(e.getText()));
        
        userList.setContainerDataSource(new BeanItemContainer<>(User.class));
        userList.removeColumn("registrationDate");
        userList.removeColumn("phone");
        userList.removeColumn("gender");
        userList.removeColumn("location");
        userList.removeColumn("password");
      
        userList.setSelectionMode(Grid.SelectionMode.SINGLE);
        userList.addSelectionListener(e -> {
            userForm.edit((User) userList.getSelectedRow());
            deleteUser.setVisible(userList.getSelectedRow() != null);
        });
        userList.setEditorEnabled(false);
        userList.setSizeFull();
        //userList.addStyleName(ValoTheme.TABLE_BORDERLESS);
        //userList.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        //userList.addStyleName(ValoTheme.TABLE_COMPACT);
        userList.setColumnOrder(ID,FIRST_NAME,LAST_NAME,EMAIL,THUMBNAIL,ROLES);
        updateContent();
    }
    private void buildLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout(userList, userForm);
        mainLayout.setSizeFull();
        addComponent(buildToolbar());
        addComponent(mainLayout);
        mainLayout.setExpandRatio(userList, 2);
        mainLayout.setExpandRatio(userForm, 1);

        setExpandRatio(mainLayout, 1);
    }
    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Users");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        HorizontalLayout tools = new HorizontalLayout(filter, newUser, deleteUser);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }
    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        UfcEventBus.unregister(this);
    }

    /* (non-Javadoc)
     * @see us.galleryw.ufc.view.users.UserFormParent#updateContent()
     */
    @Override
    public void updateContent() {
        refreshList(filter.getValue());
    }

    private void refreshList(String stringFilter) {
        userList.setContainerDataSource(new BeanItemContainer<>(User.class, UfcUI.getUserService().findAll(stringFilter)));
        deleteUser.setVisible(userList.getSelectedRow() != null);
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
