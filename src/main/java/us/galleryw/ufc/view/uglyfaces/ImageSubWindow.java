package us.galleryw.ufc.view.uglyfaces;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

class ImageSubWindow extends Window {
    public ImageSubWindow(byte[] imageBytes) {
        super("Image"); // Set window caption
        

        // Some basic content for the window
        VerticalLayout content = new VerticalLayout();
        StreamResource.StreamSource imageSource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(imageBytes);
            }
        };
        StreamResource imageResource = new StreamResource(imageSource, ".jpg");
        imageResource.setCacheTime(0);
        Embedded image = new Embedded("image", imageResource);
        content.addComponent(image);
        content.setMargin(true);
        setContent(content);
        center();

        // Trivial logic for closing the sub-window
        Button ok = new Button("OK");
        ok.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        content.addComponent(ok);
    }
}