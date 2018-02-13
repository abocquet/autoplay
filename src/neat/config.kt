package neat

object Config {
    //[NEAT]
    const val fitness_criterion     = "max"
    const val fitness_threshold     = 3.9
    const val pop_size              = 10
    //const val pop_size              = 100
    const val reset_on_extinction   = false

    //[DefaultGenome]
    // node activation options
    const val activation_default      = "sigmoid"
    const val activation_mutate_rate  = 0.0
    const val activation_options      = "sigmoid"

    // node aggregation options
    const val aggregation_default     = "sum"
    const val aggregation_mutate_rate = 0.0
    const val aggregation_options     = "sum"

    // node bias options
    const val bias_init_mean          = 0.0
    const val bias_init_stdev         = 1.0
    const val bias_max_value          = 30.0
    const val bias_min_value          = -30.0
    const val bias_mutate_power       = 0.5
    const val bias_mutate_rate        = 0.7
    const val bias_replace_rate       = 0.1

    // genome compatibility options
    const val compatibility_disjoint_coefficient = 1.0
    const val compatibility_weight_coefficient   = 0.5

    // connection add/remove rates
    const val conn_add_prob           = 0.5
    const val conn_delete_prob        = 0.5

    // connection enable options
    const val enabled_default         = true
    const val enabled_mutate_rate     = 0.01

    const val initial_connection      = "full"

    // node add/remove rates
    const val node_add_prob           = 0.2
    const val node_delete_prob        = 0.2

    // network parameters
    const val num_hidden              = 0
    const val num_inputs              = 2
    const val num_outputs             = 1

    // node response options
    const val response_init_mean      = 1.0
    const val response_init_stdev     = 0.0
    const val response_max_value      = 30.0
    const val response_min_value      = -30.0
    const val response_mutate_power   = 0.0
    const val response_mutate_rate    = 0.0
    const val response_replace_rate   = 0.0

    // connection weight options
    const val weight_init_mean        = 0.0
    const val weight_init_stdev       = 1.0
    const val weight_max_value        = 30.0
    const val weight_min_value        = -30.0
    const val weight_mutate_power     = 0.5
    const val weight_mutate_rate      = 0.8
    const val weight_replace_rate     = 0.1

    //[DefaultSpeciesSet]
    const val compatibility_threshold = 3.0

    //[DefaultStagnation]
    const val species_fitness_func = "max"
    const val max_stagnation       = 20
    const val species_elitism      = 2

    //[DefaultReproduction]
    const val elitism            = 2
    const val survival_threshold = 0.2
}