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
    public Storage save(Storage storage) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            session.save(storage);
            session.getTransaction().commit();
            System.out.println("Save storage is done");

        } catch (HibernateException e) {
            System.err.println("Save storage is failed");
            System.err.println(e.getMessage());
            storage = null;
        } finally {
            if (session != null)
                session.close();
        }

        return storage;
    }

    @Override
    public Storage delete(Storage storage) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            session.delete(storage);
            session.getTransaction().commit();
            System.out.println("Delete storage is done");

        } catch (HibernateException e) {
            System.err.println("Delete storage is failed");
            System.out.println(e.getMessage());
            storage = null;
            if (tr != null)
                tr.rollback();
        } finally {
            if (session != null)
                session.close();
        }
        return storage;
    }

    @Override
    public Storage update(Storage storage) {
        Session session =null;
        Transaction tr = null;
        try{
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            session.update(storage);
            session.getTransaction().commit();
            System.out.println("Update storage is done");

        }catch(HibernateException e){
            System.err.println("Update storage is failed");
            System.err.println(e.getMessage());
            storage = null;
            if(tr != null)
                tr.rollback();
        }finally{
            if(session != null)
                session.close();
        }
        return storage;
    }

    @Override
    public Storage findById(long id) {
        Session session = null;
        Transaction tr = null;
        Storage  resStorage = new Storage();
        try{
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            resStorage = session.get(Storage.class, id);
            if(resStorage == null)
                System.out.println("Storage with id: " + id + "does not exist");
            session.getTransaction().commit();
            System.out.println("find storage by id is done");

        }catch(HibernateException e){
            System.err.println("find storage by id is failed");
            System.err.println(e.getMessage());
        }finally{
            if(session != null){
                session.close();
            }
        }
        return resStorage;
    }

    public List<Storage>storageList(){
        List<Storage> resList = new ArrayList();
        Session session = null;
        Transaction tr = null;

        try{
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            Query query = session.createQuery("from Storage");
            resList = query.list();
            session.getTransaction().commit();
            System.out.println("get lis done");
        }catch(HibernateException e){
            System.err.println("get list done with error");
            System.out.println(e.getMessage());

        }finally{
            if(session != null){
                session.close();
            }
        }
        return resList;
    }

    public void storageExist(Storage storage) throws Exception{
        Storage readStorage = findById(storage.getId());
        if(readStorage == null)
            throw new Exception("Storage with id: " + storage.getId() + " is not exist");
    }


}
