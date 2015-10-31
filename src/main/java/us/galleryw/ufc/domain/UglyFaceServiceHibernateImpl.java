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

public class UglyFaceServiceHibernateImpl extends ServiceHibernateImplementation implements UglyFaceService {
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
        List<UglyFace> filteredFaces = new ArrayList<UglyFace>();
        List<UglyFace> allFaces = new ArrayList<UglyFace>();
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        try {
            allFaces = session.createQuery(" from UglyFace").list();
            LOG.info("allFaces.size="+allFaces.size());
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
        for (UglyFace uglyFace : allFaces) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || uglyFace.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    filteredFaces.add(uglyFace.clone());
                }
            } catch (CloneNotSupportedException ex) {
                LOG.log(Level.SEVERE, ex.toString());
            }
        }
        Collections.sort(filteredFaces, new Comparator<UglyFace>() {
            @Override
            public int compare(UglyFace o1, UglyFace o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return filteredFaces;
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
    public synchronized UglyFace findById(Serializable id) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        return (UglyFace)session.get(UglyFace.class, id);   
    }
    public synchronized void delete(UglyFace entry) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.delete(entry));
    }
    public synchronized void delete(Serializable id) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        UglyFace uglyFace=(UglyFace)session.get(UglyFace.class, id);
        sessionHelp(session, uglyFace, () -> session.delete(uglyFace));
    }
    public synchronized Serializable save(UglyFace entry) {
        LOG.info("uglyFace="+entry);
        if(entry.getUploadDate()==null)
            entry.setUploadDate(new Date());
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        Serializable s=session.save(entry);
        session.flush();
        return s;
    }
    public synchronized UglyFace merge(UglyFace entry) {
        LOG.info("uglyFace="+entry);
        if(entry.getUploadDate()==null)
            entry.setUploadDate(new Date());
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        UglyFace u= (UglyFace) session.merge(entry);
        session.flush();
        return u;
    }
    public synchronized void persist(UglyFace entry) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.persist(entry));
    }


}
