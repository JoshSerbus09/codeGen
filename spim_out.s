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
	sub   $sp, $sp, 8
	sub   $t8, $fp, 8
	sw    $sp, 0($t8)
	sub   $sp, $sp, 4
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
# <Assignment>
# Evaluate RHS of assign
# <IntLiteral>
	li    $t0, 1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# Evaluate LHS of assign
# <ArrayExpNode> (byRef)
# <IntLiteral>
	li    $t0, 0
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, -8
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	mul   $t1, $t1, 4
	sub   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </ArrayExpNode> (byRef) 

# Pop the two operand for assign
	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
# Save operand to the address
	sw    $t1, 0($t0)
	sw    $t1, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </Assignment>

# <Assignment>
# Evaluate RHS of assign
# <PlusNode>
# <ArrayExpNode> (byVal)
# <ArrayExpNode> (byRef)
# <IntLiteral>
	li    $t0, 0
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, -8
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	mul   $t1, $t1, 4
	sub   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </ArrayExpNode> (byRef) 

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# <ArrayExpNode> (byVal) 

# <ArrayExpNode> (byVal)
# <ArrayExpNode> (byRef)
# <IntLiteral>
	li    $t0, 0
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, -8
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	mul   $t1, $t1, 4
	sub   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </ArrayExpNode> (byRef) 

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# <ArrayExpNode> (byVal) 

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	add   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </PlusNode>

# Evaluate LHS of assign
# <IdNodeByRef>
# CurrLexLv: 1
# MySym.scopeLv: 1
	add   $t0, $fp, -12
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IdNOdeByRef>

# Pop the two operand for assign
	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
# Save operand to the address
	sw    $t1, 0($t0)
	sw    $t1, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </Assignment>

# <WriteStmt>
# <ArrayExpNode> (byVal)
# <ArrayExpNode> (byRef)
# <IntLiteral>
	li    $t0, 0
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

# <IdNodeByVal>
# Trying to retrieve variable:a
# Variable:a we want to access is local.
# get address for that variable
	add   $t0, $fp, -8
# Load variable value from that address
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# DONE with retrieving variable:a
# </IdNodeByVal>

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	#POP
	addu  $sp, $sp, 4
	mul   $t1, $t1, 4
	sub   $t0, $t0, $t1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </ArrayExpNode> (byRef) 

	lw    $t0, 4($sp)	#POP
	addu  $sp, $sp, 4
	lw    $t0, 0($t0)
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# <ArrayExpNode> (byVal) 

	li    $v0, 1
	lw    $a0, 4($sp)	#POP
	addu  $sp, $sp, 4
	syscall
# </WriteStmt>

# <WriteStmt>
# Write int
# <IntLiteral>
	li    $t0, 1
	sw    $t0, 0($sp)	#PUSH
	subu  $sp, $sp, 4
# </IntLiteral>

	li    $v0, 1
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
