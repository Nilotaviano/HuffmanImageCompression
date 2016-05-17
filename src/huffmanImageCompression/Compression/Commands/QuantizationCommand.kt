package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.BlockIterator
import huffmanImageCompression.Context
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size

/**
 * Created by nilot on 12/05/2016.
 */
class QuantizationCommand : ICommand {

    val quantMatArray =
            arrayOf(doubleArrayOf(16.0, 11.0, 10.0, 16.0, 24.0, 40.0, 51.0, 61.0),
                    doubleArrayOf(12.0, 12.0, 14.0, 19.0, 26.0, 58.0, 60.0, 55.0),
                    doubleArrayOf(14.0, 13.0, 16.0, 24.0, 40.0, 57.0, 69.0, 56.0),
                    doubleArrayOf(14.0, 17.0, 22.0, 29.0, 51.0, 87.0, 80.0, 62.0),
                    doubleArrayOf(18.0, 22.0, 37.0, 56.0, 68.0, 109.0, 103.0, 77.0),
                    doubleArrayOf(24.0, 35.0, 55.0, 64.0, 81.0, 104.0, 113.0, 92.0),
                    doubleArrayOf(49.0, 64.0, 78.0, 87.0, 103.0, 121.0, 120.0, 101.0),
                    doubleArrayOf(72.0, 92.0, 95.0, 98.0, 112.0, 100.0, 103.0, 99.0))

    val quantMat = createQuantMat()

    override fun execute() {
        val mat = Context.mat
        val blockIterator = BlockIterator(mat)

        val resultMat = Mat(mat.size(), mat.type())
        val processedMatIterator = BlockIterator(resultMat)

        while (blockIterator.hasNext()) {
            val block = blockIterator.next()
            val resultBlock = processedMatIterator.next()

            divideByQuantMat(block, resultBlock)
        }

        Context.mat = resultMat
    }

    override fun undo() {
        val mat = Context.mat
        val blockIterator = BlockIterator(mat)

        val resultMat = Mat(mat.size(), mat.type())
        val processedMatIterator = BlockIterator(resultMat)

        while (blockIterator.hasNext()) {
            val block = blockIterator.next()
            val resultBlock = processedMatIterator.next()

            multiplyByQuantMat(block, resultBlock)
        }

        Context.mat = resultMat
    }

    fun divideByQuantMat(mat: Mat, resultMat: Mat) {
        for (y in 0..mat.rows() - 1) {
            for (x in 0..mat.cols() - 1) {
                val result = doubleArrayOf(mat.get(y, x)[0] / quantMat.get(y, x)[0])
                resultMat.put(y, x, *result)
            }
        }
    }

    fun multiplyByQuantMat(mat: Mat, resultMat: Mat) {
        for (y in 0..mat.rows() - 1) {
            for (x in 0..mat.cols() - 1) {
                val result = doubleArrayOf(mat.get(y, x)[0] * quantMat.get(y, x)[0])
                resultMat.put(y, x, *result)
            }
        }
    }

    fun createQuantMat(): Mat {
        val quantMat = Mat(Size(8.0, 8.0), CvType.CV_32F)

        for (y in 0..7) {
            for (x in 0..7) {
                quantMat.put(y, x, quantMatArray[y][x]);
            }
        }

        return quantMat; //return the matrix
    }
}