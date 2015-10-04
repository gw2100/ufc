package us.galleryw.ufc.backend;

import java.io.Serializable;
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

public class UglyFaceServiceHibernateImpl implements UglyFaceService {
    private static Logger LOG = Logger.getLogger(UglyFaceServiceHibernateImpl.class.getName());
    private static UglyFaceService instance;

    public static UglyFaceService createService() {
        if (instance == null) {
            final UglyFaceServiceHibernateImpl contactService = new UglyFaceServiceHibernateImpl();
            instance = contactService;
        }
        return instance;
    }

    public synchronized List<UglyFace> findAll(String stringFilter) {
        List<UglyFace> filteredUsers = new ArrayList<UglyFace>();
        List<UglyFace> allUsers = new ArrayList<UglyFace>();
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        try {
            allUsers = session.createQuery(" from UglyFace").list();
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
        for (UglyFace user : allUsers) {
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
        Collections.sort(filteredUsers, new Comparator<UglyFace>() {
            @Override
            public int compare(UglyFace o1, UglyFace o2) {
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

    public synchronized void delete(UglyFace entry) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.delete(entry));
    }
    public synchronized void delete(Serializable id) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        UglyFace user=(UglyFace)session.get(UglyFace.class, id);
        sessionHelp(session, user, () -> session.delete(user));
    }
    public synchronized void save(UglyFace entry) {
        if(entry.getUploadDate()==null)
            entry.setUploadDate(new Date());
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.saveOrUpdate(entry));
    }

    private void sessionHelp(Session session, UglyFace user, Runnable shfi) {
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
