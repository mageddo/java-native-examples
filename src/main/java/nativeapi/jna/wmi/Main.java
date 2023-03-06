package nativeapi.jna.wmi;

import com.sun.jna.StringArray;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

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
    Ole32.INSTANCE.CoInitialize(null);
    Wbemcli.IWbemServices svc = WbemcliUtil.connectServer(WbemcliUtil.DEFAULT_NAMESPACE);
    final var extendedService = new IWbemServicesExtended(svc);
    final var enumerator = svc.ExecQuery("wql", "SELECT * FROM Win32_NetworkAdapterConfiguration WHERE (IPEnabled=TRUE)", Wbemcli.WBEM_FLAG_FORWARD_ONLY, null);

    try {
      while (true) {
        final Wbemcli.IWbemClassObject[] items = enumerator.Next(0, 1);
        if (items.length == 0) {
          break;
        }
        for (final Wbemcli.IWbemClassObject o : items) {
          try {
            Variant.VARIANT.ByReference pVal = new Variant.VARIANT.ByReference();
            IntByReference pType = new IntByReference();
            IntByReference plFlavor = new IntByReference();

            COMUtils.checkRC(o.Get("Caption", 0, pVal, pType, plFlavor));
            System.out.println(pVal.getValue());

            COMUtils.checkRC(o.Get("Description", 0, pVal, pType, plFlavor));
            System.out.println(pVal.getValue());

            COMUtils.checkRC(o.Get("SettingID", 0, pVal, pType, plFlavor));
            System.out.println(pVal.getValue());

            COMUtils.checkRC(o.Get("DNSServerSearchOrder", 0, pVal, pType, plFlavor));
            OaIdl.SAFEARRAY values = (OaIdl.SAFEARRAY) pVal.getValue();
            for (int i = values.getLBound(0); i <= values.getUBound(0); i++) {
              final var v = values.getElement(i);
              System.out.printf("%s: %s%n", v.getClass(), v);
            }
//            final var pid = Kernel32.INSTANCE.GetCurrentProcessId();
            final var objectPath = String.format("\\\\.\\%s:Win32_NetworkAdapterConfiguration", WbemcliUtil.DEFAULT_NAMESPACE);

            final var dnsServers = new String[]{ "8.8.8.8" };
            final var stringArray = new StringArray(dnsServers);

            // this property needs to be better configured using __PARAMETERS class
            // https://learn.microsoft.com/en-us/windows/win32/wmisdk/creating-parameters-objects-in-c--
            final var inParams = new Wbemcli.IWbemClassObject(stringArray);



//            final var inParams = new PointerByReference();

            final var outParams = new PointerByReference();
            final var callResult = new PointerByReference();

            // https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/setdnsserversearchorder-method-in-class-win32-networkadapterconfiguration
            // uint32 SetDNSServerSearchOrder(
            //  [in] string DNSServerSearchOrder[]
            //);

            // todo free sysalloc string
            COMUtils.checkRC(extendedService.ExecMethod(
              OleAuto.INSTANCE.SysAllocString(objectPath),
              OleAuto.INSTANCE.SysAllocString("SetDNSServerSearchOrder"),
              Wbemcli.WBEM_FLAG_RETURN_IMMEDIATELY,
              null, // ctx
              inParams,
              null,
              null
//              outParams,
//              callResult
            ));
//            String object_path = "\\\\LAPTOP-R89KG6V1\\root\\cimv2:Win32_Process.Handle="2588";


          } finally {
            o.Release();
          }
        }

      }
    } finally {
      enumerator.Release();
    }


  }
}
