package us.galleryw.ufc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import us.galleryw.ufc.event.UfcEventBus;
import us.galleryw.ufc.view.UfcViewType;
import us.galleryw.ufc.event.UfcEvent.BrowserResizeEvent;
import us.galleryw.ufc.event.UfcEvent.CloseOpenWindowsEvent;
import us.galleryw.ufc.event.UfcEvent.PostViewChangeEvent;

//import com.vaadin.demo.dashboard.event.DashboardEventBus;
//import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class UfcNavigator extends Navigator {
    private Logger LOG = LoggerFactory.getLogger(UfcNavigator.class);
    private static final UfcViewType ERROR_VIEW = UfcViewType.DASHBOARD;
    private ViewProvider errorViewProvider;

    public UfcNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);
        initViewChangeListener();
        initViewProviders();
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                UfcViewType view = UfcViewType.getByViewName(event.getViewName());
                UfcEventBus.post(new PostViewChangeEvent(view));
                UfcEventBus.post(new BrowserResizeEvent());
                UfcEventBus.post(new CloseOpenWindowsEvent());
            }
        });
    }

    private void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (final UfcViewType viewType : UfcViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(viewType.getViewName(), viewType.getViewClass()) {
                private View cachedInstance;
                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType.getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };
            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }
            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
