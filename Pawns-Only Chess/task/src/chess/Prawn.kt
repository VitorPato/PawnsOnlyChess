package chess

class Prawn(
    private var x: Int,
    private var y: Int,
    val color: Color
) {
    private var isFirstMove = true
    private val direction = if (color == Color.WHITE) 1 else -1

    fun character(): Char = if (color == Color.WHITE) 'W' else 'B'

    fun coordinates() = Pair(x, y)

    fun setCoordinates(coordinates: Pair<Int, Int>) {
        x = coordinates.first
        y = coordinates.second

        isFirstMove = false
    }

    fun moveSet(chess: Chess): Set<Pair<Int, Int>> {
        val moveset = movementSet(chess)
        val attackSet = attackSet(chess)

        return moveset.union(attackSet)
    }

    private fun attackSet(chess: Chess): Set<Pair<Int, Int>> {
        val playerColor = chess.currentPlayer.color
        val enemyColor = chess.playerList.first { it.color != playerColor }.color
        val enemyPieces = chess.board.setOfAllPieces()
            .map { it.coordinates() }
            .union(enPassant(chess))

        val set = mutableSetOf<Pair<Int, Int>>()
        set.add(Pair(x + 1, y + direction))
        set.add(Pair(x - 1, y + direction))

        return set.intersect(enemyPieces)
    }

        private fun enPassant(chess: Chess): Set<Pair<Int, Int>> {
        if (chess.logPlayerMoves.isEmpty()) {
            return emptySet()
        }

        val lastMove: Set<Pair<Int, Int>>
        lastMove = (chess.logPlayerMoves.last()).moveSet

        return lastMove
    }


    private fun movementSet(chess: Chess): Set<Pair<Int, Int>> {
        val pieces = chess.board.setOfAllPieces().map { it.coordinates() }
        val set = mutableSetOf<Pair<Int, Int>>()

        if (isFirstMove) {
            set.add(Pair(x, y + direction))
            set.add(Pair(x, y + (2 * direction)))
        } else {
            set.add(Pair(x, y + direction))
        }

        return set.subtract(pieces.toSet())
    }


}