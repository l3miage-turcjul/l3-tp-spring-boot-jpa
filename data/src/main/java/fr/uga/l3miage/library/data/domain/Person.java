package fr.uga.l3miage.library.data.domain;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;;

//J'ai choisis de mettre table per class dans la stratégie de hierarchie car la classe person est abstract. 
//Comme table per classe permet de créer les tables qui herite de person en répétant les attributs hérités

//ici person a beaucoup plus de champ que les autres sous classes et la hiérarchie est plutôt
@Inheritance(strategy = InheritanceType.JOINED) 
@Entity
@DiscriminatorColumn(name = "type")
public abstract class Person {

    @Id
    @GeneratedValue
    //private String id;
    private long id;

    @Column
    private Gender gender;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Date birth;

    public enum Gender {
        FEMALE, MALE, FLUID
    }

    // public String getId() {
    //     return id;
    // }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return gender == person.gender && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(birth, person.birth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gender, firstName, lastName, birth);
    }
}
