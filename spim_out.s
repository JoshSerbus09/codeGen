# This is one child in DeclList Node#220
# <FnDeclNode>
.text
.globl main
main:
__start:
#644 Push return address
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
#648 back up the old $fp into control link
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
#652 calculate thew new $fp
	addu  $fp, $sp, 8
#656 push space for locals
	sub   $sp, $sp, 0
#660 back up registers, and create RegisterPool
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t1, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t2, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t3, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t4, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t5, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t6, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	sw    $t7, 0($sp)	#PUSH
	subu  $sp, $sp, 4
#667 Generate code for function body
# <WriteStmt>
# Write String
	li    $v0, 4
.data
.L1:
.asciiz "Hello World!"
.text
	la    $t0, .L1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

#679 This label is for function's return statement to jump to
.L0:
#671 before return Grab result from top of stack, put it into $v0
	lw    $v0, 4($sp)	#POP
	addu  $sp, $sp, 4
#681 function exit
#683 load return address
	lw    $ra, 0($fp)
#686 save control link
	move  $t0, $fp
#689 restore FP
	lw    $fp, -4($fp)
#692 restore SP
	move  $sp, $t0
#697 restore all saved Registers
	lw    $t7, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t6, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t5, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t4, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t3, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t2, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
#695 function return
	li    $v0, 10
	syscall
# </FnDeclNode>

# This is THE END of one child in DeclList Node#220
