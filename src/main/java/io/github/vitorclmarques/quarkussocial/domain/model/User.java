package io.github.vitorclmarques.quarkussocial.domain.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


//entidade de banco de dados
@Entity
//identificando nome da table no banco de dados
@Table(name = "users")
@Data
public class User {
    

    //informando que essa classe é um id, e no banco ela é autoincrement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Indicando nome da coluna no banco de dados. 
    @Column (name = "name")
    private String name;

    @Column (name = "age")  
    private Integer age;


    // Getters and Setters 
    // public Long getId(){
    //     return id;
    // }

    // public void setId(Long id){
    //     this.id=id;
    // }

    // public String getName(){
    //     return name;
    // }

    // public void setName(String name){
    //     this.name = name;
    // }

    // public Integer getAge(){
    //     return age;
    // }

    // public void setAge (Integer age){
    //     this.age = age;
    // }

    // @Override
    // public boolean equals(Object o){
    //     if(this == o) return true;
    //     if(o == null || getClass() != o.getClass()) return false;
    //     User user = (User) o;
    //     return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(age, user.age);
    // }

    // @Override
    // public int hashCode(){
    //     return Objects.hash(id, name, age);
    // }


}
