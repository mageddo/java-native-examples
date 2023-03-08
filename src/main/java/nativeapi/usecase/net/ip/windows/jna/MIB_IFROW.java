package nativeapi.usecase.net.ip.windows.jna;

import com.sun.jna.Structure;
import nativeapi.utils.Fields;

import java.util.List;

/**
 * https://learn.microsoft.com/en-us/windows/win32/api/ifmib/ns-ifmib-mib_ifrow
 */
public class MIB_IFROW extends Structure {

  public char wszName[] = new char[256];
  public int dwIndex;
  public int dwType;
  public int dwMtu;
  public int dwSpeed;
  public int dwPhysAddrLen;
  public byte bPhysAddr[] = new byte[8];
  public int dwAdminStatus;
  public int dwOperStatus;
  public int dwLastChange;
  public int dwInOctets;
  public int dwInUcastPkts;
  public int dwInNUcastPkts;
  public int dwInDiscards;
  public int dwInErrors;
  public int dwInUnknownProtos;
  public int dwOutOctets;
  public int dwOutUcastPkts;
  public int dwOutNUcastPkts;
  public int dwOutDiscards;
  public int dwOutErrors;
  public int dwOutQLen;
  public int dwDescrLen;

  public byte bDescr[] = new byte[256];

  @Override
  protected List<String> getFieldOrder() {
    return Fields.findNonStaticFieldNames(getClass());
  }
}
