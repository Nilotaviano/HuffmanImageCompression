package huffmanImageCompression.Compression

import huffmanImageCompression.Context
import huffmanImageCompression.MainMenuState
import huffmanImageCompression.StateManager
import huffmanImageCompression.Utils.UIUtils
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import org.opencv.core.Mat

class CompressionController {
    @FXML var imageView: ImageView? = null
    @FXML var btnDetectEdge: Button? = null
    @FXML var imgScroll: ScrollPane? = null

    private var grayScale: Mat? = null

    fun initialize() {
        imageView?.image = Context.image
        UIUtils.setZoomScroll(imageView!!, imgScroll!!)

        grayScale = Mat()
    }

    fun restoreImage() {
        imageView?.image = Context.image
    }

    fun returnToPreviousState() {
        StateManager.changeState(MainMenuState(), Context.stage!!)
    }
}