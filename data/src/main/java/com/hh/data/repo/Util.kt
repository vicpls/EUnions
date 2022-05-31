package com.hh.data.repo

const val LTAG = "EU.data.repo"

object Util {

    /** Get only integer part of number in String.
     *
     * @param value String that can parsing to floating
     * @return Integer part of value
     */
    fun getIntegerPart(value: String): String? {
        val delimiterPos = value.indexOf('.')
        if (delimiterPos < 0) return value
        return if (delimiterPos == 0) "" else value.substring(0, delimiterPos)
    }

}