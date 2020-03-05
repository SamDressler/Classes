.data
#game messages
startup_prompt:	      	.ascii "\n/-----------------Welcome!----------------\\\n"
		      	.ascii "| Beginning a One-Player Tic-Tac-Toe Game |\n"
		      	.asciiz"\\-----------------------------------------/"

comb: 		 	.ascii  "235947  "      # move 1
		         .ascii  "1358    "      # move 2
		         .ascii  "125769  "      # move 3
		         .ascii  "1756    "      # move 4
		         .ascii  "19283746"      # move 5
		         .ascii  "3945    "      # move 6
		         .ascii  "143589  "      # move 7
		         .ascii  "2579    "      # move 8
		         .ascii  "153678  "      # move 9
board:	   	.ascii   "\n\n        | |        1|2|3\n       -----       -----"
        	.ascii     "\n        | |        4|5|6\n       -----       -----"
	        .asciiz    "\n        | |        7|8|9\n"
exit_message:	.asciiz "\nThank you for playing!"
system_first_prompt: 	  .asciiz "\nThe system will move first..."
user_first_prompt: 	  .asciiz "\nThe user will move first..."
system_O_prompt: 	  .asciiz "\nThe system will be playing as: 'O'"
system_X_prompt:          .asciiz "\nThe system will be playing as: 'X'"
user_X_prompt:	  .asciiz "\nThe user will be playing as : 'X'"
user_O_prompt:	  .asciiz "\nThe user will be playing as : 'O'"
user_choose_piece_prompt: .asciiz "\nChoose your piece: (x/o):  "
get_move_prompt: 	.asciiz "\nEnter the next move (1-9): " 
draw_game_message:		.asciiz "The game is a draw!\n"
invalid_choice_message:	  .asciiz "\n---Invalid choice, try again---\n"
press_any_key_prompt: .asciiz "\nPress any key to start the system's turn..."
continue_prompt : 	.asciiz "\nContinue? (Y/N): "
new_game_prompt: 	.asciiz "\nNew game? (Y/N): "
new_game_message:	.asciiz "\nStarting new game..."
#game variables
user_piece: 	.byte '!'
system_piece: 	.byte '!'
current_piece: 	.byte '!'
next_move: 	.byte '!'

############################################
#Code Section
############################################
.text
start:
	li $v0, 4
	la $a0, startup_prompt
	syscall
	
#need to decide whether the system or user goes first
piece_select:
	li $a1, 2 
      	li $v0, 42
      	syscall
	
	beqz $a0, system_select_piece 
	li $v0, 4
	la $a0, user_first_prompt #notify player they will be going first
	syscall
	
	jal select_user_piece 	#user selects piece

system_turn:
	li $v0, 4
	la $a0, press_any_key_prompt
	syscall
	li $v0 12
	syscall
	
		

	li $v0, 4
      	la $a0, board      
	syscall       		# Print board
	j user_turn

user_turn:
	li $v0, 4
      	la $a0, board      
	syscall       		# Print board
	jal get_users_move
	#jal validate_move
	#jal calculate_offset

    	j exit
    	
get_users_move:
	li $v0, 4
	la $a0, get_move_prompt
	syscall
	li $v0, 12
	syscall
	sb $v0, next_move
	
	jr $ra
select_user_piece:
	la $a0, user_choose_piece_prompt
	li $v0, 4
	syscall			#print out prompt so player 1 picks their piece
	li $v0, 12		#read byte entered by player1
	syscall 		#read user input
	sb $v0, user_piece	#store p1's choice into p1_piece 

validate_piece:
	lb $v0, user_piece
	beq $v0, 88, valid_chose_X	#check if equal to 'X'
	beq $v0, 120, valid_chose_X	#check if equal to 'x'
	beq $v0, 79, valid_chose_O	#check if equal to 'O'
	beq $v0, 111, valid_chose_O	#check if equal to 'o'
	li $v0, 4
	la $a0, invalid_choice_message
	syscall
	j select_user_piece

valid_chose_X:
	li $v0, 'O'
	sb $v0, system_piece
	li $v0, 4
	la $a0, user_X_prompt
	syscall
	li $v0, 'O'
	sb $v0, system_piece
	li $v0, 4
	la $a0, system_O_prompt
	syscall
	lb $t0, user_piece
	sb $t0, current_piece
	jr $ra
valid_chose_O:

	li $v0, 4
	la $a0, user_O_prompt
	syscall
	li $v0, 'X'
	sb $v0, system_piece
	li $v0, 4
	la $a0, system_X_prompt
	syscall
	lb $t0, user_piece
	sb $t0, current_piece
	jr $ra
system_select_piece:
	li $v0, 4
	la $a0, system_first_prompt
	syscall			#system will be going first
	
	li $a1, 2
	li $v0, 42
	syscall
	
	beqz $a0, system_O	#system gets O piece if the random select is 0
	j system_X		#system gets X piece if the random select is 1

#system was chosen to select piece first and then also was randomly given O
system_O:
	li $v0,'O'
	sb $v0, system_piece	#give system O
	li $v0, 'x'
	sb $v0, user_piece	#give user X
	li $v0, 4
	la $a0, system_O_prompt	#say that the system chose O
	syscall
	la $a0, user_X_prompt	#say that the user will be playing as X
	syscall	
	lb $t0, system_piece
	sb $t0, current_piece
	j system_turn

system_X:
	li $v0, 'X'
	sb $v0, system_piece	#give system O
	li $v0, 'O'
	sb $v0, user_piece	#give user X
	li $v0, 4
	la $a0, system_X_prompt #say that the system chose X	
	syscall
	la $a0, user_O_prompt 	#say that the user will be playing as O
	syscall
	lb $t0, system_piece
	sb $t0, current_piece
	j system_turn
#exit- displays an exit message before exiting the program
exit:
	li $v0, 4
	la $a0, exit_message
	syscall			#print exit message
	li $v0, 10 		#tell the program it is finished.
	syscall
	