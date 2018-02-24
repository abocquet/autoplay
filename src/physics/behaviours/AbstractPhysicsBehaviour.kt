package physics.behaviours

import models.AbstractObject
import models.Bloc
import models.ItemBloc
import physics.Vector

/**
 * @author adrienbocquet
 */
abstract class AbstractPhysicBehaviour : Cloneable {

    /**
     * Contient les distances dans les quatres directions au plus proche bloc
     */
    inner class Margins {
        var top = java.lang.Double.POSITIVE_INFINITY
        var bottom = java.lang.Double.POSITIVE_INFINITY
        var left = java.lang.Double.POSITIVE_INFINITY
        var right = java.lang.Double.POSITIVE_INFINITY

        override fun toString(): String {
            return "top : $top right : $right bottom : $bottom left : $left"
        }
    }

    var margins = Margins()

    var speed = Vector()
    var acceleration = Vector()

    abstract fun update(obj: AbstractObject, delta_t: Double, obstacles: List<AbstractObject>)

    protected fun calcMargin(obj: AbstractObject, blocs: List<AbstractObject>, deplacement: Vector){
        this.margins = Margins()
        blocs.forEach { o -> if(o is Bloc || o is ItemBloc) this.calcMargin(obj, o, deplacement) }
    }

    /**
     * @param bloc le bloc par rapport auquel on se situe
     * @param next_pos le prochaine posisition de l'objet
     *
     * Calcul les degrès de liberté du solide par rapport à un bloc donné
     */
    private fun calcMargin(obj: AbstractObject, bloc: AbstractObject, next_pos: Vector) {

        val th = obj.dimension.getHeight()
        val tw = obj.dimension.getWidth()

        val tx = obj.position.x
        val ty = obj.position.y

        val bw = bloc.dimension.width
        val bh = bloc.dimension.height

        val bx = bloc.position.x
        val by = bloc.position.y

        val nx = Math.abs(next_pos.x)
        val ny = Math.abs(next_pos.y)

        /*
		 * Le calcul de distance se fait si: le bloc est en face de l'autre, verticalement, si on
		 * regarde la trajectoire sur x et horizontalement, si on regarde sur y
		 *
		 * Pour la première seconde condition, on prend une marge correspondant à la distance
		 * parcourue sur l'axe durant le prochain déplacement afin d'éviter la possibilité aux cubes
		 * de rentrer l'un dans l'autre par un coin
		 *
		 * Le dernier test est utile si l'épaisseur du bloc à detecter est moins élévée que celle de
		 * du modèle
		 */

        // On regarde d'abord en haut...
        if (tx in bx - nx..bx + bw + nx|| tx + tw in bx - nx..bx + bw + nx|| bx in tx..tx + tw) {
            val d = by - (ty + th)
            if (0 <= d && d < margins.top) {
                margins.top = d
            }
        }

        // ... en bas ...
        if (tx in bx - nx..bx + bw + nx|| tx + tw in bx - nx..bx + bw + nx|| bx in tx..tx + tw) {
            val d = ty - (by + bh)
            if (0 <= d && d < margins.bottom) {
                margins.bottom = d
            }
        }

        // ... à gauche ...
        if (ty in by - ny+1..by + bh + ny-1|| ty + th in by - ny+1..by + bh + ny-1|| by in ty..ty + th) {
            val d = tx - (bx + bw)

            if (0 <= d && d < margins.left) {
                margins.left = d
            }
        }

        // et enfin à droite
        if (ty in by - ny+1..by + bh + ny-1|| ty + th in by - ny+1..by + bh + ny-1|| by in ty..ty + th) {
            val d = bx - (tx + tw)

            if (0 <= d && d < margins.right) {
                margins.right = d
            }
        }

    }

}
