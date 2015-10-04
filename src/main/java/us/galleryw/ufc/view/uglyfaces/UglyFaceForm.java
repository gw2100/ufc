package us.galleryw.ufc.view.uglyfaces;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;
import us.galleryw.ufc.backend.User;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class UglyFaceForm extends FormLayout {

    Button saveButton = new Button("Save", this::save);
    Button canceButtonl = new Button("Cancel", this::cancel);

    TextField name = new TextField("Name");
    TextField description = new TextField("Description");
    byte[] image;
    final Embedded imageEmbedded = new Embedded("Uploaded Image");

    // Receiver receiver=new Receiver(){
    //
    // @Override
    // public OutputStream receiveUpload(String filename, String mimeType) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // };
    class ImageUploader implements Receiver, SucceededListener {
        public File file;
        public OutputStream receiveUpload(String filename, String mimeType) {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                file = new File("/tmp/uploads/" + filename);
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(SucceededEvent event) {
            // Show the uploaded file in the image viewer
            imageEmbedded.setVisible(true);
            imageEmbedded.setSource(new FileResource(file));
        }
    }
    ImageUploader receiver = new ImageUploader();
    Upload upload = new Upload("Upload Image Here", receiver);

    UglyFace uglyFace;
    UglyFacesView parent;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<UglyFace> formFieldBindings;

    public UglyFaceForm(UglyFacesView parent) {
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

        HorizontalLayout actions = new HorizontalLayout(saveButton, canceButtonl);
        actions.setSpacing(true);
        imageEmbedded.setVisible(false);
        addComponents(actions, name, description,upload, imageEmbedded);
    }

    public void save(Button.ClickEvent event) {
        try {
            formFieldBindings.commit();
            UfcUI.getUglyFaceService().save(uglyFace);
            String msg = String.format("Saved uglyFace by " + uglyFace.getOwner().getName());
            Notification.show(msg, Type.TRAY_NOTIFICATION);
            parent.refreshList();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        parent.entryList.select(null);
    }

    public void edit(UglyFace uglyFace) {
        this.uglyFace = uglyFace;
        if (uglyFace != null) {
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(uglyFace, this);
            name.focus();
        }
        setVisible(uglyFace != null);
    }

}

// Implement both receiver that saves upload in a file and
// listener for successful upload

