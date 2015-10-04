package us.galleryw.ufc.view;

import us.galleryw.ufc.view.dashboard.DashboardView;
import us.galleryw.ufc.view.reports.ReportsView;
import us.galleryw.ufc.view.sales.SalesView;
import us.galleryw.ufc.view.schedule.ScheduleView;
import us.galleryw.ufc.view.transactions.TransactionsView;
import us.galleryw.ufc.view.uglyfaces.UglyFacesView;
import us.galleryw.ufc.view.users.UsersView;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum UfcViewType {
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true), SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false), TRANSACTIONS(
            "transactions", TransactionsView.class, FontAwesome.TABLE, false), REPORTS("reports", ReportsView.class,
            FontAwesome.FILE_TEXT_O, true), SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false),USERS("users", UsersView.class, FontAwesome.TABLE, false),UGLYFACES("uglyfaces", UglyFacesView.class, FontAwesome.TABLE, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private UfcViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static UfcViewType getByViewName(final String viewName) {
        UfcViewType result = null;
        for (UfcViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
