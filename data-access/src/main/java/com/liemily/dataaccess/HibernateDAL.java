package com.liemily.dataaccess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by Emily Li on 25/07/2017.
 */
@Component
@Lazy
public class HibernateDAL {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private SessionFactory sessionFactory;

    @Autowired
    public HibernateDAL(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void init() {
        sessionFactory.openSession();
    }

    public List query(String query) throws InterruptedException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List result = session.createQuery(query).list();
            session.getTransaction().commit();
            return result;
        } catch (HibernateException e) {
            final long WAIT_MS = 250;
            if (e.getMessage().contains("no connection is currently available")) {
                logger.warn("No connection available in the connection pool. Sleeping " + WAIT_MS + "ms", e);
                Thread.sleep(WAIT_MS);
                return query(query);
            }
            throw e;
        }
    }

    public void execute(String query) throws InterruptedException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (e.getMessage().contains("no connection is currently available")) {
                waitForConnection();
            } else {
                throw e;
            }
        }
    }

    public void save(Object object) throws InterruptedException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (e.getMessage().contains("no connection is currently available")) {
                waitForConnection();
            } else {
                logger.error("Encountered exception when attempting to save object " + object.getClass().getSimpleName(), e);
                throw e;
            }
        }
    }

    public void delete(String entity, String id) throws InterruptedException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Object objectToDelete = session.load(entity, id);
            session.delete(objectToDelete);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (e.getMessage().contains("no connection is currently available")) {
                waitForConnection();
            } else {
                throw e;
            }
        }
    }

    private void waitForConnection() throws InterruptedException {
        final long WAIT_MS = 250;
        logger.warn("No connection available in the connection pool. Sleeping " + WAIT_MS + "ms");
        Thread.sleep(WAIT_MS);
    }
}
