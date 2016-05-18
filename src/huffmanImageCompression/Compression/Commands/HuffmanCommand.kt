package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.Compression.Commands.Huffman.Node
import huffmanImageCompression.Context
import org.opencv.core.Mat
import java.util.*

/**
 * Created by nilot on 12/05/2016.
 */
class HuffmanCommand : ICommand {


    override fun execute() {
        var sourceMat = Context.mat
        val valueProbabilityMap = createEntriesMap(sourceMat)
        encode(sourceMat, valueProbabilityMap)

        var arrayList = ArrayList<FloatArray>()
        for (row in 0..sourceMat.rows() - 1) {
            arrayList.add(FloatArray(sourceMat.cols()))
        }

        var array = arrayList.toArray()
    }

    private fun createEntriesMap(sourceMat: Mat): TreeMap<Int, Double> {
        val valueProbabilityMap = TreeMap<Int, Double>()
        var count = 0
        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                val value = Math.floor(sourceMat.get(row, col).first()).toInt()
                count++

                valueProbabilityMap[value] =
                        if (valueProbabilityMap.containsKey(value)) {
                            valueProbabilityMap[value]!! + 1
                        } else {
                            1.0
                        }
            }
            for (key in valueProbabilityMap.keys) {
                valueProbabilityMap[key] = valueProbabilityMap[key]!!
            }
        }

        return valueProbabilityMap
    }

    private fun encode(sourceMat: Mat, valueProbabilityMap: TreeMap<Int, Double>) {
        var nodeQueue = PriorityQueue<Node>()

        for (entry in valueProbabilityMap) {
            nodeQueue.add(Node(entry.value, entry.key))
        }

        while (nodeQueue.size > 1) {
            val left = nodeQueue.last()
            nodeQueue.remove(left)

            val right = nodeQueue.last()
            nodeQueue.remove(right)

            nodeQueue.add(Node(left, right))
        }

        var codeValueList = buildCodeValueMap(nodeQueue.first(), "");

        var codeValueMap = TreeMap<String, Int>()
        codeValueMap.putAll(codeValueList)
    }

    private fun buildCodeValueMap(node: Node, code: String): ArrayList<Pair<String, Int>> {
        var list = ArrayList<Pair<String, Int>>()

        if (node.isLeafNode()) {
            list.add(Pair(code, node.value))
        } else {
            list.addAll(buildCodeValueMap(node.leftNode!!, "${code}0"))
            list.addAll(buildCodeValueMap(node.rightNode!!, "${code}1"))
        }

        return list
    }

    override fun undo() {
        println("Undo Huffman")
    }
}