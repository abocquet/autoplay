package level

import models.AbstractObject
import models.People

class Level(_hero: People) {

    val objects = mutableListOf<AbstractObject>()
    val personnages = mutableListOf<People>()
    val hero = _hero

    init {
        objects.add(hero)
        personnages.add(hero)
    }

}