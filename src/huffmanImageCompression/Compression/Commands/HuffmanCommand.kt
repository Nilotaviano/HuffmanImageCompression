package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.Context
import org.opencv.core.Mat
import java.util.*

/**
 * Created by nilot on 12/05/2016.
 */
class HuffmanCommand : ICommand {


    override fun execute() {
        var sourceMat = Context.mat
        val entriesSortedByValue = createEntriesMap(sourceMat)


    }

    private fun createEntriesMap(sourceMat: Mat): SortedSet<MutableMap.MutableEntry<Int, Int>> {
        val valueProbabilityMap = TreeMap<Int, Int>()

        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                val value = Math.round(sourceMat.get(row, col).first()).toInt()

                valueProbabilityMap[value] =
                        if (valueProbabilityMap.containsKey(value)) {
                            valueProbabilityMap[value]!! + 1
                        } else {
                            0
                        }
            }
        }

        val entriesSortedByValue = valueProbabilityMap.entries.toSortedSet(Comparator<Map.Entry<Int, Int>> {
            e1, e2 ->
            if (e1.value < e2.value)
                1
            else if (e1.value == e2.value)
                0
            else
                -1
        })

        return entriesSortedByValue
    }

    private fun encode(sourceMat: Mat, entriesSortedByValue: SortedSet<MutableMap.MutableEntry<Int, Int>>) {
        var codeValueTable = HashMap<String, Int>()
        var code = ""
        for (entry in entriesSortedByValue) {

        }
    }

    override fun undo() {
        println("Undo Huffman")
    }
}