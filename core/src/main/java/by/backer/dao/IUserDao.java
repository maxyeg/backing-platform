package by.backer.dao;

import by.backer.dao.entity.User;

public interface IUserDAO extends IDAO<User> {
  boolean isLoginExist(String login);

  User getByLogin(String login);
}
