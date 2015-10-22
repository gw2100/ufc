package us.galleryw.ufc.view.uglyfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings({ "serial", "unchecked" })
public final class UglyFacesAdminView extends UglyFacesView {
    private Logger LOG = LoggerFactory.getLogger(UglyFacesAdminView.class);
    public UglyFacesAdminView() {
        super();
        entryList.addValueChangeListener(e -> {
            deleteEntry.setVisible(entryList.getValue() != null);
        });
    }  
}
