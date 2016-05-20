package huffmanImageCompression

import huffmanImageCompression.Compression.FCompression
import huffmanImageCompression.Utils.FileUtils
import huffmanImageCompression.Utils.ImageUtils
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File

class MainMenuState : Application(), IState {

    override fun start(stage: Stage) {
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        if (Context.stage == null) {
            Context.stage = stage
            stage.title = "OpenCV - Projeto"
            stage.isMaximized = true
            val scene = Scene(grid)
            stage.scene = scene
        } else {
            stage.scene.root = grid
        }
        val actionTarget = Text()

        val imageView = ImageView()
        imageView.image = Context.image // In case the user returns to this screen and has already selected an image
        imageView.fitHeight = 300.0
        imageView.isPreserveRatio = true
        grid.add(imageView, 0, 0)

        val imageSelectionLabel = Label("Selecionar imagem: ")
        val imageSelectionButton = Button("...")
        imageSelectionButton.onAction = EventHandler<ActionEvent> {
            val userDirectoryPath = "${System.getProperty("user.home")}/Desktop"
            var userDirectory = File(userDirectoryPath);
            if (!userDirectory.canRead()) {
                userDirectory = File("c:/")
            }
            val fileChooser = FileChooser()
            fileChooser.title = "Selecione uma imagem"
            fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg", "*.bmp"))
            fileChooser.initialDirectory = userDirectory
            val file = fileChooser.showOpenDialog(stage)

            if (file != null) {
                val image = Image(file.inputStream())
                if (!image.isError) {
                    Context.image = ImageUtils.mat2Image(ImageUtils.imageToGrayScaleMat(image))
                    imageView.image = Context.image
                    Context.imageWasFromPDIFile = false
                } else {
                    printErrorMsg(actionTarget, "Erro ao ler imagem")
                }
            }
        }

        val pdiFileSelectionLabel = Label("Selecionar arquivo PDI: ")
        val pdiFileSelectionButton = Button("...")
        pdiFileSelectionButton.onAction = EventHandler<ActionEvent> {
            val userDirectoryPath = "${System.getProperty("user.home")}/Desktop"
            var userDirectory = File(userDirectoryPath);
            if (!userDirectory.canRead()) {
                userDirectory = File("c:/")
            }
            val fileChooser = FileChooser()
            fileChooser.title = "Selecione uma arquivo PDI"
            fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Arquivo PDI", "*.pdi"))
            fileChooser.initialDirectory = userDirectory
            val file = fileChooser.showOpenDialog(stage)

            if (file != null) {
                try {
                    Context.image = FileUtils.readImageFromPDIFile(file)
                    imageView.image = Context.image
                    Context.imageWasFromPDIFile = true

                } catch(e: Exception) {
                    printErrorMsg(actionTarget, "Erro ao ler arquivo PDI")
                }
            } else {
                printErrorMsg(actionTarget, "Erro ao ler imagem")
            }
        }

        val hbFileSelection = HBox(10.0)
        hbFileSelection.children.add(imageSelectionLabel)
        hbFileSelection.children.add(imageSelectionButton)
        hbFileSelection.children.add(Separator(Orientation.VERTICAL))
        hbFileSelection.children.add(pdiFileSelectionLabel)
        hbFileSelection.children.add(pdiFileSelectionButton)
        grid.add(hbFileSelection, 0, 2)

        val startBtn = setupButton(actionTarget, stage, "Iniciar", FCompression())
        grid.add(startBtn, 0, 3)

        grid.add(actionTarget, 0, 4)

        stage.isMaximized = true
        stage.show()
    }

    private fun setupButton(actionTarget: Text, stage: Stage, buttonText: String, state: IState): Button {
        val button = Button(buttonText)
        button.onAction = EventHandler<ActionEvent> {
            if (Context.image != null) {
                StateManager.changeState(state, stage)
            } else {
                printErrorMsg(actionTarget, "Uma imagem deve ter sido selecionada")
            }
        }
        return button
    }

    private fun printErrorMsg(actionTarget: Text, error: String) {
        actionTarget.fill = Color.FIREBRICK
        actionTarget.text = error
    }

    override fun enterState(stage: Stage) {
        start(stage)
    }

    override fun leaveState() {

    }

    companion object {
        fun launch() {
            launch(MainMenuState::class.java)
        }
    }
}
