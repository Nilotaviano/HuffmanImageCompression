package huffmanImageCompression

import java.io.Serializable
import java.util.*

/**
 * Created by nilot on 18/05/2016.
 */
class BidirectionalHuffmanTable <K, N : Number> : Serializable {
    private var map1 = TreeMap<K, N>()
    private var map2 = TreeMap<N, K>()

    operator fun get(k: K) = map1[k]

    operator fun get(k: N) = map2[k]

    operator fun set(k: K, v: N) {
        map1[k] = v
        map2[v] = k
    }

    operator fun set(k: N, v: K) {
        map1[v] = k
        map2[k] = v
    }
}