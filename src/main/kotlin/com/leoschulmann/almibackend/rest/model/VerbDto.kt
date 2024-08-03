package com.leoschulmann.almibackend.rest.model

import com.leoschulmann.almibackend.enm.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.io.Serializable

/**
 * DTO for {@link com.leoschulmann.almibackend.entity.Verb}
 */
data class VerbDto(
    @field:NotNull var id: Long? = null,
    @field:NotNull var regular: String? = null,
    @field:NotNull var nikkud: String? = null,
    var stemId: Long? = null,
    @field:NotNull var binyan: Binyan? = null,
    @field:NotNull var form: VerbForm? = null,
    @field:NotNull var person: GrammaticalPerson? = null,
    @field:NotNull var plurality: Plurality? = null,
    var transliteration: MutableSet<TransliterationDto> = mutableSetOf(),
    var translation: MutableList<TranslationDto> = mutableListOf(),
    @field:PositiveOrZero var version: Int = 0
) : Serializable {
    /**
     * DTO for {@link com.leoschulmann.almibackend.entity.embeddable.Transliteration}
     */
    data class TransliterationDto(
        @field:NotNull val transliteration: String? = null,
        @field:NotNull val language: Lang? = null
    ) : Serializable

    /**
     * DTO for {@link com.leoschulmann.almibackend.entity.embeddable.Translation}
     */
    data class TranslationDto(@field:NotNull val translation: String? = null, @field:NotNull val lang: Lang? = null) :
        Serializable
}