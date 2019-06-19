package ua.nikolay.fileStorageDI.Repository;

//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;
import org.hibernate.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FileDAO implements DAO<File> {
    @Autowired
    private static SessionFactory sessionFactory;

    public static SessionFactory createSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }


    @Override
    public File save(File file) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            session.save(file);
            session.getTransaction().commit();
            System.out.println("Save file is done");

        } catch (HibernateException e) {
            System.err.println("Save file is failed");
            System.err.println(e.getMessage());
            file = null;
        } finally {
            if (session != null)
                session.close();
        }

        return file;
    }

    public void saveFileList(List<File> files, Storage storage) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            for (File element : files) {
                element.setStorage(storage);
                session.update(element);
            }
            session.getTransaction().commit();
            System.out.println("Save files are done");
        } catch (HibernateException e) {
            tr.rollback();
            System.err.println("Save files are failed");
            System.out.println(e.getMessage());
        } finally {
            if (session != null)
                session.close();
        }
    }//saveFileList


    @Override
    public File delete(File file) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            session.delete(file);
            session.getTransaction().commit();
            System.out.println("Delete file is done");

        } catch (HibernateException e) {
            System.err.println("Delete file is failed");
            System.out.println(e.getMessage());
            file = null;
            if (tr != null)
                tr.rollback();
        } finally {
            if (session != null)
                session.close();
        }
        return file;
    }

    @Override
    public File update(File file) {
        Session session = null;
        Transaction tr = null;
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            session.update(file);
            session.getTransaction().commit();
            System.out.println("Update file " + file + " is done");

        } catch (HibernateException e) {
            System.err.println("Update file " + file + " is failed");
            System.err.println(e.getMessage());
            file = null;
            if (tr != null)
                tr.rollback();
        } finally {
            if (session != null)
                session.close();
        }
        return file;
    }

    @Override
    public File findById(long id) {
        Session session = null;
        Transaction tr = null;

        //List<File> resFile = new ArrayList<>();
        File resFile = new File();
        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();

            resFile = session.get(File.class, id);

            //Query query = session.createQuery("from File where id= : id");
            ///query.setParameter("id", id);
            //resFile = query.list();

            if (resFile == null)
                System.out.println("File with id: " + id + "does not exist");
            session.getTransaction().commit();
            System.out.println("find file by id is done");
            ;
        } catch (HibernateException e) {
            System.err.println("find file by id is failed");
            System.err.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return resFile;
    }

    public List<File> fileList() {
        List<File> resList = new ArrayList();
        Session session = null;
        Transaction tr = null;

        try {
            session = createSessionFactory().openSession();
            tr = session.getTransaction();
            tr.begin();
            Query query = session.createQuery("from File");
            resList = query.list();
            session.getTransaction().commit();
            System.out.println("get lis done");
        } catch (HibernateException e) {
            System.err.println("get list done with error");
            System.out.println(e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return resList;
    }

    public void fileExist(File file) throws Exception {
        File readFile = findById(file.getId());
        if (readFile == null)
            throw new Exception("File with id: " + file.getId() + " is not exist");
    }

    public List<File> readFileList(Storage storage) throws Exception {
        List<File> files = fileList();
        List<File> resFilesList = new ArrayList();
        if (files == null) {
            throw new Exception("Storage with id: " + storage.getId() + " have not files");
        }
        for (File element : files) {
            if (storage.equals(element.getStorage())) {
                resFilesList.add(element);
            }
        }
        return files;
    }

}
