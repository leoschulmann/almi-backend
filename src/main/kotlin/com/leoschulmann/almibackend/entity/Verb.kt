package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.enm.Binyan
import com.leoschulmann.almibackend.enm.GrammaticalPerson
import com.leoschulmann.almibackend.enm.Plurality
import com.leoschulmann.almibackend.enm.VerbForm
import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.entity.embeddable.Transliteration
import com.leoschulmann.almibackend.validator.ValidVerb
import jakarta.persistence.*

@Entity
@ValidVerb
open class Verb {


    constructor(
        regular: String?,
        nikkud: String?,
        stem: Stem?,
        binyan: Binyan?,
        form: VerbForm?,
        person: GrammaticalPerson?,
        plurality: Plurality?
    ) {
        this.regular = regular
        this.nikkud = nikkud
        this.stem = stem
        this.binyan = binyan
        this.form = form
        this.person = person
        this.plurality = plurality
    }

    constructor()

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

    @ElementCollection
    @CollectionTable(name = "verb_transliteration", joinColumns = [JoinColumn(name = "verb_id")])
    open var transliteration: MutableSet<Transliteration> = mutableSetOf()

    @ElementCollection
    @OrderColumn
    @CollectionTable(name = "verb_translation", joinColumns = [JoinColumn(name = "verb_id")])
    open var translation: MutableList<Translation> = mutableListOf()

    @OneToMany(mappedBy = "verb", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var examples: MutableSet<Example> = mutableSetOf()

    override fun toString(): String {
        return "Verb($regular, $nikkud, stem=${stem?.regular}, binyan=$binyan, form=$form, person=$person, plurality=$plurality)"
    }


}
