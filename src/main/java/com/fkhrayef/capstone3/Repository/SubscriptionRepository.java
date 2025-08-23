package com.fkhrayef.capstone3.Repository;

import com.fkhrayef.capstone3.Model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Subscription findSubscriptionById(Integer id);

    @Query("SELECT s FROM Subscription s WHERE s.status = 'active' AND s.endDate <= ?1")
    List<Subscription> findActiveSubscriptionsExpiringSoon(LocalDateTime expiryDate);
}
