package net.guides.springboot2.springboot2webappjsp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "subscription_type")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int subscriptionTypeId;
    private int userId;
    private float photo;
    private float music;
    private float art;
}
