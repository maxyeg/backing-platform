package by.backer.dao;

public interface IDAO<T> {
  T get(Long id);

  void save(T entity);

  void update(T entity);

  void delete(Long id);
}
