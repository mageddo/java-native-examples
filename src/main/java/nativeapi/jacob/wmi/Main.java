package nativeapi.jacob.wmi;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;

/**
 * https://community.spiceworks.com/topic/405339-replace-static-dns-settings-with-wmi-and-powershell
 * https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapterconfiguration
 * https://learn.microsoft.com/en-us/archive/msdn-magazine/2017/march/introducing-the-safearray-data-structure
 * https://stackoverflow.com/questions/55651597/how-to-i-access-the-data-of-a-wmi-query-via-jna-safearray-result
 * https://stackoverflow.com/questions/26745617/win32com-client-dispatch-cherrypy-coinitialize-has-not-been-called
 * https://learn.microsoft.com/en-us/windows/win32/wmisdk/describing-a-class-object-path
 * https://github.com/tpn/winsdk-10/blob/9b69fd26ac0c7d0b83d378dba01080e93349c2ed/Include/10.0.10240.0/um/WbemCli.h#L1959
 * https://github.com/tpn/winsdk-10/blob/9b69fd26ac0c7d0b83d378dba01080e93349c2ed/Include/10.0.10240.0/um/WbemCli.h#L2123
 * 25 ExecMethod
 */
public class Main {
  //
  public static void main(String[] args) {

    final var wmi = new ActiveXComponent("WbemScripting.SWbemLocator");
    // no connection parameters means to connect to the local machine
    Variant conRet = wmi.invoke("ConnectServer");
    // the author liked the ActiveXComponent api style over the Dispatch
    // style
    ActiveXComponent wmiconnect = new ActiveXComponent(conRet.toDispatch());

    // the WMI supports a query language.
    String query = "SELECT * FROM Win32_NetworkAdapterConfiguration WHERE (IPEnabled=TRUE)";
    Variant vCollection = wmiconnect.invoke("ExecQuery", new Variant(query));
    EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());

    while (enumVariant.hasMoreElements()) {

      Dispatch item = enumVariant.nextElement().toDispatch();

      System.out.println(Dispatch.call(item, "Caption").toString());
      System.out.println(Dispatch.call(item, "Description").toString());
      System.out.println(Dispatch.call(item, "SettingID").toString());
      System.out.println(Dispatch.call(item, "DNSServerSearchOrder").toString());

      System.out.println(ComUtils.checkRC(Dispatch.call(item, "SetDNSServerSearchOrder", stringArrayTo("8.8.8.8"))));
      System.out.println(ComUtils.checkRC(Dispatch.call(item, "SetDynamicDNSRegistration", new Variant(false))));

    }

  }

  // https://sourceforge.net/p/jacob-project/discussion/375946/thread/991feab7/#9bd0
  private static SafeArray stringArrayTo(final String ... values) {

    SafeArray v = new SafeArray(Variant.VariantString, 1);
    v.setString(0, values[0]);

    final var vari = new Variant(Variant.VariantArray,true);
    vari.putSafeArrayRef(v);
    return v;
  }
}
