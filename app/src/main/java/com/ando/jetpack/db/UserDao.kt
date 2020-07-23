package com.ando.jetpack.db

import androidx.room.*

/**
 * Title: $
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  14:57
 */
@Dao
interface UserDao {

    @Query("select * from t_user order by uid asc")
    suspend fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(user: User)

    @Insert
    suspend fun add(vararg users: User)

    @Query("select * from t_user WHERE  uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

    @Query("select * from t_user WHERE first_name LIKE :firstName AND last_name LIKE :lastName LIMIT 1")
    suspend fun findByName(firstName: String, lastName: String): User

    @Delete
    suspend fun delete(user: User)

}