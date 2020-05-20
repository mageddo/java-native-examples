package nativeapi.libfinder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LibraryFinder {

  public static String getFullLibPath(String relativePath) {
    return getFullLibPath(Paths.get(relativePath));
  }

  public static String getFullLibPath(Path relativePath) {
    final String foundPath = LibraryFinder.class
        .getResource(String.format("/lib%s", relativePath))
        .getPath();
    return foundPath;
  }
}
