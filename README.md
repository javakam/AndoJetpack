# Kotlin Guide & JetPack Usage
- [CodeLabs](https://codelabs.developers.google.com/?cat=Android)

- [Medium](https://medium.com/androiddevelopers)

- [architecture-components-samples](https://github.com/android/architecture-components-samples)

## KotlinGuide
<https://github.com/javakam/KotlinGuide>

- 码上开学 : <https://kaixue.io/tag/kotlin-ji-chu/>
- bilibili : <https://space.bilibili.com/27559447>
- 博客的 MarkDown : <https://github.com/kaixueio/kaixue-docs/tree/master/kotlinmaster/doc>

### Notes

[1.Kotlin 的变量、函数和类型](https://github.com/javakam/JetpackUsage/blob/master/com/ando/jetpack/kotlin/1.Kotlin 的变量、函数和类型.md)

[2.Kotlin 里那些「不是那么写的」](https://github.com/javakam/JetpackUsage/blob/master/com/ando/jetpack/kotlin/2.Kotlin 里那些「不是那么写的」.md)

[3.Kotlin 里那些「更方便的」](https://github.com/javakam/JetpackUsage/blob/master/com/ando/jetpack/kotlin/3.Kotlin 里那些「更方便的」.md)

[4.Kotlin 的泛型](https://github.com/javakam/JetpackUsage/blob/master/com/ando/jetpack/kotlin/4.Kotlin 的泛型.md)


## KotlinGuide Coroutines
[CodeLabs - Use Kotlin Coroutines in your Android App](https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0)


## Room
先看指南, 再看CodeLab, 食用更佳
- [指南](https://developer.android.google.cn/training/data-storage/room)
- [CodeLabs - Android Room with a View - Kotlin](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html?index=..%2F..index#0)
<br>对应的源代码 <https://github.com/googlecodelabs/android-room-with-a-view/tree/kotlin>

### 使用主键

每个实体必须将至少 1 个字段定义为主键。即使只有 1 个字段，您仍然需要为该字段添加 @PrimaryKey 注释。此外，如果您想让 Room 为实体分配自动 ID，则可以设置 @PrimaryKey 的 autoGenerate 属性。如果实体具有复合主键，您可以使用 @Entity 注释的 primaryKeys 属性

SQLite 中的表名称不区分大小写

注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。

如果您的应用在多个进程中运行，请在数据库构建器调用中包含 enableMultiInstanceInvalidation()。这样，如果您在每个进程中都有一个 AppDatabase 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 AppDatabase 的实例

### 定义对象之间的关系

[定义对象之间的关系](https://developer.android.google.cn/training/data-storage/room/relationships)

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
        database.execSQL("CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");

        // Copy the data
        database.execSQL("INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
        
        // Remove the old table
        database.execSQL("DROP TABLE users");

        // Change the table name to the correct one
        database.execSQL("ALTER TABLE users_new RENAME TO users");
    }
};
```

## LiveData

> 注意：您可以使用 observeForever(Observer) 方法来注册未关联 LifecycleOwner 对象的观察者。
  在这种情况下，观察者会被视为始终处于活跃状态，因此它始终会收到关于修改的通知。您可以通过调用 removeObserver(Observer) 方法来移除这些观察者。

> 注意：请确保用于更新界面的 LiveData 对象存储在 ViewModel 对象中，而不是将其存储在 Activity 或 Fragment 中，原因如下：
  避免 Activity 和 Fragment 过于庞大。现在，这些界面控制器负责显示数据，但不负责存储数据状态。
  将 LiveData 实例与特定的 Activity 或 Fragment 实例分离开，并使 LiveData 对象在配置更改后继续存在。

> 注意：您必须调用 setValue(T) 方法以从主线程更新 LiveData 对象。如果在 worker 线程中执行代码，则您可以改用 postValue(T) 方法来更新 LiveData 对象。

- 如果 Lifecycle 对象未处于活跃状态，那么即使值发生更改，也不会调用观察者。
eg:在完成添加后不会立刻更新处于后台的列表页面,当添加页面执行`finish`并且列表页面显示到前台后,才会触发`Observer.onChanged`

- 销毁 Lifecycle 对象后，会自动移除观察者。

### 自定义LiveData
当 LiveData 对象具有活跃观察者时会调用onActive() , 没有任何活跃观察者时会调用onInactive()

```
StockLiveData 为单例
class StockLiveData(symbol: String) : LiveData<BigDecimal>() {
    private val stockManager: StockManager = StockManager(symbol)

    private val listener = { price: BigDecimal ->
        value = price
    }

    override fun onActive() {
        stockManager.requestPriceUpdates(listener)
    }

    override fun onInactive() {
        stockManager.removeUpdates(listener)
    }

    companion object {
        private lateinit var sInstance: StockLiveData

        @MainThread
        fun get(symbol: String): StockLiveData {
            sInstance = if (::sInstance.isInitialized) sInstance else StockLiveData(symbol)
            return sInstance
        }
    }
}
    
Fragment 中使用:
class MyFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StockLiveData.get(symbol).observe(viewLifecycleOwner, Observer<BigDecimal> { price: BigDecimal? ->
            // Update the UI.
        })

    }
    
```
### 转换 LiveData
<https://developer.android.com/topic/libraries/architecture/livedata#transform_livedata>

- Transformations.map()

```
val userLiveData: LiveData<User> = UserLiveData()
val userName: LiveData<String> = Transformations.map(userLiveData) {
    user -> "${user.name} ${user.lastName}"
}
```

- Transformations.switchMap()

```
class MyViewModel(private val repository: PostalCodeRepository) : ViewModel() {
    private val addressInput = MutableLiveData<String>()
    val postalCode: LiveData<String> = Transformations.switchMap(addressInput) {
            address -> repository.getPostCode(address) }

    private fun setInput(address: String) {
        addressInput.value = address
    }
}
```

在这种情况下，postalCode 字段定义为 addressInput 的转换。只要您的应用具有与 postalCode 字段关联的活跃观察者，就会在 addressInput 发生更改时重新计算并检索该字段的值。
  
此机制允许较低级别的应用创建以延迟的方式按需计算的 LiveData 对象。ViewModel 对象可以轻松获取对 LiveData 对象的引用，然后在其基础之上定义转换规则。

### 合并多个 LiveData 源
<https://developer.android.com/topic/libraries/architecture/livedata#merge_livedata>

`MediatorLiveData` 是 LiveData 的子类，允许您合并多个 LiveData 源。只要任何原始的 LiveData 源对象发生更改，就会触发 MediatorLiveData 对象的观察者。

例如，如果界面中有可以从本地数据库或网络更新的 LiveData 对象，则可以向 MediatorLiveData 对象添加以下源：

- 与存储在数据库中的数据关联的 LiveData 对象。

- 与从网络访问的数据关联的 LiveData 对象。

您的 Activity 只需观察 MediatorLiveData 对象即可从这两个源接收更新。


## Lifecycle👏ViewModel

[ViewModel 概览](https://developer.android.com/topic/libraries/architecture/viewmodel.html)

[CodeLabs - Incorporate Lifecycle-Aware Components](https://codelabs.developers.google.com/codelabs/android-lifecycles/#0)
<br>对应源码 👉 <https://github.com/googlecodelabs/android-lifecycles>


### 生命周期
ViewModel 存在的时间范围是从您首次请求 `ViewModel` 直到 `Activity` 完成并销毁。

![](https://raw.githubusercontent.com/javakam/JetpackUsage/master/img/ViewModel LifeCycle.png)

- AndroidViewModel 和 ViewModel 的选择: If you need the application context (which has a lifecycle that lives as long as the application does), use AndroidViewModel

- AndroidViewModel 传入 application :

```
val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
userViewModel = ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)
```

### 自定义`LifeCycleObserver`

```
getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            void addLocationListener() {
                Log.w("BoundLocationMgr", "Listener added");
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void removeLocationListener() {
                Log.w("BoundLocationMgr", "Listener removed");
            }

        });
```

### Fragment Communication
同一`Activity`下的`Fragment`间通信,采用共享`ViewModel`的方式,即每个`Fragment`共用一个`LifeCycleOwner`对象👉`requireActivity()`
, 而且每个`Fragment`的`lifecycle`是独立的:
```
1.
mSeekBarViewModel = new ViewModelProvider(requireActivity()).get(SeekBarViewModel.class);

2.
// Update the SeekBar when the ViewModel is changed.
mSeekBarViewModel.seekbarValue.observe(requireActivity(), new Observer<I
    @Override
    public void onChanged(@Nullable Integer value) {
        if (value != null) {
            mSeekBar.setProgress(value);
        }
    }
});
```

### Persist ViewModel state across process recreation (beta)
👉 <https://codelabs.developers.google.com/codelabs/android-lifecycles/#6>


模拟系统杀死进程（需要运行P +的仿真器）。 首先，通过键入以下命令来确保该进程正在运行：

`adb shell ps "-A |grep" lifecycle`

在设备或仿真器上按Home键，然后运行：

`adb shell am kill com.example.android.codelabs.lifecycle`

您应该什么也没有得到，表明该进程已被正确终止。


再次打开应用程序（在应用程序启动器中查找LC Step6）。

ViewModel中的值未保留，但是EditText恢复了其状态。 
 
> 一些UI元素（包括EditText）使用自己的onSaveInstanceState实现保存其状态。 杀死进程后，将以与更改配置后恢复该状态相同的方式恢复该状态。 阅读ViewModels：持久性，onSaveInstanceState（），恢复UI状态和加载程序以获取更多信息。

> ⭐实际上，`lifecycle-viewmodel-savedstate` 模块还使用 onSaveInstanceState 和 onRestoreInstanceState 来保留ViewModel状态，但是这使这些操作更加方便。

- SavedStateHandle Usage

```
public class SavedStateViewModel extends ViewModel {
    private static final String NAME_KEY = "name";
    private SavedStateHandle mState;

    public SavedStateViewModel(SavedStateHandle savedStateHandle) {
        mState = savedStateHandle;
    }

    // Expose an immutable LiveData
    LiveData<String> getName() {
        // getLiveData obtains an object that is associated with the key wrapped in a LiveData
        // so it can be observed for changes.
        return mState.getLiveData(NAME_KEY);
    }

    void saveNewName(String newName) {
        // Sets a new value for the object associated to the key. There's no need to set it
        // as a LiveData.
        mState.set(NAME_KEY, newName);
    }
}
```

## Paging
[分页库概览](https://developer.android.com/topic/libraries/architecture/paging)

[CodeLabs - Android Paging](https://codelabs.developers.google.com/codelabs/android-paging/#0)
👉 对应代码 `git clone https://github.com/googlecodelabs/android-paging` 

As we're already using Flow in our app, we'll continue with this approach; but instead of using Flow<RepoSearchResult>, we'll use Flow<PagingData<Repo>>.

## DataBinding
[CodeLabs - Data Binding in Android](https://codelabs.developers.google.com/codelabs/android-databinding/index.html#0)

**Google Databinding** [https://github.com/googlesamples/android-databinding](https://github.com/googlesamples/android-databinding)<br>

- `BR`路径问题: `import androidx.databinding.library.baseAdapters.BR`

### Usage
1. 两种实现方式:案例中的 LiveDataActivity 采用的是 ViewModel 结合 LiveData 的方式;
2. 另外一种是把ViewModel实现androidx.databinding.Observable,本身成为观察者,写起来很麻烦,不推荐
3. 创建绑定的推荐方法是在扩展布局时执行此操作，如以下示例所示：
```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

    binding.user = User("Test", "User")
}
```
4. 配合ListView 或 RecyclerView 使用:
```
val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
// or
val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
```
5. 空结合运算符(Null coalescing operator)
```
如果前操作数不为空，则空合并运算符（??）选择左操作数;如果前操作数为空，则选择右操作数。

android:text="@{user.displayName ?? user.lastName}"
```
等效于:
```
android:text="@{user.displayName != null ? user.displayName : user.lastName}"
```
6. List 或者 Map
```
<data>
    <import type="android.util.SparseArray"/>
    <import type="java.util.Map"/>
    <import type="java.util.List"/>
    <variable name="list" type="List&lt;String>"/>
    <variable name="sparse" type="SparseArray&lt;String>"/>
    <variable name="map" type="Map&lt;String, String>"/>
    <variable name="index" type="int"/>
    <variable name="key" type="String"/>
</data>
…
android:text="@{list[index]}"
…
android:text="@{sparse[index]}"
…
android:text="@{map[key]}"  
也可以 android:text="@{map.key}"
还可以使用字符串字面值,单引号或双引号:
android:text='@{map["firstName"]}' 或 android:text="@{map[`firstName`]}"
```
> Note: For the XML to be syntactically correct, you have to escape the < characters. 
For example: instead of List<String> you have to write List&lt;String>.
7. 配置方法引用:
```
class MyHandlers {
    fun onClickFriend(view: View) { ... }
}
```
绑定表达式可以将视图的单击侦听器分配给onClickFriend（）方法
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="handlers" type="com.example.MyHandlers"/>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"
           android:onClick="@{handlers::onClickFriend}"/>
   </LinearLayout>
</layout>
```
8. 监听器绑定
```
class Presenter {
    fun onSaveClick(task: Task){}
}
```
然后，你可以将 click 事件绑定到 onSaveClick（）方法，如下所示：
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="task" type="com.android.example.Task" />
        <variable name="presenter" type="com.android.example.Presenter" />
    </data>
    <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent">
        <Button android:layout_width="wrap_content" android:layout_height="wrap_content"
        android:onClick="@{() -> presenter.onSaveClick(task)}" />
    </LinearLayout>
</layout>
```
也可以写成:
```
android:onClick="@{(view) -> presenter.onSaveClick(task)}"
```
另外，如果要在表达式中使用该参数，则可以按如下方式工作
```
class Presenter {
    fun onSaveClick(view: View, task: Task){}
}

android:onClick="@{(theView) -> presenter.onSaveClick(theView, task)}"
```
你可以使用带有多个参数的 lambda 表达式：
```
class Presenter {
    fun onCompletedChanged(task: Task, completed: Boolean){}
}

<CheckBox
      android:layout_width="wrap_content" 
      android:layout_height="wrap_content"
      android:onCheckedChanged="@{(cb, isChecked) -> presenter.completeChanged(task, isChecked)}" />
```

9. 别名
```
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="Vista"/>
```
10. include 标签
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </LinearLayout>
</layout>
```
不支持 merge 标签:
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <merge><!-- Doesn't work -->
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </merge>
</layout>
```

### Databinding Observable
//todo 2019年8月15日 14:21:14 https://developer.android.google.cn/topic/libraries/data-binding/observability
//继续学习Databinding



DataBinding 参考<br>
[Google Databinding](https://developer.android.google.cn/topic/libraries/data-binding#kotlin)
[DataBinding最详细使用](https://blog.csdn.net/liangjingkanji/article/details/82180695)<br>
[DataBinding的基本使用（一)](https://blog.csdn.net/qq_33689414/article/details/52205703)<br>
//todo 2019年8月14日 17:01:09 https://github.com/googlesamples/android-sunflower.git

## Navigation
[CodeLabs - Jetpack Navigation](https://codelabs.developers.google.com/codelabs/android-navigation/#0)


## WorkManager
[CodeLabs - Background Work with WorkManager - Kotlin](https://codelabs.developers.google.com/codelabs/android-workmanager/#0)


# JetPack Bugs

## Room Persistence: Error:Entities and Pojos must have a usable public constructor

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

## Not sure how to convert a Cursor to this method's return type
```
val allUsers: LiveData<List<User>> = userDao.getAll()

error:
@Query("select * from t_user order by uid asc")
fun getAll(): MutableLiveData<List<User>>

success:
@Query("select * from t_user order by uid asc")
fun getAll(): LiveData<List<User>>
```

## Android ViewModel has no zero argument constructor

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

## Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.

改了 data class `User` 中的字段,但是没有更新 version


- `Primary key constraint on id is ignored when being merged into com.ando.jetpack.room.User`

<https://stackoverflow.com/questions/47868553/how-to-suppress-the-android-room-warning-primary-key-constraint-on-id-is-ignore>

[An @Embedded field cannot contain Primary Key.](https://medium.com/@kinnerapriyap/entity-embedded-and-composite-primary-keys-with-room-db-8cb6ca6256e8)

error
```
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @Embedded var address: Address? = null
)
```
error also
```
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded var address: Address? = null
)
```
success
```
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @Embedded var address: Address? = null
)
```

- `A field can be annotated with only one of the following:ColumnInfo,Embedded,Relation`

error
```
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @ColumnInfo(name = "address") @Embedded var address: Address? = null
)
```
success
```
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @Embedded var address: Address? = null
)
```

## There is a problem with the query: [SQLITE_ERROR] SQL error or missing database (no such table: t_book)

<https://stackoverflow.com/questions/52553971/room-error-there-is-a-problem-with-the-query-sqlite-error-sql-error-or-miss>

You should mention both the entities in your roomDatabase class.
```

@Database(entities = {BaseWordId.class, ABC.class}, version = VERSION_CODE, exportSchema = false) 
public abstract class YourDatabase extends RoomDatabase {
    //your Daos
}
```

## The column songId in the junction entity com.ando.jetpack.room.PlaylistSongCrossRef is being used to resolve a relationship but it is not covered by any index. 
This might cause a full table scan when resolving the relationship, it is highly advised to create an index that covers this column.

warn
```
@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)
```
no warn
```
@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    @ColumnInfo(index = true) val songId: Long
)
```

## [WARN] Incremental annotation processing requested, but support is disabled because the following processors are not incremental: androidx.room.RoomProcessor (DYNAMIC).

<https://stackoverflow.com/questions/57670510/how-to-get-rid-of-incremental-annotation-processing-requested-warning>


禁用增量注解 : `gradle.properties` add `kapt.incremental.apt=false`

## Updating... 





