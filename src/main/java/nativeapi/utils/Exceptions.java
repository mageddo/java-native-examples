package nativeapi.utils;

public class Exceptions {
  public static ExecutionException doThrow(Number returnCode){
    return new ExecutionException(returnCode);
  }
}
