package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.Compression.Commands.Huffman.Node
import huffmanImageCompression.Context
import huffmanImageCompression.Utils.ImageUtils
import org.opencv.core.CvType
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
        val nodeQueue = buildNodeQueue(valueProbabilityMap)

        val codeValueMap = buildCodeValueMap(nodeQueue)

        for (kv in codeValueMap) {
            Context.huffmanValuesTable[kv.key] = kv.value
        }

        Context.encodedMatrix = buildEncodedMatrix(codeValueMap, sourceMat)
    }

    /**
     * Builds a priority queue that contains a tree-like structure representing
     */
    private fun buildNodeQueue(valueProbabilityMap: TreeMap<Int, Double>): PriorityQueue<Node> {
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
        return nodeQueue
    }

    private fun buildCodeValueMap(nodeQueue: PriorityQueue<Node>): TreeMap<Int, String> {
        val codeValueList = buildCodeValueList(nodeQueue.first(), "");

        var codeValueMap = TreeMap<Int, String>()
        codeValueMap.putAll(codeValueList)

        return codeValueMap
    }

    private fun buildCodeValueList(node: Node, code: String): ArrayList<Pair<Int, String>> {
        var list = ArrayList<Pair<Int, String>>()

        if (node.isLeafNode()) {
            list.add(Pair(node.value, code))
        } else {
            list.addAll(buildCodeValueList(node.leftNode!!, "${code}0"))
            list.addAll(buildCodeValueList(node.rightNode!!, "${code}1"))
        }

        return list
    }

    private fun buildEncodedMatrix(codeValueMap: TreeMap<Int, String>, sourceMat: Mat): ArrayList<ArrayList<String>> {
        var bidimensionalArray = ImageUtils.createGenericBidimensionalArray(sourceMat.cols(), sourceMat.rows(), "")

        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                bidimensionalArray[row][col] = codeValueMap[sourceMat.get(row, col).first().toInt()]!!
            }
        }

        return bidimensionalArray
    }

    override fun undo() {
        val huffmanValuesTable = Context.huffmanValuesTable
        val encodedMatrix = Context.encodedMatrix
        val decodedMat = Mat(Context.mat.size(), CvType.CV_32S)

        for (row in 0..encodedMatrix.size - 1) {
            for (col in 0..encodedMatrix[row].size - 1) {
                val value = huffmanValuesTable[encodedMatrix[row][col]]!!
                decodedMat.put(row, col, intArrayOf(value))
            }
        }
        Context.mat = decodedMat
    }
}