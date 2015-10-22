package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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


public class VoteServiceHibernateImpl extends ServiceHibernateImplementation implements VoteService {
    private static Logger LOG = Logger.getLogger(VoteServiceHibernateImpl.class.getName());
    private static VoteService instance;

    public static VoteService createService() {
        if (instance == null) {
            final VoteService contactService = new VoteServiceHibernateImpl();
            instance = contactService;
        }
        return instance;
    }

    public synchronized void delete(Vote entry) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.delete(entry));
    }
    public synchronized void delete(Serializable id) {
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        Vote user = (Vote) session.get(User.class, id);
        sessionHelp(session, user, () -> session.delete(user));
    }
    public synchronized void save(Vote entry) {
        if (entry.getVotingDate() == null)
            entry.setVotingDate(new Date());
        Session session = DatabaseUtil.getSessionFactory().getCurrentSession();
        sessionHelp(session, entry, () -> session.saveOrUpdate(entry));
    }

}
