package ua.nikolay.fileStorageDI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.FileDAO;
import ua.nikolay.fileStorageDI.Repository.StorageDAO;
import ua.nikolay.fileStorageDI.Service.FileService;
import ua.nikolay.fileStorageDI.Service.StorageService;

@Configuration
public class Application {


    @Bean
    FileService fileService() {
        return new FileService();
    }

    @Bean
    StorageService storageService() {
        return new StorageService();
    }

    @Bean
    StorageDAO storageDAO() {
        return new StorageDAO();
    }

    @Bean
    FileDAO fileDAO() {
        return new FileDAO();
    }

}
