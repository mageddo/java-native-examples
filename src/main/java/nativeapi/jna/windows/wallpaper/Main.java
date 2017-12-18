package nativeapi.jna.windows.wallpaper;

import com.sun.jna.Native;

/**
 * @deprecated it's not working yet
 */
@Deprecated
public class Main {

	public interface User32 extends com.sun.jna.Library {

		User32 INSTANCE = (User32) com.sun.jna.Native.loadLibrary("user32", User32.class, com.sun.jna.win32.W32APIOptions.DEFAULT_OPTIONS);

		//		BOOL WINAPI SystemParametersInfo(
//			_In_    UINT  uiAction,
//			_In_    UINT  uiParam,
//			_Inout_ PVOID pvParam,
//			_In_    UINT  fWinIni
//		);
		boolean SystemParametersInfo(int uiAction, int uiParam, Object pvParam, int fWinIni);
	}

	public static void main(String[] args) {

//		Memory m = new Memory(255 * 8);
//		Pointer pointer = m.getPointer(0);
		byte[] o = new byte[255];
		System.out.println(User32.INSTANCE.SystemParametersInfo(0x0073, 255, o, 1));
//		System.out.println(pointer.getString(0));
//		System.out.println(Arrays.toString(o));
		System.out.println(new String(o));
		System.out.println(Native.getLastError());
		System.out.println(User32.INSTANCE.SystemParametersInfo(0x0014, 0, "C:\\Users\\Public\\Pictures\\Sample Pictures\\Tulips.jpg", 1));
	}
}
