package us.galleryw.ufc.event;

import java.util.Collection;

import us.galleryw.ufc.domain.Transaction;
import us.galleryw.ufc.view.UfcViewType;

//import com.vaadin.demo.dashboard.domain.Transaction;
//import com.vaadin.demo.dashboard.view.DashboardViewType;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class UfcEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    public static final class PostViewChangeEvent {
        private final UfcViewType view;

        public PostViewChangeEvent(final UfcViewType view) {
            this.view = view;
        }

        public UfcViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }

}
