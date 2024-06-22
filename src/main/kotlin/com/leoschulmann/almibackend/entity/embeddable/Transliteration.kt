package com.leoschulmann.almibackend.entity.embeddable

import com.leoschulmann.almibackend.enm.Lang
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Enumerated

@Embeddable
open class Transliteration {
    @Column(nullable = false)
    open var transliteration: String? = null;

    @Enumerated
    @Column(nullable = false)
    open var language: Lang? = null;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transliteration

        if (transliteration != other.transliteration) return false
        if (language != other.language) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transliteration?.hashCode() ?: 0
        result = 31 * result + (language?.hashCode() ?: 0)
        return result
    }
}