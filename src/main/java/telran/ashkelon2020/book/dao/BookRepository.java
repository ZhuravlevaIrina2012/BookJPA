package telran.ashkelon2020.book.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import telran.ashkelon2020.book.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {

	@Query("select b from Book b where b.publisher.publisherName like ?1")
	List<Book> findBookByPublisher(String publisherName);
	
	List<Book> findBookByAuthorsName(String authorName);
}
