package chess

fun main() {
    println("Pawns-Only Chess")

    gameLoop()
}

fun gameLoop() {
    val chess = Chess()
    chess.printBoard()

    while (true) {
        chess.playTurn()
    }
}