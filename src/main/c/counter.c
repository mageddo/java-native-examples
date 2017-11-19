#include "include/counter.h"
#include<stdio.h>

int counter = 0;
int incrementAndGet(){
	counter++;
	return counter;
}
