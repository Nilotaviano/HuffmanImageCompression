package huffmanImageCompression.Utils

import javafx.scene.image.Image
import javafx.scene.image.WritablePixelFormat
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayInputStream
import java.util.*

object ImageUtils {
    // http://stackoverflow.com/a/34784966
    fun imageToMat(image: Image): Mat {
        val width = image.width.toInt()
        val height = image.height.toInt()
        val buffer = ByteArray(width * height * 4)

        val reader = image.pixelReader
        val format = WritablePixelFormat.getByteBgraInstance()
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4)

        val mat = Mat(height, width, CvType.CV_8UC4)
        mat.put(0, 0, buffer)
        return mat
    }

    // http://stackoverflow.com/a/34772385
    fun mat2Image(mat: Mat): Image {
        val buffer = MatOfByte()
        Imgcodecs.imencode(".png", mat, buffer)
        return Image(ByteArrayInputStream(buffer.toArray()))
    }

    fun convertToGrayScale(mat: Mat): Mat {
        var gray: Mat = Mat()
        if (mat.channels() == 3) {
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY)
        } else if (mat.channels() == 4) {
            Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGRA2GRAY)
        } else {
            gray = mat
        }

        return gray
    }

    fun imageToGrayScaleMat(image: Image) = convertToGrayScale(imageToMat(image))

    fun <T> createGenericBidimensionalArray(x: Int, y: Int, initialValue: T): ArrayList<ArrayList<T>> {
        val result = ArrayList<ArrayList<T>>(y)

        for (row in 0..y - 1) {
            val array = ArrayList<T>(x)

            for (i  in 0..x - 1) {
                array.add(i, initialValue)
            }

            result.add(array)
        }

        return result
    }

    fun mse(sourceMat: Mat, compressedMat: Mat): Double {
        var accumulator = 0.0;

        for (row in 0..sourceMat.rows() - 1) {
            for (col in 0..sourceMat.cols() - 1) {
                val pixel = sourceMat.get(row, col).first()
                val compressedPixel = compressedMat.get(row, col).first()
                val error = compressedPixel - pixel
                accumulator += Math.pow(error, 2.0)
            }
        }
        return accumulator / (sourceMat.height() * sourceMat.width())
    }
}