package by.backer.utils;

import by.backer.dao.entity.User;
import by.backer.model.UserDTO;

public class EntityConverter {
  public static User convert(final UserDTO dto) {
    User entity = new User();
    entity.setLogin(dto.getLogin());
    entity.setEmail(dto.getEmail());
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setPassword(dto.getPassword());
    entity.setSalt(dto.getSalt());
    return entity;
  }

  public static UserDTO convert(final User entity) {
    UserDTO dto = new UserDTO();
    dto.setId(String.valueOf(entity.getId()));
    dto.setLogin(entity.getLogin());
    dto.setFirstName(entity.getFirstName());
    dto.setLastName(entity.getLastName());
    dto.setEmail(entity.getEmail());
    return dto;
  }
}
