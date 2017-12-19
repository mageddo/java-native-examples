#include <windows.h>
#include <stdlib.h>
#include <stdio.h>

/*
	creating a winProc - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633570(v=vs.85).aspx
	creating a window to associate the winProc - https://msdn.microsoft.com/library/windows/desktop/ms632680
*/
int main(){
	
	
	LRESULT CALLBACK MyWinProc(
    HWND hwnd,        // handle to window
    UINT uMsg,        // message identifier
    WPARAM wParam,    // first message parameter
    LPARAM lParam)    // second message parameter 
	{ 
 		printf("action=winproccalled\n");
 		if (1==1)
		 return 0;
		 
	    switch (uMsg)  { 
	        case WM_CREATE: 
	            // Initialize the window. 
	            return 0; 
	 
	        case WM_PAINT: 
	            // Paint the window's client area. 
	            return 0; 
	 
	        case WM_SIZE: 
	            // Set the size and position of the window. 
	            return 0; 
	 
	        case WM_DESTROY: 
	            // Clean up window-specific data objects. 
	            return 0; 
	 
	        // 
	        // Process other messages. 
	        // 
	 
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
	HWND w = CreateWindowEx(WS_EX_CLIENTEDGE, clazz.lpszClassName, "My Window", WS_BORDER | WS_CAPTION | WS_HSCROLL, CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, CW_USEDEFAULT, NULL, NULL, NULL, NULL);
	printf("action=createWindow, w=%p, error=%d\n", w, GetLastError());
	
	/*
		show the window - https://msdn.microsoft.com/en-us/library/windows/desktop/ms633548(v=vs.85).aspx
		BOOL WINAPI ShowWindow(
		  _In_ HWND hWnd,
		  _In_ int  nCmdShow
		);

	*/
	
	printf("action=show-window, r=%d, error=%d\n", ShowWindow(w, SW_SHOW), GetLastError());
	
	return 0;
}
