package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionServicesImpl implements ISubscriptionServices{
    private static final Logger logger = LogManager.getLogger(SubscriptionServicesImpl.class);

    private ISubscriptionRepository subscriptionRepository;
    private ISkierRepository skierRepository;

    @Override
    public Subscription addSubscription(Subscription subscription) {
        logger.debug("Adding subscription with start date: {}", subscription.getStartDate());
        
        // Set end date based on subscription type
        switch (subscription.getTypeSub()) {
            case ANNUAL:
                subscription.setEndDate(subscription.getStartDate().plusYears(1));
                logger.debug("Set end date for annual subscription: {}", subscription.getEndDate());
                break;
            case SEMESTRIEL:
                subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                logger.debug("Set end date for semestrial subscription: {}", subscription.getEndDate());
                break;
            case MONTHLY:
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                logger.debug("Set end date for monthly subscription: {}", subscription.getEndDate());
                break;
        }

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription added with number: {}", savedSubscription.getNumSub());
        return savedSubscription;
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        logger.debug("Updating subscription with number: {}", subscription.getNumSub());
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription updated with number: {}", updatedSubscription.getNumSub());
        return updatedSubscription;
    }

    @Override
    public Subscription retrieveSubscriptionById(Long numSubscription) {
        logger.debug("Retrieving subscription by ID: {}", numSubscription);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        if (subscription != null) {
            logger.info("Subscription retrieved with number: {}", subscription.getNumSub());
        } else {
            logger.warn("Subscription with ID {} not found", numSubscription);
        }
        return subscription;
    }

    @Override
    public Set<Subscription> getSubscriptionByType(TypeSubscription type) {
        logger.debug("Retrieving subscriptions by type: {}", type);
        Set<Subscription> subscriptions = subscriptionRepository.findByTypeSubOrderByStartDateAsc(type);
        logger.info("Retrieved {} subscriptions for type: {}", subscriptions.size(), type);
        return subscriptions;
    }

    @Override
    public List<Subscription> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate) {
        logger.debug("Retrieving subscriptions between dates: {} and {}", startDate, endDate);
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate);
        logger.info("Retrieved {} subscriptions between dates: {} and {}", subscriptions.size(), startDate, endDate);
        return subscriptions;
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *") // Cron expression to run a job every 30 seconds
    public void retrieveSubscriptions() {
        logger.debug("Retrieving all subscriptions with their end dates...");
        for (Subscription sub: subscriptionRepository.findDistinctOrderByEndDateAsc()) {
            Skier aSkier = skierRepository.findBySubscription(sub);
            logger.info("Subscription: {} | End Date: {} | Skier: {} {}",
                    sub.getNumSub(), sub.getEndDate(), aSkier.getFirstName(), aSkier.getLastName());
        }
    }

    @Scheduled(cron = "*/30 * * * * *") // Cron expression to run a job every 30 seconds
    public void showMonthlyRecurringRevenue() {
        logger.debug("Calculating monthly recurring revenue...");
        Float monthlyRevenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL) / 6
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL) / 12;

        logger.info("Calculated Monthly Revenue: {}", monthlyRevenue);
    }
}
