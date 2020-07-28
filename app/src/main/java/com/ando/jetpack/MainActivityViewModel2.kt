package com.ando.jetpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/28  14:02
 */
class MainActivityViewModel2 : ViewModel() {

    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}