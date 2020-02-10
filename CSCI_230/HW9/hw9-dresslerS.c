//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <dlfcn.h>

#include "libHW9.h"

struct _data{
    char *name;
    long number;
};
typedef struct _data* data;

int main(int argv, char **argc){
	void *handle;
	int  (*SCAN)(FILE**);
	data (*LOAD)(FILE*, int);
	void (*SEARCH)(data, char*, int);
	void (*FREE)(data, int);
    handle = dlopen("./libHW9.so", RTLD_LAZY);
    
    if(!handle){
    	fprintf(stderr, "%s\n", dlerror());
    	dlclose(handle);
    	exit(EXIT_FAILURE);
    }
    dlerror();
    
    *(void **)(&SCAN) = dlsym(handle, "SCAN");
    if(!SCAN){
    	fprintf(stderr, "%s\n", dlerror());
    	dlclose(handle);
    	exit(EXIT_FAILURE);
    }
    dlerror();
    *(void **) (&LOAD) = dlsym(handle, "LOAD");
    if(!LOAD){
    	fprintf(stderr, "%s\n", dlerror());
    	dlclose(handle);
    	exit(EXIT_FAILURE);
    }
    dlerror();
    *(void**) (&SEARCH) = dlsym(handle, "SEARCH");
    if(!SEARCH){
    	fprintf(stderr, "%s\n", dlerror());
    	dlclose(handle);
    	exit(EXIT_FAILURE);
    }
    dlerror();
    *(void**) (&FREE) = dlsym(handle, "FREE");
    if(!FREE){
    	fprintf(stderr, "%s\n", dlerror());
    	dlclose(handle);
    	exit(EXIT_FAILURE);
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
        
        dlclose(handle);
        return 0;      
}
