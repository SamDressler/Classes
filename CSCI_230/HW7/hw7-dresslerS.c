//Sam Dressler CS 230 Hw 7 2/29/19
#include <stdio.h>
#include <stdlib.h>
struct node
{
	int data;
	struct node *next;
};
void printList(struct node *current)
{
	printf("Linked-List traversal: low->high:\n");
	while(current){
		printf("%d\n", current->data);
		current = current->next;
	}
}

struct node* searchList(int value, struct node* head){
	struct node* currentNode = head;
	while(currentNode!= NULL){
		if(value == currentNode->data){
			printf("Value found!\n");	
			return currentNode;
		}
		else{
			currentNode = currentNode->next;				
		}		
	}
	
	printf("Value not found!\n");
	//where data is freed if value is not found 
	struct node* temp;
		while(head != NULL){
	    temp = head;
   		head = head->next;
		free(temp);
        temp = NULL;
	}
	exit(0);
}

int main(void)
{
	struct node *head, *newNode, *currentNode;
	head = NULL;
	for(int i = 0; i < 10 ; i ++){
		if(head == NULL){
			head =(struct node*)malloc(sizeof(struct node));
			head->data = i;
			head->next = NULL;
		}
		else{
			currentNode = head;			
			while(currentNode->next != NULL){
				currentNode = currentNode->next;
			}
		
			newNode = (struct node*)malloc(sizeof(struct node));
			newNode->data = i;
			newNode->next = NULL;
			currentNode->next = newNode;			
		}
		
	}	
	printList(head);		
	
	int search = 0;
	printf("Enter value to search for: ");
	scanf("%d",&search);
	printf("[%d, %d]\n",search, (searchList(search,head)->data));	
    struct node* temp;
		while(head != NULL){
	        temp = head;
   		    head = head->next;
		    free(temp);
            temp = NULL;
	}
	return 0;





}

