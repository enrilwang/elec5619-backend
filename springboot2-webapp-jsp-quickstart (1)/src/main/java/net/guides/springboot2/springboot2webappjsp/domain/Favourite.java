package net.guides.springboot2.springboot2webappjsp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity
@Table(name = "user_favorite")
public class Favourite {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer user_favorite_id;
    private String creator_name;
    private Integer creator_id;


}
