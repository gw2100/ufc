package us.galleryw.ufc.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;

public class UserServiceHibernateImpl implements UserService {
    private static Logger LOG = Logger.getLogger(UserServiceHibernateImpl.class.getName());
    private static UserService instance;

    public static UserService createService() {
        if (instance == null) {
            final UserServiceHibernateImpl contactService = new UserServiceHibernateImpl();
            instance = contactService;
        }
        return instance;
    }

    public synchronized List<User> findAll(String stringFilter) {
        List<User> filteredUsers = new ArrayList<User>();
        List<User> allUsers = new ArrayList<User>();
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        try {
            allUsers = session.createQuery(" from User").list();
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
        for (User user : allUsers) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || user.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    filteredUsers.add(user.clone());
                }
            } catch (CloneNotSupportedException ex) {
                LOG.log(Level.SEVERE, ex.toString());
            }
        }
        Collections.sort(filteredUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return filteredUsers;
    }

    public synchronized long count() {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        try {
            int count = Integer.parseInt(session.createQuery("select count(*) from User").uniqueResult().toString());
            return count;
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

    public synchronized void delete(User entry) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.delete(entry));
    }
    public synchronized void save(User entry) {
        if(entry.getRegistrationDate()==null)
            entry.setRegistrationDate(new Date());
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.saveOrUpdate(entry));
    }

    private void sessionHelp(Session session, User user, Runnable shfi) {
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
