package com.blue.cat.fast.thirdbrowser

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val mosd: String? = " "
        println("Checking numArray1=$mosd")
        if (mosd.isNullOrBlank()) {
            println("numArray is Blank or null")
        }
        // 使用安全调用操作符来避免空指针异常
        if (mosd.isNullOrEmpty()) {
            println("numArray is empty or null")
            return
        }
        println("Processing numArray2")
    }
}