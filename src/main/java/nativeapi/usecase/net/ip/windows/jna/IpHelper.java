package nativeapi.usecase.net.ip.windows.jna;

import com.sun.jna.ptr.IntByReference;
import nativeapi.utils.ExecutionException;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

import static com.sun.jna.platform.win32.WinError.ERROR_INSUFFICIENT_BUFFER;
import static com.sun.jna.platform.win32.WinError.NO_ERROR;

public class IpHelper {

  private IpHelper() {
  }

  private static Pair<MIB_IFTABLE, Integer> findNetworkInterfaces0(int tableSizeInBytes) {
    final var ipHlpApi = IPHelperApi.INSTANCE;
    final var table = MIB_IFTABLE.fromBytesSize(tableSizeInBytes);
    final var inOutTableSize = new IntByReference(table.size());
    final int status = ipHlpApi.GetIfTable(table, inOutTableSize, false);
    if (status == NO_ERROR) {
      return Pair.of(table, null);
    } else if (status == ERROR_INSUFFICIENT_BUFFER) {
      return Pair.of(table, inOutTableSize.getValue() - 4);
    }
    throw new ExecutionException(status);
  }

  public static MIB_IFTABLE findNetworkInterfaces() {
    final var r = findNetworkInterfaces0(-1);
    if (r.getValue() == null) {
      return r.getKey();
    }
    final var r2 = findNetworkInterfaces0(r.getValue());
    Validate.isTrue(!Objects.equals(r2.getValue(), ERROR_INSUFFICIENT_BUFFER), "Can't get error insufficient twice");
    return r2.getKey();
  }
}
