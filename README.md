# easy-dynamic-config

## 项目背景

如果您的项目很小，不想引入配置中心等复杂组件，又想在线上动态修改配置，那么恭喜您，本项目可能会满足您的需求。

## 使用方式

- JDK要求>=1.8

### 创建表

```sql
create table ek.ek_dynamic_config
(
    id             bigint auto_increment comment '主键id',
    cf_tag         varchar(64)   not null comment '配置标签',
    cf_name        varchar(128)  not null comment '配置名称',
    cf_value       varchar(1024) null comment '配置值',
    cf_description varchar(255)  null comment '配置描述',
    cf_create_time datetime      null comment '创建时间',
    cf_update_time datetime      null comment '修改时间',
    constraint ek_dynamic_config_pk
        primary key (id)
)
    comment '动态配置表';

create unique index idx_cf_tag_cf_name
    on ek.ek_dynamic_config (cf_tag, cf_name);


```

### 插入测试数据

```sql
INSERT INTO ek.ek_dynamic_config (id, cf_tag, cf_name, cf_value, cf_description, cf_create_time, cf_update_time) VALUES (1, 'DemoConfig', 'productName', '茶壶', '商品名', '2024-09-16 10:24:43', '2024-09-16 10:24:44');
INSERT INTO ek.ek_dynamic_config (id, cf_tag, cf_name, cf_value, cf_description, cf_create_time, cf_update_time) VALUES (2, 'DemoConfig', 'size[0]', '1800', '茶壶容量(毫升)', '2024-09-16 10:25:39', '2024-09-16 10:25:40');
INSERT INTO ek.ek_dynamic_config (id, cf_tag, cf_name, cf_value, cf_description, cf_create_time, cf_update_time) VALUES (3, 'DemoConfig', 'size[1]', '1200', '茶壶容量(毫升)', '2024-09-16 10:28:36', null);

```

### 添加bom和依赖

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.easyconfig</groupId>
                <artifactId>easy-dynamic-config-bom</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

```xml
    <dependencies>
        <dependency>
            <groupId>com.easyconfig</groupId>
            <artifactId>easy-dynamic-config-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.easyconfig</groupId>
            <artifactId>easy-dynamic-config-core</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        
    </dependencies>
```

### 创建配置类示例

```java
public class DemoConfig {

    private String productName;

    private List<Integer> size;


    private List<Sku> skus;

    public static class Sku {

        public Sku() {
        }

        public Sku(String color, BigDecimal sellPrice) {
            this.color = color;
            this.sellPrice = sellPrice;
        }

        private String color;
//        private Double sellPrice;
        private BigDecimal sellPrice;

        private LocalDateTime online;

        @Override
        public String toString() {
            return "Sku{" +
                    "color='" + color + '\'' +
                    ", sellPrice=" + sellPrice +
                    ", online=" + online +
                    '}';
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Integer> getSize() {
        return size;
    }

    public void setSize(List<Integer> size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "DemoConfig{" +
                "productName='" + productName + '\'' +
                ", size=" + size +
                ", skus=" + skus +
                '}';
    }
}

```

### API使用

```java
    public static void main(String[] args) {
        Kernel kernel = ECBuilders.kernel().build();
        DemoConfig demoConfig = kernel.getProps(DemoConfig.class);
        System.out.println(demoConfig);
    }
```