package com.atguigu.repository;

import com.atguigu.pojo.User;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
/**
 * @author wxy
 * @create 2020-01-09 16:23
 */
public interface UserRepository extends ElasticsearchRepository<User,Long> {
    public List<User> findByAgeBetween(Integer age1, Integer age2);
//
//    @Query("{\n" +
//            "    \"range\": {\n" +
//            "      \"age\": {\n" +
//            "        \"gte\": \"?0\",\n" +
//            "        \"lte\": \"?1\"\n" +
//            "      }\n" +
//            "    }\n" +
//            "  }")
    List<User> findByQuery(Integer age1, Integer age2);


}
