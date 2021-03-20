package com.bookstore.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import com.bookstore.domain.AuthorEntity;

@Named
public class AuthorService extends BaseService<AuthorEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public AuthorService(){
        super(AuthorEntity.class);
    }
    
    @Transactional
    public List<AuthorEntity> findAllAuthorEntities() {
        
        return entityManager.createQuery("SELECT o FROM Author o ", AuthorEntity.class).getResultList();
    }
    
}
