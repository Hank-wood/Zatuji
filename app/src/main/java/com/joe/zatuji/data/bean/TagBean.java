package com.joe.zatuji.data.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 16/5/28.
 */
public class TagBean {
    public List<Tag> tagList;
    public TagBean(){
        tagList = new ArrayList<Tag>();
        tagList.add(new Tag("女青年","beauty",1));
        tagList.add(new Tag("男青年","men",2));
        tagList.add(new Tag("萌宝","kids",3));
        tagList.add(new Tag("光影","photography",4));
        tagList.add(new Tag("巧手工","diy_crafts",5));
        tagList.add(new Tag("极客","geek",6));
        tagList.add(new Tag("在路上","travel_places",7));
        tagList.add(new Tag("插画","illustration",8));
        tagList.add(new Tag("建筑","architecture",9));
        tagList.add(new Tag("艺术","art",10));
        tagList.add(new Tag("动漫","anime",11));
//        tagList.add(new Tag("有趣","funny",11));
        tagList.add(new Tag("吃货","food_drink",12));

    }

    public class Tag{
        public String name;
        public String requestName;
        public int position;
        public Tag(String name, String requestName, int position) {
            this.name = name;
            this.requestName = requestName;
            this.position = position;
        }

    }
}
