package ua.nikolay.fileStorageDI.Repository;

public interface DAO<T> {
    public T save(T t);
    public T delete(T t);
    public T update(T t);
    public T findById(long id);
}
