package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.entity.embeddable.Transliteration
import com.leoschulmann.almibackend.rest.model.VerbDto
import org.mapstruct.*

@Mapper
abstract class TransliterationMapper {

    abstract fun toEntity(transliterationDto: VerbDto.TransliterationDto): Transliteration

    abstract fun toDto(transliteration: Transliteration): VerbDto.TransliterationDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(
        transliterationDto: VerbDto.TransliterationDto,
        @MappingTarget transliteration: Transliteration
    ): Transliteration
}