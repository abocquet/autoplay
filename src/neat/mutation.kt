package neat

interface MutationInterface {
    fun mutate(g: Graph) : Graph
}

class NodeMutation: MutationInterface {
    override fun mutate(g: Graph) : Graph {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /*override fun mutate(g: Graph) {
        val r = Random()

        var from: Node? = null
        if(Random().nextDouble() >= g.nodes.size / (g.nodes.size + g.input_nodes.size)){
            from = g.nodes[r.nextInt(g.nodes.size)]
        } else {
            from = g.input_nodes[r.nextInt(g.input_nodes.size)]
        }

        val to = g.nodes[r.nextInt(g.nodes.size)]

        g.addNode(from, to)
    }*/
}

class ConnectionMutation: MutationInterface {
    override fun mutate(g: Graph) : Graph {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /*override fun mutate(g: Graph) : Graph {
        val new = Graph()
        val r = Random()

        var from: Node? = null
        if(Random().nextDouble() >= g.nodes.size / (g.nodes.size + g.input_nodes.size)){
            from = g.nodes[r.nextInt(g.nodes.size)]
        } else {
            from = g.input_nodes[r.nextInt(g.input_nodes.size)]
        }

        val to = g.nodes[r.nextInt(g.nodes.size)]

        g.addConnection(from, to)
    }*/
}