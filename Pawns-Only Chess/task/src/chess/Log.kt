package chess

class Log(
    val input: String,
) {
    val moveSet: Set<Pair<Int, Int>>
    init {
        moveSet = initSet()
    }

    private fun initSet(): Set<Pair<Int, Int>> {
        var (x1, y1) = convertInputToSourceCoordinates(input)
        var (x2, y2) = convertInputToTargetCoordinates(input)
        val set = mutableSetOf<Pair<Int, Int>>()

        while (x1 != x2 || y1 != y2) {
            if (x1 != x2) {
                val direction = if (x1 < x2) 1 else -1
                x1 += direction
            }
            if (y1 != y2) {
                val direction = if (y1 < y2) 1 else -1
                y1 += direction
            }

            set.add(Pair(x1, y1))
        }

        return set
    }
}