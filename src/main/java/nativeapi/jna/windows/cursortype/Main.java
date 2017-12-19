package nativeapi.jna.windows.cursortype;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.*;

import java.io.IOException;
import java.util.*;

/**
 * https://msdn.microsoft.com/pt-br/library/windows/desktop/ms648029(v=vs.85).aspx
 * Test cursors - https://developer.mozilla.org/en-US/docs/Web/CSS/cursor
 * Chromium cursor map -  https://github.com/mageddo/chromium/blob/master/webkit/glue/webcursor_win.cc
 * Load icon example - https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/GDI32Test.java#L54
 * understanding makeintresource - https://stackoverflow.com/questions/3610565/why-does-makeintresource-work
 * all possible windows error codes - https://msdn.microsoft.com/en-us/library/windows/desktop/ms681386(v=vs.85).aspx
 * Cursor ids - https://msdn.microsoft.com/en-us/library/windows/desktop/ms648391(v=vs.85).aspx
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		while(true){
			final Main main = new Main();
			System.out.println(main.getCurrentCursor());
			Thread.sleep(2000);
		}
	}

	private final Map<WinNT.HANDLE, Cursor> cursors;
	private final User32 user32;

	public Main(){
		user32 = User32.INSTANCE;
		cursors = loadCursors();
	}


	/**
	 * Load all possible cursors to a map
	 */
	private Map<WinNT.HANDLE, Cursor> loadCursors() {
		final Map<WinNT.HANDLE, Cursor> cursors = new HashMap<>();
		for (final Cursor cursor : Cursor.values()) {

			final Memory memory = new Memory(Native.getNativeSize(Long.class, null));
			memory.setLong(0, cursor.getCode());
			final Pointer resource = memory.getPointer(0);
			final WinNT.HANDLE hcursor = User32.INSTANCE.LoadImageA(
				null, resource, WinUser.IMAGE_CURSOR, 0, 0, WinUser.LR_SHARED
			);
			if(hcursor == null || Native.getLastError() != 0){
				throw new Error("Cursor could not be loaded: " + String.valueOf(Native.getLastError()));
			}

			cursors.put(hcursor, cursor);
		}
		return Collections.unmodifiableMap(cursors);
	}

	public Cursor getCurrentCursor(){
		final CURSORINFO cursorinfo = new CURSORINFO();
		final int success = User32.INSTANCE.GetCursorInfo(cursorinfo);
		if(success != 1){
			throw new Error("Could not retrieve cursor info: " + String.valueOf(Native.getLastError()));
		}

		// you can use the address printed here to map the others cursors like ALL_SCROLL
		System.out.printf("currentPointer=%s%n", cursorinfo.hCursor);
		// some times cursor can be hidden, in this case it will be null
		if(cursorinfo.hCursor != null && cursors.containsKey(cursorinfo.hCursor)){
			return cursors.get(cursorinfo.hCursor);
		}
		return null;
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

	public interface User32 extends com.sun.jna.Library {
		User32 INSTANCE = Native.loadLibrary("User32.dll", User32.class);

		//		BOOL WINAPI GetCursorInfo(
//			_Inout_ PCURSORINFO pci
//		);
		int GetCursorInfo(CURSORINFO cursorinfo);

//		HANDLE WINAPI LoadImage(
//			_In_opt_ HINSTANCE hinst,
//			_In_     LPCTSTR   lpszName,
//			_In_     UINT      uType,
//			_In_     int       cxDesired,
//			_In_     int       cyDesired,
//			_In_     UINT      fuLoad
//		);
		WinNT.HANDLE LoadImageA(
			WinDef.HINSTANCE hinst,
			Pointer lpszName,
			int uType,
			int cxDesired,
			int cyDesired,
			int fuLoad
		);
	}

	public enum Cursor {
		APPSTARTING(32650),
		NORMAL(32512),
		CROSS(32515),
		HAND(32649),
		HELP(32651),
		IBEAM(32513),
		NO(32648),
		SIZEALL(32646),
		SIZENESW(32643),
		SIZENS(32645),
		SIZENWSE(32642),
		SIZEWE(32644),
		UP(32516),
		WAIT(32514),
		PEN(32631)
		;

		private final int code;

		Cursor(final int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
}
