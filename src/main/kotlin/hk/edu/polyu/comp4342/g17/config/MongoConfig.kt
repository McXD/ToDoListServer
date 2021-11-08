package hk.edu.polyu.comp4342.g17.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoClientFactoryBean

//@Configuration
open class MongoConfig {
//    @Bean
    open fun mongo(): MongoClientFactoryBean{
        val mongo = MongoClientFactoryBean()
        mongo.setHost("localhost")
        return mongo
    }
}