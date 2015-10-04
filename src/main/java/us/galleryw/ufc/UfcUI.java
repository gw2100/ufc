package us.galleryw.ufc;

import java.util.Locale;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;

import us.galleryw.ufc.backend.UglyFaceService;
import us.galleryw.ufc.backend.UglyFaceServiceHibernateImpl;
import us.galleryw.ufc.backend.UserService;
import us.galleryw.ufc.backend.UserServiceHibernateImpl;
import us.galleryw.ufc.data.DataProvider;
import us.galleryw.ufc.data.dummy.DummyDataProvider;
import us.galleryw.ufc.domain.User;
import us.galleryw.ufc.event.UfcEvent.CloseOpenWindowsEvent;
import us.galleryw.ufc.event.UfcEvent.UserLoggedOutEvent;
import us.galleryw.ufc.event.UfcEvent.UserLoginRequestedEvent;
import us.galleryw.ufc.event.UfcEvent.BrowserResizeEvent;
import us.galleryw.ufc.event.UfcEventBus;
import us.galleryw.ufc.view.LoginView;
import us.galleryw.ufc.view.MainView;

import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Theme("dashboard")
@Widgetset("us.galleryw.ufc.UfcWidgetSet")
@Title("UglyFaceContest")
@SuppressWarnings("serial")
public final class UfcUI extends UI {
    private Logger LOG = LoggerFactory.getLogger(UfcUI.class);
    /*
     * This field stores an access to the dummy backend layer. In real
     * applications you most likely gain access to your beans trough lookup or
     * injection; and not in the UI but somewhere closer to where they're
     * actually accessed.
     */
    private final DataProvider dataProvider = new DummyDataProvider();
    private final UfcEventBus dashboardEventbus = new UfcEventBus();
    private final UserService userService = UserServiceHibernateImpl.createService();
    private final UglyFaceService uglyFaceService = UglyFaceServiceHibernateImpl.createService();

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.US);
        LOG.info("init");

        UfcEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(final BrowserWindowResizeEvent event) {
                UfcEventBus.post(new BrowserResizeEvent());
            }
        });
    }

    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        LOG.info("user="+user);
        if (user != null && "admin".equals(user.getRole())) {
            LOG.info("mainView");
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            LOG.info("loginview");
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {
        LOG.info("userLoginRequested");
        User user = getDataProvider().authenticate(event.getUserName(), event.getPassword());
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        LOG.info("userLoggedOut");
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((UfcUI) getCurrent()).dataProvider;
    }

    public static UfcEventBus getDashboardEventbus() {
        return ((UfcUI) getCurrent()).dashboardEventbus;
    }
    public static UserService getUserService() {
        return ((UfcUI) getCurrent()).userService;
    }
    public static UglyFaceService getUglyFaceService() {
        return ((UfcUI) getCurrent()).uglyFaceService;
    }
}
