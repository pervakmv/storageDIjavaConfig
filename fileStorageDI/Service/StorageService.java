package ua.nikolay.fileStorageDI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;
import ua.nikolay.fileStorageDI.Repository.FileDAO;
import ua.nikolay.fileStorageDI.Repository.StorageDAO;
import ua.nikolay.fileStorageDI.Service.Exception.ValidateException;

import java.util.List;

@Service
public class StorageService {
    @Autowired
    private StorageDAO storageDAO;
    @Autowired
    private FileDAO fileDAO;

    public Storage save(Storage storage) throws InternalServerErrorException {
        return storageDAO.save(storage);
    }

    public Storage delete(Storage storage)throws InternalServerErrorException {
        return storageDAO.delete(storage);
    }

    public Storage update(Storage storage)throws InternalServerErrorException {
        return storageDAO.update(storage);
    }


    public Storage findById(long id)throws InternalServerErrorException {
        return storageDAO.findById(id);
    }

    public List<Storage> storageList() throws InternalServerErrorException {
        return storageDAO.storageList();
    }

    public void storageExist(Storage storage) throws ValidateException {
        storageDAO.storageExist(storage);
    }

    public void validatePutFile(Storage storage, File file) throws ValidateException {
        if (file.getStorage() != null) {
            if (file.getStorage().equals(storage))
                throw new ValidateException("File with id: " + file.getId() + " already in storage " + storage.getId());
            else {
                throw new ValidateException("File with id: " + file.getId() + " in storage with id :" + storage.getId());
            }
        }
        try {
            checkFormat(storage, file);

        } catch (InternalServerErrorException e) {
            throw new ValidateException(e.getMessage());
        }

        storageDAO.storageExist(storage);
        fileDAO.fileExist(file);
        long size = file.getSize();
        long free = 0;
        try {
            free = freeSize(storage);
        } catch (InternalServerErrorException e) {
            throw new ValidateException(e.getMessage());
        }

        if (size > free)
            throw new ValidateException("No free space in storage id: " + storage.getId() + "for file with id:" + file.getId());
    }


    public void checkFormat(Storage inStorage, File inFile) throws InternalServerErrorException {
        boolean ok = false;
        for (String str : inStorage.formatAsArray()) {
            if (str.equals(inFile.getFormat())) {
                ok = true;
                break;
            }
        }
        if (ok != true)
            throw new InternalServerErrorException("From of file with id: " + inFile.getId() + " not supported");
    }


    public void validateFilesForStorage(List<File> files, Storage storage) throws InternalServerErrorException {
        //Проверяем допустимость формата
        long size = 0;
        for (File file : files) {
            size += file.getSize();
            checkFormat(storage, file);
        }
        if (size > freeSize(storage))
            throw new InternalServerErrorException("No free space in storage id: " + storage.getId() + "for file transfer files");
    }


    public long freeSize(Storage storage) throws InternalServerErrorException {

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

    public File validFileToTransfer(Storage storageFrom, Storage storageTo, long id) throws InternalServerErrorException {
        File file = fileDAO.findById(id);
        if (file == null)
            throw new InternalServerErrorException("File with id: " + file.getId() + " is not exist in the storage with id: " + storageFrom.getId());
        checkFormat(storageTo, file);
        if (file.getSize() > freeSize(storageTo))
            throw new InternalServerErrorException("No free space in storage id: " + storageTo.getId() + "for file transfer files");

        return file;

    }

}
