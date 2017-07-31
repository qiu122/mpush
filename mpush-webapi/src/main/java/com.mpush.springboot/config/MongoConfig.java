package com.mpush.springboot.config;

/**
 * Created by qiu.xiaolong on 2017/7/28.
 */
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mpush.springboot.properties.UserDefinedProperty;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhu on 2016/12/21.
 * 这是mongo 的配置类
 */
@Configuration
public class MongoConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserDefinedProperty mongoProp;

    @Bean(name = "serverAddress")
    public ServerAddress getServerAddress() {
        try {
            return new ServerAddress(mongoProp.getMongodb().getHost(),mongoProp.getMongodb().getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* mongo认证 即用户名 密码  start  */
    @Bean(name = "mongoCredential")
    public List<MongoCredential> getMongoCredential()  {
        List list = new ArrayList();
        list.add(MongoCredential.createMongoCRCredential(mongoProp.getMongodb().getUsername(), mongoProp.getMongodb().getDbname(), mongoProp.getMongodb().getPassword().toCharArray()));
        return list;
    }
    @Bean(name = "mongoClient")
    public MongoClient getMongoClient()  {
        return new MongoClient(getServerAddress(),getMongoCredential());
    }
    /* mongo认证  end  */

    /* mongo非认证  start  */
   /* @Bean(name = "mongoClient")
    public MongoClient getMongoClient()  {
        return new MongoClient(getServerAddress());
    }*/
    /* mongo非认证  end  */

    @Bean(name = "morphia")
    public Morphia getMorphia()  {
        return  new Morphia();
    }
    @Bean(name = "dbName")
    public String getDBName() {
        return mongoProp.getMongodb().getDbname();
    }
    @Bean(name = "datastore")
    public Datastore getCreateDatastore()  {
        return getMorphia().createDatastore(getMongoClient(),getDBName());
    }


}
