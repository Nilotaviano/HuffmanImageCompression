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
        val desiredType = CvType.CV_8SC1
        val shiftAmount = -Math.ceil(maxValue.toDouble() / 2).toInt()
        val blockIterator = BlockIterator(Context.mat)
        val processedMat = Mat(Context.mat.size(), desiredType)
        val processedMatIterator = BlockIterator(processedMat)

        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            println("Bloco original:")
            println(oldBlock.dump())
            val newBlock = processedMatIterator.next()
            shiftInterval(oldBlock, shiftAmount, desiredType).copyTo(newBlock)
            println("Bloco original shiftet:")
            println(oldBlock.dump())
            println("Bloco novo shifted:")
            println(newBlock.dump())
            dct(newBlock).copyTo(newBlock)
            println("Bloco novo após DCT:")
            println(newBlock.dump())
            break
        }
        println("Bloco antigo da imagem original")
        println(BlockIterator(Context.mat).next().dump())
        Context.mat = processedMat
    }

    /**
     * http://stackoverflow.com/a/8311564
     */
    fun dct(sourceMat: Mat): Mat {
        val resultMat: Mat = Mat(sourceMat.size(), sourceMat.type())

        for (col in 0..sourceMat.cols() - 1) {
            for (row in 0..sourceMat.rows() - 1) {
                resultMat.put(row, col, 0.0)
                for (i in 0..sourceMat.cols() - 1) {
                    for (j in 0..sourceMat.rows() - 1) {
                        var currentDCTMatValue = resultMat.get(row, col).first()
                        val matValue = sourceMat.get(j, i).first()
                        currentDCTMatValue += matValue * Math.cos(Math.PI / sourceMat.cols() * (i + 1 / 2) * col) * Math.cos(Math.PI / (sourceMat.rows()) * (j + 1 / 2) * row)

                        resultMat.put(row, col, currentDCTMatValue)
                    }
                }
            }
        }

        return resultMat
    }
    


    override fun undo() {
        val desiredType = CvType.CV_8UC1
        val shiftAmount = Math.ceil(maxValue.toDouble() / 2).toInt()
        val blockIterator = BlockIterator(Context.mat)
        val processedMat = Mat(Context.mat.size(), desiredType)
        val processedMatIterator = BlockIterator(processedMat)
        println("undo")
        println()
        while (blockIterator.hasNext()) {
            val oldBlock = blockIterator.next()
            val newBlock = processedMatIterator.next()
            println("Bloco antes do idct")
            println(newBlock.dump())
            idct(newBlock).copyTo(newBlock)
            println("Bloco após idct")
            println(newBlock.dump())
            shiftInterval(newBlock, shiftAmount, desiredType).copyTo(newBlock)
            println("Bloco após idct e shift")
            println(newBlock.dump())
            break
        }

        Context.mat = processedMat
    }

    fun idct(sourceMat: Mat): Mat {
        val resultMat: Mat = Mat(sourceMat.size(), sourceMat.type())

        for (col in 0..sourceMat.cols() - 1) {
            for (row in 0..sourceMat.rows() - 1) {
                resultMat.put(row, col, 1/4 * sourceMat.get(0, 0).first())

                for (i in 1..sourceMat.cols() - 1) {
                    var temp = resultMat.get(row, col).first()
                    temp += 1/2 * sourceMat.get(0, i).first()

                    resultMat.put(row, col, temp)
                }
                for (j in 1..sourceMat.rows() - 1) {
                    var temp = resultMat.get(row, col).first()
                    temp += 1/2 * sourceMat.get(j, 0).first()

                    resultMat.put(row, col, temp)
                }

                for (i in 1..sourceMat.cols() - 1) {
                    for (j in 1..sourceMat.rows() - 1) {
                        var temp = resultMat.get(row, col).first()
                        temp += sourceMat.get(j, i).first() * Math.cos(Math.PI/sourceMat.cols()*(col+ 1/2)*i)*Math.cos(Math.PI/sourceMat.rows()*(row+1/2)*j)

                        resultMat.put(row, col, temp)
                    }
                }
                resultMat.put(row, col, resultMat.get(row, col).first() * 2 / sourceMat.cols() * 2 / sourceMat.rows())
            }
        }
        return resultMat
    }


    private fun shiftInterval(block: Mat, shift: Int, cvType: Int): Mat {
        val newBlock = Mat(block.height(), block.width(), cvType)

        for (row in 0..block.rows() - 1) {
            for (col in 0..block.cols() - 1) {
                val pixel = block.get(row, col)

                pixel[0] = pixel[0] + shift

                newBlock.put(row, col, *pixel)
            }
        }
        return newBlock
    }
}
