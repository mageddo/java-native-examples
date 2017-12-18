#include <stdlib.h>
#include <stdio.h>
#include <windows.h>

// http://www.cplusplus.com/forum/windows/8555/

// https://msdn.microsoft.com/en-us/library/windows/desktop/ms683188(v=vs.85).aspx
typedef int (CALLBACK/*WINAPI*/* c_getenv)(const char* name, char* buf, int size);

int main() {
	
	c_getenv proc;
	HMODULE lib;
	
	lib = LoadLibrary("Kernel32.dll");
	if(lib == NULL){
		printf("%s\n", "Failed to load lib kernel32");
		return 1;
	}
	
	proc = (c_getenv) GetProcAddress(lib, "GetEnvironmentVariableA");

	printf("p=%p, error=%d\n", proc, GetLastError());
	
	
	const int size = 512;
	char* buf = malloc(sizeof(char) * size);
	
    printf("r=%d, value=%s\n", proc("PATH", buf, size), buf);
    
    FreeLibrary(lib);

}
