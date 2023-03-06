package nativeapi.jna.wmi;

import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.IntByReference;

/**
 * https://stackoverflow.com/questions/55651597/how-to-i-access-the-data-of-a-wmi-query-via-jna-safearray-result
 * https://stackoverflow.com/questions/72265941/calling-iwbemservices-getobject-with-jna-and-java
 * https://github.com/java-native-access/jna/pull/1474
 * https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/COM/WbemcliTest.java
 */
public class GetLastBootMain {
  // 20230305170531.500000-180
  public static void main(String[] args) {

    Ole32.INSTANCE.CoInitialize(null);

//    Wbemcli.IWbemServices svc = WbemcliUtil.connectServer("ROOT\\CIMV2");
    Wbemcli.IWbemServices svc = WbemcliUtil.connectServer(WbemcliUtil.DEFAULT_NAMESPACE);
//    Wbemcli.IEnumWbemClassObject enumerator = svc.ExecQuery("wmic", "wmic nicconfig where (IPEnabled=TRUE)", Wbemcli.WBEM_FLAG_FORWARD_ONLY, null);
    Wbemcli.IEnumWbemClassObject enumerator = svc.ExecQuery("wql", "SELECT * FROM Win32_OperatingSystem", Wbemcli.WBEM_FLAG_FORWARD_ONLY, null);
    try {
      while (true) {
        final var items = enumerator.Next(0, 1);
        for (Wbemcli.IWbemClassObject o : items) {
          Variant.VARIANT.ByReference pVal = new Variant.VARIANT.ByReference();
          IntByReference pType = new IntByReference();
          IntByReference plFlavor = new IntByReference();
          COMUtils.checkRC(o.Get("LastBootUpTime", 0, pVal, pType, plFlavor));
          System.out.println(pVal.getValue());
        }
        if (items.length == 0) {
          break;
        }
      }
    } finally {
      enumerator.Release();
    }

  }
}
