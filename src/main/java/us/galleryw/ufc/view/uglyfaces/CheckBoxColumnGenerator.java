package us.galleryw.ufc.view.uglyfaces;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.galleryw.ufc.UfcUI;
import us.galleryw.ufc.backend.UglyFace;
import us.galleryw.ufc.backend.User;
import us.galleryw.ufc.backend.Vote;

import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

class CheckBoxColumnGenerator implements Table.ColumnGenerator {
    private Logger LOG = LoggerFactory.getLogger(CheckBoxColumnGenerator.class);
    UglyFacesView parent;
    public CheckBoxColumnGenerator(UglyFacesView parent) {
        this.parent = parent;
    }
    public Component generateCell(Table source, Object itemId, Object columnId) {
        Long uglyFaceId = (Long) source.getItem(itemId).getItemProperty("id").getValue();
        UglyFace uglyFace = UfcUI.getUglyFaceService().findById(uglyFaceId);
        User currentUser = UfcUI.getCurrentUser();
        CheckBox checkBox = new CheckBox("Vote");
        checkBox.setValue(uglyFace.hasVoteBy(currentUser) != null);
        checkBox.addValueChangeListener(e -> {
            UglyFace u = UfcUI.getUglyFaceService().findById(uglyFaceId);
            Boolean changedValue = (Boolean) e.getProperty().getValue();
            if (changedValue) {
                Vote vote = new Vote();
                vote.setVotingDate(new Date());
                vote.setVoter(currentUser);
                vote.setUglyFace(u);
                WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
                vote.setVotingIp(webBrowser.getAddress());
                u.getVotes().add(vote);
            } else {
                Vote vote = u.hasVoteBy(UfcUI.getCurrentUser());
                if (vote != null)
                    u.getVotes().remove(vote);
            }
            UfcUI.getUglyFaceService().merge(u);
            parent.refreshList();
        });
        return checkBox;
    }
}