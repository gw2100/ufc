package us.galleryw.ufc.view.uglyfaces;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;
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
public final class UglyFacesView extends VerticalLayout implements View {
    private Logger LOG = LoggerFactory.getLogger(UglyFacesView.class);

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city", "theater", "room", "title", "seats" };

    TextField filter = new TextField();
    Grid entryList = new Grid();
    Button newEntry = new Button("New uglyFace");
    Button deleteEntry = new Button("Delete uglyFace");
    private UglyFaceForm entryForm = new UglyFaceForm(this);

    public UglyFacesView() {
        setSizeFull();
        addStyleName("users");
        UfcEventBus.register(this);
        configureComponents();
        buildLayout();
    }
    private void configureComponents() {

        newEntry.addClickListener(e -> entryForm.edit(new UglyFace()));
        deleteEntry.addClickListener(e -> {
            UfcUI.getUglyFaceService().delete((UglyFace) entryList.getSelectedRow());
            refreshList();
        });

        filter.setInputPrompt("Filter...");
        filter.addTextChangeListener(e -> refreshList(e.getText()));

        entryList.setContainerDataSource(new BeanItemContainer<>(User.class));
        // entryList.setColumnOrder("id", "firstName", "lastName", "email");
        // entryList.removeColumn("registrationDate");
        // entryList.removeColumn("phone");
        entryList.setSelectionMode(Grid.SelectionMode.SINGLE);
        entryList.addSelectionListener(e -> {
            entryForm.edit((UglyFace) entryList.getSelectedRow());
            deleteEntry.setVisible(entryList.getSelectedRow() != null);
        });
        entryList.setEditorEnabled(false);
        entryList.setSizeFull();
        refreshList();
    }
    private void buildLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout(entryList, entryForm);
        mainLayout.setSizeFull();
        addComponent(buildToolbar());
        addComponent(mainLayout);
        mainLayout.setExpandRatio(entryList, 3);
        mainLayout.setExpandRatio(entryForm, 1);

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

        HorizontalLayout tools = new HorizontalLayout(filter, newEntry, deleteEntry);
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

    void refreshList() {
        refreshList(filter.getValue());
    }

    private void refreshList(String stringFilter) {
        entryList.setContainerDataSource(new BeanItemContainer<>(UglyFace.class, UfcUI.getUglyFaceService().findAll(stringFilter)));
        deleteEntry.setVisible(entryList.getSelectedRow() != null);
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
