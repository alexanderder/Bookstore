package com.bookstore.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import com.bookstore.domain.AuthorEntity;
import com.bookstore.service.AuthorService;

@Named("authorBean")
@ViewScoped
public class AuthorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AuthorBean.class.getName());

    private List<AuthorEntity> authorList;

    private AuthorEntity author;

    @Inject
    private AuthorService authorService;

    public String persist() {

        String message;

        try {

            if (author.getId() != null) {
                author = authorService.update(author);
                message = "Entry updated";
            } else {
                author = authorService.save(author);
                message = "Entry created";
            }
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Your changes could not be saved because an error occurred.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }

        authorList = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        return null;
    }

    public String delete() {

        String message;

        try {
            authorService.delete(author);
            message = "Entry deleted";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Error occurred on deleting this entry.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		
		authorList = null;

        return null;
    }

    public AuthorEntity getAuthor() {
        // Need to check for null, because some strange behaviour of f:viewParam
        // Sometimes it is setting to null
        if (this.author == null) {
            this.author = new AuthorEntity();
        }
        return this.author;
    }

    public void setAuthor(AuthorEntity author) {
        if (author != null) {
            this.author = author;
        }
    }

    public List<AuthorEntity> getAuthorList() {
        if (authorList == null) {
            authorList = authorService.findAllAuthorEntities();
        }
        return authorList;
    }

    public void setAuthorList(List<AuthorEntity> authorList) {
        this.authorList = authorList;
    }

}
