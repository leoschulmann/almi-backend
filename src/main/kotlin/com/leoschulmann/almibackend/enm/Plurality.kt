package com.leoschulmann.almibackend.enm

enum class Plurality {
    SINGULAR, PLURAL, SINGULAR_MASCULINE, SINGULAR_FEMININE, PLURAL_MASCULINE, PLURAL_FEMININE, NONE;


    companion object {

        private val subMap: Map<String, Plurality> by lazy {
            mapOf(
                Pair("SINGULAR_FЕМININE", SINGULAR_FEMININE),
                Pair("SINGULAR_FЕМINИНЕ", SINGULAR_FEMININE),
                Pair("SINGULAR_FЕМИНИНЕ", SINGULAR_FEMININE),
                Pair("SINGULAR_FЕМИНINE", SINGULAR_FEMININE),

                Pair("PLURAL_FЕМИНИНЕ", PLURAL_FEMININE),
                Pair("PLURAL_FЕМИНINE", PLURAL_FEMININE),
                Pair("PLURAL_FЕМININE", PLURAL_FEMININE),

                Pair("PLURAL_MASCУЛИНЕ", PLURAL_MASCULINE),
                Pair("PLURAL_MASCULИНЕ", PLURAL_MASCULINE),

                Pair("SINGULAR_MASCУЛИНЕ", SINGULAR_MASCULINE),
                Pair("SINGULAR_MASCULИНЕ", SINGULAR_MASCULINE),
            )
        }

        fun parse(value: String): Plurality {
            return try {
                Plurality.valueOf(value)
            } catch (e: IllegalArgumentException) {
                subMap[value] ?: throw e
            }
        }
    }
}
