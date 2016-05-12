package huffmanImageCompression

import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
class BlockIterator(val mat: Mat, val blockSize: Int = 8) : Iterable<Mat>, Iterator<Mat> {

    var rowIndex: Int = 0
    var colIndex: Int = 0

    override fun hasNext(): Boolean = rowIndex < mat.rows() || colIndex < mat.cols()

    override fun next(): Mat {
        val subMat = mat.submat(rowIndex, rowIndex + blockSize - 1, colIndex, colIndex + blockSize - 1)

        if (colIndex >= mat.cols()) {
            colIndex = 0
            rowIndex += blockSize
        } else {
            colIndex += blockSize
        }

        return subMat
    }

    override fun iterator(): Iterator<Mat> = BlockIterator(mat, blockSize)
}