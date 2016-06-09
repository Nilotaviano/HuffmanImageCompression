package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.DSA.BlockIterator
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
class DCTCommand : ICommand {

    private val maxValue = 255

    override fun execute(mat: Mat): Mat {
        val shiftAmount = -Math.floor(maxValue.toDouble() / 2).toInt()

        mat.convertTo(mat, CvType.CV_32FC1)

        val blockIterator = BlockIterator(mat)
        val resultMat = Mat(mat.size(), mat.type())
        val resultMatIterator = BlockIterator(resultMat)

        println("shift")
        println("Matriz antes do shift")
        println(BlockIterator(mat).next().dump())

        shiftInterval(mat, mat, shiftAmount)

        println("shift")
        println("Matriz após shift")
        println(BlockIterator(mat).next().dump())

        while (blockIterator.hasNext()) {
            val block = blockIterator.next()
            val resultBlock = resultMatIterator.next()
            dct(block, resultBlock)
        }
        resultMat.convertTo(resultMat, CvType.CV_32S)
        return resultMat
    }

    fun dct(sourceMat: Mat, resultMat: Mat) {
        Core.dct(sourceMat, resultMat)
    }


    override fun undo(mat: Mat): Mat {
        val shiftAmount = Math.floor(maxValue.toDouble() / 2).toInt()

        mat.convertTo(mat, CvType.CV_32FC1)

        val blockIterator = BlockIterator(mat)
        val resultMat = Mat(mat.size(), mat.type())
        val resultMatIterator = BlockIterator(resultMat)

        while (blockIterator.hasNext()) {
            val block = blockIterator.next()
            val resultBlock = resultMatIterator.next()

            idct(block, resultBlock)
        }

        shiftInterval(resultMat, resultMat, shiftAmount)

        resultMat.convertTo(resultMat, CvType.CV_32S)
        return resultMat
    }

    fun idct(sourceMat: Mat, resultMat: Mat) {
        Core.idct(sourceMat, resultMat)
    }


    private fun shiftInterval(sourceMat: Mat, resultMat: Mat, shift: Int): Mat {

        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                val pixel = sourceMat.get(row, col)

                pixel[0] = pixel[0] + shift

                resultMat.put(row, col, *pixel)
            }
        }
        return resultMat
    }
}
