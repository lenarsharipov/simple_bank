package com.lenarsharipov.simplebank.service.search;

import com.lenarsharipov.simplebank.model.*;
import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Builder
public record ClientSpecification(String phone,
                                  String fullName,
                                  String email,
                                  LocalDate birthDate) implements Specification<Client> {

    @Override
    public Predicate toPredicate(@NonNull Root<Client> root,
                                 @NonNull CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if (phone != null && !phone.isEmpty()) {
            Join<Client, Phone> phoneJoin = root.join(Client_.PHONES, JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(phoneJoin.get(Phone_.NUMBER), phone));
        }
        if (fullName != null && !fullName.isEmpty()) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.like(root.get(Client_.FULL_NAME), fullName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            Join<Client, Email> emailJoin = root.join(Client_.EMAILS, JoinType.INNER);
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(emailJoin.get(Email_.ADDRESS), email));
        }
        if (birthDate != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.greaterThan(root.get(Client_.BIRTH_DATE), birthDate));
        }
        return predicate;
    }
}