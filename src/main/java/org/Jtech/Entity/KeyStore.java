package org.Jtech.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "api_client_key")
public class KeyStore {

    @Id
    @Column(name = "key_id")
    @JsonProperty("key_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;


    @Column(name="key_name", nullable = false)
    public String KeyName;


    @Column(name = "key_val",nullable = false)
    private String  KeyVal;

    @CreationTimestamp
    @Column(name="created_at",nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name="is_active",nullable = false)
    private boolean isActive;

    @Column(name = "last_used_at")
    private Timestamp lastUsedAt;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Timestamp getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Timestamp lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getKeyVal() {
        return KeyVal;
    }

    public void setKeyVal(String keyVal) {
        KeyVal = keyVal;
    }

    public String getKeyName() {
        return KeyName;
    }

    public void setKeyName(String keyName) {
        KeyName = keyName;
    }
}
