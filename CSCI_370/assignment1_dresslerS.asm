	.data	
startup_prompt: .asciiz "Start a One-Player Tic-Tac-Toe Game.\n"
first_user_choice: .asciiz "Choose a piece: (X/O):  "
invalid_choice: .asciiz "Invalid choice, try again." 
resp: .space 200
   board:  .ascii   "\n\n        | |        1|2|3\n       -----       -----"
           .ascii     "\n        | |        4|5|6\n       -----       -----"
           .asciiz    "\n        | |        7|8|9\n"         

	.text 	
 main:
 	li $v0, 4		#load register v0 wuh syscall code for printing a string
 	la $a0, startup_prompt  #load the address of the game started prompt into $a0 
	syscall 		#print out the string
choose_piece:	
	li $v0, 4			
	la $a0, first_user_choice
	syscall			#print out the user prompt telling them to choose a piece

	li $v0, 8 		#load register $v0 with the syscall code for reading a string
	la $a0, resp		#load the address for the response buffer
	li $a1, 200		#load the size of the buffer into register $a1
	syscall			#read the response
	
	beq $a1, X, valid_piece
	beq $a1, x, valid_piece
	beq $a1, O, valid_piece
	beq $a1, o, valid_piece
	j choose_piece:
valid_piece:		
	#li $v0, 4		
	#la $a1, resp
	#syscall			#try to print out the response
	
	li $v0, 4
	la $a0, board
	syscall
	
