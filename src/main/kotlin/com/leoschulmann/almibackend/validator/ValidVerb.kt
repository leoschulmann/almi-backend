package com.leoschulmann.almibackend.validator

import com.leoschulmann.almibackend.enm.GrammaticalPerson
import com.leoschulmann.almibackend.enm.Plurality
import com.leoschulmann.almibackend.enm.VerbForm
import com.leoschulmann.almibackend.entity.Verb
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

private val log = KotlinLogging.logger {}

@Constraint(validatedBy = [VerbValidator::class])
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ValidVerb(
    val message: String = "Invalid verb property combination",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class VerbValidator : ConstraintValidator<ValidVerb, Verb> {
    override fun isValid(value: Verb, context: ConstraintValidatorContext): Boolean {

        log.debug { "Validating verb: $value..." }

        val validInfinitive = validInfinitive(value.plurality, value.person, value.form)
        val validPresentOrImperative = validPresentOrImperative(value.plurality, value.person, value.form)
        val validPast = validPast(value.plurality, value.person, value.form)
        val validFuture = validFuture(value.plurality, value.person, value.form)

        val res = validInfinitive || validPresentOrImperative || validPast || validFuture
        if (res) {
            return true
        } else {

            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(
                """Verb $value failed to validate : validInfinitive=$validInfinitive, 
                    |validPast=$validPast, validFuture=$validFuture, 
                    |validPresentOrImperative=$validPresentOrImperative""".trimMargin()
            ).addConstraintViolation()
            return false
        }

    }

    private fun validPast(plurality: Plurality?, person: GrammaticalPerson?, form: VerbForm?): Boolean {

        if (form != VerbForm.PAST) {
            return false;
        };

        return when (person) {
            GrammaticalPerson.FIRST -> simplePlurality.contains(plurality)
            GrammaticalPerson.SECOND -> extendedPlurality.contains(plurality)
            GrammaticalPerson.THIRD -> pastTenseThirdPersonPlurality.contains(plurality)
            GrammaticalPerson.NONE, null -> false
        }
    }

    private fun validFuture(plurality: Plurality?, person: GrammaticalPerson?, form: VerbForm?): Boolean {
        if (form != VerbForm.FUTURE) {
            return false
        }
        return when (person) {
            GrammaticalPerson.FIRST -> simplePlurality.contains(plurality)
            GrammaticalPerson.SECOND, GrammaticalPerson.THIRD -> extendedPlurality.contains(plurality)
            GrammaticalPerson.NONE, null -> false
        }
    }

    private fun validPresentOrImperative(plurality: Plurality?, person: GrammaticalPerson?, form: VerbForm?): Boolean {
        return presentAndImperative.contains(form) && person == GrammaticalPerson.NONE && extendedPlurality.contains(
            plurality
        )
    }

    private fun validInfinitive(plurality: Plurality?, person: GrammaticalPerson?, form: VerbForm?): Boolean {
        return form == VerbForm.INFINITIVE && plurality == Plurality.NONE && person == GrammaticalPerson.NONE
    }

    companion object {
        private val presentAndImperative: List<VerbForm> = listOf(VerbForm.PRESENT, VerbForm.IMPERATIVE)
        private val simplePlurality: List<Plurality> = listOf(Plurality.SINGULAR, Plurality.PLURAL)
        private val extendedPlurality: List<Plurality> = listOf(
            Plurality.SINGULAR_FEMININE,
            Plurality.SINGULAR_MASCULINE,
            Plurality.PLURAL_FEMININE,
            Plurality.PLURAL_MASCULINE
        )
        private val pastTenseThirdPersonPlurality: List<Plurality> = listOf(
            Plurality.SINGULAR_MASCULINE, Plurality.SINGULAR_FEMININE, Plurality.PLURAL
        )
    }
}
