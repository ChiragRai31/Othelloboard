
const val EMPTY = '.'
const val PLAYER1 = 'X'
const val PLAYER2 = 'O'

class Othello {
    private val board = Array(8) { CharArray(8) { EMPTY } }

    init {
        // Initialize the starting position
        board[3][3] = PLAYER1
        board[3][4] = PLAYER2
        board[4][3] = PLAYER2
        board[4][4] = PLAYER1
    }

    fun printBoard() {
        println("  0 1 2 3 4 5 6 7")
        for (i in board.indices) {
            print("$i ")
            for (j in board[i].indices) {
                print("${board[i][j]} ")
            }
            println()
        }
    }

    fun isValidMove(row: Int, col: Int, player: Char): Boolean {
        // Check if the move is within bounds and the cell is empty
        if (row !in 0..7 || col !in 0..7 || board[row][col] != EMPTY) return false

        // Check for valid capture in all directions
        val directions = arrayOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1),
            Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1)
        )

        for (direction in directions) {
            if (canCapture(row, col, direction.first, direction.second, player)) {
                return true
            }
        }
        return false
    }

    private fun canCapture(row: Int, col: Int, dRow: Int, dCol: Int, player: Char): Boolean {
        var r = row + dRow
        var c = col + dCol
        var foundOpponent = false

        while (r in 0..7 && c in 0..7) {
            if (board[r][c] == getOpponent(player)) {
                foundOpponent = true
            } else if (board[r][c] == player) {
                return foundOpponent
            } else {
                break
            }
            r += dRow
            c += dCol
        }
        return false
    }

    private fun getOpponent(player: Char): Char {
        return if (player == PLAYER1) PLAYER2 else PLAYER1
    }

    fun makeMove(row: Int, col: Int, player: Char) {
        if (!isValidMove(row, col, player)) {
            throw IllegalArgumentException("Invalid move!")
        }

        board[row][col] = player
        flipDiscs(row, col, player)
    }

    private fun flipDiscs(row: Int, col: Int, player: Char) {
        val directions = arrayOf(
            Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1),
            Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1)
        )

        for (direction in directions) {
            flipInDirection(row, col, direction.first, direction.second, player)
        }
    }

    private fun flipInDirection(row: Int, col: Int, dRow: Int, dCol: Int, player: Char) {
        val opponent = getOpponent(player)
        var r = row + dRow
        var c = col + dCol
        val discsToFlip = mutableListOf<Pair<Int, Int>>()

        while (r in 0..7 && c in 0..7) {
            if (board[r][c] == opponent) {
                discsToFlip.add(Pair(r, c))
            } else if (board[r][c] == player) {
                for (disc in discsToFlip) {
                    board[disc.first][disc.second] = player
                }
                return
            } else {
                break
            }
            r += dRow
            c += dCol
        }
    }

    fun isBoardFull(): Boolean {
        return board.all { row -> row.all { cell -> cell != EMPTY } }
    }
}

fun main() {
    val game = Othello()
    var currentPlayer = PLAYER1

    while (true) {
        game.printBoard()

        // Check if the board is full
        if (game.isBoardFull()) {
            println("The game is over! It's a draw!")
            break
        }

        try {
            println("Player ${if (currentPlayer == PLAYER1) "1 (X)" else "2 (O)"}'s turn")
            println("Enter your move (row and column): ")
            val input = readLine() ?: throw IllegalArgumentException("Input cannot be null!")
            val (row, col) = input.split(" ").map { it.toInt() }

            game.makeMove(row, col, currentPlayer)
            currentPlayer = if (currentPlayer == PLAYER1) PLAYER2 else PLAYER1
        } catch (e: NumberFormatException) {
            println("Invalid input! Please enter numbers for row and column.")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
    }
}

