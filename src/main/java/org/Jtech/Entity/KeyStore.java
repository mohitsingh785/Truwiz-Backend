package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "api_key")
public class KeyStore {

    @Id
    @Column(name = "key_id")
    @JsonProperty("key_id")
    public Integer Id;


    @Column(name="key_name", nullable = false)
    public String KeyName;


    @Column(name = "key_val",nullable = false)
    private String  KeyVal;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }

    public String getKeyVal() {
        return KeyVal;
    }

    public void setKeyVal(String keyVal) {
        KeyVal = keyVal;
    }
}
