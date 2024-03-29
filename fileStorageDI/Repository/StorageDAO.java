package ua.nikolay.fileStorageDI.Repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;
import ua.nikolay.fileStorageDI.Service.Exception.ValidateException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StorageDAO implements DAO<Storage> {
    @Autowired
    private static SessionFactory sessionFactory;

    public static SessionFactory createSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    @Override
    public Storage save(Storage storage) throws InternalServerErrorException {

        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.save(storage);
            session.getTransaction().commit();
            System.out.println("Save storage is done");

        } catch (HibernateException e) {
            throw new InternalServerErrorException("Save storage is failed");
        }

        return storage;
    }

    @Override
    public Storage delete(Storage storage) throws InternalServerErrorException {

        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.delete(storage);
            session.getTransaction().commit();
            System.out.println("Delete storage is done");
        } catch (HibernateException e) {
            throw new InternalServerErrorException("Delete storage is failed");
        }
        return storage;
    }

    @Override
    public Storage update(Storage storage) throws InternalServerErrorException {

        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.update(storage);
            session.getTransaction().commit();
            System.out.println("Update storage is done");

        } catch (HibernateException e) {
            throw new InternalServerErrorException("Update storage is failed");
        }
        return storage;
    }

    @Override
    public Storage findById(long id) throws InternalServerErrorException {
//        Session session = null;
//        Transaction tr = null;
        Storage resStorage = new Storage();
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            resStorage = session.get(Storage.class, id);
            if (resStorage == null)
                System.out.println("Storage with id: " + id + "does not exist");
            session.getTransaction().commit();
            System.out.println("find storage by id is done");

        } catch (HibernateException e) {
            throw new InternalServerErrorException("find storage by id is failed");
        }
        return resStorage;
    }

    public List<Storage> storageList() throws InternalServerErrorException {
        List<Storage> resList = new ArrayList();
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Storage");
            resList = query.list();
            session.getTransaction().commit();
            System.out.println("get lis done");
        } catch (HibernateException e) {
            throw new InternalServerErrorException("get lis done with error");
        }
        return resList;
    }

    public void storageExist(Storage storage) throws ValidateException {
        Storage readStorage = null;
        try {
            readStorage = findById(storage.getId());
        } catch (InternalServerErrorException e) {
            throw new ValidateException(e.getMessage());
        }
        if (readStorage == null)
            throw new ValidateException("Storage with id: " + storage.getId() + " is not exist");
    }


}
