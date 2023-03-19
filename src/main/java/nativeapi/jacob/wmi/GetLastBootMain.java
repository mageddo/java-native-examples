package nativeapi.jacob.wmi;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;


/**
 * https://stackoverflow.com/questions/55651597/how-to-i-access-the-data-of-a-wmi-query-via-jna-safearray-result
 * https://stackoverflow.com/questions/72265941/calling-iwbemservices-getobject-with-jna-and-java
 * https://github.com/java-native-access/jna/pull/1474
 * https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/COM/WbemcliTest.java
 */
public class GetLastBootMain {
  public static void main(String[] args) {

    System.setProperty("jacob.dll.path", "G:\\users\\typer\\Downloads\\jacob-1.20\\jacob\\META-INF\\jacob-1.20-x64.dll");

    final var wmi = new ActiveXComponent("WbemScripting.SWbemLocator");
    // no connection parameters means to connect to the local machine
    Variant conRet = wmi.invoke("ConnectServer");
    // the author liked the ActiveXComponent api style over the Dispatch
    // style
    ActiveXComponent wmiconnect = new ActiveXComponent(conRet.toDispatch());

    // the WMI supports a query language.
    String query = "SELECT * FROM Win32_OperatingSystem";
    Variant vCollection = wmiconnect.invoke("ExecQuery", new Variant(query));
    EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());

    while (enumVariant.hasMoreElements()) {
      Dispatch item = enumVariant.nextElement().toDispatch();
      String lastBootUpTime = Dispatch.call(item, "LastBootUpTime").toString();
      System.out.println(lastBootUpTime);
    }

  }
}
