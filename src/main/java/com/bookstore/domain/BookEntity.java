package com.bookstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

@Entity(name = "Book")
@Table(name = "\"BOOK\"")
public class BookEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(length = 50, name = "\"name\"")
    private String name;

    @Digits(integer = 5, fraction = 2)
    @Column(precision = 7, scale = 2, name = "\"price\"")
    private BigDecimal price;

    @Column(name = "\"stock\"")
    @Digits(integer = 4, fraction = 0)
    private Integer stock;

    @Column(name = "\"launch_date\"")
    @Temporal(TemporalType.DATE)
    private Date launchDate;

    @Column(name = "\"discontinued\"")
    private Boolean discontinued;

    @ManyToOne(optional = true)
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")
    private AuthorEntity author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "BOOK_TAGS",
            joinColumns = {
                @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "TAG_ID", referencedColumnName = "ID")})
    private List<TagwordEntity> tags;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getLaunchDate() {
        return this.launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Boolean getDiscontinued() {
        return this.discontinued;
    }

    public void setDiscontinued(Boolean discontinued) {
        this.discontinued = discontinued;
    }

    public AuthorEntity getAuthor() {
        return this.author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public List<TagwordEntity> getTags() {
        return this.tags;
    }

    public void setTags(List<TagwordEntity> tags) {
        this.tags = tags;
    }

}
