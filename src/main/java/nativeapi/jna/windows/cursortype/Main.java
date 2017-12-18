package nativeapi.jna.windows.cursortype;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * https://msdn.microsoft.com/pt-br/library/windows/desktop/ms648029(v=vs.85).aspx
 * https://github.com/adobe/chromium/blob/master/webkit/glue/webcursor_win.cc
 * https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/GDI32Test.java
 * https://github.com/tpn/winsdk-10/blob/master/Include/10.0.10240.0/um/WinUser.h
 * https://stackoverflow.com/questions/3610565/why-does-makeintresource-work
 * https://msdn.microsoft.com/en-us/library/windows/desktop/ms681386(v=vs.85).aspx
 * https://msdn.microsoft.com/en-us/library/windows/desktop/ms648045(v=vs.85).aspx
 * https://msdn.microsoft.com/en-us/library/windows/desktop/ms648051(v=vs.85).aspx
 */
public class Main {

	public interface User32 extends com.sun.jna.Library {
		User32 INSTANCE = com.sun.jna.Native.loadLibrary("User32.dll", User32.class);


//		BOOL WINAPI GetCursorPos(
//			_Out_ LPPOINT lpPoint
//		);

		boolean GetCursorPos(LPoint p);

		com.sun.jna.platform.win32.WinDef.HCURSOR GetCursor();
//		BOOL WINAPI GetIconInfo(
//			_In_  HICON     hIcon,
//			_Out_ PICONINFO piconinfo
//		);
		boolean GetIconInfo(WinDef.HICON hicon, ICONINFO iconinfo);


//		LPTSTR MAKEINTRESOURCE(
//			WORD wInteger
//		);
			String MAKEINTRESOURCE(WinDef.WORD code);

//		HICON WINAPI LoadIcon(
//			_In_opt_ HINSTANCE hInstance,
//			_In_     LPCTSTR   lpIconName
//		);

		WinDef.HICON LoadIcon(WinDef.HINSTANCE hInstance, String   lpIconName );

//		HANDLE WINAPI LoadImage(
//			_In_opt_ HINSTANCE hinst,
//			_In_     LPCTSTR   lpszName,
//			_In_     UINT      uType,
//			_In_     int       cxDesired,
//			_In_     int       cyDesired,
//			_In_     UINT      fuLoad
//		);

		WinNT.HANDLE LoadImage(
			WinDef.HINSTANCE hinst,
			String   lpszName,
			int      uType,
			int       cxDesired,
			int       cyDesired,
			int      fuLoad
		);
	}

	public static void main(String[] args) throws InterruptedException, IOException {

//		URL url = Main.class.getResource("/numbers.txt");
//		while(true){
//			URLConnection conn = url.openConnection();
//			System.out.println(conn.getLastModified());
//		String msg = "IDC_ARROW";
//		Memory m = new Memory(msg.length() + 1 );
//		m.setString(0, msg);
//m.getPointer(0)
//		WinDef.HICON icon = new WinDef.HICON(new WinNT.HANDLE());
//		WinDef.HICON icon = new WinDef.HICON(new Pointer(new WinDef.WORD(32650).longValue()));
//		System.out.println(icon);;
//			ICONINFO inf = new ICONINFO();
//			System.out.println(User32.INSTANCE.GetIconInfo(icon, inf));
//		System.out.println(User32.INSTANCE.MAKEINTRESOURCE(new WinDef.WORD(32650)));
//		System.out.println(com.sun.jna.platform.win32.User32.INSTANCE.LoadImage(null, "OCR_APPSTARTING", 2, 0, 0, WinUser.LR_MONOCHROME));
//		System.out.println(Native.getLastError());
//		System.out.println(User32.INSTANCE.LoadImage(null, "OCR_IDI_APPLICATION", 2, 0, 0, 0));
//	System.exit(-1);
		while (true){
//			ICONINFO inf = new ICONINFO();
//			System.out.println(User32.INSTANCE.GetCursor().getPointer().);
//			System.out.println(User32.INSTANCE.GetIconInfo(User32.INSTANCE.GetCursor(), inf));
//			System.out.println(inf.hbmMask);
//			System.out.println(inf.xHotspot);
//			LPoint p = new LPoint();
//			System.out.println(User32.INSTANCE.GetCursorPos(p));
//			System.out.println(p);
			System.out.println(User32.INSTANCE.GetCursor().getPointer().getInt(0));
			Thread.sleep(1000);
		}
	}

	public static class ICONINFO extends Structure {
		public boolean    fIcon;
		public WinDef.DWORD   xHotspot;
		public WinDef.DWORD   yHotspot;
		public WinDef.HBITMAP hbmMask;
		public WinDef.HBITMAP hbmColor;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("fIcon", "xHotspot", "yHotspot", "hbmMask", "hbmColor");
		}
	}

	public static class LPoint extends Structure {

		public long x,y;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("x", "y");
		}
	}
}
