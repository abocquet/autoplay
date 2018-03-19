package neat

import GUI.annotations.FieldMutable

class ConfigC {
    //[NEAT]
    var fitness_criterion     = "max"
    var fitness_threshold     = 3.9
    var pop_size              = 50
    //var pop_size              = 100
    var reset_on_extinction   = false

    //[DefaultGenome]
    // node activation options
    var activation_default      = "sigmoid"
    var activation_mutate_rate  = 0.0
    var activation_options      = "sigmoid"

    // node aggregation options
    var aggregation_default     = "sum"
    var aggregation_mutate_rate = 0.0
    var aggregation_options     = "sum"

    // node bias options
    @FieldMutable(-50.0, 50.0, 0.1) var bias_init_mean          = 0.0
    @FieldMutable(-50.0, 50.0, 0.1) var bias_init_stdev         = 1.0
    @FieldMutable(-50.0, 50.0, 0.1) var bias_max_value          = 30.0
    @FieldMutable(-50.0, 50.0, 0.1) var bias_min_value          = -30.0
    @FieldMutable(-50.0, 50.0, 0.1) var bias_mutate_power       = 0.5
    @FieldMutable(-50.0, 50.0, 0.1) var bias_mutate_rate        = 0.7
    @FieldMutable(-50.0, 50.0, 0.1) var bias_replace_rate       = 0.1

    // genome compatibility options
    @FieldMutable(0.0, 1.0, 0.05) var compatibility_disjoint_coefficient = 1.0
    @FieldMutable(0.0, 1.0, 0.05) var compatibility_weight_coefficient   = 0.5

    // connection add/remove rates
    @FieldMutable(0.0, 1.0, 0.05) var conn_add_prob           = .1 //0.5
    @FieldMutable(0.0, 1.0, 0.05) var conn_delete_prob        = .1 //0.5

    // connection enable options
    var enabled_default         = true
    var enabled_mutate_rate     = 0.01

    var initial_connection      = "full"

    // node add/remove rates
    @FieldMutable(0.0, 1.0, 0.05) var node_add_prob           = 0.05 //0.2
    @FieldMutable(0.0, 1.0, 0.05) var node_delete_prob        = 0.05 //0.2

    // node response options
    var response_init_mean      = 1.0
    var response_init_stdev     = 0.0
    var response_max_value      = 30.0
    var response_min_value      = -30.0
    var response_mutate_power   = 0.0
    var response_mutate_rate    = 0.0
    var response_replace_rate   = 0.0

    // connection weight options
    @FieldMutable(-50.0, 50.0, 0.1) var weight_init_mean        = 0.0
    @FieldMutable(-50.0, 50.0, 0.1) var weight_init_stdev       = 1.0
    @FieldMutable(-50.0, 50.0, 0.1) var weight_max_value        = 30.0
    @FieldMutable(-50.0, 50.0, 0.1) var weight_min_value        = -30.0
    @FieldMutable(-50.0, 50.0, 0.1) var weight_mutate_power     = 0.5
    @FieldMutable(-50.0, 50.0, 0.1) var weight_mutate_rate      = 0.8
    @FieldMutable(-50.0, 50.0, 0.1) var weight_replace_rate     = 0.1

    //[DefaultSpeciesSet]
    @FieldMutable(0.0, 5.0, 0.1) var compatibility_threshold = 1.5 //3.0

    //[DefaultStagnation]
    var species_fitness_func = "max"
    @FieldMutable(10.0, 1000.0, 1.0) var max_stagnation       = 20
    @FieldMutable(0.0, 10.0, 1.0) var species_elitism         = 2

    //[DefaultReproduction]
    @FieldMutable(0.0, 10.0, 1.0) var elitism  = 2
    var survival_threshold = 0.2
}

val Config = ConfigC()