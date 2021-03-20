package com.bookstore.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.transaction.Transactional;

import com.bookstore.domain.BookEntity;
import com.bookstore.domain.TagwordEntity;

@Named
public class BookService extends BaseService<BookEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public BookService(){
        super(BookEntity.class);
    }
    
    @Transactional
    public List<BookEntity> findAllBookEntities() {
        
        return entityManager.createQuery("SELECT o FROM Book o ", BookEntity.class).getResultList();
    }
    
    @Transactional
    public List<TagwordEntity> findAvailableTags(BookEntity book) {
        return entityManager.createQuery("SELECT o FROM Tagword o where not exists (select 1 from Book p where p = :p and o member of p.tags)", TagwordEntity.class).setParameter("p", book).getResultList();
    }
    
}
