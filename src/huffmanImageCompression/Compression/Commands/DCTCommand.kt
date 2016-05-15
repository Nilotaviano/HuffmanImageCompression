package huffmanImageCompression.Compression.Commands

import huffmanImageCompression.BlockIterator
import huffmanImageCompression.Context
import org.opencv.core.CvType
import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
class DCTCommand : ICommand {

    private val maxValue = 255


    override fun execute() {
        val desiredType = CvType.CV_8SC4
        val shiftAmount = -Math.ceil(maxValue.toDouble() / 2).toInt()
        val blockIterator = BlockIterator(Context.mat)
        val processedMat = Mat(Context.mat.size(), desiredType)
        val processedMatIterator = BlockIterator(processedMat)

        // O(WTF)
        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            val newBlock = processedMatIterator.next()
            shiftInterval(oldBlock, shiftAmount, desiredType).copyTo(newBlock)
            break
        }
        println(BlockIterator(processedMat).next().dump())

        Context.mat = processedMat
    }

    private fun shiftInterval(block: Mat, shift: Int, cvType: Int): Mat {
        val newBlock = Mat(block.height(), block.width(), cvType)

        for (row in 0..block.rows() - 1) {
            for (col in 0..block.cols() - 1) {
                val pixel = block.get(row, col)

                for (i in 0..pixel.size - 1) {
                    pixel[i] = pixel[i] + shift
                }

                newBlock.put(row, col, *pixel)
            }
        }
        return newBlock
    }

    override fun undo() {
        val desiredType = CvType.CV_8UC4
        val shiftAmount = Math.ceil(maxValue.toDouble() / 2).toInt()
        val blockIterator = BlockIterator(Context.mat)
        val processedMat = Mat(Context.mat.size(), desiredType)
        val processedMatIterator = BlockIterator(processedMat)

        // O(WTF)
        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            val newBlock = processedMatIterator.next()
            shiftInterval(oldBlock, shiftAmount, desiredType).copyTo(newBlock)
            break
        }
        println(BlockIterator(processedMat).next().dump())

        Context.mat = processedMat
    }
}