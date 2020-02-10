//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
int SCAN (FILE *(*stream)){
    if(*stream == NULL){  
        printf("file not found\n"); exit(0);
    }
    int size = 0 ;
    char *line = NULL;
    size_t len = 0;
    while ((getline(&line, &len, *stream)) != -1) size++;
return size;
}
