package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.entity.embeddable.Translation
import jakarta.persistence.*

@Entity
open class Stem {

    constructor(regular: String?, verbs: MutableSet<Verb>, translation: MutableList<Translation>) {
        this.regular = regular
        this.verbs = verbs
        this.translation = translation
    }

    constructor()

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stem_gen")
    @SequenceGenerator(name = "stem_gen", sequenceName = "stem_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null
        protected set

    @Column(name = "regular", nullable = false, length = 64)
    open var regular: String? = null

    @OneToMany(mappedBy = "stem", orphanRemoval = true)
    open var verbs: MutableSet<Verb> = mutableSetOf()

    @ElementCollection
    @CollectionTable(name = "stem_translation", joinColumns = [JoinColumn(name = "stem_id")])
    @OrderColumn
    open var translation: MutableList<Translation> = mutableListOf()
}