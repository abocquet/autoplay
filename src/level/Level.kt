package level

import models.AbstractObject
import models.People

class Level(val hero: People) {

    val objects = mutableListOf<AbstractObject>()
    val personnages = mutableListOf<People>()

    init {
        personnages.add(hero)
        objects.add(hero)
    }

}