package level

import bot.FlowerBot
import bot.TurtleBot
import controllers.HeroController
import controllers.PeopleController
import graphics.GraphicCore
import graphics.renderers.BlocRenderer
import graphics.renderers.SpriteSheetRenderer
import graphics.renderers.SquareRenderer
import models.Bloc
import models.People
import models.Tube
import physics.PhysicCore
import physics.behaviours.GravityBehaviour
import yamlbeans.YamlReader
import java.awt.Dimension
import java.io.File
import java.io.FileReader
import javax.imageio.ImageIO
import kotlin.test.fail


class LevelLoader {

    fun load(filename: String) : Level{

        val reader = YamlReader(FileReader(filename))
        val config = reader.read() as Map<*, *>

        val maille = readInt(config, "params.maille")
        val width = readInt(config, "params.width")
        val height = readInt(config, "params.height")

        val hx = readInt(config, "hero.x")
        val hy = readInt(config, "hero.y")

        val hero = People(
                (hx * maille).toDouble(), (hy * maille).toDouble(),
                Dimension(40, 40), GravityBehaviour(),
                SpriteSheetRenderer(ImageIO.read(File("assets/smb_mario_sheet.png")), arrayOf(180), arrayOf(210), 0, 15, 15)
        )

        val level = Level(hero)

        /* Import des blocs */
        val blocSheet = ImageIO.read(File("assets/blocks_sheet.png"))

        (config["blocs"] as Map<*, *>).forEach {
            val body = it.value as Map<*,*>
            val type = body["type"].toString()

            val x = readInt(body, "x").toDouble() * maille
            val y = readInt(body, "y").toDouble() * maille

            val bloc = when(type) {
                "plain" -> Bloc(x, y, readInt(body, "width") * maille, readInt(body, "height") * maille, renderer = BlocRenderer(blocSheet, 32, 0, 16))
                "tube" -> Tube(x, y)
                else -> fail("Type $type is not handled (yet ?) for blocks")
            }

            if(bloc is Tube && body["flower"].toString() == "true"){
                val flower = FlowerBot(bloc)
                level.personnages.add(flower)
                level.objects.add(flower)
            }

            level.objects.add(bloc)
        }

        val mapsize = level.objects.map { it.position.x }.max()!!.toInt() + 1000

        level.objects.add(Bloc(-1.0, -1.0, mapsize, maille, BlocRenderer(blocSheet, 16, 16, 16))) // Le sol
        level.objects.add(Bloc(-1.0, 0.0, 1, 1000, SquareRenderer())) // Le mur de droite

        /* Import des bots */

        (config["bots"] as Map<*, *>).forEach {
            val body = it.value as Map<*,*>

            val y = readInt(body, "y").toDouble() * maille

            val from = readInt(body, "goes.from").toDouble() * maille
            val to = readInt(body, "goes.to").toDouble() * maille

            val type = body["type"].toString()

            val bot = when(type) {
                "turtle" -> TurtleBot(from, y, to - from)
                else -> fail("Type $type is not handled (yet ?) for bots")
            }

            level.personnages.add(bot)
            level.objects.add(bot)
        }

        /* lancement du niveau */

        val graphics = GraphicCore(level, width*maille, height*maille)
        val physics = PhysicCore(level)

        val heroController = HeroController(level, graphics, physics)
        val personnagesController = PeopleController(level, graphics, physics)

        graphics.start()
        physics.start()

        return level

    }

    fun readInt(bloc: Map<*, *>, path: String): Int {
        var current = bloc
        val split = path.split(".")

        try {
            for (i in 0 until split.size) {
                if (i < split.size - 1) {
                    current = current[split[i]] as Map<*, *>
                } else {
                    return current[split[i]].toString().toInt()
                }
            }

            throw Exception("$path is a bad path")
        } catch (e: Exception){
            println("Error while loading $path : ")
            throw e
        }
    }

}