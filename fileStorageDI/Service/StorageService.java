package ua.nikolay.fileStorageDI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.FileDAO;
import ua.nikolay.fileStorageDI.Repository.StorageDAO;

import java.util.List;

@Service
public class StorageService {
    @Autowired
    private StorageDAO storageDAO;
    @Autowired
    private FileDAO fileDAO;

    public Storage save(Storage storage) {
        return storageDAO.save(storage);
    }

    public Storage delete(Storage storage) {
        return storageDAO.delete(storage);
    }

    public Storage update(Storage storage) {
        return storageDAO.update(storage);
    }


    public Storage findById(long id) {
        return storageDAO.findById(id);
    }

    public List<Storage> storageList() {
        return storageDAO.storageList();
    }

    public void storageExist(Storage storage) throws Exception {
        storageDAO.storageExist(storage);
    }

    public void validatePutFile(Storage storage, File file) throws Exception {
        if (file.getStorage() != null) {
            if (file.getStorage().equals(storage))
                throw new Exception("File with id: " + file.getId() + " already in storage " + storage.getId());
            else {
                throw new Exception("File with id: " + file.getId() + " in storage with id :" + storage.getId());
            }
        }
        checkFormat(storage, file);
        storageDAO.storageExist(storage);
        fileDAO.fileExist(file);
        long size = file.getSize();
        if (size > freeSize(storage))
            throw new Exception("No free space in storage id: " + storage.getId() + "for file with id:" + file.getId());
    }


    public void checkFormat(Storage inStorage, File inFile) throws Exception {
        boolean ok = false;
        for (String str : inStorage.formatAsArray()) {
            if (str.equals(inFile.getFormat())) {
                ok = true;
                break;
            }
        }
        if (ok != true)
            throw new Exception("From of file with id: " + inFile.getId() + " not supported");
    }


    public void validateFilesForStorage(List<File> files, Storage storage) throws Exception {
        //Проверяем допустимость формата
        long size = 0;
        for (File file : files) {
            size += file.getSize();
            checkFormat(storage, file);
        }
        if (size > freeSize(storage))
            throw new Exception("No free space in storage id: " + storage.getId() + "for file transfer files");
    }


    public long freeSize(Storage storage) {
        List<File> files = fileDAO.fileList();
        long sum = 0;
        for (File element : files) {
            if (element.getStorage() != null) {
                if (element.getStorage().equals(storage)) {
                    sum += element.getSize();
                }
            }
        }
        long freeSize = storage.getStorageMaxSize() - sum;
        return freeSize > 0 ? freeSize : 0;
    }

    public File validFileToTransfer(Storage storageFrom, Storage storageTo, long id) throws Exception {
        File file = fileDAO.findById(id);
        if (file == null)
            throw new Exception("File with id: " + file.getId() + " is not exist in the storage with id: " + storageFrom.getId());
        checkFormat(storageTo, file);
        if (file.getSize() > freeSize(storageTo))
            throw new Exception("No free space in storage id: " + storageTo.getId() + "for file transfer files");

        return file;

    }

}
