package at.davl.spring.dao;
import at.davl.spring.models.Person;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    /*
    private static int PEOPLE_COUNT; // 0
    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "nexoc";
    private static final String PASSWORD = "081180";
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

     */


    public List<Person> index() {
        //return jdbcTemplate.query("SELECT * FROM Person", new PersonMapper());
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));

        /*
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM PERSON";
            ResultSet resultSet =  statement.executeQuery(SQL);

            while (resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;

         */

    }

    public void save(Person person) {

        jdbcTemplate.update("INSERT INTO Person(name, age, email, address) Values(?, ?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail(), person.getAddress());


        //person.setId(++PEOPLE_COUNT);
        //people.add(person);
        /*
        try {
            Statement statement = connection.createStatement();
            String SQL = "INSERT INTO Person VALUES(" + ++PEOPLE_COUNT + ",'"
                    + person.getName() + "', "
                    + person.getAge() + ",'"
                    + person.getEmail() + "');";
            statement.executeUpdate(SQL);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO Person VALUES(1, ?, ?, ?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
             */
    }


    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?",
                new Object[] {email},
                new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }


    public Person show(int id) {
        /*
        Person person = null;
        //people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM Person WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next(); // we get first one

            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
         */
        //return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
        // new PersonMapper()).stream().findAny().orElse(null);
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void update(int id, Person updatedPerson){
        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=?, address=? WHERE id=?",
                updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), updatedPerson.getAddress(), id) ;
        /*
        Person personToBeUpdated = show(id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        */
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
        /*
        //people.removeIf(p -> p.getId() == id);
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("DELETE FROM Person WHERE id=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

         */
    }

    //////////////////////////////////////////////////////////7
    // TESTS

    public void testMultipleUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO Person VALUES (?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Person VALUES(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4, people.get(i).getEmail());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after-before));
    }

    private List<Person> create1000People() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(
                    i, "Name " + i, 30, "test"+i+"mail.ru", "some address"));
        }
        return people;
    }




}