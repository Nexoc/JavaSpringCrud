package at.davl.spring.models;

import jakarta.validation.constraints.*;

public class Person {
    private int id;
    @NotEmpty(message= "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30")
    private String name;
    @Min(value = 1, message = "Age should be greater than 0")
    private int age;
    @NotEmpty(message = "Write here your valid Email")
    @Email(message = "It should be a valid Email")
    private String email;

    // Country, City, Index (int 6)
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this format: Country , CIty, ZIP Code (6 digit)")
    private String address;


    public Person() {}

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person(int id, String name, int age, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}
}
