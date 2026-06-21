package domain.model;

import java.util.List;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Embedded;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.api.annotations.indexing.Keyword;
import org.infinispan.api.annotations.indexing.Text;
import org.infinispan.api.annotations.indexing.option.Structure;
import org.infinispan.api.annotations.indexing.option.TermVector;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoName;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Indexed
@ProtoName("book")
@Table
@Entity
public class Book {

   private Long id;
   private String title;
   private Integer yearOfPublication;
   private String description;
   private Float price;
   private Author author;
   private List<Review> reviews;

   protected Book() {
      // required by JPA
   }

   @ProtoFactory
   public Book(String title, Integer yearOfPublication, String description, Float price, Author author, List<Review> reviews) {
      this.title = title;
      this.yearOfPublication = yearOfPublication;
      this.description = description;
      this.price = price;
      this.author = author;
      this.reviews = reviews;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Long getId() {
      return id;
   }

   @Keyword(normalizer = "lowercase", projectable = true)
   @ProtoField(value = 1)
   public String getTitle() {
      return title;
   }

   @Basic
   @ProtoField(value = 2)
   public Integer getYearOfPublication() {
      return yearOfPublication;
   }

   @Text(termVector = TermVector.WITH_POSITIONS_OFFSETS_PAYLOADS, norms = false)
   @ProtoField(value = 3)
   @jakarta.persistence.Column(length = 2000)
   public String getDescription() {
      return description;
   }

   @Basic
   @ProtoField(value = 4)
   public Float getPrice() {
      return price;
   }

   @Embedded(structure = Structure.FLATTENED)
   @ProtoField(value = 5)
   public Author getAuthor() {
      return author;
   }

   @ElementCollection
   @Embedded(structure = Structure.NESTED)
   @ProtoField(value = 6)
   public List<Review> getReviews() {
      return reviews;
   }

   @Override
   public String toString() {
      return "Book{" +
            "title='" + title + '\'' +
            ", yearOfPublication=" + yearOfPublication +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", author=" + author +
            ", reviews=" + reviews +
            '}';
   }

}
