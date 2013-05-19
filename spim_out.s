# This is one child in DeclList Node#220
# <FnDeclNode>
.text
_foo:
#644 Push return address
	sw    $ra, 0($sp)	#PUSH
	subu  $sp, $sp, 4
#648 back up the old $fp into control link
	sw    $fp, 0($sp)	#PUSH
	subu  $sp, $sp, 4
#652 calculate thew new $fp
	addu  $fp, $sp, 24
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
# Write int
# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, 0
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

	li    $v0, 1
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

# <WriteStmt>
# Write int
# <IdNodeByVal>
# Trying to retrieve variable:b
# Variable:b we want to access is local.
# get address for that variable
	add   $t0, $fp, -4
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:b
# </IdNodeByVal>

	li    $v0, 1
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

# <WriteStmt>
# Write int
# <IdNodeByVal>
# Trying to retrieve variable:c
# Variable:c we want to access is local.
# get address for that variable
	add   $t0, $fp, -8
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:c
# </IdNodeByVal>

	li    $v0, 1
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

# <WriteStmt>
# Write int
# <IdNodeByVal>
# Trying to retrieve variable:d
# Variable:d we want to access is local.
# get address for that variable
	add   $t0, $fp, -12
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:d
# </IdNodeByVal>

	li    $v0, 1
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

# <PlusNode>
# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, 0
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

# <IdNodeByVal>
# Trying to retrieve variable:b
# Variable:b we want to access is local.
# get address for that variable
	add   $t0, $fp, -4
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:b
# </IdNodeByVal>

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	add   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </PlusNode>

	b     .L0
#679 This label is for function's return statement to jump to
.L0:
#671 before return Grab result from top of stack, put it into $v0
	lw    $v0, 4($sp)	#POP
	addu  $sp, $sp, 4
#681 function exit
#683 load return address
	lw    $ra, -16($fp)
#686 save control link
	move  $t0, $fp
#689 restore FP
	lw    $fp, -20($fp)
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
	jr    $ra
# </FnDeclNode>

# This is THE END of one child in DeclList Node#220
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
# <IntLiteral>
	li    $t0, 3
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IntLiteral>
	li    $t0, 7
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IntLiteral>
	li    $t0, 5
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IntLiteral>
	li    $t0, 8
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

#1728#CallExpNode, check if function is called correctly
# <IdNodeForFunctionCall>
#Jump to function:foo from IdNode
	jal   _foo
# </IdNOdeForFunctionCall>

	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	li    $v0, 1
	syscall
# </WriteStmt>

#679 This label is for function's return statement to jump to
.L1:
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
