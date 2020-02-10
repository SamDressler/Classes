#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
int SEED = 0;
int getRand(){
	int choice;
	int connections = 3;
	SEED += time(0);
	srand(SEED);
	printf("%d\n",rand());
	printf("%d\n",rand());
	printf("%d\n",rand());
	choice = (rand() % connections);
	return choice;
}
int main(){
	int choice;	
	for(int i = 0; i < 10; i++){

		choice = getRand();
		printf("%d\n",choice);
	}

}
