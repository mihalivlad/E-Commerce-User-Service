package com.example.UserService.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username field must not be blank")
    @NaturalId
    private String username;

    @NotBlank(message = "Password field must not be blank")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registrationDate;

    @NotNull(message = "Billing address must not be null")
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "billing_address", referencedColumnName = "id")
    @Setter
    private Address billingAddress;

    @NotNull(message = "Delivery address must not be null")
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "delivery_address", referencedColumnName = "id")
    @Setter
    private Address deliveryAddress;


    public User(String username, String password, Date registrationDate,Address billingAddress, Address deliveryAddress) {
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
        this.billingAddress = billingAddress;
        this.deliveryAddress = deliveryAddress;
    }

    public User(String username, String password, Date registrationDate) {
        this.username = username;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        User user = (User) obj;
        return id.equals(user.id) &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                registrationDate.equals(user.registrationDate) &&
                billingAddress.equals(user.billingAddress) &&
                deliveryAddress.equals(user.deliveryAddress);
    }
}
