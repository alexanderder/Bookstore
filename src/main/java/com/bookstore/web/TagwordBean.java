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

import com.bookstore.domain.TagwordEntity;
import com.bookstore.service.TagwordService;

@Named("tagwordBean")
@ViewScoped
public class TagwordBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TagwordBean.class.getName());

    private List<TagwordEntity> tagwordList;

    private TagwordEntity tagword;

    @Inject
    private TagwordService tagwordService;

    public String persist() {

        String message;

        try {

            if (tagword.getId() != null) {
                tagword = tagwordService.update(tagword);
                message = "Entry updated";
            } else {
                tagword = tagwordService.save(tagword);
                message = "Entry created";
            }
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Your changes could not be saved because an error occurred.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }

        tagwordList = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        return null;
    }

    public String delete() {

        String message;

        try {
            tagwordService.delete(tagword);
            message = "Entry deleted";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Error occurred on deleting this entry.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		
		tagwordList = null;

        return null;
    }

    public TagwordEntity getTagword() {
        // Need to check for null, because some strange behaviour of f:viewParam
        // Sometimes it is setting to null
        if (this.tagword == null) {
            this.tagword = new TagwordEntity();
        }
        return this.tagword;
    }

    public void setTagword(TagwordEntity tagword) {
        if (tagword != null) {
            this.tagword = tagword;
        }
    }

    public List<TagwordEntity> getTagwordList() {
        if (tagwordList == null) {
            tagwordList = tagwordService.findAllTagwordEntities();
        }
        return tagwordList;
    }

    public void setTagwordList(List<TagwordEntity> tagwordList) {
        this.tagwordList = tagwordList;
    }

}
