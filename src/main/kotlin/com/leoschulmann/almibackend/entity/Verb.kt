package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.enm.Binyan
import com.leoschulmann.almibackend.enm.GrammaticalPerson
import com.leoschulmann.almibackend.enm.Plurality
import com.leoschulmann.almibackend.enm.VerbForm
import jakarta.persistence.*

@Entity
open class Verb {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verb_gen")
    @SequenceGenerator(name = "verb_gen", sequenceName = "verb_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null
        protected set

    @Column(name = "regular", nullable = false)
    open var regular: String? = null

    @Column(name = "nikkud", nullable = false)
    open var nikkud: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "stem_id", nullable = false)
    open var stem: Stem? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "binyan", nullable = false)
    open var binyan: Binyan? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "form", nullable = false)
    open var form: VerbForm? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "person", nullable = false)
    open var person: GrammaticalPerson? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "plurality", nullable = false)
    open var plurality: Plurality? = null
}
