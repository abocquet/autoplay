package GUI.Panels

import GUI.NumberField
import GUI.annotations.FieldMutable
import neat.Population
import java.awt.CardLayout
import java.awt.Color
import java.awt.GridLayout
import java.lang.Math.ceil
import javax.swing.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class CenterPanel(var population: Population) : JPanel(){

    val uiConfName = JTextField("Nouvelle configuration")
    val statPanel = PlotPanel()

    val cardContainer = JPanel(CardLayout())

    val confCard = JPanel()
    val uiStartButton = JButton("Lancer l'entrainement")
    val uiTargetField = NumberField("Nombre de générations", number = 100.0, minimum = 1.0, maximum = 10_000.0, step = 10.0)
    val configPanel = JPanel()

    val runCard = JPanel()
    val uiStopButton = JButton("Arreter")
    val uiProgressBar = JProgressBar()
    val uiProgressLabel = JLabel("Entrainement en cours")

    init {
        // Partie commune

        uiConfName.font = uiConfName.font.deriveFont(20.0f)
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        add(uiConfName)
        add(statPanel)
        add(cardContainer)

        // Carte de configuration
        repaintFields()

        val startPanel = JPanel()
        startPanel.layout = BoxLayout(startPanel, BoxLayout.X_AXIS)
        startPanel.add(uiTargetField)
        startPanel.add(uiStartButton)

        confCard.layout = BoxLayout(confCard, BoxLayout.Y_AXIS)
        confCard.add(startPanel)
        confCard.add(configPanel)

        cardContainer.add(confCard, "CONFIGURE")

        // Carte d'entrainement

        runCard.layout = BoxLayout(runCard, BoxLayout.Y_AXIS)
        runCard.add(uiStopButton)
        runCard.add(uiProgressBar)
        runCard.add(uiProgressLabel)

        cardContainer.add(runCard, "TRAINING")

    }

    fun repaintFields() {

        configPanel.removeAll()

        val numberProperties = (population.config::class).memberProperties
                .filter { it.returnType.toString() == "kotlin.Int" || it.returnType.toString() == "kotlin.Double" }
                .filter { it.annotations.any { it is FieldMutable } }

        configPanel.layout = GridLayout(
                ceil(numberProperties.size / 2.0).toInt(), 2,
                10, 0
        )

        numberProperties.forEachIndexed { index, prop ->

            if(prop is KMutableProperty<*>) {

                val annotation = prop.annotations.first { it is FieldMutable } as FieldMutable

                val field = NumberField(
                        prop.name, number = prop.getter.call(population.config).toString().toDouble(),
                        minimum = annotation.min, maximum = annotation.max, step = annotation.step
                ) {
                    prop.setter.call(population.config,
                        if(prop.returnType.toString() == "kotlin.Int") { it.toInt() } else { it }
                    )
                }

                if (index % 2 == 0) {
                    val blackline = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black)
                    field.border = blackline
                }

                configPanel.add(field)
            }
        }

    }
}