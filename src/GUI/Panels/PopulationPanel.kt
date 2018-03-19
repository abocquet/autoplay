package GUI.Panels

import neat.Population
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class PopulationPanel(var population: Population) : JPanel() {

    init {

        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        refresh()

    }

    fun refresh(){
        removeAll()
        population.members.forEach { genome ->
            val genomePanel = JPanel()
            val graphPanel = GenomePanel(genome)
            genomePanel.layout = BoxLayout(genomePanel, BoxLayout.Y_AXIS)

            val uiTitle = JLabel("Fitness : ${population.fitness(genome)}")
            uiTitle.font = uiTitle.font.deriveFont(Font.BOLD)
            val uiPlayButton = JButton("Jouer")

            val subPanel = JPanel()
            subPanel.layout = BorderLayout()
            subPanel.add(uiTitle, BorderLayout.WEST)
            subPanel.add(uiPlayButton, BorderLayout.EAST)
            subPanel.preferredSize = Dimension(150, 50)

            genomePanel.add(graphPanel)
            genomePanel.add(subPanel)

            genomePanel.preferredSize = subPanel.preferredSize + graphPanel.preferredSize

            val blackline = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)
            genomePanel.border = blackline

            this.add(genomePanel)
            genomePanel.repaint()
        }

        validate()
        repaint()
    }

}

private operator fun Dimension.plus(dim: Dimension): Dimension {
    return Dimension(dim.width + width, dim.height + height)
}
