//Sam Dressler HW4 CS230 2-10-19
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *resize(char array[], int *size) {
char *ptr;
    //ptr is a temporary array that is created with 5 more spaces of memory than the previous array.
    ptr = calloc(*size+5, sizeof(char));
    // once the array is created, the old memory in the n-5 spaces is copied over into the new array.
    memcpy(ptr, array, *size);		
    // increase the size of the array permanently
    *size+=5;				
    // release the old memory
    free(array);
//return new array with added space    				
return ptr;				
}

int main(void) {
int  index;
int  count = 0;
int  initSize = 5;
char *input;
    //dynamically creating an array of initial size 5
    input = calloc(initSize, sizeof(char));

    printf("Enter characters: ");
    for (index = 0; ; index++) {
       //takes characters in using getChar and stores in input
       input[index] = getchar();
       //keeping track of how many characters have been added since the last resize
       count++;
       //once the number of characters reaches the max amount of allocated space
       if (count == 5) {
          //reset number of characters since latest resize
          count = 0; 
          //calls the resize function that increases the size of input
          input = resize(input, &initSize);		
       }
       //exits the infinite for loop once the enter key has been detected.
       if (input[index] == '\n') {
           //replaces "Enter" escape character with end of string escape character
           input[index] = '\0';
           break;
        }		
    }
    //creating the required output
char *output;
char finalText[] = "HW 4 input: ";
    output = (char *) calloc((strlen(finalText)+strlen(input)),sizeof(char));
    output = strcat(output, finalText);
    output = strcat(output, input);
    printf("%s\n",output);
    printf("You entered %ld characters.\n",strlen(input));
    printf("The total number of characters in the output is %ld\n",strlen(output));
free(input);
free(output);
return 0;
}
