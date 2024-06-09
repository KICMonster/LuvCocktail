package com.monster.luv_cocktail.domain.service;

import com.monster.luv_cocktail.domain.entity.Cocktail;
import com.monster.luv_cocktail.domain.entity.ViewLog;
import com.monster.luv_cocktail.domain.repository.CocktailsRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewService {
    @Autowired
    private CocktailsRepository cocktailRepository;

    public ViewService() {
    }

    public static Specification<ViewLog> inTimeRange(ZonedDateTime start, ZonedDateTime end) {
        return (root, query, builder) -> {
            Predicate predicate = builder.between(root.get("viewDate"), start, end);
            return predicate;
        };
    }
}
