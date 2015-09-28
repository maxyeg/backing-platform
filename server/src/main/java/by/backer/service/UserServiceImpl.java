package by.backer.service;

import by.backer.dao.IUserDAO;
import by.backer.dao.entity.User;
import by.backer.model.CredentialsDTO;
import by.backer.model.UserDTO;
import by.backer.model.exception.BackerException;
import by.backer.utils.EntityConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

@Controller("userService")
@RequestMapping(IUserService.REST_URL)
public class UserServiceImpl implements IUserService {

  private static final int ITERATIONS_NUMBER = 1000;
  @Autowired
  IUserDAO userDAO;

  @Autowired
  MailSender mailSender;

  @Override
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public UserDTO login(@RequestBody final CredentialsDTO credentials) throws BackerException {
    if (StringUtils.isEmpty(credentials.getLogin()) || StringUtils.isEmpty(credentials.getPassword())) {
      throw new BackerException("Credentials can't be empty.");
    }
    User user = userDAO.getByLogin(credentials.getLogin());
    if (user == null) {
      throw new BackerException("No such login.");
    } else {
      byte[] salt = new byte[0];
      try {
        salt = base64ToByte(user.getSalt());
        byte[] hashedPassword = getHash(ITERATIONS_NUMBER, credentials.getPassword(), salt);
        if (Arrays.equals(hashedPassword, base64ToByte(user.getPassword()))) {
          return EntityConverter.convert(user);
        } else {
          throw new BackerException("Password doesn't match.");
        }
      } catch (IOException e) {
        throw new BackerException(e.getMessage());
      }
    }
  }

  @Override
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public UserDTO register(@RequestBody final UserDTO user) throws BackerException {
    if (user != null && StringUtils.isEmpty(user.getId()) && StringUtils.isNotEmpty(user.getLogin())) {
      try {
//            if (StringUtils.isNotEmpty(registrationInfoDTO.getUser().getEmail())) {
//                createValidationToken();
//                sendActivationEmail(registrationInfoDTO.getUser());
        String password = user.getPassword();
        byte[] salt = getNextSalt();
        byte[] hashedPassword = getHash(ITERATIONS_NUMBER, password, salt);
        user.setPassword(byteToBase64(hashedPassword));
        user.setSalt(byteToBase64(salt));
        userDAO.save(EntityConverter.convert(user));
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setLogin(user.getLogin());
        credentialsDTO.setPassword(password);
        return login(credentialsDTO);
//            } else {
//                throw new BackerException("User doesn't have email. We can't send activation email.");
//            }
      } catch (NoSuchAlgorithmException e) {
        throw new BackerException(e.getMessage());
      }
    } else {
      throw new BackerException("User object is null or doesn't have login or you specified id parameter.");
    }
  }

  private byte[] getHash(final int iterationNumber, final String password, final byte[] salt) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.reset();
      md.update(salt);
      byte[] input = md.digest(password.getBytes("UTF-8"));
      for (int i = 0; i < iterationNumber; i++) {
        md.reset();
        input = md.digest(input);
      }
      return input;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  private byte[] getNextSalt() throws NoSuchAlgorithmException {
    // Uses a secure Random not a simple Random
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    // Salt generation 64 bits long
    byte[] bSalt = new byte[8];
    random.nextBytes(bSalt);
    return bSalt;
  }

  public static byte[] base64ToByte(String data) throws IOException {
    BASE64Decoder decoder = new BASE64Decoder();
    return decoder.decodeBuffer(data);
  }

  public static String byteToBase64(byte[] data){
    BASE64Encoder endecoder = new BASE64Encoder();
    return endecoder.encode(data);
  }

  private void createValidationToken() {
    //TODO
  }

  private void sendActivationEmail(final UserDTO user) {
    mailSender.send(createEmail(user));
  }

  private SimpleMailMessage createEmail(final UserDTO user) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("egoshinme@gmail.com");
    message.setTo(user.getEmail());
    message.setSubject("User account activation in Tipster App.");
    //TODO: add token and link.
    message.setText("Please activate your account.");
    mailSender.send(message);
    return message;
  }

}
