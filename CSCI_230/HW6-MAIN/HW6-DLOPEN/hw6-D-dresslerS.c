//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
struct _data{
    char *name;
    long number;
}data;
void FREE (struct _data *BlackBox, int size){
    int i = 0;
    for(i = 0; i < size; i++){
        free(BlackBox[i].name);     
    }
    BlackBox = NULL;
    free(BlackBox);
}
