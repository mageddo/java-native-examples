#include "lib/sort.h"
#include<stdio.h>
#include<time.h>

int main(){

	int ages[] = {1,2,3,4,5,6,7,8,9,10};
	clock_t start = clock();

	sort(ages, 10);
	clock_t end = clock();
	float seconds = (float)(end - start);
	for(int i=0; i < 10; i++){
		printf("%d, ", ages[i]);
	}
	printf("in: %.2f seconds", seconds);

}