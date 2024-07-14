package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.entity.embeddable.Translation
import jakarta.persistence.*

@Entity
open class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "example_gen")
    @SequenceGenerator(name = "example_gen", sequenceName = "example_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null
        protected set

    @Column(name = "example", nullable = false, length = 1024)
    open var example: String? = null

    @ElementCollection
    @CollectionTable(name = "example_translation")
    open var exampleTranslation: MutableSet<Translation> = mutableSetOf()

    @ManyToOne(optional = false)
    @JoinColumn(name = "verb_id", nullable = false)
    open var verb: Verb? = null

    @Version
    open var version: Int = 0
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Example

        if (id != other.id) return false
        if (example != other.example) return false
        if (exampleTranslation != other.exampleTranslation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (example?.hashCode() ?: 0)
        result = 31 * result + exampleTranslation.hashCode()
        return result
    }
}