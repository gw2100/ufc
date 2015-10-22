package us.galleryw.ufc.backend;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class UglyFaceTest {
    private final static Logger LOG = Logger.getLogger(UglyFaceTest.class);
    static User user = new User();
    static UglyFace uglyFaceM = new UglyFace();
    Session session;
    Transaction t;

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code
        System.out.println("@BeforeClass - oneTimeSetUp");
    }

    public UglyFaceTest() {
        LOG.info("init");
    }

    @Before
    public void setUp() {
        LOG.info("setup");
        session = DatabaseUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
    }

    @After
    public void tearDow() {
        LOG.info("teardown");
        if (session.isOpen()){
            session.getTransaction().commit();
            //session.close();
        }
    }

    @Test
    public void testSave() {
        LOG.info("testSave");
        assertTrue(uglyFaceM.getId() == null);
        user.setEmail("a@b.c");
        session.saveOrUpdate(user);
        uglyFaceM.setOwner(user);
        session.saveOrUpdate(uglyFaceM);
        LOG.info("after saving: uglyFace=" + uglyFaceM);
        assertTrue(uglyFaceM.getId() != null);
    }

    @Test
    public void testCascade() {
        LOG.info("testCascade");
        LOG.info("uglyFace.id=" + uglyFaceM.getId());
        UglyFace uglyFace = (UglyFace) session.load(UglyFace.class, uglyFaceM.getId());
        Vote vote = new Vote();
//        vote.setUglyFace(uglyFace);
//        vote.setVoter(user);
        uglyFace.getVotes().add(vote);
        Comment c = new Comment();
        c.setText("testText");
        c.setUglyFace(uglyFace);
        uglyFace.getComments().add(c);
        LOG.info("cascade:saveOrUpdate");
        session.saveOrUpdate(uglyFace);
        assertTrue(session.createQuery("from Vote").list().size() > 0);
        assertTrue(session.createQuery("from Comment").list().size() > 0);

    }
//    @Test
//    public void testCascade2() {
//        assertTrue(session.createQuery("from Vote").list().size() > 0);
//        assertTrue(session.createQuery("from Comment").list().size() > 0);
//        LOG.info("votes.size=" + session.createQuery("from Vote").list().size());
//        LOG.info("comments.size=" + session.createQuery("from Comment").list().size());
//        LOG.info("Comment=" + session.createQuery("from Comment").list().get(0));
//    }
}
