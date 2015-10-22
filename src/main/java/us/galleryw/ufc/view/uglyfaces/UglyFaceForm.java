package us.galleryw.ufc.view.uglyfaces;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

public class UglyFaceForm extends FormLayout {
    private static final long serialVersionUID = 1L;
    private Logger LOG = LoggerFactory.getLogger(UglyFaceForm.class);
    Button saveButton = new Button("Save", this::save);
    Button canceButtonl = new Button("Cancel", this::cancel);
    TextField name = new TextField("Name");
    TextField description = new TextField("Description");
    byte[] originalImageBytes;
    byte[] image;
    byte[] thumbnail;
    Dimension imageBoundary = new Dimension(800, 800);
    Dimension imageThumbnailBoundary = new Dimension(100, 100);

    final Image imageUploaded = new Image("Uploaded Image");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageUploader receiver = new ImageUploader();
    Upload upload = new Upload("Upload Image Here", receiver);

    UglyFace uglyFace;
    UglyFacesView parent;
    BeanFieldGroup<UglyFace> beanFieldGroup;

    class ImageUploader implements Receiver, SucceededListener {
        public File file;
        private String fileName;
        public OutputStream receiveUpload(String filename, String mimeType) {
            LOG.info("filename=" + filename);
            fileName = filename;
            return baos;
        }

        public void uploadSucceeded(SucceededEvent event) {
            try {
                originalImageBytes = baos.toByteArray();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUploaded.setSource(new StreamResource(new StreamSource() {
                @Override
                public InputStream getStream() {
                    try {
                        image = ImageHelper.scaleToBound(originalImageBytes, imageBoundary);
                        thumbnail = ImageHelper.scaleToBound(originalImageBytes, imageThumbnailBoundary);
                        LOG.info("imageBytes=" + image.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return new ByteArrayInputStream(image);
                }
            }, fileName));
            imageUploaded.setVisible(true);
            if (name.getValue() == null || name.getValue().length() == 0)
                name.setValue(fileName);
        }
    }

    public UglyFaceForm(UglyFacesView parent) {
        this.parent = parent;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);
        HorizontalLayout actions = new HorizontalLayout(saveButton, canceButtonl);
        actions.setSpacing(true);
        imageUploaded.setVisible(false);
        addComponents(actions, name, description, upload, imageUploaded);
        upload.addSucceededListener(receiver);
    }

    public void save(Button.ClickEvent event) {
        try {
            beanFieldGroup.commit();
            if (uglyFace.getImage() != null)
                LOG.info("imageSize=" + uglyFace.getImage().length);
            else
                LOG.info("image is null");
            uglyFace.setImage(image);
            uglyFace.setThumbnail(thumbnail);
            uglyFace.setOwner(UfcUI.getCurrentUser());
            uglyFace.setUploadDate(new Date());
            UfcUI.getUglyFaceService().persist(uglyFace);
            LOG.info("uglyFace.id=" + uglyFace.getId());
            reset();
            // String msg = String.format("Saved uglyFace by " +
            // uglyFace.getOwner().getName());
            // Notification.show(msg, Type.TRAY_NOTIFICATION);
            parent.entryList.select(null);
            parent.refreshList();
        } catch (FieldGroup.CommitException e) {
        }
    }

    public void cancel(Button.ClickEvent event) {
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        parent.entryList.select(null);
        reset();
    }

    public void newUglyFace(UglyFace uglyFace) {
        this.uglyFace = uglyFace;
        if (uglyFace != null) {
            beanFieldGroup = BeanFieldGroup.bindFieldsBuffered(uglyFace, this);
            name.focus();
            setVisible(true);
        }
    }
    private void reset() {
        imageUploaded.setSource(null);
        name.setValue("");
        description.setValue("");
        originalImageBytes = null;
        image = null;
        thumbnail = null;
        baos.reset();;
        setVisible(false);
    }
}
