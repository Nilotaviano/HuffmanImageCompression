package huffmanImageCompression.Utils

import huffmanImageCompression.Context
import huffmanImageCompression.DSA.BidirectionalHashMap
import javafx.scene.image.Image
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.io.*
import java.util.*

/**
 * Created by nilot on 17/05/2016.
 */
object FileUtils {
    fun readImageFromPDIFile(file: File): Image {
        val (encodedMat, huffmanValuesTable, encodedMatrix) = deserializeFromPDIFile(file)
        Context.mat = encodedMat
        Context.huffmanValuesTable = huffmanValuesTable
        Context.encodedMatrix = encodedMatrix

        return ImageUtils.mat2Image(encodedMat)
    }

    private fun deserializeFromPDIFile(file: File): Triple<Mat, BidirectionalHashMap<String, Int>, ArrayList<ArrayList<String>>> {
        val (huffmanValuesTable, bitSet, matrixSize) = readSerializableObjects(file)
        val encodedMat = Mat(matrixSize.first, matrixSize.second, CvType.CV_32S)
        val encodedMatrix = ImageUtils.createGenericBidimensionalArray(matrixSize.second, matrixSize.first, "")
        var count = 0

        for (row in 0..matrixSize.first - 1) {
            for (col in 0..matrixSize.second - 1) {
                var matchFound = false
                var code = ""
                while (!matchFound) {
                    val bit = bitSet[count++]

                    if (bit) {
                        code += '1'
                    } else {
                        code += '0'
                    }

                    val value = huffmanValuesTable[code]

                    if (value != null) {
                        encodedMatrix[row][col] = code
                        encodedMat.put(row, col, intArrayOf(value))
                        matchFound = true
                    }
                }
            }
        }

        return Triple(encodedMat, huffmanValuesTable, encodedMatrix)
    }

    private fun readSerializableObjects(file: File): Triple<BidirectionalHashMap<String, Int>, BitSet, Pair<Int, Int>> {
        val fileInputStream = FileInputStream(file)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val objectsRead = objectInputStream.readObject()
        val triple =
                if (objectsRead is Triple<*, *, *>) {
                    objectsRead
                } else {
                    null
                }

        if (triple != null
                && triple.first is BidirectionalHashMap<*, *>
                && triple.second is BitSet
                && triple.third is Pair<*, *>) {
            @Suppress("UNCHECKED_CAST")
            return triple as Triple<BidirectionalHashMap<String, Int>, BitSet, Pair<Int, Int>>
        } else {
            throw Exception("Objeto lido não foi o esperado")
        }
    }

    fun saveToPDIFile(file: File) {
        val objects = createSerializableObject()
        val fileOutputStream = FileOutputStream(file)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)

        objectOutputStream.writeObject(objects)
        objectOutputStream.close()
        fileOutputStream.close()
    }

    private fun createSerializableObject(): Triple<BidirectionalHashMap<String, Int>, BitSet, Pair<Int, Int>> {
        val bitSet = encodedMatrixToBitSet()
        val size = Pair(Context.encodedMatrix.size, Context.encodedMatrix.first().size)
        val objects = Triple(Context.huffmanValuesTable, bitSet, size)

        return objects
    }

    private fun encodedMatrixToBitSet(): BitSet {
        val bitSet = BitSet()
        val encodedMatrix = Context.encodedMatrix
        var count = 0

        for (row in 0..encodedMatrix.size - 1) {
            for (col in 0..encodedMatrix.first().size - 1) {
                for (c in encodedMatrix[row][col]) {
                    if (c == '1') {
                        bitSet.set(count++, true)
                    } else {
                        bitSet.set(count++, false)
                    }
                }
            }
        }

        return bitSet
    }

    fun getCompressionRate(sourceFileSize: Long, compressedFileSize: Long) = (sourceFileSize.toDouble() / compressedFileSize.toDouble()).toString()
}