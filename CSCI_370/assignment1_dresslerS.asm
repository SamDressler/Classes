.data	
startup_prompt:      	.ascii "\n/-----------------Welcome!----------------\\\n"
		     	.ascii "| Beginning a One-Player Tic-Tac-Toe Game |\n"
		     	.asciiz"\\-----------------------------------------/\n"
choose_piece_prompt: 	.asciiz "Player 1:\nChoose your piece: (X/O):  "
invalid_choice_message:	.asciiz "\n---Invalid choice, try again---\n"
invalid_move_message:	.asciiz "\n---Invalid move, try again---\n"

p1_select_move_prompt: 	.asciiz "Player 1:\nEnter the next move (1-9): " 
p2_select_move_prompt: 	.asciiz "Player 2:\nEnter the next move (1-9): " 
p1_wins:		.asciiz "Player 1 Wins!!!\n"
p2_wins:		.asciiz "Player 2 Wins!!!\n"
draw:			.asciiz "The game is a draw!\n"

continue_prompt : 	.asciiz "\nContinue (Y/N): "
new_game_prompt: 	.asciiz "\nNew game (Y/N): "
exit_message: 		.asciiz "\nThank you for playing!"
p1_choice: 		.asciiz "\nPlayer 1 chose : "
p2_choice: 		.asciiz "\nPlayer 2 gets : "

move: 			.byte '*'
p1_piece: 		.byte '*'
p2_piece: 		.byte '*'
cur_piece: 		.byte '*'

true: 			.word 1
false:			.word 0
board:  .ascii   "\n\n        | |        1|2|3\n       -----       -----"
        .ascii     "\n        | |        4|5|6\n       -----       -----"
        .asciiz    "\n        | |        7|8|9\n"        
         
winning_combinations: 	
		#horizontal win conditions
		.ascii "1 2 3"
		.ascii "4 5 6"
		.ascii "7 8 9"
		#vertical win conditions
		.ascii "1 4 7"
		.ascii "2 5 8"
		.ascii "3 6 9"
		#diagonal win conditions
		.ascii "1 5 9"
		.ascii "7 5 3"
draw_combination:
		.ascii "1 2 3 4 5 6 7 8 9"
.text 	
##################################################################################################
#Main Function 
main:
 	li $v0, 4		#load register v0 wuh syscall code for printing a string
 	la $a0, startup_prompt  #load the address of the game started prompt into $a0 
	syscall 		#print out the string
	
	jal choose_piece	#prompt the player to choose their piece

validated:	
	
	jal print_board		#print out the board with the index
	
jump2:		
	j continue 
	

######### Code the decides what happens 
#first_move - procedure that deceides which player goes first 
first_move:
	li $a1, 2 
      	li $v0, 42
     	syscall
     	beqz $a0, p1_first
     	
p1_first: 
	sb $t0, p1_piece
	lb $t0, cur_piece
	
#select_move prints prompt asking what 
select_move:
	
	li $v0, 4
	la $a0, p1_select_move_prompt
	syscall

	li $v0, 12
	syscall
	
	lb $v0, move
	
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	beq $v0, 49, invalid_move
	
new_game:
	li $v0, 4
	la $a0, new_game_prompt
	syscall
	
	li, $v0, 12
	syscall 
	
	beq $v0, 89 , main	#continue if Y or y
	beq $v0, 121, main
	beq $v0, 78 , exit 	#exit if user enters n or N
	beq $v0, 110, exit
exit:
	li $v0, 4
	la $a0, exit_message
	syscall			#print exit message
	li $v0, 10 		#tell the program it is finished.
	syscall
###################################################################################################
invalid_move:
	li $v0, 4
	la $a0, invalid_move_message
	

print_board:
	li $v0, 4
	la $a0, board
	syscall
	
	jr $ra
continue:
	li $v0, 4
	la $a0, continue_prompt
	syscall
	
	li, $v0, 12
	syscall 
	
	beq $v0, 89 , select_move	#continue if Y or y
	beq $v0, 121, select_move
	beq $v0, 78 , new_game		#exit if user enters n or N
	beq $v0, 110, new_game
	
	
choose_piece:
	li $v0, 4
	la $a0, choose_piece_prompt
	syscall			#print out prompt so player 1 picks their piece

	li $v0, 12		#read byte entered by player1
	syscall 		#read user input

	sb $v0, p1_piece	#store p1's choice into p1_piece

validate_piece:
	lb $v0, p1_piece	#load player 1's piece to see if it is valid
	
	sb $v1, p2_piece
	beq $v0, 88, valid_chose_X	#check if equal to 'X'
	beq $v0, 120, valid_chose_X	#check if equal to 'x'
	beq $v0, 79, valid_chose_O	#check if equal to 'O'
	beq $v0, 111, valid_chose_O	#check if equal to 'o'
	
	li $v0, 4
	la $a0, invalid_choice_message
	syscall
	
	j jump1
		
valid_chose_X: 			#Player 1 chose X so set player 2's piece to O
	addi $v0, $zero, 88
	sb $v0, p1_piece

	li $v0, 4
	la $a0, p1_choice
	syscall
	
	li $v0, 11
	lb $a0, p1_piece
	syscall			#print out Player 1 chose X
	
	addi $v0, $zero, 79
	sb $v0, p2_piece
	
	li $v0, 4
	la $a0, p2_choice
	syscall
	
	li $v0, 11
	lb $a0, p2_piece	
	syscall			#print out player 2 gets O
	
	j validated
valid_chose_O:			#Player 1 chose O so set player 2's piece to X	
	addi $v0, $zero, 79
	sb $v0, p1_piece
	
	li $v0, 4
	la $a0, p1_choice
	syscall
	
	li $v0, 11
	lb $a0, p1_piece
	syscall
	
	addi $v0, $zero, 88
	sb $v0, p2_piece
	
	li $v0, 4
	la $a0, p2_choice
	syscall
	
	li $v0, 11
	lb $a0, p2_piece
	syscall
	
	j validated
	
	
	

	
		
