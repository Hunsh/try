package com.thxpace.portal.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("portal")
public class Navigation extends MongoModel {
    private String name;
    private int level;
    private ObjectId parent;
    private int sort;
    private String imgUrl;
    private String linkUrl;
    private String[] tags;
}
