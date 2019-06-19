package ua.nikolay.fileStorageDI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.FileDAO;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileDAO fileDAO;

    public File save(File file){
        return fileDAO.save(file);
    }

    public File delete(File file){
        return fileDAO.delete(file);
    }

    public File update(File file){
        return fileDAO.update(file);
    }


    public File findById(long id){
        return fileDAO.findById(id);
    }

    public List<File> fileList(){
        return fileDAO.fileList();
    }

    public void fileExist(File file) throws Exception{
        fileDAO.fileExist(file);
    }

    public void saveFileList(List<File>files, Storage storage){
        fileDAO.saveFileList(files, storage);
    }

    public List<File> readFileList(Storage storage) throws Exception{
        return fileDAO.readFileList(storage);
    }


    public void validateToDelete(Storage storage, File file) throws Exception{
        File fileFromBase = fileDAO.findById(file.getId());
        if (fileFromBase == null) {
            throw new Exception("The file with id:" + file.getId() + " is not exist.");
        }
        if (!storage.equals(fileFromBase.getStorage())) {
            throw new Exception("The storage with id:" + storage.getId() + " does not have faile with id:" + file.getId());
        }
    }

}
