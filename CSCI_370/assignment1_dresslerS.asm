.data	
	startup_prompt: .ascii "Start a One-Player Tic-Tac-Toe Game."
	
	   board:  .ascii   "\n\n        | |        1|2|3\n       -----       -----"
                   .ascii     "\n        | |        4|5|6\n       -----       -----"
                   .asciiz    "\n        | |        7|8|9\n"
.text 
 .globl main
 	main:
 	li $v0, 4
 	la $a0, startup_prompt
	syscall 
	
	li $v0, 4
	la $a0, board
	syscall