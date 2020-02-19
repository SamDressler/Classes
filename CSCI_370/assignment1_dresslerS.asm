#Sam Dressler
#CSCI 370, 2/18/2020
###########################################################################################################
#2-player tic-tac-toe game
#Player 1 will choose a piece and then player 2 will select a piece
#A player will then be randomly chosen to go first
#once the starting player is decided, they will choose a location 1-9 on the board and begin the game.
#after each move the program will ask the user if they want to continue. If no, the program will 
#ask the user if they want to play another game.
#once the game completes (p1Win, p2Win, or Draw) the game will ask if they want to play a new game,
#if no then the program will exit, if yes then the program will repeat.
########################################################################################################### 
.data
next_move: 		.word 1
true: 			.word 1
false:			.word 0
offset:			.word 0
startup:	      	.ascii "\n/-----------------Welcome!----------------\\\n"
		     	.ascii "| Beginning a One-Player Tic-Tac-Toe Game |\n"
		     	.asciiz"\\-----------------------------------------/\n"
choose_piece_prompt: 	.asciiz "Player 1:\nChoose your piece: (X/O):  "
invalid_choice_message:	.asciiz "\n---Invalid choice, try again---\n"
invalid_move_message:	.asciiz "\n---Invalid move, try again---\n"
p1_get_move_prompt: 	.asciiz "\nPlayer 1 - Enter the next move (1-9): " 
p2_get_move_prompt: 	.asciiz "\nPlayer 2 - Enter the next move (1-9): " 
p1_wins:		.asciiz "Player 1 Wins!!!\n"
p2_wins:		.asciiz "Player 2 Wins!!!\n"
draw:			.asciiz "The game is a draw!\n"

continue_prompt : 	.asciiz "\nContinue? (Y/N): "
new_game_prompt: 	.asciiz "\nNew game? (Y/N): "
exit_message: 		.asciiz "\nThank you for playing!"
p1_choice: 		.asciiz "\nPlayer 1 chose : "
p2_choice: 		.asciiz "\nPlayer 2 gets : "
p1_goes_first: 		.asciiz "\n********\nPlayer 1 was selected to go first!\n********"
p2_goes_first: 		.asciiz "\n********\nPlayer 2 was selected to go first!\n********"

p1_piece: 		.byte '*'
p2_piece: 		.byte '*'
current_piece: 		.byte '*'
board:  .ascii   "\n\n        | |        1|2|3\n       -----       -----"
        .ascii     "\n        | |        4|5|6\n       -----       -----"
        .asciiz    "\n        | |        7|8|9\n"   
#Begin code section 
.text
start:
	la $a0, startup
	li $v0, 4
	syscall
choosePieceStart:
	#jump to label that handles getting the choice from the user and then validating it
	j get_piece


get_piece:
	la $a0, choose_piece_prompt
	li $v0, 4
	syscall			#print out prompt so player 1 picks their piece

	li $v0, 12		#read byte entered by player1
	syscall 		#read user input

	sb $v0, p1_piece	#store p1's choice into p1_piece
#validate_piece - this checks what the user enters and if it is x/X or o/X it is valid
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
	
	j get_piece

#valid_chose_X - this sets player 1's piece to X and player 2's piece to O
valid_chose_X: 			
	li $v0,'X'
	sb $v0, p1_piece
print_p2_piece:	 
	li $v0, 4
	la $a0, p1_choice
	syscall
	
	li $v0, 11
	lb $a0, p1_piece
	syscall			#print out Player 1 chose X
set_p2_piece:	
	addi $v0, $zero, 79
	sb $v0, p2_piece
	
	li $v0, 4
	la $a0, p2_choice
	syscall
	
	li $v0, 11
	lb $a0, p2_piece	
	syscall			#print out player 2 gets O
	
	j valid_piece

#valid_chose_O - this sets player 1's piece to O and player 2's piece to X
valid_chose_O:		
	li $v0, 'O'
	sb $v0, p1_piece
print_p1_piece:	
	li $v0, 4
	la $a0, p1_choice
	syscall
	
	li $v0, 11
	lb $a0, p1_piece
	syscall
set_p1_piece:	
	addi $v0, $zero, 88
	sb $v0, p2_piece
	
	li $v0, 4
	la $a0, p2_choice
	syscall
	
	li $v0, 11
	lb $a0, p2_piece
	syscall
	
	j valid_piece	

valid_piece:
	jal print_board
	jal random_first_move
print_board:
	li $v0, 4
	la $a0, board
	syscall
	jr $ra #return to calling function
	
#random_first_move - randomly decides what player goes first, if 0 then its player1's turn if 1 then its player2's turn
random_first_move:
	li $a1, 2 
      	li $v0, 42
     	syscall
	beqz $a0, p1_turn
	j p2_turn

#p1_turn - asks for the next move from player 1. 
#Each turn consists of: Move selection, move validation, move displaying,
#			Checking for win, checking for draw, and switching turns
p1_turn:
	lb $t0, p1_piece
	sb $a0, current_piece 
	li $v0, 11
	syscall
	j first_pass
p1_turn_loop:
	li $v0, 4
	la $a0, invalid_choice_message
	syscall
first_pass:			#skip the invalid message first time through
	la $a0, p1_get_move_prompt
	li $v0, 4
	syscall

	li $v0,5
	syscall
	
	sw $v0, next_move
	lw $a0, next_move
	li $t0, 1
	li $t1, 9
	blt $a0, $t0, p1_turn_loop	#if the user enters a number less than 1 print invalid choice
	bgt $a0, $t1, p1_turn_loop 	#if the user enters a number greeater than 9 print invalid choice and try again
	#load next_move into $a0 to be used in calculate offset
	lw $a0, next_move
	#find location for next move
find_offset:
	jal calculate			#call the function to get the offset
validate_move:
#	jal check_move	
	jal set_move
	jal print_board
#check for win
#check for draw
	lw $a0, offset			#verify offset TEMP
	li $v0, 1
	syscall

		

	j p2_turn #end of player 1's turn so jump to player 2
#p2_trun - asks for the next move from player 2
#Each turn consists of: Move selection, move validation, move displaying,
#			Checking for win, checking for draw, and switching turns
p2_turn:
p2_turn_loop:
	la $a0, p2_get_move_prompt
	li $v0, 4
	syscall
	
	j exit
	j p1_turn #end of player 2's turn so jump to player 1
#checkmove- check if the player's move is valid and then return

#setmove - take the offset and create the move
set_move:
	lw  $t0, offset           # Load $t0 with the offset.
	li  $t1, 'X'              # Load $t1 with the marker 'X'.
	sb  $t1, board($t0)       # Store the marker to the location, board+offset.
	
	jr $ra
#calculate - function that calculates the offset for a player's move
#parameters - $a0 - the users valid move
#returns - $v0 the offset value
calculate: 	

	move $t7, $a0		#move the passed value to $t7
				#store the value of the offset in $v0
	#start calculating second half
	li $t1, 1
	sub $t6, $t7, $t1	# offset = [$v0 -1]			
	
	li $t1, 3		#offset = [offset / 3]
	div $t6, $t6, $t1
	
	li $t0, 44
	mul $t6, $t6, $t0	#middle * 44
	#done with second half
	#start first half		
	li $t0, 2		
	mul $t1, $t0, $t7	#2 x (user_value)	
	
	li $t0, 7
	add $t1, $t1, $t0	#2*usrval + 7
	
	add $t1, $t1, $t6	#first half + second half
	
	sw $t1, offset  
	
	#li $v0, 1
	#lw $a0, offset
	#syscall			#test print offset

	jr $ra		#return to caller
			
#exit- displays an exit message before exiting the program
exit:
	li $v0, 4
	la $a0, exit_message
	syscall			#print exit message
	li $v0, 10 		#tell the program it is finished.
	syscall
