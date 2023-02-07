package com.junitmockito;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
     private BookRepository bookRepository;

    public List<Book> getAllBookRecords(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long bookId){
        return bookRepository.findById(bookId).get();
    }

    public Book getBookByName(String name){
        return bookRepository.findByName(name).get();
    }

    public Book createBookRecord(Book bookRecord){
        return bookRepository.save(bookRecord);
    }

    public Book updateBookRecord(Book bookRecord) throws NotFoundException {
        if(bookRecord == null || bookRecord.getBookId() == null){
            throw new NotFoundException("BookRecord or ID must not be null!");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());
        if(!optionalBook.isPresent()){
            throw new NotFoundException("Book with ID" +bookRecord.getBookId() + "does not exist.");
        }

        Book existingBookRecord = optionalBook.get();
        existingBookRecord.setName(bookRecord.getName());
        existingBookRecord.setSummary(bookRecord.getSummary());
        existingBookRecord.setRating(bookRecord.getRating());

        return bookRepository.save(existingBookRecord);
    }

    public void deleteBookById(Long bookId) throws NotFoundException {
        if(!bookRepository.findById(bookId).isPresent()){
            throw new NotFoundException("bookId" + bookId + "not present");
        }
        bookRepository.deleteById(bookId);
    }

}
