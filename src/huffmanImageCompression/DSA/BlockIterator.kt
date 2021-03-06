package huffmanImageCompression.DSA

import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
class BlockIterator(val mat: Mat, val blockSize: Int = 8) : Iterable<Mat>, Iterator<Mat> {

    var rowIndex: Int = 0
    var colIndex: Int = 0

    override fun hasNext(): Boolean = rowIndex < mat.rows()

    override fun next(): Mat {
        val submatRows = if (rowIndex + blockSize < mat.rows())
            rowIndex + blockSize
        else
            mat.rows() - 1

        val submatCols = if (colIndex + blockSize < mat.cols())
            colIndex + blockSize
        else
            mat.cols() - 1

        val subMat = mat.submat(rowIndex, submatRows, colIndex, submatCols)

        if (colIndex + blockSize >= mat.cols()) {
            colIndex = 0
            rowIndex += blockSize
        } else {
            colIndex += blockSize
        }

        return subMat
    }

    override fun iterator(): Iterator<Mat> = BlockIterator(mat, blockSize)
}