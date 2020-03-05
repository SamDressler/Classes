#Sam Dressler
#CSCI 370, 2/18/2020
###########################################################################################################
#2-player tic-tac-toe game
########################################################################################################### 
.data
offset:			.word 0
counter: 		.word 0
count:			.word 1
here: 			.asciiz "here: \n"
startup:	      	.ascii "\n/-----------------Welcome!----------------\\\n"
		     	.ascii "| Beginning a One-Player Tic-Tac-Toe Game |\n"
		     	.asciiz"\\-----------------------------------------/\n"
choose_piece_prompt: 	.asciiz "Choose your piece: (X/O):  "
invalid_choice_message:	.asciiz "\n---Invalid choice, try again---\n"
invalid_move_message:	.asciiz "\n---Invalid move, try again---\n"
get_move_prompt: 	.asciiz "\nEnter the next move (1-9): " 
p1_wins:		.asciiz "Player 1 Wins!!!\n"
p2_wins:		.asciiz "Player 2 Wins!!!\n"
draw_message:		.asciiz "The game is a draw!\n"
starting_piece:		.asciiz "Starting piece: "
continue_prompt : 	.asciiz "\nContinue? (Y/N): "
new_game_prompt: 	.asciiz "\nNew game? (Y/N): "
new_game_message:	.asciiz "\nStarting new game..."
exit_message: 		.asciiz "\nThank you for playing!"
space:			.byte ' '
p1_piece: 		.byte '*'
p2_piece: 		.byte '*'
current_piece: 		.byte '*'
board_counter: 		.byte 1
next_move: 		.byte 1
board: 			.ascii   "\n\n        | |        1|2|3\n       -----       -----"
		        .ascii     "\n       X|X|        4|5|6\n       -----       -----"
		        .asciiz    "\n        | |        7|8|9\n"   
comb: .ascii  "235947  "      # move 1
         .ascii  "1358    "      # move 2
         .ascii  "125769  "      # move 3
         .ascii  "1756    "      # move 4
         .ascii  "19283746"      # move 5
         .ascii  "3945    "      # move 6
         .ascii  "143589  "      # move 7
         .ascii  "2579    "      # move 8
         .ascii  "153678  "      # move 9
         
         h_combos:		.ascii  "1 2 3"
			.ascii  "4 5 6"
			.asciiz "7 8 9"
v_combos:		.ascii  "1 2 4"
			.ascii  "2 5 8"
			.asciiz "3 6 9"			
d_combos:		.ascii  "1 5 9"
			.asciiz "3 5 7"
			
p1:    .asciiz  "Enter the next move: "
p2:    .asciiz  "O wins"
			
#Begin code section 
.text
start:
	la    $a0, board
	li    $v0, 4
	syscall
	
        la    $a0, p1
        li    $v0, 4
        syscall
        
        li    $v0, 12
        syscall
        move  $s0, $v0        
        li $v0, 11
        move $a0, $s0
        syscall

         li  $t0, 63           # Load $t0 with the offset.
        li  $t1, 'X'              # Load $t1 with the marker 'X'.
        sb  $t1, board($t0)       # Store the marker to the location, board+offset.
        
	la    $a0, board
	li    $v0, 4
	syscall
	
	sub   $s0, $s0, '1'
	mul   $s0, $s0, 8        		
	add   $s0, $s0, 2
	
	#lb    $t3, comb($s0)   # $t0 = '4'
	li $v0, 12
	syscall
	move $t3, $v0
	sub   $a0, $t3, '0'    # $a0 = 4
	move  $v0, $a0
	mul   $a0, $a0, 2
	add   $a0, $a0, 7
	sub   $v0, $v0, 1
	div   $v0, $v0, 3
	mul   $v0, $v0, 44
	add   $v0, $v0, $a0
	move  $s1, $v0 
	move  $a0, $v0
	li    $v0, 1
	syscall
	lb    $a0, board($s1)
        bne   $a0, 'X', L2
        
        add   $s0, $s0, 1	#increment $s0 so that we get the next element in the combination
	lb    $s0, comb($s0)   # $t0 = '5'	#load the next combination
	sub   $a0, $s0, '0'    # $a0 = 5	#convert that position to numerical from ascii
	move  $v0, $a0
	mul   $a0, $a0, 2
	add   $a0, $a0, 7
	sub   $v0, $v0, 1
	div   $v0, $v0, 3
	mul   $v0, $v0, 44
	add   $v0, $v0, $a0
	move  $s0, $v0 
	move  $a0, $v0
	li    $v0, 1
	syscall
	lb    $a0, board($s0)
        bne   $a0, 'X', L2
        la    $a0, p2
        li    $v0, 4
        syscall	
	
	
L2:	
	        		          		        		          		
	li   $v0, 10
	syscall
	
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
	
	
	li   $v0, 10
	syscall
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
	
	#print out Player 1 chose X
set_p2_piece:	
	addi $v0, $zero, 79
	sb $v0, p2_piece
	
	j valid_piece

#valid_chose_O - this sets player 1's piece to O and player 2's piece to X
valid_chose_O:		
	li $v0, 'O'
	sb $v0, p1_piece
set_p1_piece:	
	addi $v0, $zero, 88
	sb $v0, p2_piece
	
	j valid_piece	
#prints the initial board and then finds the starting piece
valid_piece:
	jal print_board
	jal random_first_move
#orubts out the current state of the board.
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
	beqz $a0, p1_turn_temp
	j p2_turn_temp

p1_turn_temp:
	li $v0, 4
	la $a0, starting_piece
	syscall
	li $v0, 11
	lb $a0, p1_piece
	syscall
	j p1_turn
p2_turn_temp:
	li $v0, 4
	la $a0, starting_piece
	syscall
	li $v0, 11
	lb $a0, p2_piece
	syscall
	j p2_turn
#p1_turn - asks for the next move from player 1. 
#Each turn consists of: Move selection, move validation, move displaying,
#			Checking for win, checking for draw, and switching turns
p1_turn:
	lb $t0, p1_piece
	sb $t0, current_piece 
	j turn
p2_turn:
	lb $t0, p2_piece
	sb $t0, current_piece
	j turn
#turn - the main function calls being executed during each turn
turn:	
get_users_move:
	li $v0, 4
	la $a0, get_move_prompt
	syscall				#print the get move prompt
	li $v0 5
	syscall				#get the move
	sw $v0, next_move
	lw $a0, next_move
	li $t0, 1
	li $t1, 9
	blt $a0, $t0, invalid_move	#if the user enters a number less than 1 print invalid choice
	bgt $a0, $t1, invalid_move	#if the user enters a number greeater than 9 print invalid choice and try again
	#load next_move into $a0 to be used in calculate offset
	lw $a0, next_move

#find_offset - find location for next move
find_offset:
	jal calculate			#call the function to get the offset
validate_move:
	jal check_move
move_valid:	
	jal set_move
	jal print_board
#check for win
#	jal check_win
#check for draw
	jal check_draw
#ask user to contiue
	jal continue
#switch what piece is currently being used
	lb $t0, current_piece
	lb $t1, p1_piece
	beq $t0, $t1, p2_turn
	j p1_turn

invalid_move:
	li $v0, 4
	la $a0, invalid_move_message
	syscall
	j get_users_move

#checkmove- check if the player's move is valid and then return
check_move:
	lw $t0, offset
	lb $t1, board($t0)
	li $t2, 32
	beq $t1, $t2, move_valid
	j invalid_move
#checkdraw-function increments a counter and if it gets to 9 then the game is over
check_draw:
	lw $t0, counter
	li $t1, 1
	add $t0, $t0, $t1	#increment counter
	sw $t0, counter		
	li $t2, 9
	lw $t0, counter
	beq $t0, $t2, draw	#jump to draw if the amount of possible plays has been met
	jr $ra
#setmove - take the offset and create the move
set_move:
	lw  $t0, offset           # Load $t0 with the offset.
	lb  $t1, current_piece    # Load $t1 with the marker 'X'.
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

#continue - user decides if they want to continue the game or start a new game
continue:
	li $v0, 4
	la $a0, continue_prompt
	syscall
	li, $v0, 12
	syscall 
	
	beq $v0, 89 , return		#continue if Y or y
	beq $v0, 121, return
	beq $v0, 78 , new_game		#exit if user enters n or N
	beq $v0, 110, new_game
	li $v0, 4 
	la $a0, invalid_choice_message
	syscall
	j continue
return:
	jr $ra
	
new_game:
	li $v0, 4
	la $a0, new_game_prompt
	syscall
	
	li, $v0, 12
	syscall 
	
	beq $v0, 89 , start_temp	#continue if Y or y
	beq $v0, 121, start_temp
	beq $v0, 78 , exit 		#exit if user enters n or N
	beq $v0, 110, exit
	
	li $v0, 4 
	la $a0, invalid_choice_message
	syscall
	j new_game

start_temp:	#reset some things before restarting the game
#reset counter
reset_counter:
	add $t0, $zero, $zero
	sw $t0, counter
	
	li $v0, 4
	la $a0, new_game_message
	syscall
	
	lb $t0, space
	sb $t0, current_piece 
reset_board:
	lw $t0, count
	move $a0, $t0
	
	jal calculate
	jal set_move
	
	lw $t0, count
	addi $t0, $t0, 1
	sw $t0, count
	li $t2, 10
	
	beq $t0, $t2, reset_count
	j reset_board 
reset_count:
	lw $t0, count
	add $t0, $zero, $zero
	sw $t0, count
	j start	
		
draw:	
	li $v0, 4
	la $a0, draw_message
	syscall		
	j new_game
#exit- displays an exit message before exiting the program
exit:
	li $v0, 4
	la $a0, exit_message
	syscall			#print exit message
	li $v0, 10 		#tell the program it is finished.
	syscall
