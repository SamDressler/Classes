//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
struct _data{
    char *name;
    long number;
}data;
void SEARCH (struct _data *BlackBox, char *name, int size){
    int i =0;
        for(i = 0; i < size; i++){
            if(strcmp(BlackBox[i].name, name) ==0 ){ 
                printf("******************************************************\n");
                printf("*  %10s was found in the file at location %d    *\n",name, i);
                printf("******************************************************\n");
                return;
                }                
            }
   printf("******************************************\n");
   printf("*  %10s was NOT found in file :(   *\n",name);
   printf("* Please be sure sure to check correct   *\n");
   printf("*         spelling and try again         *\n");
   printf("******************************************\n");
}
