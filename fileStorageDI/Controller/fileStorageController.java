package ua.nikolay.fileStorageDI.Controller;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.nikolay.fileStorageDI.Model.File;
import ua.nikolay.fileStorageDI.Model.Storage;
import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;
import ua.nikolay.fileStorageDI.Service.FileService;
import ua.nikolay.fileStorageDI.Service.StorageService;


import java.io.IOException;

import java.util.List;

@Controller
public class fileStorageController {
    @Autowired
    private FileService fileService;
    @Autowired
    private StorageService storageService;

    @RequestMapping(method = RequestMethod.POST, value = "/addfile", produces = "text/plain")
    public String addFile(@RequestBody String file) throws InternalServerErrorException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File newFile = objectMapper.readValue(file, File.class);
            newFile.setId((long) 0);
            fileService.save(newFile);
        } catch (JsonProcessingException e) {
            System.out.println("Exception Occured whiile converting the Json into java" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception Occured whiile converting the Json into java" + e.getMessage());
        }
        return file;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fileList", produces = "text/plain")
    public @ResponseBody
    List<File> fileList()throws InternalServerErrorException {
        List<File> resList = fileService.fileList();
        System.out.println(resList);
        return resList;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addstorage", produces = "text/plain")
    public String addStorage(@RequestBody String storage) throws InternalServerErrorException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Storage newStorage = objectMapper.readValue(storage, Storage.class);
            newStorage.setId((long) 0);
            storageService.save(newStorage);
        } catch (JsonProcessingException e) {
            System.out.println("Exception Occured while converting the Json into java" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception Occured while converting the Json into java" + e.getMessage());
        }
        return storage;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/put", produces = "text/plain")
    public void putMapInputData(@RequestBody String inputString) {
        ObjectMapper objectMapper = new ObjectMapper();
        int endStorageStringIndex = inputString.indexOf("}");
        String storage = inputString.substring(0, endStorageStringIndex + 1);
        String file = inputString.substring(endStorageStringIndex + 1);

        try {
            Storage newStorage = objectMapper.readValue(storage, Storage.class);
            System.out.println(newStorage);

            File newFile = objectMapper.readValue(file, File.class);
            put(newStorage, newFile);
        } catch (JsonProcessingException e) {
            System.out.println("Exception Occured while converting the storage string into java" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception Occured while put the file " + file + "into the storage " + storage + e.getMessage());
        }

    }


    public void put(Storage storage, File file) throws Exception {

        storageService.validatePutFile(storage, file);
        file.setStorage(storage);
        fileService.update(file);
    }


    public void delete(Storage storage, File file) throws Exception {
        fileService.validateToDelete(storage, file);
        file.setStorage(null);
        fileService.update(file);
    }

    public void transferall(Storage storageFrom, Storage storageTo) throws Exception {
        List<File> files = fileService.readFileList(storageFrom);
        //Валидация переноса
        storageService.validateFilesForStorage(files, storageTo);
        fileService.saveFileList(files, storageTo);

    }

    public void transferFile(Storage storageFrom, Storage storageTo, long id) throws Exception {
        File file = storageService.validFileToTransfer(storageFrom, storageTo, id);
        file.setStorage(storageTo);
        fileService.update(file);
    }
}
