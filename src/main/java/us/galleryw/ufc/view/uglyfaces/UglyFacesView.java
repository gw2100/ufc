package us.galleryw.ufc.view.uglyfaces;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;
import us.galleryw.ufc.backend.User;
import us.galleryw.ufc.event.UfcEventBus;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({ "serial", "unchecked" })
public class UglyFacesView extends VerticalLayout implements View {
    private Logger LOG = LoggerFactory.getLogger(UglyFacesView.class);

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "country", "city", "theater", "room", "title", "seats" };

    public static final String ID = "id";
    public static final String IMAGE = "image";
    public static final String THUMBNAIL = "thumbnail";
    public static final String NAME = "name";
    public static final String DESCRIPTIION = "description";
    public static final String OWNER = "owner";

    public static final String UPLOAD_DATE = "uploadDate";
    public static final String VOTES = "votes";
    public static final String VOTE = "vote";

    TextField filter = new TextField();
    BeanItemContainer<UglyFace> container = new BeanItemContainer<>(UglyFace.class);
    Table entryList = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            // Format by property type
            if (property.getType() == Date.class) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                return df.format((Date) property.getValue());
            } else if (property.getType() == User.class) {
                User user = (User) property.getValue();
                return user.getName();
            } else if (property.getType() == Set.class) {
                Set votes = (Set) property.getValue();
                return String.valueOf(votes.size());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    Button newEntry = new Button("New uglyFace");
    Button deleteEntry = new Button("Delete uglyFace");
    private UglyFaceForm entryForm = new UglyFaceForm(UglyFacesView.this);

    public UglyFacesView() {
        setSizeFull();
        addStyleName("users");
        UfcEventBus.register(this);
        configureComponents();
        buildLayout();
        refreshList();
    }
    private void configureComponents() {

        newEntry.addClickListener(e -> entryForm.newUglyFace(new UglyFace()));
        deleteEntry.addClickListener(e -> {
            Object value = entryList.getValue();
            if (value != null) {
                UfcUI.getUglyFaceService().delete((UglyFace) value);
                refreshList();
            }
        });

        filter.setInputPrompt("Filter...");
        filter.addTextChangeListener(e -> refreshList(e.getText()));
        entryList.setContainerDataSource(container);
        entryList.setSelectable(true);
        entryList.setImmediate(true);
        entryList.setMultiSelect(false);
        // Handle selection change.
        // entryList.addValueChangeListener(new Property.ValueChangeListener() {
        // @Override
        // public void valueChange(com.vaadin.data.Property.ValueChangeEvent
        // event) {
        // Property p=event.getProperty();
        // UglyFace uglyFace=(UglyFace)p.getValue();
        // Item item=entryList.getItem(uglyFace.getId());
        // item.getItemProperty("vote");
        // //UglyFace uglyFace=(UglyFace)entryList.getValue();
        // }
        // });

        entryList.setSizeFull();
        deleteEntry.setVisible(entryList.getValue() != null);
        entryList.addGeneratedColumn(THUMBNAIL, new ImageColumnGenerator());

        entryList.setColumnWidth(THUMBNAIL, 100);
        entryList.addGeneratedColumn(VOTE, new CheckBoxColumnGenerator(this));
        entryList.setVisibleColumns(ID, THUMBNAIL, OWNER, UPLOAD_DATE, VOTES, VOTE);
        entryList.setColumnHeader(ID, "ID");
        //entryList.setColumnHeader(IMAGE, "Image");
        entryList.setColumnHeader(THUMBNAIL, "Image");
        entryList.setColumnHeader(OWNER, "Owner");
        entryList.setColumnHeader(UPLOAD_DATE, "Upload Date");
        entryList.setColumnHeader(VOTES, "Vote Count");
        entryList.setColumnHeader(ID, "ID");
        entryList.setColumnHeader(VOTE, "Vote");

    }
    private void buildLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout(entryList, entryForm);
        mainLayout.setSizeFull();
        addComponent(buildToolbar());
        addComponent(mainLayout);
        mainLayout.setExpandRatio(entryList, 1);
        mainLayout.setExpandRatio(entryForm, 1);

        setExpandRatio(mainLayout, 1);
    }
    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Ugly Faces");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        if (UfcUI.getCurrentUser() != null) {
            HorizontalLayout tools = new HorizontalLayout(filter, newEntry, deleteEntry);
            tools.setSpacing(true);
            tools.addStyleName("toolbar");
            header.addComponent(tools);
        }

        return header;
    }
    @Override
    public void detach() {
        super.detach();
        UfcEventBus.unregister(this);
    }

    void refreshList() {
        refreshList(filter.getValue());
    }

    private void refreshList(String stringFilter) {
        container.removeAllItems();
        container.addAll(UfcUI.getUglyFaceService().findAll(stringFilter));
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
