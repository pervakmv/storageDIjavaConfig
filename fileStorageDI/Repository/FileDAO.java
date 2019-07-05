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
import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;
import ua.nikolay.fileStorageDI.Service.Exception.ValidateException;

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
    public File save(File file) throws InternalServerErrorException {


        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(file);
            session.getTransaction().commit();
            System.out.println("Save file is done");
        } catch (HibernateException e) {
            file = null;
            throw new InternalServerErrorException("Save file is failed");
        }
        return file;
    }

    public void saveFileList(List<File> files, Storage storage) throws InternalServerErrorException {
        Transaction tr = null;
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            tr = session.getTransaction();
            tr.begin();
            for (File element : files) {
                element.setStorage(storage);
                session.update(element);
            }
            session.getTransaction().commit();
            System.out.println("Save files are done");
        } catch (HibernateError e) {
            tr.rollback();
            throw new InternalServerErrorException("Save files are failed");
        }
    }//saveFileList


    @Override
    public File delete(File file) throws InternalServerErrorException {
        Transaction tr = null;
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            tr = session.getTransaction();
            tr.begin();
            session.delete(file);
            session.getTransaction().commit();
            System.out.println("Delete file is done");

        } catch (HibernateException e) {

            file = null;
            if (tr != null)
                tr.rollback();

            throw new InternalServerErrorException("Delete file is done");
        }
        return file;
    }

    @Override
    public File update(File file) throws InternalServerErrorException {

        Transaction tr = null;
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {

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
            throw new InternalServerErrorException("Update file " + file + "is failed");
        }
        return file;
    }

    @Override
    public File findById(long id) throws InternalServerErrorException {

        Transaction tr = null;

        File resFile = new File();
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            tr = session.getTransaction();
            tr.begin();
            resFile = session.get(File.class, id);

            if (resFile == null)
                System.out.println("File with id: " + id + "does not exist");
            session.getTransaction().commit();
            System.out.println("find file by id is done");
        } catch (HibernateException e) {
            System.err.println("find file by id is failed");
            throw new InternalServerErrorException("find file by id is failed");
        }
        return resFile;
    }

    public List<File> fileList() throws InternalServerErrorException {
        List<File> resList = new ArrayList();
        Transaction tr = null;
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            tr = session.getTransaction();
            tr.begin();
            Query query = session.createQuery("from File");
            resList = query.list();
            session.getTransaction().commit();
            System.out.println("get lis done");
        } catch (HibernateException e) {
            throw new InternalServerErrorException("get list donw with error");
        }
        return resList;
    }

    public void fileExist(File file) throws ValidateException {
        File readFile = null;
        try {
            readFile = findById(file.getId());
        } catch (InternalServerErrorException e) {
            throw new ValidateException(e.getMessage());
        }
        if (readFile == null)
            throw new ValidateException("File with id: " + file.getId() + " is not exist");
    }

    public List<File> readFileList(Storage storage) throws InternalServerErrorException {
        List<File> files = fileList();
        List<File> resFilesList = new ArrayList();
        if (files == null) {
            throw new InternalServerErrorException("Storage with id: " + storage.getId() + " have not files");
        }
        for (File element : files) {
            if (storage.equals(element.getStorage())) {
                resFilesList.add(element);
            }
        }
        return files;
    }

}
