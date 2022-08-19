### releasedao-spring-boot-starter

### 介绍:

releaseDao（ORM），含义：释放dao层，通过APi+注解的形式轻松操作数据库。

简化开发、提高效率，使开发人员更加专注于业务。

### 主要功能:

-  通过**APi+注解**的方式，**无需编写sql语句**，就能对数据库基本 CURD操作。
-  自带Redis缓存，**自动管理缓存**，减少数据库I/O。
-  非侵入式、开箱即用、轻量级。

### 技术:

- 反射。

- 注解。

- 数据结构。

- Redis。

  

### 接口文档：

待补充。。。

## 安装教程

开箱即用。

##### Maven引用：

```xml
<dependency>
    <groupId>io.github.calwenb</groupId>
    <artifactId>releasedao-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
```

##### 使用demo：

使用demo详情请看software-crm模块service层。

##### Quick Start：

```java
import com.wen.releasedao.core.annotation.IdField;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 客户 实体类 
 * @author calwen
 * @since 2022/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("client")
public class Client {
    @IdField(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Integer level;
    private Integer creditLevel;
    private String manager;
    private String legalPerson;
    private String loseReason;
}
```

```java
import com.boy.softwarecrm.dto.ClientFindDto;
import com.boy.softwarecrm.pojo.Client;
import com.boy.softwarecrm.servcie.ClientService;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Client业务类
 *
 * @author calwen
 * @since 2022/8/19
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public Client get(Integer id) {
        return baseMapper.selectTargetById(Client.class, id);
    }

    @Override
    public List<Client> list(ClientFindDto findDto) {
        return baseMapper.selectList(Client.class);
    }

    @Override
    public void add(Client client) {
        baseMapper.insertTarget(client);
    }

    @Override
    public void update(Integer id, Client client) {
        baseMapper.replaceTarget(client);
    }

    @Override
    public void del(Integer id) {
        baseMapper.deleteTargetById(Client.class, id);
    }
}
```

