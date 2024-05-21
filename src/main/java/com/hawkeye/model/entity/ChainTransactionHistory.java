package com.hawkeye.model.entity;

public class ChainTransactionHistory {
    private String count;
    private String key;
    private String field;
    private String value;
    private String blockHeight;
    private String isDeleted;
    private String timestamp;

    public ChainTransactionHistory(String count, String key, String field, String value, String blockHeight, String isDeleted, String timestamp) {
        this.count = count;
        this.key = key;
        this.field = field;
        this.value = value;
        this.blockHeight = blockHeight;
        this.isDeleted = isDeleted;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "count='" + count + '\'' +
                ", key='" + key + '\'' +
                ", field='" + field + '\'' +
                ", value='" + value + '\'' +
                ", blockHeight='" + blockHeight + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(String blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
