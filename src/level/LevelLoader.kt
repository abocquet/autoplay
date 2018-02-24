package level

import bot.ennemies.BaseBot
import bot.ennemies.FlowerBot
import bot.ennemies.times
import bot.items.CoinItem
import bot.items.FlowerItem
import bot.items.MushroomItem
import controllers.HeroController
import controllers.PeopleController
import graphics.GraphicCore
import graphics.renderers.BlocRenderer
import graphics.renderers.SpriteSheetRenderer
import graphics.renderers.SquareRenderer
import models.Bloc
import models.ItemBloc
import models.Mario
import models.Tube
import physics.PhysicCore
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

        val maille = readInt(config, "params.maille")!!
        val width = readInt(config, "params.width")!!
        val height = readInt(config, "params.height")!!

        val hx = readInt(config, "hero.x")!!
        val hy = readInt(config, "hero.y")!!

        val hero = Mario(
            (hx * maille).toDouble(), (hy * maille).toDouble()
        )

        val level = Level(hero)

        /* Import des blocs */
        val blocSheet = ImageIO.read(File("assets/blocks_sheet.png"))
        val itemSheet = ImageIO.read(File("assets/smb_items_sheet.png"))

        val it = (config["blocs"] as MutableList<*>).iterator()
        while(it.hasNext()){
            val cur = it.next()
            val body = cur as Map<*, *>
            val type = body["type"].toString()

            if(type == "stair"){

                val x = readInt(body, "x")!!.toDouble()
                val y = readInt(body, "y")!!.toDouble()
                val size = readInt(body, "size") ?: 4

                if(body["reversed"] == "true"){
                    for(yi in 0 until size){
                        for(xi in yi until size){
                            level.objects.add(Bloc(
                                    (x+size-xi)*maille, (y+yi)*maille,
                                    maille, maille,
                                    renderer = BlocRenderer(blocSheet, 0, 16, 16))
                            )
                        }
                    }
                } else {
                    for(yi in 0 until size){
                        for(xi in yi until size){
                            level.objects.add(Bloc(
                                    (x+xi)*maille, (y+yi)*maille,
                                    maille, maille,
                                    renderer = BlocRenderer(blocSheet, 0, 16, 16))
                            )
                        }
                    }
                }

                it.remove()
            }
        }

        (config["blocs"] as MutableList<*>).forEach {
            val body = it as Map<*,*>
            val type = body["type"].toString()

            val x = readInt(body, "x")!!.toDouble() * maille
            val y = readInt(body, "y")!!.toDouble() * maille

            val bloc = when(type) {
                "plain" -> Bloc(x, y, (readInt(body, "width") ?: 1) * maille, (readInt(body, "height") ?: 1) * maille, renderer = BlocRenderer(blocSheet, 32, 0, 16))
                "stairbloc" -> Bloc(x, y,  maille, maille, renderer = BlocRenderer(blocSheet, 0, 16, 16))
                "life" -> ItemBloc(x, y, (readInt(body, "width") ?: 1) * maille, (readInt(body, "height") ?: 1) * maille, { l -> if(l.hero.life < 2) MushroomItem() else FlowerItem() })
                "coin" -> ItemBloc(x, y, (readInt(body, "width") ?: 1) * maille, (readInt(body, "height") ?: 1) * maille, { CoinItem() })
                "deadly" -> Bloc(x, y, (readInt(body, "width")  ?: 1)* maille, (readInt(body, "height") ?: 1) * maille, renderer = SquareRenderer(), isDeadly = true)
                "end" -> Bloc(x, y, 30, 69, renderer = SpriteSheetRenderer(itemSheet, 245, 90, 10, 24), isEnd = true)
                "tube" -> Tube(x, y, height = readInt(body, "height") ?: 2)
                "ground" -> Bloc(x, y, readInt(body, "width")!! * maille, maille, BlocRenderer(blocSheet, 16, 16, 16))
                else -> fail("Type $type is not handled (yet ?) for blocks")
            }

            if(bloc is Tube && body["flower"].toString() == "true"){
                val flower = FlowerBot(bloc)
                level.personnages.add(flower)
                level.objects.add(flower)
            }

            level.objects.add(bloc)
        }

        level.objects.add(Bloc(-1.0, 0.0, 1, 1000, SquareRenderer())) // Le mur de droite

        /* Import des bots */

        (config["bots"] as MutableList<*>).forEach {
            val body = it as Map<*,*>
            val y = readInt(body, "y")!!.toDouble() * maille

            val from = readInt(body, "goes.from")!!.toDouble() * maille
            val to = readInt(body, "goes.to")!!.toDouble() * maille

            val type = body["type"].toString()

            val bot = when(type) {
                "turtle" -> BaseBot(from, y, Dimension(15, 25) * 2, to - from, SpriteSheetRenderer(ImageIO.read(File("assets/smb_enemies_sheet.png")), arrayOf(150), arrayOf(210), 0, 15, 25))
                "goomba" -> BaseBot(from, y, Dimension(15, 15) * 2, to - from, SpriteSheetRenderer(ImageIO.read(File("assets/smb_enemies_sheet.png")), arrayOf(0, 0, 30), arrayOf(0, 0, 30), 4, 15, 15, animationStep = 0.5))
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

    fun readInt(bloc: Map<*, *>, path: String): Int? {
        var current = bloc
        val split = path.split(".")

        try {
            for (i in 0 until split.size) {
                if (i < split.size - 1) {
                    current = current[split[i]] as Map<*, *>
                } else {
                    return current[split[i]]?.toString()?.toInt()
                }
            }

            throw Exception("$path is a bad path")
        } catch (e: Exception){
            println("Error while loading $path : ")
            throw e
        }
    }

}