# library
<h2> Description: </h2>
<div>
Library is a RESTful Java Spring Boot application which allows to obtain the catalogue of books and authors.
Spring Data JPA is used to access data. The program implements two entities - Author and Book with ManyToMany bidirectional relationship. Where the owner side is Author.
The Author entity has the folowing fields: name, second name, age, email, password, role(user or admin) and actuality (Enum with fields ACTIVE and REMOVED) and list of books.
The Book entity has fields: title, description and list of authors.  A Book can be writen by one or more authors.
The program allows to obtain the catalogue of authors(only that, who have Active status) and for your account get list of your books.  The program allows you to add/remove/change your account (for example email) and add/remove/change your books. You can also add another author to your book (you must know his email).
After removing an author or a book their data will not be removed from database.
Also program allows you to get list of books(that have at least one author).
</div>
<h2>Used Technologies:</h2>
 <div>
 Back-end: Spring Boot, Spring Web, Spring Data JPA, Spring Security, java-jwt, ModelMapper, Hibernate Validator, MariaDB, Mockito, Lombok.
  </div>
  <div>
 Front-end: ReactJS.
 </div>
 <div>
  Server Build: Maven.
  </div>
  <div>
 Client Build: npm.
 </div>
 <h2> Requirements:</h2>
 <div> Java 17 </div>
 <div> MariaDB </div>
 <div> Maven </div>
<h2>Run:</h2> 
  <h3>first way:</h3>
  <div>
    <div>go to the project directory.
      <div>run: mvn clean package</div>
      <div>then: java -jar target/library-0.0.1-SNAPSHOT.jar</div>
      <div>go to the *project directory*/react/library</div>
      <div>run: npm install</div>
      <div>then: npm start</div>
  </div>
  <h3>second way:</h3>
    <div>
    <div>go to the project directory.</div>
    <div>then run: mvn clean package</div>
    <div>run: docker build -t spring-library .</div>
    <div>then: docker run  -p 8080:8080 -t spring-library</div>
    <div>go to the *project directory*/react/library</div>
    <div>run: docker build -t react-library .</div>
    <div>then: docker run -p 5001:3000 -t react-library</div>
    </div>
   <br/>
  <div>You also need to provide acces to your MariaDB database. </div>
  <div>You must set your database url, password and username to *project directory*/src/main/resources/application.properties file</div>
