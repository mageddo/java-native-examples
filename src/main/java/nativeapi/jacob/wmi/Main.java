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


    System.setProperty("jacob.dll.path", "G:\\users\\typer\\Downloads\\jacob-1.20\\jacob\\META-INF\\jacob-1.20-x64.dll");

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

      System.out.println(Dispatch.call(item, "SetDNSServerSearchOrder", stringArrayTo("8.8.8.8")).toString());
      System.out.println(Dispatch.call(item, "SetDynamicDNSRegistration", new Variant(false)).toString());

    }

//    ComThread.InitMTA();
//    try {
//      ActiveXComponent wmi2 = new ActiveXComponent("winmgmts:\\\\.");
//      Variant instances = wmi2.invoke("InstancesOf", "Win32_NetworkAdapterConfiguration");
//      Enumeration<Variant> en = new EnumVariant(instances.getDispatch());
//      while (en.hasMoreElements()) {
//        final var o = en.nextElement().getDispatch();
//        ActiveXComponent bb = new ActiveXComponent(o);
//
////        final var servers = new Variant[]{new Variant("8.8.8.8")};
//        final var r = Dispatch.call(bb, "SetDNSServerSearchOrder", new String[]{"8.8.8.8"}).toString();
//
//        break;
//      }
//    } finally {
//      ComThread.Release();
//    }


//    new Dispatch("Win32_NetworkAdapterConfiguration");

//    final var o = wmiconnect.invoke(
//      "ExecQuery", new Variant("SELECT * FROM meta_class WHERE __class = 'Win32_NetworkAdapterConfiguration'")
//    );
//    final var servers = new Variant[]{new Variant("8.8.8.8")};
//
//    final var r = Dispatch.call(o.toDispatch(), "SetDNSServerSearchOrder", servers).toString();
//    EnumVariant enums = new EnumVariant(o.toDispatch());
//    while (enums.hasMoreElements()) {
//
//      Dispatch item = enumVariant.nextElement().toDispatch();
//    }


//    final var pid = Kernel32.INSTANCE.GetCurrentProcessId();
//    final var objectPath = String.format("\\\\.\\%s:Win32_NetworkAdapterConfiguration", WbemcliUtil.DEFAULT_NAMESPACE);

//    final var o = new ActiveXComponent(objectPath);


//    final var servers = new Variant[]{new Variant("8.8.8.8")};
//    final var r = Dispatch.call(o, "SetDNSServerSearchOrder", servers).toString();
//    System.out.println("->>>>>>>>>>>>");
//    System.out.println(r);


//            COMUtils.checkRC(extendedService.ExecMethod(
//              OleAuto.INSTANCE.SysAllocString(objectPath),
//              OleAuto.INSTANCE.SysAllocString("SetDNSServerSearchOrder"),
//              Wbemcli.WBEM_FLAG_RETURN_IMMEDIATELY,
//              null, // ctx
//              inParams,
//              null,
//              null
////              outParams,
////              callResult
//            ));


//
//            // this property needs to be better configured using __PARAMETERS class
//            // https://learn.microsoft.com/en-us/windows/win32/wmisdk/creating-parameters-objects-in-c--
//            final var inParams = new Wbemcli.IWbemClassObject(stringArray);
//
//
//
////            final var inParams = new PointerByReference();
//
//            final var outParams = new PointerByReference();
//            final var callResult = new PointerByReference();
//
//            // https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/setdnsserversearchorder-method-in-class-win32-networkadapterconfiguration
//            // uint32 SetDNSServerSearchOrder(
//            //  [in] string DNSServerSearchOrder[]
//            //);
//
//            // todo free sysalloc string
//            COMUtils.checkRC(extendedService.ExecMethod(
//              OleAuto.INSTANCE.SysAllocString(objectPath),
//              OleAuto.INSTANCE.SysAllocString("SetDNSServerSearchOrder"),
//              Wbemcli.WBEM_FLAG_RETURN_IMMEDIATELY,
//              null, // ctx
//              inParams,
//              null,
//              null
////              outParams,
////              callResult
//            ));
////            String object_path = "\\\\LAPTOP-R89KG6V1\\root\\cimv2:Win32_Process.Handle="2588";
//
//
//          } finally {
//            o.Release();
//          }
//        }
//
//      }
//    } finally {
//      enumerator.Release();
//    }


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
