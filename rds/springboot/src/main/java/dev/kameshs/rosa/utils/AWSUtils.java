package dev.kameshs.rosa.utils;


import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AWSUtils {

  static final Pattern AWS_ROLE_ARN_REGX = Pattern.compile(
      "arn:aws:iam::(?<accountId>[\\d]+):role/(?<roleName>.*)$");

  public static Optional<String> getRoleNameFromArn(String arn) {
    String roleName = null;

    Matcher matcher = AWS_ROLE_ARN_REGX.matcher(arn);

    if (matcher.matches()) {
      roleName = matcher.group("roleName");
    }

    return Optional.ofNullable(roleName);
  }
}
