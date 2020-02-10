//Sam Dressler CS 230 HW5 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct
 _data{
    char *name;
    long number;
}data;

int 
SCAN (FILE *(*stream)){
    if(*stream == NULL){  
        printf("file not found"); exit(0);
    }
    int size = 0 ;
    char *line = NULL;
    size_t len = 0;
    while ((getline(&line, &len, *stream)) != -1) size++;
return size;
}

struct 
_data* LOAD (FILE *stream, int size){
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

void 
SEARCH (struct _data *BlackBox, char *name, int size){
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

void 
FREE (struct _data *BlackBox, int size){
    int i = 0;
    for(i = 0; i < size; i++){
        free(BlackBox[i].name);     
    }
    BlackBox = NULL;
    free(BlackBox);
}
int 
main(int argv, char **argc){
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


