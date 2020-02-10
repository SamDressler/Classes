//Sam Dressler CS230 4/10/19
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void
main(void){
    size_t count;
    char *temp;
    int size = 256;
    char buffer [size];
	temp = NULL;
	FILE *file = fopen("hw10.data", "r");
	if (file == NULL){
	    printf("ERROR: \"hw10.data\" not found");
	    exit(0);
	}
	printf("Buffer Address: %p\n", buffer);
	printf("%s\n",buffer);
	printf("File contents: ");
	while((getline(&temp, &count, file)) != -1){  
		printf("%s", temp);
	}
    temp = NULL;
	//Loading the buffer
	printf("\nLoading buffer...\n");
	rewind(file);
	getline(&temp, &count, file);
	strncpy(buffer, temp, 256);
	temp = NULL;
	printf("\nBuffer contents: %s>\n", buffer);
	
	return;

}
