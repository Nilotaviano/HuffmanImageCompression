package huffmanImageCompression.DSA

class Node : Comparable<Node> {

    private var _probability = -1.0
    var probability: Double
        get() {
            var result = _probability

            if (_leftNode != null && _rightNode != null) {
                result = _leftNode!!.probability + _rightNode!!.probability
            }

            return result
        }
        set(value) {
            _probability = value
        }

    private var _value = -1
    var value: Int
        get() = _value
        set(value) {
            _value = value
        }

    private var _leftNode: Node? = null
    var leftNode: Node?
        get() = _leftNode
        set(value) {
            _leftNode = value
        }

    private var _rightNode: Node? = null
    var rightNode: Node?
        get() = _rightNode
        set(value) {
            _rightNode = value
        }

    constructor(probability: Double, value: Int) {
        _probability = probability
        _value = value
    }

    constructor(leftNode: Node, rightNode: Node) {
        _leftNode = leftNode
        _rightNode = rightNode
    }

    fun isLeafNode() = _leftNode == null && _rightNode == null

    override fun compareTo(other: Node): Int {
        return if (this.probability < other.probability)
            1
        else if (this.probability == other.probability)
            0
        else -1
    }
}
