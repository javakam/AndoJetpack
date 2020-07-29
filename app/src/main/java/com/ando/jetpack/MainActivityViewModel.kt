package com.ando.jetpack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ando.jetpack.room.dao.AppDatabase
import com.ando.jetpack.room.User
import com.ando.jetpack.room.dao.UserRepository
import kotlinx.coroutines.launch

/**
 * Title:
 * <p>
 * Description:
 * If you need the application context (which has a lifecycle that lives as long as the application does), use AndroidViewModel
 *
 *
 * </p>
 * @author Changbao
 * @date 2020/7/28  14:02
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: LiveData<List<User>>

    init {
        val userDao = AppDatabase.getDatabase(application, viewModelScope).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

}