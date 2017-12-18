#include <stdlib.h>
#include <stdio.h>
#include <windows.h>

// http://www.cplusplus.com/forum/windows/8555/

// https://msdn.microsoft.com/en-us/library/windows/desktop/ms683188(v=vs.85).aspx
// ids dos cursores - https://msdn.microsoft.com/en-us/library/windows/desktop/ms648070(v=vs.85).aspx
// ids dos cursores para a funcao do systemcursor - https://msdn.microsoft.com/en-us/library/windows/desktop/ms648395(v=vs.85).aspx
// manipulando o cursor e setando outro cursor no sistema - https://github.com/mageddo/FFmpeg/blob/master/libavdevice/gdigrab.c#L432
// erros do windows - https://msdn.microsoft.com/en-us/library/windows/desktop/ms681382(v=vs.85).aspx
// como pegar o tipo do cursor atual - https://stackoverflow.com/questions/13241940/get-cursor-type-from-hcursor
typedef HCURSOR (CALLBACK/*WINAPI*/* c_getcursor)();
typedef int (WINAPI* c_getenv)(PCURSORINFO);
typedef int (CALLBACK/*WINAPI*/* c_geticoninfo)(HICON*, PICONINFO);

// https://stackoverflow.com/questions/3610565/why-does-makeintresource-work
char* into2Res (int i) {
	return (unsigned int)i;
}

int main() {
	
	while(1){
		
		CURSORINFO pci = {0};
		pci.cbSize = sizeof(pci);
		
		printf("info-success=%d, err=%d, cursor=%p\n", GetCursorInfo(&pci), GetLastError(), pci);
		
		 //char* cursors[] = { "OCR_APPSTARTING", "OCR_NORMAL", "OCR_CROSS", "OCR_HAND", "OCR_HELP", "OCR_IBEAM", "OCR_NO", "OCR_SIZEALL", "OCR_SIZENESW", "OCR_SIZENS", "OCR_SIZENWSE", "OCR_SIZEWE", "OCR_UP", "OCR_WAIT" };
		 int cursors[] = { 32650,32512,32515,32649,32651,32513,32648,32646,32643,32645,32642,32644,32516,32514 };
		int i = 0;
		for(; i < 14; i++){
			
			char* resource = (unsigned int)cursors[i];
			HCURSOR c = (HCURSOR) LoadImage(NULL, resource, IMAGE_CURSOR, 0, 0, LR_SHARED); 
			printf("cursor=%p, i=%p, resource=%d\n", pci.hCursor, c, resource);
			if (pci.hCursor == c) {
				printf("match!\n");
			}
		}
		
		printf("=====================================\n\n");
	
		sleep(2);
	}
}


