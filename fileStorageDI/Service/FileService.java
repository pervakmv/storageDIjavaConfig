package ua.nikolay.fileStorageDI.Service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;
import ua.nikolay.fileStorageDI.Repository.FileDAO;
import ua.nikolay.fileStorageDI.Service.Exception.ValidateException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileDAO fileDAO;

    public File save(File file) throws InternalServerErrorException {
        return fileDAO.save(file);
    }

    public File delete(File file) throws InternalServerErrorException{
        return fileDAO.delete(file);
    }

    public File update(File file) throws InternalServerErrorException{
        return fileDAO.update(file);
    }


    public File findById(long id) throws InternalServerErrorException{
        return fileDAO.findById(id);
    }

    public List<File> fileList() throws InternalServerErrorException{
        return fileDAO.fileList();
    }

    public void fileExist(File file) throws Exception{
        fileDAO.fileExist(file);
    }

    public void saveFileList(List<File>files, Storage storage) throws InternalServerErrorException{
        fileDAO.saveFileList(files, storage);
    }

    public List<File> readFileList(Storage storage) throws InternalServerErrorException{
        return fileDAO.readFileList(storage);
    }


    public void validateToDelete(Storage storage, File file) throws ValidateException {
        File fileFromBase = null;
        try{
             fileFromBase = fileDAO.findById(file.getId());
        }catch(InternalServerErrorException e){
            throw new ValidateException(e.getMessage());
        }

        if (fileFromBase == null) {
            throw new ValidateException("The file with id:" + file.getId() + " is not exist.");
        }
        if (!storage.equals(fileFromBase.getStorage())) {
            throw new ValidateException("The storage with id:" + storage.getId() + " does not have faile with id:" + file.getId());
        }
    }

}
