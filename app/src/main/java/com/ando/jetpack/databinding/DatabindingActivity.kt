package com.ando.jetpack.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.ando.jetpack.R

/**
 * Title: DatabindingActivity
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2019/8/14  14:24
 */
open class DatabindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDatabindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_databinding)

        binding.lifecycleOwner = this

        val memberList = mutableListOf<MemberBean>()
        val m1 = MemberBean();
        m1.setName("小明")
        m1.setAge(12)
        val likes1 = ObservableArrayList<String>()
        likes1.add("足球")
        likes1.add("篮球")
        likes1.add("乒乓球")
        m1.setLikes(likes1)

        memberList.add(m1)
    }
}