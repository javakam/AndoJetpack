package com.ando.jetpack.kotlin.kaixue.chapter2

/**
 * Title: HenCoder Kotilin$
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2019/8/26  14:20
 */
const val PATH = ""

class TopLevelDemo {

    val user = User()

    fun say() {

        user.name = "小明"
        println(user.name)

        //
        User.TAG
        User.Data.TAG

        var ins = InnerClass()
        //Cannot access
        //println(ins.age)
    }

    class InnerClass {
        private val age: Int = 5
    }
}