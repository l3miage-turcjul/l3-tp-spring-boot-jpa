package fr.uga.l3miage.library.data.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NamedQuery(name="all-books",query = "select b from Book b order by b.title")
@NamedQuery(name="find-books-by-title",query="select b from Book b where lower(b.title) like lower(:title)")
@NamedQuery(name="find-books-by-author-and-title",query = "select b from Book b join b.authors a where a.id=:auteur and lower(b.title) like lower(:title) order by b.title")
@NamedQuery(name="find-books-by-authors-name",query="select b from Book b join b.authors a where lower(a.fullName) like lower(:name)")
@NamedQuery(name="find-books-by-several-authors",query="select b from Book b where size(b.authors) >:compte")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="isbn")
    private long isbn;

    @Column(name="publisher")
    private String publisher;

    @Column(name="annee")
    private short year;

    @Column(name="language")
    private Language language;

    @ManyToMany(mappedBy = "books",fetch = FetchType.EAGER)
    private Set<Author> authors;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        if (this.authors == null) {
            this.authors = new HashSet<>();
        }
        this.authors.add(author);
    }

    public enum Language {
        FRENCH,
        ENGLISH
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn == book.isbn && year == book.year && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && language == book.language && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isbn, publisher, year, language, authors);
    }
}
