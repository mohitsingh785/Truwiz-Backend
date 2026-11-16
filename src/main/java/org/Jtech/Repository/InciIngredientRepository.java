package org.Jtech.Repository;

import org.Jtech.Entity.InciIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface InciIngredientRepository extends JpaRepository<InciIngredient,Long> {

    // Synonyms and tags are Sets, so both can be fetch-joined in one query
    @Query("""
           select distinct i from InciIngredient i
           left join fetch i.inciSynonyms
           left join fetch i.inciTags tm
           left join fetch tm.inciTag
           where i.inciId in :ids
           """)
    List<InciIngredient> findWithSynonymsAndTagsByIds(@Param("ids") Set<Long> ids);


    @Query("""
       select distinct i from InciIngredient i
       left join fetch i.inciTags tm
       left join fetch tm.inciTag
       where i.inciId in :ids
       """)
    List<InciIngredient> findWithTagsByIds(@Param("ids") Set<Long> ids);
}
