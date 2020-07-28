package com.ando.jetpack.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  14:57
 */
@Dao
interface UserDao {

    @Query("select * from t_user order by uid asc")
    fun getAll(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(user: User)

    @Insert
    suspend fun add(vararg users: User)

    @Query("select * from t_user where  uid in (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

    @Query("select * from t_user where first_name like :firstName and last_name like :lastName limit 1")
    suspend fun findByName(firstName: String, lastName: String): User

    @Query("delete from t_user")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(user: User)

}