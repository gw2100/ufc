package us.galleryw.ufc.view;

//import com.vaadin.demo.dashboard.DashboardNavigator;
import us.galleryw.ufc.UfcNavigator;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
public class MainView extends HorizontalLayout {

    public MainView() {
        setSizeFull();
        addStyleName("mainview");
        ComponentContainer content = new CssLayout();
        addComponent(new UfcMenu());
        addComponent(content);      
        content.addStyleName("view-content");
        content.setSizeFull();
        setExpandRatio(content, 1.0f);
        new UfcNavigator(content);
    }
}
