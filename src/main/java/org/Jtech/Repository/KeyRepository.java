package org.Jtech.Repository;
import jakarta.transaction.Transactional;
import org.Jtech.Entity.KeyStore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KeyRepository  extends CrudRepository<KeyStore,Integer> {


    @Query(value = "SELECT * FROM api_key WHERE key_name = :keyName", nativeQuery = true)
    KeyStore getApiKey(@Param("keyName") String keyName);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO api_key(key_id, key_name, key_val) VALUES(:keyId, :keyName, :keyValue)", nativeQuery = true)
    void addApiKey(@Param("keyId") Integer keyId, @Param("keyName") String keyName, @Param("keyValue") String keyValue);




}
