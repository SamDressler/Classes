//Sam Dressler CS230 3/28/19
//Ignore compiler warnings. I don't know why they are being thrown at me
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct poem{
	char *word;
	struct poem *next;
};
struct codex {
     char *word1;
     char *word2;
     struct codex *next;
};

typedef struct poem pNode;
typedef struct codex cNode;

void DISPLAY_POEM(pNode *head);
void DISPLAY_CODEX(cNode *(*head));
void POEM_INSERT(pNode *(*head),pNode *(*end), char temp1[]);
void LOAD_CODEX(FILE *(*file), cNode *(*head), cNode *(*end));
void REPLACE_WORDS(pNode*(*pHead), cNode *(*cHead));
void pFREE(pNode **pHead);
void cFREE(cNode **cHead);

int main(void){
	FILE *file;
	pNode *pHead, *pEnd;
	cNode *cHead, *cEnd;
	pNode* current;
	cNode* currentX;
	pHead = NULL;cHead = NULL;
	pEnd = NULL; cEnd = NULL;
	char temp1 [30];	
	int size = 0;	
	
	file = fopen("hw8data.txt","r");
	if(file == NULL){
		printf("hw8data.txt not found\n");
		exit(0);	
	}
	//while the poem file has lines
	while(1)
	{		
		//scan in the first word to be checked for punctuation
		fscanf(file, "%s", temp1);
		//if the word is the last word, break
		if(feof(file)) break;
		//variable to hold length of temp word to save typing
		int len = strlen(temp1);	
		//check if the last caracter in the word is a comma
		if (temp1[len-1] == ',') {
		//if yes then replace with null terminator
        	temp1[len-1] = '\0';
			//insert the word before the comma
       		POEM_INSERT(&pHead, &pEnd, temp1 );
       		//insert the comma
			POEM_INSERT(&pHead, &pEnd, ",");
      	 } 
		else if (temp1[len-1] == '.') {
          	temp1[len-1] = '\0';
			POEM_INSERT(&pHead, &pEnd, temp1 );
			POEM_INSERT(&pHead, &pEnd, ".");
       } 
       	else if (temp1[len-1] == ';'){
       		temp1[len-1] = '\0'; 
       		POEM_INSERT(&pHead, &pEnd, temp1);
       		POEM_INSERT(&pHead, &pEnd, ";");
       	}
		else {	
	    	//printf("%s\n", temp1);
			POEM_INSERT(&pHead, &pEnd, temp1);	
       }
	}	
    printf("======================================\n");
	printf("           Uncorrected Poem\n");
	printf("======================================\n");
	DISPLAY_POEM(pHead);
	printf("======================================\n");
	fclose(file);
	
	file = fopen("hw8codex.txt","r");
	if(file == NULL){
		printf("hw8codex.txt not found\n");	
		exit(0);
	}	
		
		LOAD_CODEX(&file, &cHead, &cEnd);
    printf("      CODEX      \n");
    printf("=================\n");
    	DISPLAY_CODEX(&cHead);
	printf("======================================\n");
	printf("Correcting Mispelled Words\n");
		REPLACE_WORDS(&pHead, &cHead);
	printf("======================================\n");
	printf("           Corrected Poem\n");
	printf("======================================\n");
		DISPLAY_POEM(pHead);
	printf("======================================\n");
	pFREE(&pHead);
	cFREE(&cHead);
	return 0;
}
void DISPLAY_POEM(pNode *head){
	pNode *current;
	pNode *tempNode;
	current = head;
	char *temp;
	while(current){
		temp = current->word;
	//	printf("%s\n",temp);
		
		if(strcmp(temp, ".") == 0){
			printf("%s\n",temp);
		}
		else if(strcmp(temp, ",") == 0){
			printf("%s\n",temp);
		}
		else {
			tempNode = current->next;
			
			if(strcmp(tempNode->word, ",") == 0 || strcmp(tempNode->word, ".") == 0){
				printf("%s", temp);
			}else
				printf("%s ",temp);
		}
		current = current->next;		
	}
	
}
void pFREE(pNode *(*head)){
	pNode *current = NULL;
	current = *head;
	while(current){
		free(current->word);
		free(current);
		current = current->next;
	}
	current = NULL;
}
void cFREE(cNode *(*head)){
	cNode *current = NULL;
	current = *head;
	while(current){
		free(current->word1);
		free(current->word2);
		free(current);
		current = current->next;
	}
	current = NULL;
}
void DISPLAY_CODEX(cNode *(*head)){
	cNode *current;
	current = *head;
	while(current){
		printf("%s %s\n",current->word1, current->word2);
		current = current->next;
	}
}

void REPLACE_WORDS(pNode*(*pHead), cNode *(*cHead)){
	pNode *current;
	pNode *newNode;
	pNode *end;
	cNode *cRef;
	int flag = 0;
	//creating a reference variable to the head of the codex linked list.
	
	current = *pHead;
	while(current != NULL){
		cRef = *cHead;
		while(cRef != NULL){
		//testing to see if current word matches a word in the codex
			if(strcmp(current->word, cRef->word1) == 0){
			//if it matches then check if the word it is being replaced with is skip
				if(strcmp(cRef->word2, "skip") == 0){
				//if program finds skip we remove the node
					printf("found skip\n");
					//if skip is at the head
					if(current == (*pHead)){
						flag = 1;
						(*pHead) = current->next;
						end = (*pHead);
						free(current);
						current = (*pHead);
					}else{
						end->next = current->next;
						free(current);
						current = end;					
					}
				}
				//If it didnt match with skip then we just create the new node to replace the current 
				else{
					//actual creating of the node
					newNode = (pNode*) malloc(sizeof(pNode));
					newNode->word = (char *) calloc(sizeof(char), strlen(cRef->word2)+1);
					printf("Changing \"%s\" to \"%s\"\n", current->word, cRef->word2);
					strcpy(newNode->word, cRef->word2);
					//if new node is at the head
					if(current == (*pHead)){
						//set flag to determine if we move to next node;
						flag = 1;
						(*pHead) = newNode;
						end = (*pHead);
						newNode->next = current->next;
						free(current);
						current = (*pHead);
						//if the node is anywhere else
					}else{
						end->next = newNode;
						newNode->next = current->next;
						free(current);
						current = newNode;				
					}
				}
				
				break;	
			}
			cRef = cRef->next;
		}
		if(flag == 0){
			end = current;
			current = current->next;
		}else{
			flag = 0;
		}
	}
	printf("-Spell Checking Complete-\n");

}
void LOAD_CODEX(FILE *(*file), cNode *(*head), cNode *(*end)){
	cNode *current = NULL;
	char *line = NULL;
	size_t len = 0;
	char *tok1;
	char *tok2;	
		//while codex file has a line, I am creating the linked list
	while ((getline(&line, &len, *file)) != -1) {
		//use line from getline and then split the line so that each word can be put in a node
		tok1 = strtok(line, " ");
		tok2 = strtok(NULL, "\n");
		//create te new node
		current = (cNode*)malloc(sizeof(cNode));
		//allocate memory for each word
		current->word1 = calloc((strlen(tok1)+1),sizeof(char));
		current->word2 = calloc((strlen(tok2)+1),sizeof(char)); 
		//if its the first node
		if(*head == NULL){
			//copy words
			strcpy(current->word1, tok1);
			strcpy(current->word2, tok2);
			//(current)->word1 = tok1; //Copying the words like this doesn't work
			//set next to null
			(current)->next = NULL;
		//	keep track of head
			*head = current;
		//  keep track of where next node is to be inserted
			*end = current;		
		}
		//for remaining nodes
		else{
			strcpy(current->word1, tok1);
			strcpy(current->word2, tok2);
			current->next = NULL;
			(*end)->next = current;
			(*end) = current;
		}
	}
}
void POEM_INSERT(pNode *(*head),pNode *(*end), char temp1 [20]){
	int i;
	pNode *current;
	current = (pNode*)malloc(sizeof(pNode));
	current->word = (char *)calloc(sizeof(char),strlen(temp1+1));

	if((*head) == NULL){
		strcpy(current->word, temp1);
		current->next = NULL;
		(*end) = current;
		(*head) = current;
	}
	else{
	strcpy(current->word, temp1);
	current->next = NULL;
	((*end)->next) = current;
	(*end) = current;
		
	}
}


