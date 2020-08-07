package com.ando.jetpack.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableList
import androidx.databinding.library.baseAdapters.BR

/**
 * Title: MemberBean
 * <p>
 * Description:  import androidx.databinding.library.baseAdapters.BR

 * </p>
 * @author Changbao
 * @date 2019/8/14  15:47
 */
class MemberBean : BaseObservable() {

    private var name: String = ""
    private var age: Int = 0
    private var likes: ObservableList<String>? = null

    @Bindable
    fun getName(): String {
        return name;
    }

    @Bindable
    fun getAge(): Int {
        return age;
    }

    @Bindable
    fun getLikes(): ObservableList<String>? {
        return likes
    }

    fun setName(name: String) {
        this.name = name
        notifyPropertyChanged(BR.name)
    }

    fun setAge(age: Int) {
        this.age = age
        notifyPropertyChanged(BR.age)

    }

    fun setLikes(likes: ObservableList<String>?) {
        this.likes = likes
        notifyPropertyChanged(BR.likes)

    }
}