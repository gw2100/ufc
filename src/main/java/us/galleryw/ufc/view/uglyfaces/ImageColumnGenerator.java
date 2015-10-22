package us.galleryw.ufc.view.uglyfaces;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import us.galleryw.ufc.UfcUI;

import com.vaadin.data.Property;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

class ImageColumnGenerator implements Table.ColumnGenerator {
    /**
     * Generates the cell containing the Double value. The column is irrelevant
     * in this use case.
     */
    public Component generateCell(Table source, Object itemId, Object columnId) {
        Component component = null;
        // Get the object stored in the cell as a property
        Property prop = source.getItem(itemId).getItemProperty(columnId);
        if (prop.getType().equals(byte[].class)) {
            byte[] thumbnailBytes = (byte[]) prop.getValue();
            if (thumbnailBytes == null) {
                component = new Label("");
            } else {
                StreamResource.StreamSource imageSource = new StreamResource.StreamSource() {
                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(thumbnailBytes);
                    }
                };
                StreamResource imageResource = new StreamResource(imageSource, ".jpg");
                imageResource.setCacheTime(0);
                Embedded image = new Embedded("", imageResource);
                image.requestRepaint();
                component = image;
                Property imageProp = source.getItem(itemId).getItemProperty(UglyFacesView.IMAGE);
                byte[] imageBytes = (byte[]) imageProp.getValue();
                image.addClickListener(new ClickListener() {
                    @Override
                    public void click(ClickEvent event) {
                        ImageSubWindow sub = new ImageSubWindow(imageBytes);
                        UfcUI.getCurrent().addWindow(sub);
                    }
                });
            }
        }
        return component;

    }
}