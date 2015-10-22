package us.galleryw.ufc.view.uglyfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;

import com.vaadin.navigator.View;

@SuppressWarnings({ "serial", "unchecked" })
public final class UglyFacesUserView extends UglyFacesView implements View {
    private Logger LOG = LoggerFactory.getLogger(UglyFacesUserView.class);

    public UglyFacesUserView() {
        super();
        entryList.addValueChangeListener(e -> {
            UglyFace selected = (UglyFace) entryList.getValue();
            if (selected != null && UfcUI.getCurrentUser().equals(selected.getOwner())&&(selected.getVotes()==null||!selected.getVotes().isEmpty()))
                deleteEntry.setVisible(true);
        });
    }
}
