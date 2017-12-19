package nativeapi.jna.windows.shutdownhook;

/**
 * WinProc creating sample in c++ - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633570(v=vs.85).aspx
 * https://stackoverflow.com/questions/21369256/how-to-use-wndproc-as-a-class-function
 * https://stackoverflow.com/questions/47837851/javafx-how-to-detect-windows-logoff-shutdown-request
 * https://msdn.microsoft.com/en-us/library/windows/desktop/aa376889.aspx
 * creating a winproc to handle windows events - https://msdn.microsoft.com/en-us/library/windows/desktop/aa376890.aspx
 * 	how to create a winproc at JNA - https://stackoverflow.com/questions/4678247/java-jna-windowproc-implementation
 * criando uma window - https://msdn.microsoft.com/library/windows/desktop/ms632680
 * criando uma WindowClass - https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633576
 */
public class Main {
	/*
	 * steps:
	 *
	 * 1. Create a  WinProc
	 * 2. Create a window class using the created WinProc
	 * 3. Create a window using the created window class
	 * 4. Use the WinProc to handle shutdown events - https://stackoverflow.com/questions/40825747/close-my-program-before-windows-shutdown
	 */

	public static void main(String[] args) {

	}
}
