package us.galleryw.ufc.backend;

import java.util.Date;
import java.util.Random;



//import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    final private static SessionFactory sessionFactory;

    static {
        try {
            final Configuration configuration = new Configuration().configure();
            final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                    .buildServiceRegistry();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            insertExampleData();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        //
        // if (!sessionFactory.getCurrentSession().getTransaction().isActive())
        // sessionFactory.getCurrentSession().beginTransaction();

        return sessionFactory;
    }

    public static void insertExampleData() {
        logger.info("insertExampleData");
        final String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia", "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
                "Lisa", "Marge" };
        final String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel", "Simons", "Verne", "Scott", "Allison", "Gates",
                "Rowling", "Barks", "Ross", "Schneider", "Tate" };
        final String cities[] = { "Amsterdam", "Berlin", "Helsinki", "Hong Kong", "London", "Luxemburg", "New York", "Oslo", "Paris",
                "Rome", "Stockholm", "Tokyo", "Turku" };
        final String streets[] = { "4215 Blandit Av.", "452-8121 Sem Ave", "279-4475 Tellus Road", "4062 Libero. Av.", "7081 Pede. Ave",
                "6800 Aliquet St.", "P.O. Box 298, 9401 Mauris St.", "161-7279 Augue Ave", "P.O. Box 496, 1390 Sagittis. Rd.",
                "448-8295 Mi Avenue", "6419 Non Av.", "659-2538 Elementum Street", "2205 Quis St.", "252-5213 Tincidunt St.",
                "P.O. Box 175, 4049 Adipiscing Rd.", "3217 Nam Ave", "P.O. Box 859, 7661 Auctor St.", "2873 Nonummy Av.",
                "7342 Mi, Avenue", "539-3914 Dignissim. Rd.", "539-3675 Magna Avenue", "Ap #357-5640 Pharetra Avenue",
                "416-2983 Posuere Rd.", "141-1287 Adipiscing Avenue", "Ap #781-3145 Gravida St.", "6897 Suscipit Rd.", "8336 Purus Avenue",
                "2603 Bibendum. Av.", "2870 Vestibulum St.", "Ap #722 Aenean Avenue", "446-968 Augue Ave", "1141 Ultricies Street",
                "Ap #992-5769 Nunc Street", "6690 Porttitor Avenue", "Ap #105-1700 Risus Street", "P.O. Box 532, 3225 Lacus. Avenue",
                "736 Metus Street", "414-1417 Fringilla Street", "Ap #183-928 Scelerisque Road", "561-9262 Iaculis Avenue" };
        // PersonContainer c = null;
        Random r = new Random(0);
        Session sess = getSessionFactory().getCurrentSession();
        Transaction transaction = sess.beginTransaction();

        try {
            for (int i = 0; i < 10; i++) {
                User p = new User();
                p.setFirstName(fnames[r.nextInt(fnames.length)]);
                p.setLastName(lnames[r.nextInt(lnames.length)]);
                p.setEmail(p.getFirstName().toLowerCase() + "." + p.getLastName().toLowerCase() + "@vaadin.com");
                //p.setIsAdmin(false);
                int n = r.nextInt(100000);
                if (n < 10000) {
                    n += 10000;
                }
                p.setRegistrationDate(new Date());
                sess.saveOrUpdate(p);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());

        }
    }
}
