package by.backer.dao;

import by.backer.dao.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

public class UserDAOImpl implements IUserDAO {

  private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public User get(final Long id) {
    try {
      User bet = entityManager.find(User.class, id);
      return bet;
    } catch (NoResultException e) {
      return null;
    }
  }

  @Transactional
  @Override
  public void save(final User user) {
    try {
      if (StringUtils.isNotEmpty(String.valueOf(user.getId()))) {
        entityManager.merge(user);
      } else {
        entityManager.persist(user);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Transactional
  @Override
  public void update(final User user) {
    try {
      if (StringUtils.isNotEmpty(String.valueOf(user.getId()))) {
        entityManager.merge(user);
      } else {
        entityManager.persist(user);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  @Transactional
  @Override
  public void delete(final Long id) {
    User user = entityManager.find(User.class, id);
    entityManager.remove(user);
  }

  @Transactional
  @Override
  public boolean isLoginExist(final String login) {
    return entityManager.createQuery("select u from User u where u.login=:login", User.class)
        .setParameter("login", login)
        .getSingleResult()
        != null;
  }

  @Transactional
  @Override
  public User getByLogin(final String login) {
    return entityManager.createQuery("select u from  User u where u.login=:login",
        User.class)
        .setParameter("login", login)
        .getSingleResult();
  }
}
