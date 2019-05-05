package server.frontend.commands.access;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helpers {
  public static final String TABLE_NAME = "USERS";
  static final String PASS_CODE = "admin";

  public static String countHash(String source){
    return DigestUtils.sha256Hex(source);
  }
  private Helpers() {

  }
}
