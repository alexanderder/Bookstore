package com.bookstore.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import com.bookstore.domain.TagwordEntity;

@Named
public class TagwordService extends BaseService<TagwordEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TagwordService(){
        super(TagwordEntity.class);
    }
    
    @Transactional
    public List<TagwordEntity> findAllTagwordEntities() {
        
        return entityManager.createQuery("SELECT o FROM Tagword o ", TagwordEntity.class).getResultList();
    }

}
