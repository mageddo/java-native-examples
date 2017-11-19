#include<stdio.h>

int main(){
	char name[30];
	int r = gethostname (&name, 30);
	printf("%d - %s", r, name);
}
