package neat

object config {
    //[NEAT]
    val fitness_criterion     = "max"
    val fitness_threshold     = 3.9
    val pop_size              = 150
    val reset_on_extinction   = false

    //[DefaultGenome]
    // node activation options
    val activation_default      = "sigmoid"
    val activation_mutate_rate  = 0.0
    val activation_options      = "sigmoid"

    // node aggregation options
    val aggregation_default     = "sum"
    val aggregation_mutate_rate = 0.0
    val aggregation_options     = "sum"

    // node bias options
    val bias_init_mean          = 0.0
    val bias_init_stdev         = 1.0
    val bias_max_value          = 30.0
    val bias_min_value          = -30.0
    val bias_mutate_power       = 0.5
    val bias_mutate_rate        = 0.7
    val bias_replace_rate       = 0.1

    // genome compatibility options
    val compatibility_disjoint_coefficient = 1.0
    val compatibility_weight_coefficient   = 0.5

    // connection add/remove rates
    val conn_add_prob           = 0.5
    val conn_delete_prob        = 0.5

    // connection enable options
    val enabled_default         = true
    val enabled_mutate_rate     = 0.01

    val feed_forward            = true
    val initial_connection      = "full"

    // node add/remove rates
    val node_add_prob           = 0.2
    val node_delete_prob        = 0.2

    // network parameters
    val num_hidden              = 0
    val num_inputs              = 2
    val num_outputs             = 1

    // node response options
    val response_init_mean      = 1.0
    val response_init_stdev     = 0.0
    val response_max_value      = 30.0
    val response_min_value      = -30.0
    val response_mutate_power   = 0.0
    val response_mutate_rate    = 0.0
    val response_replace_rate   = 0.0

    // connection weight options
    val weight_init_mean        = 0.0
    val weight_init_stdev       = 1.0
    val weight_max_value        = 30
    val weight_min_value        = -30
    val weight_mutate_power     = 0.5
    val weight_mutate_rate      = 0.8
    val weight_replace_rate     = 0.1

    //[DefaultSpeciesSet]
    val compatibility_threshold = 3.0

    //[DefaultStagnation]
    val species_fitness_func = "max"
    val max_stagnation       = 20
    val species_elitism      = 2

    //[DefaultReproduction]
    val elitism            = 2
    val survival_threshold = 0.2
}