package com.ando.jetpack.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ando.jetpack.room.*

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

    //-------------------------------Book

    /**
     * 1. “select * from t_user” 先查询 User 再查询 Book 表。
     *
     * 2. 该方法需要 Room 运行两次查询，因此应向该方法添加 @Transaction 注释，以确保整个操作以原子方式执行。
     */
    @Transaction
    @Query("select * from t_user")
    suspend fun getUsersAndBooks(): List<UserAndBook>

    /**
     * 1. “select * from t_user” 先查询 User 再查询 Playlist 表。
     *
     * 2. 该方法需要 Room 运行两次查询，因此应向该方法添加 @Transaction 注释，以确保整个操作以原子方式执行。
     */
    @Transaction
    @Query("select * from t_user")
    suspend fun getUsersWithPlaylists(): List<UserWithPlaylists>


    @Transaction
    @Query("select * from t_playlist")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("select * from t_song")
    fun getSongsWithPlaylists(): List<SongWithPlaylists>


}