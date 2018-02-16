
import bot.TurtleBot
import controllers.HeroController
import controllers.PeopleController
import graphics.GraphicCore
import graphics.renderers.SpriteSheetRenderer
import level.Level
import models.Bloc
import models.People
import physics.PhysicCore
import physics.behaviours.GravityBehaviour
import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {



    val hero = People(
        200.0, 100.0, Dimension(40, 40), GravityBehaviour(),
        SpriteSheetRenderer(ImageIO.read(File("assets/smb_mario_sheet.png")), arrayOf(180), arrayOf(210), 0, 15, 15)
    )

    val turtle = TurtleBot(400.0, 10.0, 100.0)

    val bloc = Bloc(300.0, 100.0, 200, 10)
    val ground = Bloc(-100.0, 0.0, 10000, 10)

    val level = Level(hero)

    level.objects.add(turtle)
    level.personnages.add(turtle)

    level.objects.add(bloc)
    level.objects.add(ground)

    val graphics = GraphicCore(level, 1080, 720)
    val physics = PhysicCore(level)

    val heroController = HeroController(level, graphics, physics)
    val personnagesController = PeopleController(level, graphics, physics)

    graphics.start()
    physics.start()
}
