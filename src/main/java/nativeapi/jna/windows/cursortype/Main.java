package nativeapi.jna.windows.cursortype;

import com.sun.jna.Memory;
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
 *
 * @deprecated - it is incomplete, this sample is not working yet, see src/main/c/cmd/windows/cursortype.c
 */
public class Main {

	public interface User32 extends com.sun.jna.Library {
		User32 INSTANCE = com.sun.jna.Native.loadLibrary("User32.dll", User32.class);

		//		BOOL WINAPI GetCursorInfo(
//			_Inout_ PCURSORINFO pci
//		);
		int GetCursorInfo(CURSORINFO cursorinfo);

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

		WinDef.HICON LoadIcon(WinDef.HINSTANCE hInstance, String lpIconName);

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
			String lpszName,
			int uType,
			int cxDesired,
			int cyDesired,
			int fuLoad
		);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		final CURSORINFO cursorinfo = new CURSORINFO();
		System.out.printf("success=%b, info=%s, error=%d", User32.INSTANCE.GetCursorInfo(cursorinfo), cursorinfo, Native.getLastError());
		Thread.sleep(1000);
	}


	public static class LPoint extends Structure {

		public long x, y;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("x", "y");
		}
	}

	//	typedef struct {
//		DWORD   cbSize;
//		DWORD   flags;
//		HCURSOR hCursor;
//		POINT   ptScreenPos;
//	} CURSORINFO, *PCURSORINFO, *LPCURSORINFO;
	public static class CURSORINFO extends Structure {

		public int cbSize;
		public int flags;
		public WinDef.HCURSOR hCursor;
		public WinDef.POINT ptScreenPos;

		public CURSORINFO() {
			this.cbSize = Native.getNativeSize(CURSORINFO.class, null);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("cbSize", "flags", "hCursor", "ptScreenPos");
		}
	}

	public static class ICONINFO extends Structure {
		public boolean fIcon;
		public WinDef.DWORD xHotspot;
		public WinDef.DWORD yHotspot;
		public WinDef.HBITMAP hbmMask;
		public WinDef.HBITMAP hbmColor;

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("fIcon", "xHotspot", "yHotspot", "hbmMask", "hbmColor");
		}
	}
}
