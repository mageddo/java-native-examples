package nativeapi.jna.windows.shutdownhook;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import sun.plugin2.os.windows.Windows;

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

	public static interface User32 {

		User32 INSTANCE = Native.loadLibrary("User32", User32.class);

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

		//		BOOL WINAPI GetMessage(
//			_Out_    LPMSG lpMsg,
//			_In_opt_ HWND  hWnd,
//			_In_     UINT  wMsgFilterMin,
//			_In_     UINT  wMsgFilterMax
//		);
		boolean GetMessage(WinUser.MSG lpMsg, WinDef.HWND hWnd, int wMsgFilterMin, int wMsgFilterMax);

		//		LRESULT WINAPI DispatchMessage(
//			_In_ const MSG *lpmsg
//		);
		WinDef.LRESULT DispatchMessage(WinUser.MSG lpmsg);
	}

	public static class MyWinProc implements WinUser.WindowProc {
		@Override
		public WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
			System.out.printf("status=proc-callback, event=%d %n", uMsg);
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
			final WinUser.WNDCLASSEX clazz = new WinUser.WNDCLASSEX();
			clazz.lpszClassName = "MyPerfectWindow";
			clazz.cbSize = Native.getNativeSize(WinUser.WNDCLASSEX.class);
			clazz.style = 4096;
//		clazz.hbrBackground = new WinDef.HBRUSH();
			clazz.cbClsExtra = 0;
			clazz.cbWndExtra = 0;

			WinDef.ATOM classInst = User32.INSTANCE.RegisterClassEx(clazz);
			System.out.printf("action=registerclass, clazz=%s, error=%d\n", classInst, Native.getLastError());

			WinDef.HWND w = User32.INSTANCE.CreateWindowEx(
				512, clazz.lpszClassName, "My Window",
				WinUser.WS_OVERLAPPEDWINDOW, -2147483648, -2147483648, 250, 100,
				null, null, null, null
			);
			System.out.printf("action=createWindow, w=%s, error=%d\n", w, Native.getLastError());

			WinUser.MSG msg = new WinUser.MSG();
			while (User32.INSTANCE.GetMessage(msg, null, 0, 0)) {
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
	}
}
