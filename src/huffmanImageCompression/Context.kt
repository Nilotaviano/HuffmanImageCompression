package huffmanImageCompression

import huffmanImageCompression.Utils.ImageUtils
import javafx.scene.image.Image
import javafx.stage.Stage
import org.opencv.core.Mat
import java.util.*

object Context {
    var stage: Stage? = null
    private var _image: Image? = null

    var image: Image?
        get() = _image
        set(value) {
            if (value != null) {
                mat = ImageUtils.imageToGrayScaleMat(value)
            }

            _image = value
        }

    private var _mat: Mat = Mat()
    var mat: Mat
        get() = _mat
        set(value) {
            _mat = value
        }

    var huffmanValuesTable = BidirectionalHuffmanTable<String, Int>()
    var encodedMatrix = ArrayList<ArrayList<String>>(0)
    var imageWasFromPDIFile = false
}