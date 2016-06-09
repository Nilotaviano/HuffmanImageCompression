package huffmanImageCompression.DSA

import javafx.beans.property.BooleanPropertyBase

/**
 * Created by nilot on 15/05/2016.
 */
class ObservableIterator<T>(var iter: ListIterator<T>) : ListIterator<T> {

    val hasPreviousProperty = object : BooleanPropertyBase() {
        override fun invalidated() {
            //ignore
        }

        override fun getBean(): Any {
            return hasPrevious()
        }

        override fun getName(): String {
            return "hasPreviousProperty"
        }
    };

    val hasNextProperty = object : BooleanPropertyBase() {
        override fun invalidated() {
            //ignore
        }

        override fun getBean(): Any {
            return hasNext()
        }

        override fun getName(): String {
            return "hasPreviousProperty"
        }
    };

    override fun hasPrevious() = iter.hasPrevious()

    override fun previous(): T {
        val previous = iter.previous();

        hasPreviousProperty.set(hasPrevious())
        hasNextProperty.set(hasNext())

        return previous;
    }

    override fun previousIndex() = iter.previousIndex()

    override fun hasNext() = iter.hasNext()

    override fun next(): T {
        val next = iter.next();

        hasPreviousProperty.set(hasPrevious())
        hasNextProperty.set(hasNext())

        return next;
    }

    override fun nextIndex() = iter.nextIndex()
}