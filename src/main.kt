import level.LevelLoader

fun main(args: Array<String>) {


    val loaded = LevelLoader().load("maps/level1.yaml")

    /*val hero = People(
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
    physics.start()*/
}
