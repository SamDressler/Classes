//Sam Dressler
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
struct _data{
    char *name;
    long number;
}data;
struct _data* LOAD (FILE *stream, int size){
    rewind(stream);
    struct _data *BlackBox = calloc(size , sizeof(data));
    char *line = NULL;
    size_t l = 0;
    int i; 
    char *tok1;  
    char *tok2; 
    for(i=0; i<size; i++){ 
       
        getline(&line, &l, stream);   
        tok1 = strtok(line," ");      //used strtok to split the line by the space
        tok2 = strtok(NULL, "\n");    // and then again by where strtok modifed line
        BlackBox[i].name = calloc(strlen(tok1),sizeof(char));
        strcpy(BlackBox[i].name, tok1);   //needed to use strcpy or else it just used the last name in file
        BlackBox[i].number = atol(tok2); //needed to use atol in order to get the string containing number to long
     }
     
     
     free(line);
     fclose(stream);
     return BlackBox;
}
