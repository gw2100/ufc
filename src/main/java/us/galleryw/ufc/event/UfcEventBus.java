package us.galleryw.ufc.event;

import us.galleryw.ufc.UfcUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
//import com.vaadin.demo.dashboard.DashboardUI;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class UfcEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        UfcUI.getUfcEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        UfcUI.getUfcEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        UfcUI.getUfcEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
            final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
