# JetPackUsage
- [CodeLabs](https://codelabs.developers.google.com/?cat=Android)

-[Medium](https://medium.com/androiddevelopers)

- [architecture-components-samples](https://github.com/android/architecture-components-samples)

## Coroutines
[CodeLabs - Use Kotlin Coroutines in your Android App](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0)


## Room
先看指南, 再看CodeLab, 食用更佳
- [指南](https://developer.android.google.cn/training/data-storage/room)
- [CodeLabs - Room](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html?index=..%2F..index#0)

### 使用主键

每个实体必须将至少 1 个字段定义为主键。即使只有 1 个字段，您仍然需要为该字段添加 @PrimaryKey 注释。此外，如果您想让 Room 为实体分配自动 ID，则可以设置 @PrimaryKey 的 autoGenerate 属性。如果实体具有复合主键，您可以使用 @Entity 注释的 primaryKeys 属性

SQLite 中的表名称不区分大小写

注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。

如果您的应用在多个进程中运行，请在数据库构建器调用中包含 enableMultiInstanceInvalidation()。这样，如果您在每个进程中都有一个 AppDatabase 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 AppDatabase 的实例


2020年7月23日 15:23:46  todo  https://developer.android.google.cn/training/data-storage/room/relationships

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


AndroidViewModel 和 ViewModel 的选择: If you need the application context (which has a lifecycle that lives as long as the application does), use AndroidViewModel


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














