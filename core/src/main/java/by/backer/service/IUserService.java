package by.backer.service;

import by.backer.model.CredentialsDTO;
import by.backer.model.UserDTO;
import by.backer.model.exception.BackerException;

public interface IUserService {
  public static final String REST_URL = "/rest/user";

  UserDTO login(CredentialsDTO credentials) throws BackerException;

  UserDTO register(UserDTO registrationInfoDTO) throws BackerException;
}
