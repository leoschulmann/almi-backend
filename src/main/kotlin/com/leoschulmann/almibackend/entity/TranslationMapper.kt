package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.rest.model.VerbDto
import org.mapstruct.*

@Mapper
abstract class TranslationMapper {

    abstract fun toEntity(translationDto: VerbDto.TranslationDto): Translation

    abstract fun toDto(translation: Translation): VerbDto.TranslationDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(
        translationDto: VerbDto.TranslationDto,
        @MappingTarget translation: Translation
    ): Translation
}