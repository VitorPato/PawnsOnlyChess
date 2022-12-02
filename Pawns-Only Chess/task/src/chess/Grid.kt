package chess

class Grid(
    private val x: Int,
    private val y: Int,
) {
    var piece: Prawn? = null
    internal set

    fun spawn(xCoordinate: Int, yCoordinate: Int, color: Color) {
        piece = Prawn(xCoordinate, yCoordinate, color)
    }

    fun coordinates() = Pair(x, y)

    fun print() {
        var str = " "

        if (piece ==  null) {
            str += "  |"
        } else
            str += "${piece!!.character()} |"

        print(str)
    }

    fun pieceColor(): Color {
        return piece?.color ?: Color.NULL
    }

    fun moveSet(chess: Chess): Set<Pair<Int, Int>> {
        return piece?.moveSet(chess) ?: emptySet()
    }

    fun piece() = piece

    fun reset() {
        piece = null
    }
}