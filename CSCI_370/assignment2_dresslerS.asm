#Sam Dressler CSCI 370
#PVE Tick-Tack-Toe Game. 
#System AI implemented using 1 move look ahead to find best move
.data
#game messages
startup_prompt:	      	.ascii "\n/-----------------Welcome!----------------\\\n"
		      	.ascii "| Beginning a One-Player Tic-Tac-Toe Game |\n"
		      	.asciiz"\\-----------------------------------------/"
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
user_choose_piece_prompt: .asciiz "\nPlayer, choose your piece: (x/o):  "
get_move_prompt: 	.asciiz "\nPlayer, enter your next move (1-9): " 
draw_message:		.asciiz "\n--------------------\nThe game is a draw!\n--------------------\n"
user_win_prompt:		.asciiz "\nThe User Wins!!!"
system_win_prompt:		.asciiz "\nThe System Wins!"
invalid_choice_message:	  .asciiz "\n---Invalid choice, try again---\n"
press_any_key_prompt: .asciiz "\nPress any key to start the system's turn..."
continue_prompt : 	.asciiz "\nContinue? (Y/N): "
new_game_prompt: 	.asciiz "\nNew game? (Y/N): "
new_game_message:	.asciiz "\nStarting new game..."
here: .asciiz "\nI am here"
#game variables
user_piece: 	.byte '!'
system_piece: 	.byte '!'
current_piece: 	.byte '!'
next_move: 	.byte '!'
next_move_word:  .word 0
space:		.byte ' '
user_goes_first: .word 0
system_goes_first: .word 0
offset:		.word 0
temp:		.word 0
counter:	.word 0
current_index:	.word 0
possible_wins: 	.word 0
sys_first_move: .word 0
sys_prev_move:  .word 0
user_prev_move: .word 0
best_choice_move: .word 0
num_choice:	.word 0
look_ahead_counter: .word 0
offset_counter: 	.word 0
comb_offset_counter:	.word 0 #used to store where we are at in the comb array
count:		.word 1    #index 01234567
comb: 		 	 .ascii  "235947  "      # win sequences involving move 1
		         .ascii  "1358    "      # 			|| move 2
		         .ascii  "125769  "      # 			|| move 3
		         .ascii  "1756    "      # 			|| move 4
		         .ascii  "19283746"      # 			|| move 5
		         .ascii  "3945    "      # 			|| move 6
		         .ascii  "143589  "      # 			|| move 7
		         .ascii  "2579    "      # 			|| move 8
		         .ascii  "153678  "      # 			|| move 9
		         
num_combs:		 .word 3, 2, 3, 2, 4, 2, 3, 2, 3     #the number of win combos for all moves - 1 so that the loop increments the correct # of times
									
offset_list:		 .word 9, 11, 13, 59, 61, 63, 109, 111, 113 # pre-defined the offsets used for faster way to reset board/ check for wins

corner_offsets:		 .word 9, 13, 109, 113
center_offset:		 .word 61
edge_offsets:		 .word 11, 59, 63, 111
corners:		 .word 0, 2, 6 ,8 #corner values minus one
edges:			 .word 1, 3, 5, 7
loop_counter:		 .word 0 	#variable used to run checks "num_combs" number of times
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
	
	lw $t0, system_goes_first
	beq $t0, 1, sys_turn
	lw $t0, user_goes_first
	beq $t0, 1, usr_turn
#-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/  
#	Infinite loop till game ends	      /
#-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/  
sys_turn:
	lb $v0, system_piece
	sb $v0, current_piece
	j system_turn
usr_turn:
	lb $v0, user_piece
	sb $v0, current_piece
	j user_turn
system_turn:
	li $v0, 4
	la $a0, press_any_key_prompt
	syscall
	li $v0 12
	syscall
	###############################
	#Sys first move always random
	lw $t0, sys_first_move		#load the flag to see if this is the systems first move
	beqz $t0, random_move	#if its the system first move, take a random move

	###############################
	#Normal system turn procedure
	j sys_determine_move		#figure out what move to take for the system

	
sys_turn_complete:
	jal check_sys_win	#check win
	li $v0, 4
      	la $a0, board      
	syscall  		# Print board
	jal check_draw
	jal continue
	j usr_turn

user_turn:
	
	jal get_user_move
	jal set_move
	jal checks
	li $v0, 4
      	la $a0, board      
	syscall       		# Print board
	jal continue		# check if the user wants to continue
	j sys_turn
#-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/  
sys_determine_move:
	#1st check if sys can win
	#2nd check if user can win then block
	#3rd utalize 1-move lookahead
	#4th take random_move
	j check_if_sys_can_win		#function checks if the system is one off anywhere and then chooses the move that will cause the sys to win
cant_win_in_one:
	j check_user_win
cant_block:
	jal sys_look_ahead

no_optimal_choice:
	j random_move #function returns to sys_turn_complete once move is chosen
#########################################################
#CHECK USER WIN - check if the user can win and block him if he can
check_user_win:
	sw $zero, loop_counter
	lw $s1, user_prev_move
	sub $t0, $s1, 1
	mul $t1, $t0, 4
	lw $t2, num_combs($t1)
	sw $t2, possible_wins
	mul $t3, $t0, 8
	sw $t3, comb_offset_counter
	lw $s2, possible_wins
	lw $s3, loop_counter
	lb $s4, user_piece
	
	check_block_loop:
		beq $s2, $s3, cant_block#if we've gone through the loop the possible_wins number of times, check the combs in revers to make sure we can't win
		addi $s3, $s3, 1		#increment the counter
		sw $s3, loop_counter		#store the new counter value
		
		lw $t0, comb_offset_counter	#load the offset counter
		lb $t1, comb($t0)		#get the board position indicated 
		sub $t1, $t1, '1'			#convert the byte to integer
		mul $t1, $t1, 4				#multiply by 4 to get the location in the offset array
		lw $t2, offset_list($t1)		#get the offset for that position
		lb $t3, board($t2)			#check the board at that offset
		beq $t3, $s4, user_one_off		#check if the piece in the board and the system are the same
		addi $t0, $t0, 2			#else add two to the comb_offset_counter and loop to the beginning
		sw $t0, comb_offset_counter
		j check_block_loop
	user_one_off:
		#user is one off, need to check and block
		lw $t0, comb_offset_counter	#load the index
		addi $t0, $t0, 1		#increment the index to get the last spot before we can win
		lb $t1, comb($t0)		#load whatever byte is at that position
		sub $t1, $t1, '1' 		#convert it from byte to integer
		mul $t2, $t1, 4			#multiply by 4 to get index in offset array
		lw $t3, offset_list($t2)	#load the offset from the list for the index specified

		lb $t4, board($t3)		#load the piece at that offset
		beq $t4, ' ', block
		addi $t0, $t0, 1		#if the space is not empty, increment counter and check next sequence
		sw $t0, comb_offset_counter	#store the counter
		j check_block_loop		#jump to the beginning loop

	block:
		sw $t3, offset			#store the offset for use in the take_move function
		move $s1, $t1			#store the new move in $s1 so that it is used correctly in takemove
		j take_move		#if the space is empty, take the move

##########################################################
#CHECK IF SYS CAN WIN - function that will check if the system is one off anywhere and will choose that move
check_if_sys_can_win:	
	
	sw $zero, loop_counter		#reset the loop counter each time this function is called
	lw $s1, sys_prev_move		#load the system's previous move	
	sub $t0, $s1, 1			#take prev move minus 1 to get index for num wins
	mul $t1, $t0, 4			#mult by 4 to get the allignment in the num_combs array
	lw  $t2, num_combs($t1)		#load the number of wins
	sw $t2, possible_wins		#store the number of wins in possible_wins variable
	mul $t3,$t0, 8 			#multiply the prev_move - 1 by 8 to get row in combination array
	sw $t3, comb_offset_counter		#store the index used to parse the comb array the normal way
	lw $s2, possible_wins		#load the number of wins
	lw $s3, loop_counter		#load the loop_counter
	lb $s4, current_piece		#load the current_piece
	for_each_comb_loop:
		beq $s2, $s3, cant_win_in_one#if we've gone through the loop the possible_wins number of times, check the combs in revers to make sure we can't win
		addi $s3, $s3, 1		#increment the counter
		sw $s3, loop_counter		#store the new counter value
		
		lw $t0, comb_offset_counter	#load the offset counter
		lb $t1, comb($t0)		#get the board position indicated 
		sub $t1, $t1, '1'			#convert the byte to integer
		mul $t1, $t1, 4				#multiply by 4 to get the location in the offset array
		#sw $t1, offset_counter			#store that position in the offset counter variable
		lw $t2, offset_list($t1)		#get the offset for that position
		lb $t3, board($t2)			#check the board at that offset
		beq $t3, $s4, sys_one_off		#check if the piece in the board and the system are the same
		addi $t0, $t0, 2			#else add two to the comb_offset_counter and loop to the beginning
		sw $t0, comb_offset_counter
		j for_each_comb_loop
	sys_one_off:
		#we are one off,
		lw $t0, comb_offset_counter	#load the index
		addi $t0, $t0, 1		#increment the index to get the last spot before we can win
		lb $t1, comb($t0)		#load whatever byte is at that position
		sub $t1, $t1, '1' 		#convert it from byte to integer
		mul $t2, $t1, 4			#multiply by 4 to get index in offset array
		lw $t3, offset_list($t2)	#load the offset from the list for the index specified

		lb $t4, board($t3)		#load the piece at that offset
		beq $t4, ' ', sys_temp
		addi $t0, $t0, 1		#if the space is not empty, increment counter and check next sequence
		sw $t0, comb_offset_counter	#store the counter
		j for_each_comb_loop		#jump to the beginning loop

	sys_temp:
		sw $t3, offset			#store the offset for use in the take_move function
		move $s1, $t1			#store the new move in $s1 so that it is used correctly in takemove
		j take_move		#if the space is empty, take the move

##############################################################################
#sys_look_ahead - function that looks ahead to see where the system should go
sys_look_ahead:
	lw $t0, sys_prev_move
	#move $a0, $t0
	#li $v0, 1
	#syscall
	#previous move was center
	beq $t0, 5, center_picked
	
	#previous move was a corner
	beq $t0, 1, corner_picked
	beq $t0, 3, corner_picked
	beq $t0, 7, corner_picked
	beq $t0, 9, corner_picked
	
	#previous move was an edge
	beq $t0, 2, edge_picked
	beq $t0, 4, edge_picked
	beq $t0, 6, edge_picked
	beq $t0, 8, edge_picked
	
	jr $ra	#return to sys_determine_move

center_picked:
	#center was picked last time so pick a corner
	sw $zero, look_ahead_counter	# initialize the counter
	lw $s0, look_ahead_counter			#move the counter from $t0, to $s0
	center_picked_loop:
		beq $s0, 4, no_optimal_choice 	#if none of the corners are available choose a random_move
		mul $t0, $s0, 4 	 	#get the current iteration of the loop to find the corner we are checking.
		lw $t1, corner_offsets($t0)	#load the offset for the 1st, 2nd, 3rd, or 4th corner
		lw $t7, corners($t0)
		move $s1, $t7 
		sw $t1, offset			#store the offset to make sure we are getting  the right one when we plot the move
		lb $t2, board($t1)		#load the byte that is currently at that offset in the board.
		beq $t2, ' ', take_move		#if the space is empty, take the move
		addi $s0, $s0, 1 		#increment the counter
		j center_picked_loop		#else, restart the loop. 
#####################################
#TAKE MOVE: $s1 is the (move being taken - 1)	
#	    offset is the location where the move is being placed
	take_move:
		addi $s1, $s1, 1		#add 1
		sw $s1, sys_prev_move		#add 1 to get the actual move the system takes
		lb $s2, system_piece		#load the system piece
		lw $t1, offset			#get the offset
		sb $s2, board($t1)		#place the system piece at the offset found in the previous step
		j sys_turn_complete
############################
#CORNER PICKED
corner_picked:
	lw $s0, sys_prev_move
	#check to see if opposite corner is available
	beq $s0, 1, check_move_9
	beq $s0, 3, check_move_7
	beq $s0, 7, check_move_3
	beq $s0, 9, check_move_1
opposite_move_not_avail:
	#create fork by choosing final corner 
	sw $zero, look_ahead_counter
	lw $s0, look_ahead_counter
	choose_corner_loop1:
		beq $s0, 4, check_center1
		mul $t0, $s0, 4
		lw $t1, corner_offsets($t0)
		lw $t7, corners($t0)
		move $s1, $t7
		sw $t1, offset
		lb $t2, board($t1)
		beq $t2, ' ', take_move
		addi $s0, $s0, 1
		j choose_corner_loop1	
	check_center1:
	lw $t0, center_offset	#get the offset for the center
	sw $t0, offset		#store away incase its used for plotting the move
	lb $t1, board($t0)	#load the piece in the center 
	li $s1, 4		#store the move
	beq $t1, ' ', take_move	#check if the move is taken and if not, take it
				#else do nothing
choose_edge:
	sw $zero, look_ahead_counter
	lw $s0, look_ahead_counter
	choose_edge_loop:
		beq $s0, 4, no_optimal_choice
		mul $t0, $s0, 4
		lw $t1, edge_offsets($t0)	
		lw $t7, edges($t0)
		move $s1, $t7
		sw $t1, offset
		lb $t2, board($t1)
		beq $t2, ' ', take_move
		addi $s0, $s0, 1	#increment the counter to go through each of the edges
		j choose_edge_loop
check_move_1:
	li $t0, 0			#set the index in the corner_offsets array	
	mul $t0, $t0, 4			#multiply by 4 to get on the word boundry
	lw $t1, corner_offsets($t0)	#load the value of the offset
	sw $t1, offset			#store the value 
	lb $t2, board($t1)		#get the piece at that offset value
	li $s1, 0			#store the move-1 to be used in universal take_move method
	beq $t2, ' ', take_move 
	j opposite_move_not_avail		#
check_move_3:
	li $t0, 1
	mul $t0, $t0, 4
	lw $t1, corner_offsets($t0)
	sw $t1, offset
	lb $t2, board($t1)
	li $s1, 1
	beq $t2, ' ', take_move 
	j opposite_move_not_avail
check_move_7:
	li $t0, 2
	mul $t0, $t0, 4
	lw $t1, corner_offsets($t0)
	sw $t1, offset
	lb $t2, board($t1)
	li $s1, 6
	beq $t2, ' ', take_move 
	j opposite_move_not_avail
check_move_9:
	li $t0, 3
	mul $t0, $t0, 4
	lw $t1, corner_offsets($t0)
	sw $t1, offset
	lb $t2, board($t1)
	li $s1, 8
	beq $t2, ' ', take_move 
	j opposite_move_not_avail
############################
#EdGE PICKED:
edge_picked:
	#check center
	lw $t0, center_offset	#get the offset for the center
	sw $t0, offset		#store away incase its used for plotting the move
	lb $t1, board($t0)	#load the piece in the center 
	li $s1, 4		#store the move
	beq $t1, ' ', take_move	#check if the move is taken and if not, take it
				#else do nothing
	#check corners
	choose_corner:
	sw $zero, look_ahead_counter
	lw $s0, look_ahead_counter
	choose_corner_loop:
		beq $s0, 4, no_optimal_choice
		mul $t0, $s0, 4
		lw $t1, corner_offsets($t0)
		lw $t7, corners($t0)
		move $s1, $t7
		sw $t1, offset
		lb $t2, board($t1)
		beq $t2, ' ', take_move
		addi $s0, $s0, 1
		j choose_corner_loop	
#########################################################
#USER's CHECK WIN
#check win
#Gets the move and checks if that caused a victory or a draw
check_sys_win:
	sw $zero, loop_counter
	lb $s0, current_piece	#store the current piece in s0
	lw $s1, sys_prev_move	#load the previous move (1-9)
	move $t0, $s1
	sub $t0, $t0, 1				#subtract 1 from the next move to get index
	mul $t0, $t0, 4				#multiply by 4 to get onto the correct word boundry
	lw $s2, num_combs($t0)			#get the number of possible wins and store in s2
	sw $s2, possible_wins
	#initialize the comb_offset_counter
	lw $t0, sys_prev_move		#find offset for the starting combination index i.e. 1 -> 1-0 = 0*8 = &0 => 2
	sub $t0, $t0, 1
	mul $t0, $t0, 8				#store the first index of the comb in t0 i.e. 1-> 0 is the first index
	sw $t0, comb_offset_counter		#store the offset in the comb array in the counter variable
loop2_start:					#check for win until possible wins for that move are depleated
	lw $t0, loop_counter
	lw $t1, possible_wins
	bne $t0, $t1, good			#check if the current iteration is not eqal to the max number of possible checks for that position
	jr $ra					#return to sys_turn_complete if its equal, means the move didn't cause the system to win
good:					
	addi $t0, $t0, 1			#increment counter 
	sw $t0, loop_counter			#incremet the loop counter once after checking if its less than the number of wins
	
	lw $t0, comb_offset_counter
	lb $t1, comb($t0)			#store the value of that index in t1 i.e. 1->  2 is the first spot to check 
	 
	sub $t1, $t1, '1'			#convert the byte to integer
	mul $t1, $t1, 4				#multiply by 4 to get the location in the offset array
	sw $t1, offset_counter			#store that position in the offset counter variable
	
	lw $a0, offset_list($t1)		#check what piece is at the offset counter i.e. for move 1-> this checks what piece is at 2 or offset 11
	lb $t2, board($a0)			#check the piece is in the board at that offset
	beq $s0, $t2, one_off_2			#if the pieces are the same jump to one_off win function
	lw $t0, comb_offset_counter		#if not the same, increment the comb counter by 2
	addi $t0, $t0, 2		
	sw $t0, comb_offset_counter		#increment the comb_offset_counter
	j loop2_start				#restart the loop
one_off_2:
	lw $t0, comb_offset_counter		#load where we were in the comb array
	addi, $t0, $t0, 1			#increment that location by 1
	sw $t0, comb_offset_counter		#store it for later use
	
	lb $t1, comb($t0)			#use the index in the comb array to find what the next place is we need to check
	sub $t1, $t1, '1'			#subtract 1 to turn the hex to decimal, i.e. 1->3 3-1 = 2,
	mul $t1, $t1, 4				#mult by 4 to get location in offset array
	
	lw $t2, offset_list($t1)		#get the offset at the index calculated in previous line
	lb $t3, board($t2)			#get the piece at that offset
	beq $s0, $t3, system_wins		#check if the current piece stored in $s0 and the piece at the offset are the same
	
	lw $t0, comb_offset_counter		#load comb_offset_counter to increment
	addi $t0, $t0, 1			#if not the same, increment the comb_offset_counter by 1 
	sw $t0, comb_offset_counter		#increment the comb_offset_counter
	
	j loop2_start				#restart the loop
	
##############################
#USER's CHECK WIN
#check win
#Gets the move and checks if that caused a victory or a draw
checks:
	sw $zero, loop_counter
	lb $s0, current_piece	#store the current piece in s0
	lw $s1, next_move_word	#store the move in s1
	sw $s1, user_prev_move
	move $t0, $s1
	sub $t0, $t0, 1				#subtract 1 from the next move to get index
	mul $t0, $t0, 4				#multiply by 4 to get onto the correct word boundry
	lw $s2, num_combs($t0)			#get the number of possible wins and store in s2
	sw $s2, possible_wins
	#initialize the comb_offset_counter
	lw $t0, next_move_word			#find offset for the starting combination index i.e. 1 -> 1-0 = 0*8 = &0 => 2
	sub $t0, $t0, 1
	mul $t0, $t0, 8				#store the first index of the comb in t0 i.e. 1-> 0 is the first index
	sw $t0, comb_offset_counter		#store the offset in the comb array in the counter variable
loop1_start:					#check for win until possible wins for that move are depleated
	lw $t0, loop_counter
	lw $t1, possible_wins
	beq $t0, $t1, check_draw		#check if the current iteration is less than the possible number of wins
	addi $t0, $t0, 1
	sw $t0, loop_counter			#incremet the loop counter once after checking if its less than the number of wins
	
	lw $t0, comb_offset_counter
	lb $t1, comb($t0)			#store the value of that index in t1 i.e. 1->  2 is the first spot to check 
	
	sub $t1, $t1, '1'			#convert the byte to integer
	mul $t1, $t1, 4				#multiply by 4 to get the location in the offset array
	sw $t1, offset_counter			#store that position in the offset counter variable
	
	lw $a0, offset_list($t1)		#check what piece is at the offset counter i.e. for move 1-> this checks what piece is at 2 or offset 11
	lb $t2, board($a0)			#check the piece is in the board at that offset
	beq $s0, $t2, one_off			#if the pieces are the same jump to one_off win function
	lw $t0, comb_offset_counter		#if not the same, increment the comb counter by 2
	addi $t0, $t0, 2		
	sw $t0, comb_offset_counter		#increment the comb_offset_counter
	j loop1_start				#restart the loop
one_off:
	lw $t0, comb_offset_counter		#load where we were in the comb array
	addi, $t0, $t0, 1			#increment that location by 1
	sw $t0, comb_offset_counter		#store it for later use
	
	lb $t1, comb($t0)			#use the index in the comb array to find what the next place is we need to check
	sub $t1, $t1, '1'			#subtract 1 to turn the hex to decimal, i.e. 1->3 3-1 = 2,
	mul $t1, $t1, 4				#mult by 4 to get location in offset array
	
	lw $t2, offset_list($t1)		#get the offset at the index calculated in previous line
	lb $t3, board($t2)			#get the piece at that offset
	beq $s0, $t3, user_wins			#check if the current piece stored in $s0 and the piece at the offset are the same
	
	lw $t0, comb_offset_counter		#load comb_offset_counter to increment
	addi $t0, $t0, 1			#if not the same, increment the comb_offset_counter by 1 
	sw $t0, comb_offset_counter		#increment the comb_offset_counter
	
	j loop1_start				#restart the loop
	
#checkdraw-function increments a counter and if it gets to 9 then the game is over
#check draw is called each time the program checks for a win
check_draw:
	li $t1, 9
	lw $t0, counter
	addi $t0, $t0, 1
	sw $t0, counter		
	beq $t0, $t1, draw	#jump to draw if the amount of possible plays has been met
	jr $ra   	
#Random Move
###########
# Generate a random move and plot it
# Input:
# Output: $v0 (char move), $v1 (integer offset)
random_move:
	li $t1, 1			#set the flag to indicate that it is no longer the systems first move
	sw $t1, sys_first_move
R1:  xor   $a0, $a0, $a0      # Set a seed number.
     li    $a1, 9             # random number from 1 to 9
     li    $v0, 42            # random number generator
     syscall
     beqz $a0, add_one	      #if the random move gives us a 0, add one to it
     j dont_add_one	      # if the random move doesn't give us a zero, don't add one to it
add_one:
     addi $a0, $a0, 1
dont_add_one:
     sw    $a0, sys_prev_move	#stores random number as previous sys move from 1 to 9
     sub   $a0, $a0, 1
     mul   $a2, $a0, 4		      # Get the word allignment
     lw    $v1, offset_list($a2)      # Get the offset Value for the move
     lb    $t0, board($v1)    # Check the location
     bne   $t0, ' ', R1       # Go to R1 if the cell is not empty.
     lb    $t1, system_piece     # Get the system piece
     sb    $t1, board($v1)	      # Load the word into the board
     j sys_turn_complete	
#set_move: Place the move that was validated in the previous step
set_move:
	lw $t0, offset
	lb $t1, current_piece
	sb $t1, board($t0)
	
	jr $ra
#validate move: check if the move the user took is valid
#Param $a0 - holds move, 1-9
validate_move:
	lw $t0, offset
	lb $t1, board($t0)	
	li $t2, 32
	bne $t1, $t2, invalid_move
	jr $ra
	#check if the position is already filled
#calculate the offset for plotting moves
calculate_offset:
	lb      $a0, next_move
	move	$s0, $a0
	sub 	$a0, $s0, '0' 		#convert ascii move to a numeric value
	sw 	$a0, next_move_word 	#store the integer version
	move  	$v0, $a0		#assign parameter to local variable
	mul  	$a0, $a0, 2		#multiply the value by 2
	add   	$a0, $a0, 7		#add 7 to the product from the previous line
	sub   	$v0, $v0, 1		#subtract 1 from users choice
	div   	$v0, $v0, 3		#divide by 3
	mul   	$v0, $v0, 44		#multiply quotient by 44
	add   	$v0, $v0, $a0		#get sum
	move 	$s0, $v0 		#save the value
	#move  	$a0, $v0
	#li    	$v0, 1
	#syscall
	sw $v0, offset	
	
	j validate_move
#the move is invalid because its out of range
invalid_move:
	li $v0, 4
	la $a0, invalid_choice_message
	syscall
get_user_move:
	li $v0, 4
	la $a0, get_move_prompt
	syscall
	li $v0, 12
	syscall
	sb $v0, next_move
	blt $v0, 49, invalid_move
	bgt $v0, 57, invalid_move
	
	j calculate_offset
#select user piece:
#if the user chooses piece  first then they are asked what piece they want
select_user_piece:
	li $v0, 1
	sw $v0, user_goes_first #set user goes first flag to true
	la $a0, user_choose_piece_prompt
	li $v0, 4
	syscall			#print out prompt so player 1 picks their piece
	li $v0, 12		#read byte entered by player1
	syscall 		#read user input
	sb $v0, user_piece	#store p1's choice into p1_piece 
#validate piece:
#checks if the users choice is valid
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
	li $v0, 'X'
	sb $v0, system_piece
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
	li $v0, 1
	sw $v0, system_goes_first #set system goes first flag to true
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
#####################################################
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
user_wins:
	li $v0, 4
      	la $a0, board      
	syscall 
	li  $v0, 4
	la $a0, user_win_prompt
	syscall
	j new_game
system_wins:
	li $v0, 4
      	la $a0, board      
	syscall 
	li $v0, 4
	la $a0, system_win_prompt 
	syscall
	j new_game
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
	sw $zero, system_goes_first
	sw $zero, user_goes_first
	li $t0, 1
	sw $t0, counter
	li $t1, 0
	sw $t1, sys_first_move	#set sys_first_move to true so that the system does a random move during its first turn
reset_loop:
	lw $t0, counter
	li $t1, 10
	beq $t0, $t1,reset_loop_complete
	sub $t4, $t0, 1
	mul $t4, $t4, 4 
	lw $t2, offset_list($t4)
	lb $t3, space
	sb $t3, board($t2)
	add $t0, $t0, 1
	sw $t0, counter
	j reset_loop
reset_loop_complete:
	sw $zero, counter
	j start
###################################################
#draw
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