# JetPackUsage
- [CodeLabs](https://codelabs.developers.google.com/?cat=Android)

-[Medium](https://medium.com/androiddevelopers)

- [architecture-components-samples](https://github.com/android/architecture-components-samples)

## Coroutines
[CodeLabs - Use Kotlin Coroutines in your Android App](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0)


## Room
先看指南, 再看CodeLab, 食用更佳
- [指南](https://developer.android.google.cn/training/data-storage/room)
- [CodeLabs - Android Room with a View - Kotlin](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html?index=..%2F..index#0)
对应的源代码 <https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin>

### 使用主键

每个实体必须将至少 1 个字段定义为主键。即使只有 1 个字段，您仍然需要为该字段添加 @PrimaryKey 注释。此外，如果您想让 Room 为实体分配自动 ID，则可以设置 @PrimaryKey 的 autoGenerate 属性。如果实体具有复合主键，您可以使用 @Entity 注释的 primaryKeys 属性

SQLite 中的表名称不区分大小写

注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。

如果您的应用在多个进程中运行，请在数据库构建器调用中包含 enableMultiInstanceInvalidation()。这样，如果您在每个进程中都有一个 AppDatabase 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 AppDatabase 的实例

### 定义对象之间的关系
对象嵌套
```
@Entity
data class Address(...)

@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    ...
    @ColumnInfo(name = "address") @Embedded var address: Address? = null
)
```
一对一
```
@Entity
data class Book(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "ownerUserId") val ownerUserId: Long?
)

🍎映射表
//不加 @Entity
data class UserAndBook(
    @Embedded val user: User,
    @Relation(parentColumn = "uid", entityColumn = "ownerUserId")
    val book: Book
)

/**
 * 1. “select * from t_user” 先查询 User 再查询 Book 表。
 *
 * 2. 该方法需要 Room 运行两次查询，因此应向该方法添加 @Transaction 注释，以确保整个操作以原子方式执行。
 */
@Transaction
@Query("select * from t_user")
suspend fun getUsersAndBooks(): List<UserAndBook>
```
一对多
```
@Entity
data class Playlist(
    @PrimaryKey val playlistId: Long,
    val userCreatorId: Long,
    val playlistName: String
)

🍎映射表
//不加 @Entity
data class UserWithPlaylists(
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userCreatorId"
    )
    val playlists: List<Playlist>
)

/**
 * 1. “select * from t_user” 先查询 User 再查询 Playlist 表。
 *
 * 2. 该方法需要 Room 运行两次查询，因此应向该方法添加 @Transaction 注释，以确保整个操作以原子方式执行。
 */
@Transaction
@Query("select * from t_user")
suspend  fun getUsersWithPlaylists(): List<UserWithPlaylists>
```
多对多
```
@Entity(tableName = "t_playlist")
data class Playlist(
    @PrimaryKey val playlistId: Long,
    val userCreatorId: Long,
    val playlistName: String
)

@Entity(tableName = "t_song")
data class Song(
    @PrimaryKey val songId: Long,
    val songName: String,
    val artist: String
)

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)
```
两种情形：
```
data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)

data class SongWithPlaylists(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "songId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val playlists: List<Playlist>
)
```
dao : 
```
@Transaction
@Query("select * from t_playlist")
fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

@Transaction
@Query("select * from t_song")
fun getSongsWithPlaylists(): List<SongWithPlaylists>
```

嵌套关系 👉 <https://developer.android.google.cn/training/data-storage/room/relationships#nested-relationships>

> 注意：使用嵌套关系查询数据需要 Room 处理大量数据，可能会影响性能。因此，请在查询中尽量少用嵌套关系。


[定义对象之间的关系](https://developer.android.google.cn/training/data-storage/room/relationships)




### Room migrations

[Understanding migrations with Room](https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929)

[Testing Room migrations](https://medium.com/androiddevelopers/testing-room-migrations-be93cdb0d975)

[Repository complex implementation](https://github.com/android/architecture-components-samples/tree/master/BasicSample)

#### Migrations with complex schema changes
SQLite’s ALTER TABLE… command is quite limited. For example, changing the id of the user from an int to a String takes several steps:

- create a new temporary table with the new schema,
- copy the data from the users table to the temporary table,
- drop the users table
- rename the temporary table to users

Using Room, the Migration implementation looks like this:
```
static final Migration MIGRATION_3_4 = new Migration(3, 4) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // Create the new table
        database.execSQL(
                "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");
// Copy the data
        database.execSQL(
                "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
// Remove the old table
        database.execSQL("DROP TABLE users");
// Change the table name to the correct one
        database.execSQL("ALTER TABLE users_new RENAME TO users");
    }
};
```

## ViewModel

[ViewModel 概览](https://developer.android.com/topic/libraries/architecture/viewmodel.html)

[ViewModel 的已保存状态模块](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate)

[ViewModels : A Simple Example](https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e)


- AndroidViewModel 和 ViewModel 的选择: If you need the application context (which has a lifecycle that lives as long as the application does), use AndroidViewModel

- AndroidViewModel 传入 application :

```
val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
userViewModel = ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)
```

## Paging
[CodeLabs - Android Paging](https://codelabs.developers.google.com/codelabs/android-paging/#0)



## DataBinding
[CodeLabs - Data Binding in Android](https://codelabs.developers.google.com/codelabs/android-databinding/index.html#0)



## Navigation
[CodeLabs - Jetpack Navigation](https://codelabs.developers.google.com/codelabs/android-navigation/#0)



## WorkManager
[CodeLabs - Background Work with WorkManager - Kotlin](https://codelabs.developers.google.com/codelabs/android-workmanager/#0)



## JetPack Bugs

- Room Persistence: Error:Entities and Pojos must have a usable public constructor
<https://stackoverflow.com/questions/44485631/room-persistence-errorentities-and-pojos-must-have-a-usable-public-constructor>

`@Ignore` 应该放到类中声明
```
error:
@Entity(tableName = "t_user")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") var uid: Long,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?,
    @Ignore var picture: Bitmap? = null
)

success:
@Entity(tableName = "t_user")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") var uid: Long,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?
) {
    @Ignore
    var picture: Bitmap? = null
    //or
    // constructor() : this(0, "", "")
}

```

- Not sure how to convert a Cursor to this method's return type
```
val allUsers: LiveData<List<User>> = userDao.getAll()

error:
@Query("select * from t_user order by uid asc")
fun getAll(): MutableLiveData<List<User>>

success:
@Query("select * from t_user order by uid asc")
fun getAll(): LiveData<List<User>>
```

- Android ViewModel has no zero argument constructor
<https://stackoverflow.com/questions/44194579/android-viewmodel-has-no-zero-argument-constructor>

For lifecycle_version = '2.2.0' ViewProviders.of API is deprecated . It`s my situation :
```
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserRepository

    val allUsers: LiveData<List<User>>
......


error:
val userViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

success:
val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
userViewModel = ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)

```

-  Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.
改了 data class `User` 中的字段,但是没有更新 version


- 













