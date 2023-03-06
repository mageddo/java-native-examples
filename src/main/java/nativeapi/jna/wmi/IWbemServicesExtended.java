package nativeapi.jna.wmi;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.Wbemcli;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import org.apache.commons.lang3.reflect.MethodUtils;

public class IWbemServicesExtended {

  private final Wbemcli.IWbemServices delegate;

  public IWbemServicesExtended(Wbemcli.IWbemServices delegate) {
    this.delegate = delegate;
  }

  /**
   * https://github.com/tpn/winsdk-10/blob/9b69fd26ac0c7d0b83d378dba01080e93349c2ed/Include/10.0.10240.0/um/WbemCli.h#L2123
   * https://learn.microsoft.com/en-us/windows/win32/api/wbemcli/nf-wbemcli-iwbemservices-execmethod
   * https://stackoverflow.com/questions/72265941/calling-iwbemservices-getobject-with-jna-and-java
   * HRESULT ExecMethod(
   * [in]  const BSTR       strObjectPath,
   * [in]  const BSTR       strMethodName,
   * [in]  long             lFlags,
   * [in]  IWbemContext     *pCtx,
   * [in]  IWbemClassObject *pInParams,
   * [out] IWbemClassObject **ppOutParams,
   * [out] IWbemCallResult  **ppCallResult
   * );
   */
  public WinNT.HRESULT ExecMethod(
    WTypes.BSTR strObjectPath,
    WTypes.BSTR strMethodName,
    int lFlags,
    Wbemcli.IWbemContext pCtx,
    Object pInParams,
    PointerByReference ppOutParams,
    PointerByReference ppCallResult
  ) {
    return (WinNT.HRESULT) this._invokeNativeObject(
      24,
      new Object[]{
        this.delegate.getPointer(),
        strObjectPath,
        strMethodName,
        lFlags,
        pCtx,
        pInParams,
        ppOutParams,
        ppCallResult
      },
      WinNT.HRESULT.class
    );
  }

  private Object _invokeNativeObject(int vtableId, Object[] args, Class<?> returnType) {
    try {
      return MethodUtils.invokeMethod(this.delegate, true, "_invokeNativeObject", vtableId, args, returnType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
