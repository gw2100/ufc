package us.galleryw.ufc.view.uglyfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;

import com.vaadin.navigator.View;

@SuppressWarnings({ "serial", "unchecked" })
public final class UglyFacesVisitorView extends UglyFacesView implements View {
    private Logger LOG = LoggerFactory.getLogger(UglyFacesVisitorView.class);

    public UglyFacesVisitorView() {
        super();
        entryList.setVisibleColumns(THUMBNAIL,OWNER,VOTES);
    }
}
