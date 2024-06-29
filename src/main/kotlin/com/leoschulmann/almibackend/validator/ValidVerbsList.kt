package com.leoschulmann.almibackend.validator

import com.leoschulmann.almibackend.enm.GrammaticalPerson.*
import com.leoschulmann.almibackend.enm.Plurality
import com.leoschulmann.almibackend.enm.VerbForm.*
import com.leoschulmann.almibackend.entity.Verb
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass


@MustBeDocumented
@Constraint(validatedBy = [VerbsListValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ValidVerbsList(
    val message: String = "Invalid input",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class VerbsListValidator : ConstraintValidator<ValidVerbsList, List<Verb>> {

    override fun isValid(verbs: List<Verb>, ctx: ConstraintValidatorContext): Boolean {

        val codes: MutableSet<Int> = possibleVerbCodes.toMutableSet()

        if (verbs.size > codes.size) {
            ctx.disableDefaultConstraintViolation()
            ctx.buildConstraintViolationWithTemplate("Unexpected big number of verb forms: ${verbs.size}")
                .addConstraintViolation()
        }

        verbs.forEach { verb ->
            val verbCode = verb.getVerbCode()
            codes.remove(verbCode)
        }

        if (codes.isEmpty()) {
            return true
        } else {
            ctx.disableDefaultConstraintViolation()
            codes.map { code -> Verb.decode(code) }
                .forEach { verb ->
                    ctx.buildConstraintViolationWithTemplate("List missing form: '$verb'").addConstraintViolation()
                }
            return false
        }
    }
}

private val possibleVerbCodes: Set<Int> = setOf(
    Verb(null, null, null, null, INFINITIVE, NONE, Plurality.NONE).getVerbCode(),

    Verb(null, null, null, null, PRESENT, NONE, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.PLURAL_FEMININE).getVerbCode(),

    Verb(null, null, null, null, PAST, FIRST, Plurality.SINGULAR).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, FIRST, Plurality.PLURAL).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.PLURAL_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.PLURAL).getVerbCode(),

    Verb(null, null, null, null, FUTURE, FIRST, Plurality.SINGULAR).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, FIRST, Plurality.PLURAL).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.PLURAL_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.PLURAL_MASCULINE).getVerbCode(),

    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.PLURAL_FEMININE).getVerbCode()
)
