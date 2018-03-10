package GUI

import GUI.Panels.CenterPanel
import GUI.Panels.GraphPanel
import GUI.Panels.PopulationPanel
import neat.Population
import neat.stucture.Genome
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JScrollPane

class MainFrame(population: Population) : JFrame() {

    init {
        title = "Autoplay"
        size = Dimension(900, 600)
        isVisible = true

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)

        layout = BorderLayout()
        contentPane.add(JScrollPane(CenterPanel()), BorderLayout.CENTER)
        contentPane.add(JScrollPane(PopulationPanel(population)), BorderLayout.EAST)

        pack()
    }

}

class SimpleFrame(g: Genome) : JFrame() {

    init {
        isVisible = true
        val panel = GraphPanel(g)
        size = panel.preferredSize
        contentPane = panel
    }

}