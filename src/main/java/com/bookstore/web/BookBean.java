package com.bookstore.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import com.bookstore.domain.BookEntity;
import com.bookstore.domain.TagwordEntity;
import com.bookstore.domain.AuthorEntity;
import com.bookstore.service.BookService;
import com.bookstore.service.AuthorService;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@Named("bookBean")
@ViewScoped
public class BookBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(BookBean.class.getName());

    private List<BookEntity> bookList;

    private BookEntity book;

    @Inject
    private BookService bookService;

    @Inject
    private AuthorService authorService;

    private List<AuthorEntity> allAuthorsList;

    private DualListModel<TagwordEntity> tags;
    private List<String> transferedTagIDs;
    private List<String> removedTagIDs;

    public String persist() {

        String message;

        try {

            if (book.getId() != null) {
                book = bookService.update(book);
                message = "Entry updated";
            } else {
                book = bookService.save(book);
                message = "Entry created";
            }
        } catch (PersistenceException e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Your changes could not be saved because an error occurred.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }

        bookList = null;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        return null;
    }

    public String delete() {

        String message;

        try {
            bookService.delete(book);
            message = "Entry deleted";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occured", e);
            message = "Error occurred on deleting this entry.";
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));

        bookList = null;

        return null;
    }

    // Get a List of all authors
    public List<AuthorEntity> getAuthors() {
        if (this.allAuthorsList == null) {
            this.allAuthorsList = authorService.findAllAuthorEntities();
        }
        return this.allAuthorsList;
    }

    public DualListModel<TagwordEntity> getTags() {
        return tags;
    }

    public void setTags(DualListModel<TagwordEntity> tagwords) {
        int oldSize = this.tags.getSource().size() + this.tags.getTarget().size();
        int newSize = tagwords.getSource().size() + tagwords.getTarget().size();
        if (oldSize > 0 && newSize == 0) {
            // Workaroud: Do not let overwrite with empty List.
            return;
        }
        this.tags = tagwords;
    }

    public List<TagwordEntity> getFullTagsList() {
        List<TagwordEntity> allList = new ArrayList<>();
        allList.addAll(tags.getSource());
        allList.addAll(tags.getTarget());
        return allList;
    }

    public void onTagsDialog(BookEntity book) {
        // Prepare the tag PickList
        this.book = book;
        List<TagwordEntity> availableTagwordsFromDB = bookService
                .findAvailableTags(this.book);
        this.tags = new DualListModel<>(availableTagwordsFromDB, this.book.getTags());

        transferedTagIDs = new ArrayList<>();
        removedTagIDs = new ArrayList<>();
    }

    public void onTagsPickListTransfer(TransferEvent event) {
        // If a tag is transferred within the PickList, we just transfer it in this
        // bean scope. We do not change anything it the database, yet.
        for (Object item : event.getItems()) {
            String id = ((TagwordEntity) item).getId().toString();
            if (event.isAdd()) {
                transferedTagIDs.add(id);
                removedTagIDs.remove(id);
            } else if (event.isRemove()) {
                removedTagIDs.add(id);
                transferedTagIDs.remove(id);
            }
        }

    }

    public void onTagsSubmit() {
        // Now we save the changes of the PickList to the database.
        try {

            List<TagwordEntity> availableTagwordsFromDB = bookService.findAvailableTags(this.book);
			List<TagwordEntity> tagsToRemove = new ArrayList<>();

            for (TagwordEntity tagword : this.book.getTags()) {
                if (removedTagIDs.contains(tagword.getId().toString())) {
                    tagsToRemove.add(tagword);
                }
            }
            
            for (TagwordEntity tagword : tagsToRemove) {
                this.book.getTags().remove(tagword);;
            }

            for (TagwordEntity tagword : availableTagwordsFromDB) {
                if (transferedTagIDs.contains(tagword.getId().toString())) {
                    this.book.getTags().add(tagword);
                }
            }

            this.book = bookService.update(this.book);

            FacesMessage facesMessage = new FacesMessage("Changes saved.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        } catch (PersistenceException e) {
            FacesMessage facesMessage = new FacesMessage(
                    "Your changes could not be saved because an error occurred.");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            // Set validationFailed to keep the dialog open
            FacesContext.getCurrentInstance().validationFailed();
        }
    }

    public BookEntity getBook() {
        // Need to check for null, because some strange behaviour of f:viewParam
        // Sometimes it is setting to null
        if (this.book == null) {
            this.book = new BookEntity();
        }
        return this.book;
    }

    public void setBook(BookEntity book) {
        if (book != null) {
            this.book = book;
        }
    }

    public List<BookEntity> getBookList() {
        if (bookList == null) {
            bookList = bookService.findAllBookEntities();
        }
        return bookList;
    }

    public void setBookList(List<BookEntity> bookList) {
        this.bookList = bookList;
    }

}
