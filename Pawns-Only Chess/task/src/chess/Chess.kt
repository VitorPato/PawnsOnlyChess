package chess

import kotlin.system.exitProcess

enum class PlayerInputState {
    VALID_INPUT,
    INVALID_REGEX,
    INVALID_MOVE,
    NO_PRAWN_AT_LOCATION,
    EXIT_GAME,
}

var turnCounter = 0

class Chess {
    val board = Board()
    val playerList: List<Player>
    val logPlayerMoves = mutableSetOf<Log>()
    init {
        val list = mutableListOf<Player>()

        println("First Player's name: ")
        list.add(Player(readln(), Color.WHITE))

        println("Second Player's name: ")
        list.add(Player(readln(), Color.BLACK))

        playerList = list
    }

    var currentPlayer = playerList.first()
    private set

    fun printBoard() {
        board.print()
    }

    fun playTurn() {
        turnCounter++
        val playerMove = getPlayerMove(this)
        updateBoard(playerMove)
        printBoard()
        logPlayerMove(playerMove)
        checkWinner(playerMove)
        nextPlayer()
    }

    private fun checkWinner(input: String) {
        val whitePieces = board.setOfPlayerPieces(Color.WHITE)
        val blackPieces = board.setOfPlayerPieces(Color.BLACK)

        when {
            whitePieces.any { it.coordinates().second == 7 } || blackPieces.isEmpty() -> {
                println("White Wins!")
                println("Bye!")
                exitProcess(0)
            }
            blackPieces.any { it.coordinates().second == 0 } || whitePieces.isEmpty() -> {
                println("Black Wins!")
                println("Bye!")
                exitProcess(0)
            }
            isDraw() -> {
                println("Stalemate!")
                println("Bye!")
                exitProcess(0)
            }
        }
    }



    private fun updateBoard(input: String) {
        board.movePiece(input, logPlayerMoves)
    }

    private fun nextPlayer() {
        currentPlayer = playerList.first { it != currentPlayer }
    }

    private fun currentPlayerPieceList() = board.setOfPlayerPieces(currentPlayer.color)

    private fun getPlayerMove(game: Chess): String {
        val playerName = game.currentPlayer.playerName
        var playerInput: String

        do {
            println("$playerName's turn: ")
            playerInput = readln()
        } while (validatePlayerMove(playerInput) != PlayerInputState.VALID_INPUT)

        return playerInput
    }

    private fun validatePlayerMove(
        playerMove: String,
    ): PlayerInputState {
        val playerColor = currentPlayer.color.toString().lowercase()

        when {
            playerMove.lowercase() == "exit" -> {
                println("Bye!")
                exitProcess(0)
            }
            !regexCheck(playerMove) -> {
                println("Invalid Input")
                return PlayerInputState.INVALID_REGEX
            }
            !currentPlayerPieceAtLocation(playerMove) -> {
                println("No $playerColor pawn at ${playerMove.substring(0, 2)}")
                return PlayerInputState.INVALID_MOVE
            }
            !validMovement(playerMove) -> {
                println("Invalid Input")
                return PlayerInputState.INVALID_MOVE
            }
        }

        return PlayerInputState.VALID_INPUT
    }

    private fun isDraw(): Boolean {
        val player = playerList.first { it != currentPlayer }
        val color = player.color
        val playerPieces = board.setOfPlayerPieces(color)

        return playerPieces.all { it.moveSet(this).isEmpty() }
    }

    private fun currentPlayerPieceAtLocation(input: String): Boolean {
        val (x1, y1) = convertInputToSourceCoordinates(input)

        val playerPiecesLocations = currentPlayerPieceList().map { it.coordinates() }

        return Pair(x1, y1) in playerPiecesLocations
    }

    private fun validMovement(input: String): Boolean {
        val (x1, y1) = convertInputToSourceCoordinates(input)
        val (x2, y2) = convertInputToTargetCoordinates(input)

        val piece = board.gameState[y1][x1].moveSet(this)

        return Pair(x2, y2) in piece
    }

    private fun logPlayerMove(input: String) {
        val (x1, y1) = convertInputToSourceCoordinates(input)
        logPlayerMoves.add(Log(input))

    }

}

fun regexCheck(input: String): Boolean {
    val regex = Regex("[a-hA-H][1-8][a-hA-H][1-8]")

    return regex.matches(input)
}

fun convertInputToSourceCoordinates(input: String): Pair<Int, Int> {
    val playerMove = input.substring(0, 2)

    val x = playerMove.first().uppercaseChar() - 'A'
    val y = playerMove.last() - '1'

    return Pair(x, y)
}

fun convertInputToTargetCoordinates(input: String): Pair<Int, Int> {
    val playerMove = input.substring(2, 4)

    val x = playerMove.first().uppercaseChar() - 'A'
    val y = playerMove.last() - '1'

    return Pair(x, y)
}