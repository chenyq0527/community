package com.cyq.community.mapper;

import com.cyq.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    //param给参数起别名。
    //如果当前sql需要用到动态sql，且参数只有这一个，那么必须起别名，否则会报错！！
    int selectDiscussPostRows(@Param("userId") int userId);

}
