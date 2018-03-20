package GUI

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.lang.Math.round
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class NumberField (fieldName: String, var number: Double = 2.4, val minimum: Double = 0.0, val maximum: Double = 10.0, val step: Double = 1.0, var onChange: (Double) -> Unit = {}) : JPanel(), ActionListener, ChangeListener {


    var value : Double
        get() = number
        set(arg) {
            number = arg
            number = Math.min(number, maximum)
            number = Math.max(number, minimum)

            number = round(number * 10).toDouble() / 10

            uiText.text = number.toString()
            uiSlider.value = (number / step).toInt()
        }

    val UILabel = JLabel(fieldName)
    val uiText = JTextField(number.toString(), 5)
    val uiSlider = JSlider()

    init {
        uiSlider.minimum = (minimum / step).toInt()
        uiSlider.maximum = (maximum / step).toInt()
        uiSlider.value = (number / step).toInt()

        uiSlider.addChangeListener(this)
        uiText.addActionListener(this)

        val changePanel = JPanel()
        changePanel.layout = BoxLayout(changePanel, BoxLayout.X_AXIS)
        changePanel.add(uiSlider)
        changePanel.add(uiText)

        layout = BorderLayout()
        uiText.columns = 5

        add(UILabel, BorderLayout.WEST)
        add(changePanel, BorderLayout.EAST)
    }

    override fun actionPerformed(e: ActionEvent) {
        value = uiText.text.toDouble() * step
        this.onChange(value)
    }

    override fun stateChanged(e: ChangeEvent) {
        value = uiSlider.value * step
        this.onChange(value)
    }
}
