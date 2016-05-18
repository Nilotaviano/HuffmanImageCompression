package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.BlockIterator
import huffmanImageCompression.Context
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
class DCTCommand : ICommand {

    private val maxValue = 255

    override fun execute() {
        val shiftAmount = -Math.floor(maxValue.toDouble() / 2).toInt()

        Context.mat.convertTo(Context.mat, CvType.CV_32FC1)

        val mat = Context.mat
        val blockIterator = BlockIterator(mat)
        val resultMat = Mat(mat.size(), mat.type())
        val resultMatIterator = BlockIterator(resultMat)

        shiftInterval(mat, mat, shiftAmount)

        while (blockIterator.hasNext()) {
            val block = blockIterator.next()
            val resultBlock = resultMatIterator.next()
            dct(block, resultBlock)
        }
        resultMat.convertTo(resultMat, CvType.CV_32S)
        Context.mat = resultMat
    }

    fun dct(sourceMat: Mat, resultMat: Mat) {
        Core.dct(sourceMat, resultMat)
    }
    


    override fun undo() {
        val shiftAmount = Math.floor(maxValue.toDouble() / 2).toInt()

        Context.mat.convertTo(Context.mat, CvType.CV_32FC1)

        val mat = Context.mat
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
        Context.mat = resultMat
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
