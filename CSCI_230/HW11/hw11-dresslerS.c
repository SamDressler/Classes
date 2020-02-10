//Sam Dressler 4/24/19 CS 230 HW 11 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
struct AdjListNode{
	char name [100];
	int cost;
	struct AdjListNode *next;
};

struct Node{
	char name[100];
	struct Node* next;
	struct AdjListNode *AL_head; //linked list of linked list implementation
	int connections;
};
//defining structures
typedef struct Node Node;
typedef struct AdjListNode AdjListNode;
//global variables
int TOTAL_COST = 0;
int SEED = 0;

//method that prints the adjacency lists. 
//Each adjacency list shows the nodes that are connected to each other in the graph and
//also displays the number of connections. This will be used to randomly pick the next node
void printAdjLists(Node *(*head)){
	Node *current;
	current = *head;
	AdjListNode *current2;
	while(current != NULL){
		if(current->AL_head->next != NULL){
			current2 = current->AL_head->next;
			printf("%s->", current->AL_head->name);
			while(current2){

				if(current2->next != NULL){
					printf("%s, %d-> ",current2->name, current2->cost);
				}else{
					printf("%s, %d\nNumber of Connections: %d\n---\n",
										current2->name, current2->cost, current->connections);
				}
				current2= current2->next;
			}
		}
		if(strcmp(current->name,"Home")==0){
			printf("%s\nconnections: %d\n",current->name, current->connections);
		}
		current = current->next;
	}

}
//method that inserts the node by finding its spot in the linked list.
void insertEdge(Node **head,AdjListNode *newNode, char *src,char *dest,int temp_cost){
	Node *current = *head;
	AdjListNode *AdjListCurrent = NULL;
	AdjListNode *AdjListTail = NULL;
	while(current){
		if(strcmp(src,current->AL_head->name)==0){
			AdjListCurrent = current->AL_head;

			if(AdjListCurrent->next == NULL){
				current->connections++;
				if(current->connections > 10) {
					printf("ERROR: Maximum number of connections passed\n");
					exit(0);
				}
				AdjListCurrent->next = newNode;
				strcpy(newNode->name, dest);
				newNode->cost = temp_cost;
				newNode->next = NULL;
				AdjListTail = newNode;
				break;
			}
			else{
				current->connections++;
				if(current->connections > 10) {
					printf("ERROR: Maximum number of connections passed\n");
					exit(0);
				}
				while(1){
					AdjListTail = AdjListCurrent;
					while(1){
						if(AdjListTail->next != NULL){
					 		AdjListTail = AdjListTail->next;
					 	}
					 	else{
					 		break;
					 	}
  				    }
					if(strcmp(AdjListCurrent->name,src)==0){
						strcpy(newNode->name,dest);
						newNode->cost = temp_cost;
						newNode->next = NULL;
						AdjListTail->next = newNode;
						AdjListTail = newNode;
						break;
					}
					else{
						current = current->next;
						AdjListCurrent = current->AL_head;
					}
				}
			break;
			}
		}
	else{
			current = current->next;
		}
	}
}
Node *findNewStartNode(char *start, Node **head){
	Node *current;
	AdjListNode *ALCurrent = NULL;
	current = *head;
	while(1){
		if(strcmp(start,current->name)==0){
			return current;
		}
		else{
			current = current->next;
			if(current == NULL){
				printf("ERROR: Location not found -Program Terminated-\n");
				exit(0);
			}
		}
	}
}
void displayChoices(AdjListNode *current, int connections){
	printf("-Places to go from %s-\n\n",current->name);
	int i =1;
	while(current){
		current = current->next;
		if(current){
			printf("Option %d : %s %d\n",i,current->name, current->cost );
			i ++;
		}
	}
}
int getRandom(int connections){
	int choice;
	SEED += time(0);
	srand(SEED);
	choice = rand() % connections;
	return choice;
}
AdjListNode *findNextNode(AdjListNode *(*AdjListHead), int connections){
	AdjListNode *AdjListCurrent = *AdjListHead;
	displayChoices(AdjListCurrent,connections);
	printf("*********************************\n");
	int choice = getRandom(connections);
	int i = 0;
	while(i <= choice){
		AdjListCurrent = AdjListCurrent->next;
		i ++;
	}
	if(strcasecmp(AdjListCurrent->name,"Home") ==0){
		printf("Randomly chose option #%d!\n",choice+1);
		printf("You have arrived home!\n");
		TOTAL_COST += AdjListCurrent->cost;
		printf("Current total: %d\n", TOTAL_COST);
		return AdjListCurrent;
	}
	else{
		printf("Randomly chose option #%d -> \"%s\" with cost: %d\n",choice+1 ,AdjListCurrent->name, AdjListCurrent->cost);
		TOTAL_COST += AdjListCurrent->cost;
		printf("Current total: %d\n", TOTAL_COST);
		return AdjListCurrent;
	}

}
int countConnections(AdjListNode *current){
	int connections = 0;
	while(1){
		current = current->next;
		if(current == NULL)return connections;
		else connections ++;
	}
}
AdjListNode *drunkardWalk(char *start, Node*(*head)){
	int connections = 0;
	Node *current = *head;
	AdjListNode *AdjListCurrent;
	AdjListCurrent = NULL;
	current = findNewStartNode(start, head);
	connections = countConnections(current->AL_head);
	if(connections == 0){
		printf("ERROR: location \"%s\" has no nodes to connect to.\n",current->AL_head->name);
		printf("Exiting program\n");
		exit(0);
	}
	printf("connections: %d\n", connections);
	AdjListCurrent = current->AL_head;
	while(1){
		printf("*********************************\n");
		AdjListCurrent = findNextNode(&AdjListCurrent,connections);
		printf("*********************************\n");
		if(strcasecmp(AdjListCurrent->name, "Home") != 0){
			printf("Next Start Location: %s\n", AdjListCurrent->name);
			//basically repeat the first step of the method with the name of the new node
			current = findNewStartNode(AdjListCurrent->name, head);
			connections = current->connections;
			if(connections == 0){
				printf("ERROR: No where to go from \"%s\"\n",current->name);
				exit(0);
			}
			AdjListCurrent = current->AL_head;	
		}
		else{
			return AdjListCurrent;
		}
	}
	
}
int main(void){
	size_t len = 0;
	char* line = NULL;
	Node *head = NULL;
	Node *tail = NULL;
	Node *current = NULL;
	AdjListNode *AdjListHead;
	AdjListNode *AdjListTail;
	AdjListNode *newNode;
	AdjListNode* endingNode;
	char *tok;
	char *src, *dest;
	char *startLocation;
	int temp_cost;
	FILE *file = fopen("hw11.data","r");
	if(file == NULL){
		printf("ERROR: \"hw11.data\" not found\n");
		exit(0);
	}
	while(1){
		getline(&line,&len,file);

		tok = strtok(line, "\n");

		if(strcmp(tok,"STOP") == 0){
			break;
		}
		if(strcmp(line,"")==0){
			printf("ERROR: File Format Error");
			break;
		}
		else{
			current = (Node*)malloc(sizeof(Node));
			AdjListHead = (AdjListNode*)malloc(sizeof(AdjListNode));
			if(head == NULL){
				strcpy(current->name,tok);
				current->next = NULL;
				current->AL_head = AdjListHead;
				current->connections = 0;
				strcpy(AdjListHead->name, tok);
				AdjListHead->next = NULL;
				head = current;
				tail = current;
			}
			else{
				
				strcpy(current->name,tok);
				current->next = NULL;
				current->AL_head = AdjListHead;
				current->connections =0;
				strcpy(AdjListHead->name, tok);
				AdjListHead->next = NULL;
				tail->next = current;
				tail = current;
			}
		}
	}
	while(1){
		getline(&line,&len,file);
		if(strcmp(line, "STOP STOP 0\n") == 0 || strcmp(line, "STOPSTOP0\n") == 0 ) break;
		src  = strtok(line, " ");
		dest = strtok(NULL, " ");
		temp_cost = atoi(strtok(NULL, "\n"));	
		//new node is the destination of the edge being added-
		newNode = (AdjListNode*)malloc(sizeof(AdjListNode));
		insertEdge(&head, newNode, src, dest, temp_cost);
	}

	getline(&line, &len, file);
	startLocation = strtok(line,"\n");
	printf("==================================\n");
	printf("|\t HW11 Sam Dressler \t|\n");
	printf("==================================\n");
	printf("Adjacency lists for each node: \n");
	printAdjLists(&head);
	printf("======================================\n");
	printf("Starting Location: %s\n",startLocation);
 	endingNode = drunkardWalk(startLocation, &head);
	printf("|  Starting Location: %s\n",startLocation);
	printf("|  Ending Location: %s\n",endingNode->name);
	printf("|  Total cost: %d\n",TOTAL_COST);
	printf("======================================\n");
	fclose(file);
	line = NULL;
    current = head;
    while(current){
        free(current);
        current = current->next;        
    }
    current = NULL;
	return 0;
}
