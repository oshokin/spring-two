package ru.oshokin.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Title not null")
    @Size(min = 6, message = "Title length cannot be less than 5")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Price shouldn''t be empty")
    @Min(value = 1, message = "Min 1")
    @Column(name = "price")
    private double price;

}
