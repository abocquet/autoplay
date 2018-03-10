package GUI.Panels

import neat.Population
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class PopulationPanel(val population: Population) : JPanel() {

    init {

        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        refresh()

    }

    fun refresh(){
        removeAll()
        population.population.forEachIndexed { index, genome ->
            val genomePanel = JPanel()
            val graphPanel = GraphPanel(genome)
            genomePanel.layout = BoxLayout(genomePanel, BoxLayout.Y_AXIS)

            val uiTitle = JLabel("Fitness : ${population.cache.fitness(genome)}")
            uiTitle.font = uiTitle.font.deriveFont(Font.BOLD)
            val uiPlayButton = JButton("Jouer")

            val subPanel = JPanel()
            subPanel.layout = BorderLayout()
            subPanel.add(uiTitle, BorderLayout.WEST)
            subPanel.add(uiPlayButton, BorderLayout.EAST)
            subPanel.preferredSize = Dimension(150, 50)

            genomePanel.add(graphPanel)
            genomePanel.add(subPanel)

            graphPanel.scale = 150.0 / graphPanel.preferredSize.width

            genomePanel.preferredSize = subPanel.preferredSize + graphPanel.preferredSize

            val blackline = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)
            genomePanel.border = blackline

            this.add(genomePanel)
        }
    }

}

private operator fun Dimension.plus(dim: Dimension): Dimension {
    return Dimension(dim.width + width, dim.height + height)
}
