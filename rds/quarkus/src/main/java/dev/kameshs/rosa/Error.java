package dev.kameshs.rosa;

public class Error {

  public int status;
  public String message;
//
//  public static Error fromAwsErrorDetails(AwsErrorDetails awsErrorDetails) {
//    Error err = new Error();
//    SdkHttpResponse sdkHttpResponse = awsErrorDetails.sdkHttpResponse();
//    err.status = sdkHttpResponse != null ? sdkHttpResponse.statusCode()
//      : HttpStatus.SC_INTERNAL_SERVER_ERROR;
//    err.message = awsErrorDetails.errorMessage();
//    return err;
//  }

  public static Error internalServerError(Exception e) {
    Error err = new Error();
    err.status = 500;
    err.message = e.getMessage();
    return err;
  }
}
