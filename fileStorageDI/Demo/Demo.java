package ua.nikolay.fileStorageDI.Demo;

import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Model.File;

import ua.nikolay.fileStorageDI.Repository.FileDAO;
import ua.nikolay.fileStorageDI.Repository.StorageDAO;


import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        StorageDAO storageDAO = new StorageDAO();

        FileDAO fileDAO = new FileDAO();


       // List<Storage> storages = storageDAO.storageList();
        //System.out.println(storages);

//        List<File> files = fileDAO.fileList();
//        System.out.println(files);
  //      System.out.println(fileDAO.findById(204));

        List<File> files = fileDAO.fileList();
         for(File element : files){
             System.out.println(element.getId());
         }
        System.out.println(storageDAO.findById(3));
        System.out.println(fileDAO.findById(5));
        System.out.println(fileDAO.findById(200));


       // System.out.println(fileDAO.findById(1));
       // System.out.println(fileDAO.findById(200));

    }
}
