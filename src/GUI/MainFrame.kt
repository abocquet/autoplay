package GUI

import GUI.Panels.CenterPanel
import GUI.Panels.PopulationPanel
import level.LevelLoader
import neat.Population
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.WindowEvent
import java.io.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.concurrent.thread


class MainFrame(var population: Population) : JFrame(), MouseListener {

    var centerPanel = CenterPanel(population)
    val popPanel = PopulationPanel(population)

    var trainThread : Thread? = null
    var shouldStopTraining = false

    val uiAppMenu = JMenu("Autoplay")
    val uiPlayMenuItem = JMenuItem("Jouer")
    val uiQuitItem = JMenuItem("Quitter")

    val uiFileMenu = JMenu("Fichier")
    val uiSaveItem = JMenuItem("Sauvegarder")
    val uiSaveUnderItem = JMenuItem("Sauvegarder sous...")
    val uiLoadItem = JMenuItem("Charger")

    var saveFile: String? = null

    init {
        // Initialisation de la fenetre
        title = "Autoplay"
        size = Dimension(1400, 600)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        layout = BorderLayout()
        contentPane.add(JScrollPane(centerPanel), BorderLayout.CENTER)
        contentPane.add(JScrollPane(popPanel), BorderLayout.EAST)

        centerPanel.statPanel.plot(listOf(), listOf(), label="Pire", color = Color.RED, dotSize = 0, linked = true)
        centerPanel.statPanel.plot(listOf(), listOf(), label="Moyenne", color = Color.BLUE, dotSize = 0, linked = true)
        centerPanel.statPanel.plot(listOf(), listOf(), label="Meilleur", color = Color.GREEN, dotSize = 0, linked = true)

        // Ajout du menu

        val uiMenuBar = JMenuBar()
        jMenuBar = uiMenuBar


        // premier menu
        uiAppMenu.add(uiPlayMenuItem)
        uiAppMenu.addSeparator()
        uiAppMenu.add(uiQuitItem)

        uiPlayMenuItem.addMouseListener(this)
        uiQuitItem.addMouseListener(this)

        // Second menu
        uiFileMenu.add(uiSaveItem)
        uiFileMenu.add(uiSaveUnderItem)
        uiFileMenu.add(uiLoadItem)

        uiSaveItem.addMouseListener(this)
        uiSaveUnderItem.addMouseListener(this)
        uiLoadItem.addMouseListener(this)

        uiMenuBar.add(uiAppMenu)
        uiMenuBar.add(uiFileMenu)


        // Ajout des listeners
        centerPanel.uiStartButton.addMouseListener(this)
        centerPanel.uiStopButton.addMouseListener(this)

        isVisible = true
    }

    override fun mousePressed(e: MouseEvent?) {
        if(e == null) {
            return
        }

        when(e.source) {
            centerPanel.uiStartButton -> startTraining()
            centerPanel.uiStopButton -> shouldStopTraining = true

            uiPlayMenuItem -> LevelLoader().load("maps/level1.yaml")
            uiQuitItem -> dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))

            uiSaveItem -> save()
            uiSaveUnderItem -> saveUnder()
            uiLoadItem -> load()
            else -> print(e.source)
        }
    }

    private fun startTraining(){
        val iterations = centerPanel.uiTargetField.value.toInt()

        centerPanel.uiProgressBar.maximum = iterations
        (centerPanel.cardContainer.layout as CardLayout).next(centerPanel.cardContainer)

        trainThread = thread {
            centerPanel.uiProgressBar.value = 0

            for(i in 0 until iterations){
                if(shouldStopTraining){
                    break ;
                }

                centerPanel.uiProgressBar.value++
                centerPanel.uiProgressLabel.text = "Avancement : ${centerPanel.uiProgressBar.value} / $iterations"
                population.evolve()

                val scores = population.scores
                centerPanel.statPanel["Pire"]?.add(population.generation.toDouble(),    scores.min()!!)
                centerPanel.statPanel["Moyenne"]?.add(population.generation.toDouble(), scores.sum() / scores.count())
                centerPanel.statPanel["Meilleur"]?.add(population.generation.toDouble(),    scores.max()!!)

                centerPanel.statPanel.repaint()
            }

            (centerPanel.cardContainer.layout as CardLayout).next(centerPanel.cardContainer)
            popPanel.refresh()
            shouldStopTraining = false

        }
    }

    private fun save(){

        if(saveFile != null){

            println("en sortie: ${population.config.elitism}")
            val fos = FileOutputStream(saveFile)
            val oos = ObjectOutputStream(fos)

            oos.writeObject(population)
            oos.close()

        } else {
            saveUnder()
        }

    }

    private fun saveUnder(){
        val uiFileChooser = JFileChooser()
        uiFileChooser.currentDirectory = File(System.getProperty("user.home"))
        uiFileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        if (uiFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveFile = "${uiFileChooser.selectedFile.absoluteFile}/${centerPanel.uiConfName.text}.autoplay"
            save()
        }

    }

    private fun load(){

        val uiFileChooser = JFileChooser()
        uiFileChooser.currentDirectory = File(System.getProperty("user.home"))
        uiFileChooser.fileFilter = FileNameExtensionFilter("Autoplay files", "autoplay")

        if (uiFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            ObjectInputStream(FileInputStream(uiFileChooser.selectedFile)).use { it ->
                val loadedPop = it.readObject()

                when (loadedPop){
                    is Population -> {
                        population = loadedPop

                        centerPanel.uiConfName.text = uiFileChooser.selectedFile.nameWithoutExtension
                        centerPanel.population = population
                        centerPanel.repaintFields()

                        popPanel.population = population
                        popPanel.refresh()

                        centerPanel.statPanel.points.forEach { it.clear() }
                    }
                    else -> println("Failed to restore Population")
                }
            }
        }

    }

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseClicked(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

}
