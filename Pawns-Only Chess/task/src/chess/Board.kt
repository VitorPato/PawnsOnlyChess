package chess

import kotlin.system.exitProcess

class Board {
    var gameState = List(8) { y -> List(8) { x -> Grid(x, y)} }
        private set
    init {
        resetBoard()
    }

    private fun resetBoard() {
        gameState = List(8) { y -> List(8) { x -> Grid(x, y)} }

         for (index in 0..7) {
            gameState[1][index].spawn(index, 1, Color.WHITE)
            gameState[6][index].spawn(index, 6, Color.BLACK)
         }
    }

    fun print() {
        println("  +---+---+---+---+---+---+---+---+")

        for (y in 7 downTo 0) {
            print("${y+1} |")

            for (x in 0..7) {
                    gameState[y][x].print()
            }
            println()
            println("  +---+---+---+---+---+---+---+---+")
        }

        print(" ")
        "abcdefgh".forEach { print("   $it") }
        println()
    }

    fun setOfPlayerPieces(color: Color): Set<Prawn> {
        val set = mutableSetOf<Prawn>()

        gameState.forEach { it.forEach {
            if (it.pieceColor() == color) set.add(it.piece()!!)
        } }

        return set
    }

    fun setOfAllPieces(): Set<Prawn> {
        val set = mutableSetOf<Prawn>()

        gameState.forEach { it.forEach {
            if (it.pieceColor() != Color.NULL) set.add(it.piece()!!)
        } }

        return set
    }

    fun movePiece(
        input: String,
        logPlayerMoves: MutableSet<Log>
    ) {
        val (x1, y1) = convertInputToSourceCoordinates(input)
        val (x2, y2) = convertInputToTargetCoordinates(input)

        // Cluncky way to deal with Enpassants //
        // This is ugly                         //
        if (logPlayerMoves.isNotEmpty() && Pair(x2, y2) in logPlayerMoves.last().moveSet) {
            logPlayerMoves.last().moveSet.forEach {
                (x, y) -> gameState[y][x].reset()
            }
        }


        gameState[y1][x1].piece!!.setCoordinates(Pair(x2, y2))
        gameState[y2][x2].piece = gameState[y1][x1].piece
        gameState[y1][x1].reset()
    }
}