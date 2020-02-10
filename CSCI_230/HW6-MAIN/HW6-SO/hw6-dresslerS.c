//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dlfcn.h>
#include "libHW9.h"

struct _data{
    char *name;
    long number;
}data;
int main(int argv, char **argc){

	void *handle;
    handle = dlopen("libHW9.so", RTLD_NOW);
    if(!handle){
    	fprintf(stderr, "%s\n", dlerror());
    	exit(0);
    }
    dlerror();
    
    if(argv == 1){   
        printf("*****************************************\n");
        printf("* You must include a name to search for *\n");
        printf("*****************************************\n");
        exit(0);    
    }
  
        char * searchWord = argc[1];
        FILE *stream = fopen("hw5.data","r");
//SCAN function scans the file and determines the number of lines;
        int size = SCAN(&stream);
//Load function populates the array of Struct
        struct _data *BlackBox = LOAD(stream, size);
//Search function searches through the array of struct until it finds or doesnt find the search word
        SEARCH(BlackBox, searchWord, size);
//Free function frees the memory that was allocated for each name in the array of struct and freed
//the array of struct itself.
        FREE(BlackBox,size);
        
        return 0;      
}
