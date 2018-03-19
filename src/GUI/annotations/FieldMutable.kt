package GUI.annotations

@Target(AnnotationTarget.PROPERTY)
annotation class FieldMutable(val min: Double, val max: Double, val step: Double)