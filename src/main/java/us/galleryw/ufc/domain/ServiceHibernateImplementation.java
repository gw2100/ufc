package us.galleryw.ufc.backend;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;

public class ServiceHibernateImplementation {
    static Logger LOG = Logger.getLogger(ServiceHibernateImplementation.class.getName());

    public ServiceHibernateImplementation() {
        super();
    }

    protected void sessionHelp(Session session, Object user, Runnable shfi) {
        try {
            shfi.run();
        } catch (StaleObjectStateException e) {
            LOG.log(Level.SEVERE, e.toString());
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw e;
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, e.toString());
            if (session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw e;
        }
    
    }

}