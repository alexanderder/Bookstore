package com.bookstore.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.bookstore.domain.BaseEntity;

public class BaseService<T extends BaseEntity> {

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseService() {
    }
    
    private Class<T> type;

    public BaseService(Class<T> type) {
        this.type = type;
    }
    
    public Class<T> getType() {
        return type;
    }
    
    @Transactional
    public T save(T entity) {
        this.entityManager.persist(entity);
        this.entityManager.flush();
        this.entityManager.refresh(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        return this.entityManager.merge(entity);
    }
    
    @Transactional
    public T find(Long id) {
        if (id == null) return null;
        return this.entityManager.find(this.type, id);
    }
    
    @Transactional
    public T find(Class<T> type, Object id) {
        if (id == null) return null;
        return this.entityManager.find(type, id);
    }

    @Transactional
    public void delete(T entity) {

        if (this.entityManager.contains(entity)) {
            this.entityManager.remove(entity);
        } else {
            T attached = find(entity.getId());
            this.entityManager.remove(attached);
        }
        
        this.entityManager.flush();
    }

}
