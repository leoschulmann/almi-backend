package com.leoschulmann.almibackend.entity.embeddable

import com.leoschulmann.almibackend.enm.Lang
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
open class Translation {
    @Column(nullable = false)
    open var translation: String? = null;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var lang: Lang? = null;

    constructor(translation: String?, lang: Lang?) {
        this.translation = translation
        this.lang = lang
    }

    constructor()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Translation

        if (translation != other.translation) return false
        if (lang != other.lang) return false

        return true
    }

    override fun hashCode(): Int {
        var result = translation?.hashCode() ?: 0
        result = 31 * result + (lang?.hashCode() ?: 0)
        return result
    }
}