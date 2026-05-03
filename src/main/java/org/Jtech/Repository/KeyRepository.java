package org.Jtech.Repository;
import jakarta.transaction.Transactional;
import org.Jtech.Entity.KeyStore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.sql.Timestamp;

public interface KeyRepository  extends CrudRepository<KeyStore,Integer> {


    @Query(value = "SELECT * FROM api_client_key WHERE key_name = :keyName", nativeQuery = true)
    KeyStore getApiKey(@Param("keyName") String keyName);


    @Modifying
    @Transactional
    @Query(value = "UPDATE api_client_key SET last_used_at = :lastUsedAt where key_name = :keyName", nativeQuery = true)
    void updateLastUsedAt(@Param("lastUsedAt") Timestamp lastUsedAt,@Param("keyName") String keyName);


}
