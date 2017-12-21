package nativeapi.jna.windows.shutdownhook;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.W32APIOptions;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * WinProc creating sample in c++ - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633570(v=vs.85).aspx
 * https://stackoverflow.com/questions/21369256/how-to-use-wndproc-as-a-class-function
 * https://stackoverflow.com/questions/47837851/javafx-how-to-detect-windows-logoff-shutdown-request
 * https://msdn.microsoft.com/en-us/library/windows/desktop/aa376889.aspx
 * creating a winproc to handle windows events - https://msdn.microsoft.com/en-us/library/windows/desktop/aa376890.aspx
 * how to create a winproc at JNA - https://stackoverflow.com/questions/4678247/java-jna-windowproc-implementation
 * criando uma window - https://msdn.microsoft.com/library/windows/desktop/ms632680
 * criando uma WindowClass - https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633576
 */
public class Main {

	public interface User32 extends Library {

		User32 INSTANCE = Native.loadLibrary("User32", User32.class, W32APIOptions.UNICODE_OPTIONS);

		//		ATOM WINAPI RegisterClassEx(
//			_In_ const WNDCLASSEX *lpwcx
//		);
		WinDef.ATOM RegisterClassEx(WinUser.WNDCLASSEX lpwcx);

		//	HWND WINAPI CreateWindowEx(
//	  _In_     DWORD     dwExStyle,
//	  _In_opt_ LPCTSTR   lpClassName,
//	  _In_opt_ LPCTSTR   lpWindowName,
//	  _In_     DWORD     dwStyle,
//	  _In_     int       x,
//	  _In_     int       y,
//	  _In_     int       nWidth,
//	  _In_     int       nHeight,
//	  _In_opt_ HWND      hWndParent,
//	  _In_opt_ HMENU     hMenu,
//	  _In_opt_ HINSTANCE hInstance,
//	  _In_opt_ LPVOID    lpParam
//	);
		WinDef.HWND CreateWindowEx(
			int dwExStyle,
//			Pointer lpClassName,
			String lpClassName,
			String lpWindowName,
			int dwStyle,
			int x,
			int y,
			int nWidth,
			int nHeight,
			WinDef.HWND hWndParent,
			WinDef.HMENU hMenu,
			WinDef.HINSTANCE hInstance,
			WinDef.LPVOID lpParam
		);

		//		BOOL WINAPI GetMessageW(
//			_Out_    LPMSG lpMsg,
//			_In_opt_ HWND  hWnd,
//			_In_     UINT  wMsgFilterMin,
//			_In_     UINT  wMsgFilterMax
//		);
		boolean GetMessageW(WinUser.MSG lpMsg, WinDef.HWND hWnd, int wMsgFilterMin, int wMsgFilterMax);

		//		LRESULT WINAPI DispatchMessageW(
//			_In_ const MSG *lpmsg
//		);
		WinDef.LRESULT DispatchMessageW(WinUser.MSG lpmsg);

	}

	public interface Comctl32 extends Library {
		Comctl32 INSTANCE = Native.loadLibrary("Comctl32", Comctl32.class);
		boolean InitCommonControlsEx( INITCOMMONCONTROLSEX lpInitCtrls );
	}
	public interface Kernel32 extends Library {
		Kernel32 INSTANCE = Native.loadLibrary("Kernel32", Kernel32.class);
		WinDef.HMODULE GetModuleHandleW(String moduleName);
	}

//	typedef struct tagINITCOMMONCONTROLSEX {
//		DWORD dwSize;
//		DWORD dwICC;
//	} INITCOMMONCONTROLSEX, *LPINITCOMMONCONTROLSEX;
	public static class INITCOMMONCONTROLSEX extends Structure {

	public INITCOMMONCONTROLSEX(int dwICC) {
		this.dwSize = Native.getNativeSize(INITCOMMONCONTROLSEX.class, null);
		this.dwICC = dwICC;
	}

	public int dwSize;
		public int dwICC;
		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("dwSize", "dwICC");
		}
	}


	public static class MyWinProc implements WinUser.WindowProc {
		private final OutputStream out;

		public MyWinProc() {
			try {
				out = new FileOutputStream(new File("C:\\Users\\typerpc\\Documents\\log.log"));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
			final String msg = String.format("action=proc-callback, event=%d %n", uMsg);
			System.out.print(msg);
			try {
				out.write(msg.getBytes());
				out.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return new WinDef.LRESULT(0);
		}

		/*
		 * steps:
		 *
		 * 1. Create a  WinProc
		 * 2. Create a window class using the created WinProc
		 * 3. Create a window using the created window class
		 * 4. Use the WinProc to handle shutdown events - https://stackoverflow.com/questions/40825747/close-my-program-before-windows-shutdown
		 */

		public static void main(String[] args) {
//	registering a window - https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633587
//							https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633577
//	typedef struct tagWNDCLASSEX {
//	  UINT      cbSize;
//	  UINT      style;
//	  WNDPROC   lpfnWndProc;
//	  int       cbClsExtra;
//	  int       cbWndExtra;
//	  HINSTANCE hInstance;
//	  HICON     hIcon;
//	  HCURSOR   hCursor;
//	  HBRUSH    hbrBackground;
//	  LPCTSTR   lpszMenuName;
//	  LPCTSTR   lpszClassName;
//	  HICON     hIconSm;
//	} WNDCLASSEX, *PWNDCLASSEX;
//
//	ATOM WINAPI RegisterClassEx(
//	  _In_ const WNDCLASSEX *lpwcx
//	);

//			final INITCOMMONCONTROLSEX initcommoncontrolsex = new INITCOMMONCONTROLSEX(0x00008000);
//			System.out.printf("success=%b, error=%d\n", Comctl32.INSTANCE.InitCommonControlsEx(initcommoncontrolsex), Native.getLastError());
//
			final WinUser.WNDCLASSEX clazz = new WinUser.WNDCLASSEX();
			clazz.lpszClassName = "My Window";
			clazz.cbSize = Native.getNativeSize(WinUser.WNDCLASSEX.class, null);
			clazz.style = 4096;
			//clazz.hbrBackground = new WinDef.HBRUSH();
			clazz.cbClsExtra = 0;
			clazz.cbWndExtra = 0;
			clazz.lpfnWndProc = new MyWinProc();

			final WinDef.ATOM classInst = com.sun.jna.platform.win32.User32.INSTANCE.RegisterClassEx(clazz);
//			WinDef.ATOM classInst = User32.INSTANCE.RegisterClassEx(clazz);
			System.out.printf("action=registerclass, clazz=%s, error=%d\n", classInst, Native.getLastError());

//			HWND w = CreateWindowEx(WS_EX_CLIENTEDGE, clazz.lpszClassName, "My Window", WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT, 250, 100, NULL, NULL, NULL, NULL);

			Memory m = new Memory(clazz.lpszClassName.length() * Native.WCHAR_SIZE);
			m.setString(0, clazz.lpszClassName);

			WinDef.HWND w = User32.INSTANCE.CreateWindowEx(
				512, clazz.lpszClassName, "My Window",
				WinUser.WS_OVERLAPPEDWINDOW, -2147483648, -2147483648, 250, 100,
				null, null, null, null
			);
			System.out.printf("action=createWindow, w=%s, error=%d\n", w, Native.getLastError());

			WinUser.MSG msg = new WinUser.MSG();
			while (User32.INSTANCE.GetMessageW(msg, null, 0, 0)) {
				User32.INSTANCE.DispatchMessageW(msg);
			}
		}
	}
}
