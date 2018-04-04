package controllers

import graphics.GraphicCore
import level.Level
import physics.PhysicCore
import java.io.FileInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.Clip
import kotlin.concurrent.thread


class AudioController(level: Level, graphics: GraphicCore, physicCore: PhysicCore) : AbstractController(level, graphics, physicCore) {

    init {
        play("assets/soundtrack.mp3")
    }

    fun play(src: String){
        thread {
            try {
                val clip = AudioSystem.getClip()
                val inputStream = AudioSystem.getAudioInputStream(
                        AudioController::class.java.getResourceAsStream(src))
                clip.open(inputStream)
                clip.start()
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }
    }

}