#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

/*
	question - https://stackoverflow.com/questions/47837851/javafx-how-to-detect-windows-logoff-shutdown-request
	creating a winProc - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633570(v=vs.85).aspx
	creating a window to associate the winProc - https://msdn.microsoft.com/library/windows/desktop/ms632680
	handler para aplicacoes de console - https://docs.microsoft.com/en-us/windows/console/handlerroutine
	register handler to application events - https://docs.microsoft.com/en-us/windows/console/setconsolectrlhandler
	handler for gui applications - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633573(v=vs.85).aspx
	exemplo de aplicacao que olha quando o sistema restarta
		- https://www.codeproject.com/Articles/772868/Restart-Manager-Support-For-Windows-Application
		- https://github.com/killbug2004/NT_4.0_SourceCode/blob/master/nt4/private/ntos/w32/ntuser/kernel/exitwin.c
	all winproc events codes - https://github.com/tpn/winsdk-10/blob/master/Include/10.0.10240.0/um/WinUser.h
*/
int main(){

	printf("CS_BYTEALIGNCLIENT=%d, WS_EX_CLIENTEDGE=%d, CW_USEDEFAULT=%d\n", CS_BYTEALIGNCLIENT, WS_EX_CLIENTEDGE, CW_USEDEFAULT);

	FILE *f = fopen("file.txt", "w");
	if (f == NULL) {
	    printf("Error opening file!\n");
	    exit(1);
	}

	BOOL WINAPI MyHandler(DWORD dwCtrlType){
		printf("dwCtrlType=%d\n", dwCtrlType);
		fprintf(f, "action=console, event=%d\n", dwCtrlType);
		fflush(f);
		return 1;
	}

	LRESULT CALLBACK MyWinProc(
    HWND hwnd,        // handle to window
    UINT uMsg,        // message identifier
    WPARAM wParam,    // first message parameter
    LPARAM lParam)    // second message parameter
	{
 		printf("action=winproccalled, event=%d\n", uMsg);
 		fprintf(f, "action=winproccalled, event=%d\n", uMsg);
 		fflush(f);
 		switch(uMsg){
		    case WM_CLOSE:
		    	printf("closing\n");
		        PostQuitMessage(0);
		        break;
		    default:
		        return DefWindowProc(hwnd, uMsg, wParam, lParam);
	    };
	    return 0;
	}

	printf("fn=%p\n", MyWinProc);

/*
	registering a window - https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633587
							https://msdn.microsoft.com/pt-br/library/windows/desktop/ms633577
	typedef struct tagWNDCLASSEX {
	  UINT      cbSize;
	  UINT      style;
	  WNDPROC   lpfnWndProc;
	  int       cbClsExtra;
	  int       cbWndExtra;
	  HINSTANCE hInstance;
	  HICON     hIcon;
	  HCURSOR   hCursor;
	  HBRUSH    hbrBackground;
	  LPCTSTR   lpszMenuName;
	  LPCTSTR   lpszClassName;
	  HICON     hIconSm;
	} WNDCLASSEX, *PWNDCLASSEX;

	ATOM WINAPI RegisterClassEx(
	  _In_ const WNDCLASSEX *lpwcx
	);
*/
	WNDCLASSEX clazz = {0};
	clazz.lpszClassName = "MyPerfectWindow";
	clazz.cbSize = sizeof(WNDCLASSEX);
	clazz.style = CS_BYTEALIGNCLIENT;
	clazz.lpfnWndProc = MyWinProc;
	clazz.hbrBackground = (HBRUSH)(COLOR_BACKGROUND);
	clazz.cbClsExtra = 0;
	clazz.cbWndExtra = 0;


	ATOM classInst = RegisterClassEx(&clazz);
	printf("action=registerclass, clazz=%p, error=%d\n", classInst, GetLastError());
/*
	HWND WINAPI CreateWindowEx(
	  _In_     DWORD     dwExStyle,
	  _In_opt_ LPCTSTR   lpClassName,
	  _In_opt_ LPCTSTR   lpWindowName,
	  _In_     DWORD     dwStyle,
	  _In_     int       x,
	  _In_     int       y,
	  _In_     int       nWidth,
	  _In_     int       nHeight,
	  _In_opt_ HWND      hWndParent,
	  _In_opt_ HMENU     hMenu,
	  _In_opt_ HINSTANCE hInstance,
	  _In_opt_ LPVOID    lpParam
	);
*/
	HWND w = CreateWindowEx(WS_EX_CLIENTEDGE, clazz.lpszClassName, "My Window", WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, CW_USEDEFAULT, 250, 100, NULL, NULL, NULL, NULL);
	printf("action=createWindow, w=%p, error=%d\n", w, GetLastError());

	SetConsoleCtrlHandler(MyHandler, 1);

	MSG msg = {0};
	while( GetMessage( &msg, NULL, 0, 0 ) > 0 )
        DispatchMessage( &msg );

	return 0;
}



/*
out:
action=winproccalled, event=36
action=winproccalled, event=129
action=winproccalled, event=131
action=winproccalled, event=1
action=winproccalled, event=799 // ateh aqui foi a aplicacao subindo
action=console, event=0
action=winproccalled, event=800
action=winproccalled, event=59
action=winproccalled, event=17
action=winproccalled, event=59
action=winproccalled, event=22 # WM_ENDSESSION
*/
