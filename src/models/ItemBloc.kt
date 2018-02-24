package models

import bot.items.ItemBot
import graphics.renderers.ItemBlocRenderer
import level.Level

class ItemBloc(x: Double, y: Double, width: Int, height: Int, val itemer: (level: Level) -> ItemBot) :
        Bloc(
                x, y, width, height,
                ItemBlocRenderer()
        )
{

    var used = false

    fun use(level: Level) : ItemBot? {

        if(!used) {
            used = true
            val item = itemer(level)
            item.position = this.position.copy()
            item.physicBehaviour.speed.y = 350.0
            return item
        }

        return null
    }

}