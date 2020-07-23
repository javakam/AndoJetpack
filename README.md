# JetPackUsage
<https://codelabs.developers.google.com/?cat=Android>

## Room
先看指南, 再看CodeLab, 食用更佳
- [指南](https://developer.android.google.cn/training/data-storage/room)
- [CodeLab](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/index.html?index=..%2F..index#0)

### 使用主键

每个实体必须将至少 1 个字段定义为主键。即使只有 1 个字段，您仍然需要为该字段添加 @PrimaryKey 注释。此外，如果您想让 Room 为实体分配自动 ID，则可以设置 @PrimaryKey 的 autoGenerate 属性。如果实体具有复合主键，您可以使用 @Entity 注释的 primaryKeys 属性

SQLite 中的表名称不区分大小写

注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。

如果您的应用在多个进程中运行，请在数据库构建器调用中包含 enableMultiInstanceInvalidation()。这样，如果您在每个进程中都有一个 AppDatabase 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 AppDatabase 的实例


2020年7月23日 15:23:46  todo  https://developer.android.google.cn/training/data-storage/room/relationships
