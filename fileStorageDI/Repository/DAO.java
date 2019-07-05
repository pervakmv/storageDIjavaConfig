package ua.nikolay.fileStorageDI.Repository;

import ua.nikolay.fileStorageDI.Repository.Exception.InternalServerErrorException;

public interface DAO<T>  {
    public T save(T t) throws InternalServerErrorException;
    public T delete(T t)throws InternalServerErrorException;
    public T update(T t)throws InternalServerErrorException;
    public T findById(long id) throws InternalServerErrorException;
}
