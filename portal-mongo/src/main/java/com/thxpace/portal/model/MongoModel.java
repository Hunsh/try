package com.thxpace.portal.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

public class MongoModel {
    private ObjectId _id;

    public ObjectId getObjectId() {
        return _id;
    }

    public void setObjectId(ObjectId objectId) {
        this._id = objectId;
    }
}
