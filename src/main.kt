
import bot.TurtleBot
import controllers.HeroController
import controllers.PeopleController
import graphics.GraphicCore
import graphics.renderers.SquareRenderer
import level.Level
import models.Bloc
import models.People
import physics.PhysicCore
import physics.behaviours.GravityBehaviour
import java.awt.Dimension

fun main(args: Array<String>) {

    val hero = People(300.0, 400.0, Dimension(20, 20), GravityBehaviour(), SquareRenderer())
    val turtle = TurtleBot(400.0, 10.0, 100.0)

    val bloc = Bloc(300.0, 100.0, 100, 100)
    val ground = Bloc(-100.0, 0.0, 10000, 10)

    val level = Level(hero)
    level.objects.add(hero)
    level.personnages.add(hero)

    level.objects.add(turtle)
    level.personnages.add(turtle)

    level.objects.add(bloc)
    level.objects.add(ground)

    val graphics = GraphicCore(level)
    val physics = PhysicCore(level)

    val heroController = HeroController(level, graphics, physics)
    val personnagesController = PeopleController(level, graphics, physics)

    graphics.drawables.add(hero)
    graphics.drawables.add(turtle)
    graphics.drawables.add(ground)
    graphics.drawables.add(bloc)

    graphics.start()
    physics.start()
}
