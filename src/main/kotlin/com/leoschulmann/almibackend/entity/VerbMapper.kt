package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.rest.model.VerbDto
import org.mapstruct.*

@Mapper(uses = [TranslationMapper::class, TransliterationMapper::class])
abstract class VerbMapper {

    @Mapping(source = "stemId", target = "stem.id")
    abstract fun toEntity(verbDto: VerbDto): Verb

    @Mapping(source = "stem.id", target = "stemId")
    abstract fun toDto(verb: Verb): VerbDto

    @Mapping(source = "stemId", target = "stem.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(verbDto: VerbDto, @MappingTarget verb: Verb): Verb
}