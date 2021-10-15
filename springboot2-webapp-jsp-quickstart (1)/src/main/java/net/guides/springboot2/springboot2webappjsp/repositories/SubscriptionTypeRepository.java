package net.guides.springboot2.springboot2webappjsp.repositories;

import net.guides.springboot2.springboot2webappjsp.domain.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionTypeRepository extends JpaRepository<SubscriptionType,Integer> {
    SubscriptionType getSubscriptionTypeBySubscriptionTypeId(int subscriptionTypeId);
}
