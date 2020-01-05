
package Hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();
        //initialize sessionFactory object by calling method initHibernateUtil()

	private static SessionFactory initHibernateUtil() {
        //method responsible for configuring a sessionFactory oject based on the confg file
		try {
			return new Configuration().configure().buildSessionFactory();
		} catch (HibernateException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {//method resposible for closing the session factory
		// Close caches and connection pools
		getSessionFactory().close();
	}
}
