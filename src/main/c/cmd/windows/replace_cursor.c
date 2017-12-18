#include <stdlib.h>
#include <stdio.h>
#include <windows.h>

// change the system cursor

// http://www.cplusplus.com/forum/windows/8555/
// https://msdn.microsoft.com/en-us/library/windows/desktop/ms683188(v=vs.85).aspx
// ids dos cursores - https://msdn.microsoft.com/en-us/library/windows/desktop/ms648070(v=vs.85).aspx
// ids dos cursores para a funcao do systemcursor - https://msdn.microsoft.com/en-us/library/windows/desktop/ms648395(v=vs.85).aspx
// manipulando o cursor e setando outro cursor no sistema - https://github.com/mageddo/FFmpeg/blob/master/libavdevice/gdigrab.c#L432
// erros do windows - https://msdn.microsoft.com/en-us/library/windows/desktop/ms681382(v=vs.85).aspx
 typedef HCURSOR (CALLBACK/*WINAPI*/* c_getcursor)();
typedef int (WINAPI* c_getenv)(PCURSORINFO);
typedef int (CALLBACK/*WINAPI*/* c_geticoninfo)(HICON*, PICONINFO);

int main() {
	

	while(1){
		
		POINT p;
		if (GetCursorPos(&p)){
			printf("cursor.x=%d\n", p.x);
		}else{
			printf("error\n");
		}
		printf("cursor=%p\n", GetCursor());

		CURSORINFO pci = {0};
		pci.cbSize = sizeof(pci);
		
		printf("info-success=%d, err=%d, cursor=%p\n", GetCursorInfo(&pci), GetLastError(), pci);
		printf("resource=%d, r=%d, GetLastError=%d\n", MAKEINTRESOURCE(32515), SetSystemCursor(pci.hCursor, 32648), GetLastError());
		
		sleep(100);
	}
}
