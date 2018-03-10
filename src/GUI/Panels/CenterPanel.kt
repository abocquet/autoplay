package GUI.Panels

import GUI.NumberField
import neat.Config
import java.awt.Color
import java.awt.GridLayout
import java.lang.Math.ceil
import javax.swing.*
import kotlin.reflect.full.memberProperties

class CenterPanel : JPanel(){

    val UIConfName = JTextField("Nouvelle configuration")
    val statPanel = StatPanel()

    val uiPlayButton = JButton("Lancer l'entrainement")

    val configPanel = JPanel()
    val configLayout = GridLayout()

    init {
        UIConfName.font = UIConfName.font.deriveFont(20.0f)

        configPanel.layout = configLayout

        val configReflexion = Config::class

        configLayout.columns = 2
        configLayout.rows = ceil(configReflexion.memberProperties.size / 2.0).toInt()


        var i = 0
        configReflexion.memberProperties.forEach {

            if(it.returnType.toString() == "kotlin.Int" || it.returnType.toString() == "kotlin.Double") {

                val m = if (it.returnType == Int::class) 0.0 else -10.0
                val M = if (it.returnType == Int::class) 100.0 else 10.0
                val s = if (it.returnType == Int::class) 1.0 else .1

                val field = NumberField(it.name, number = it.getter.call(Config).toString().toDouble(), minimum = m, maximum = M, step = s)

                if (i++ % 2 == 0) {
                    val blackline = BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black)
                    field.border = blackline
                }

                configLayout.hgap = 10
                configPanel.add(field)
            }
        }

        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(UIConfName)
        add(statPanel)
        add(uiPlayButton)
        add(configPanel)
    }
}