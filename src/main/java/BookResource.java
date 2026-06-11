
import io.micrometer.core.instrument.search.Search;
import io.quarkiverse.infinispan.embedded.Embedded;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.infinispan.Cache;
import org.infinispan.commons.api.query.Query;
import org.infinispan.protostream.ProtobufUtil;
import org.infinispan.protostream.SerializationContext;
import search.Book;
import search.generator.ModelGenerator;

import java.util.List;

@Path("books")
public class BookResource {

   @Inject
   @Embedded("book")
   Cache<String, Book> booksCache;

   @PUT
   public void initData() {
      booksCache.clear();
      booksCache.putAll(ModelGenerator.generateBooks());
   }

   @GET
   @Path("/description/{term}")
   public List<Book> query(@PathParam("term") String term) {

       SerializationContext ctx = ProtobufUtil.newSerializationContext();

       IO.println(ctx.getFileDescriptors());

       Query<Book> query = booksCache.query("from search.Book b where b.description : :description");
      query.setParameter("description", term);
      List<Book> books = query.execute().list();
      Log.info(books);
      return books;
   }
    @GET
    public List<Book> query() {
        List<Book> books = booksCache.values().stream().toList();
        Log.info(books);
        return books;
    }
}
