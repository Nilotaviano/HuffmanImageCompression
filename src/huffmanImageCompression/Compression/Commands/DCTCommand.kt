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
        val shiftAmount = -Math.ceil(maxValue.toDouble() / 2).toInt()

        Context.mat.convertTo(Context.mat, CvType.CV_32FC1)

        val mat = Context.mat
        val blockIterator = BlockIterator(mat)
        val processedMat = Mat(mat.size(), mat.type())
        val processedMatIterator = BlockIterator(processedMat)

        shiftInterval(mat, mat, shiftAmount)

        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            val newBlock = processedMatIterator.next()
            dct(oldBlock, newBlock)
        }

        Context.mat = processedMat
    }

    fun dct(sourceMat: Mat, resultMat: Mat) {
        Core.dct(sourceMat, resultMat)
    }
    


    override fun undo() {
        val shiftAmount = Math.ceil(maxValue.toDouble() / 2).toInt()

        Context.mat.convertTo(Context.mat, CvType.CV_32FC1)

        val mat = Context.mat
        val blockIterator = BlockIterator(mat)
        val processedMat = Mat(mat.size(), mat.type())
        val processedMatIterator = BlockIterator(processedMat)

        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            val newBlock = processedMatIterator.next()

            idct(oldBlock, newBlock)
        }

        shiftInterval(processedMat, processedMat, shiftAmount)

        Context.mat = processedMat
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
