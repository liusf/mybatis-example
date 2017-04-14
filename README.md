### 使用 Mybatis 做数据库迁移和代码生成

#### 数据迁移
使用 [Mybatis Migrations](http://www.mybatis.org/migrations-maven-plugin/plugin-info.html)
```
cd migrations
# 新建migration
mvn migration:new -Dmigration.description="create users"

# 应用migration
mvn migration:up

# 回滚migration
mvn migration:down

# 查看migration status
mvn migration:status
```
每次 migration:up 修改了表结构之后,需要重新使用下面的代码生成脚本更新代码.
#### 代码生成
我们使用[mybatis generator](http://www.mybatis.org/generator/)进行数据库表对应代码生成.

```
## 使用local的mysql, root账号, test数据库生成代码
## 生成的代码位于com.tigerbrokers.templates.model.tables下
./generator
```
#### 使用示例
假如经过设计,我们在数据库中新增了一个表 stocks. 首先在本项目目录下执行
```
mvn migration:new -Dmigration.description="create stocks"
```
此时会在 `migrations/scripts` 下生成一个新的sql文件,该文件名以时间戳开头,其余部分是我们上面写的描述. 
在这个文件中分别写好建表语句和回滚语句.
执行以下语句进行数据库 migration. Migration使用的数据库是在 `migrations/environments` 下的`properties`文件中配置的.
```
mvn migration:up
```
之后根据数据库的表结构生成相关Java代码和Mybatis mapper文件:
```
./generator.sh
```
Mybatis Generator的配置文件在 `src/main/resources/generatorConfig.xml`. 在这个文件中,需要配置:
1. 数据库地址(与上面保持一致)
2. 生成的Java类package和地址
3. 生成Mapper xml所在地址
4. 需要自动生成的每个表,以及表对应生成的Java类名
具体可以本项目的示例文件.

#### DAO的单元测试
代码中的 `DbTestCase` 是DB单元测试的基类,具体使用参看该类的注释和`src/test`下的示例 Testcase.