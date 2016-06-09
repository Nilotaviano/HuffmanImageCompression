package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.Context
import huffmanImageCompression.DSA.Node
import huffmanImageCompression.Utils.ImageUtils
import javafx.beans.property.StringPropertyBase
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.util.*

/**
 * Created by nilot on 12/05/2016.
 */
class HuffmanCommand(val avgPixelLengthProperty: StringPropertyBase, val mseProperty: StringPropertyBase) : ICommand {

    override fun execute(mat: Mat): Mat {
        val valueProbabilityMap = createEntriesMap(mat)
        encode(mat, valueProbabilityMap)

        if (!Context.imageWasFromPDIFile) {
            calculateAndSetMSE(mat)
        }

        return mat // Huffman doesn't change the displayed matrix
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

    // Ugly as fuck, but I just want to get this done
    private fun calculateAndSetMSE(compressedMat: Mat) {
        val sourceMat = ImageUtils.imageToGrayScaleMat(Context.image!!)

        // UNDO Quantization and DCT
        var uncompressedMat = QuantizationCommand().undo(compressedMat)
        uncompressedMat = DCTCommand().undo(uncompressedMat)

        mseProperty.set(ImageUtils.mse(sourceMat, uncompressedMat).toString())
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
        val nodeQueue = PriorityQueue<Node>()

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

        val codeValueMap = TreeMap<Int, String>()
        codeValueMap.putAll(codeValueList)

        return codeValueMap
    }

    private fun buildCodeValueList(node: Node, code: String): ArrayList<Pair<Int, String>> {
        val list = ArrayList<Pair<Int, String>>()

        if (node.isLeafNode()) {
            list.add(Pair(node.value, code))
        } else {
            list.addAll(buildCodeValueList(node.leftNode!!, "${code}0"))
            list.addAll(buildCodeValueList(node.rightNode!!, "${code}1"))
        }

        return list
    }

    private fun buildEncodedMatrix(codeValueMap: TreeMap<Int, String>, sourceMat: Mat): ArrayList<ArrayList<String>> {
        val bidimensionalArray = ImageUtils.createGenericBidimensionalArray(sourceMat.cols(), sourceMat.rows(), "")
        var totalPixelLength = 0
        val totalPixelCount = sourceMat.rows() * sourceMat.cols()

        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                val code = codeValueMap[sourceMat.get(row, col).first().toInt()]!!
                totalPixelLength += code.length
                bidimensionalArray[row][col] = code
            }
        }

        if (!Context.imageWasFromPDIFile) {
            val avgPixelLength = totalPixelLength.toFloat() / totalPixelCount
            avgPixelLengthProperty.set(avgPixelLength.toString())
        }

        return bidimensionalArray
    }

    override fun undo(mat: Mat): Mat {
        val huffmanValuesTable = Context.huffmanValuesTable
        val encodedMatrix = Context.encodedMatrix
        val decodedMat = Mat(Context.mat.size(), CvType.CV_32S)

        for (row in 0..encodedMatrix.size - 1) {
            for (col in 0..encodedMatrix[row].size - 1) {
                val value = huffmanValuesTable[encodedMatrix[row][col]]!!
                decodedMat.put(row, col, intArrayOf(value))
            }
        }
        avgPixelLengthProperty.set("0")
        return decodedMat
    }
}