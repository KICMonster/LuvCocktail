package com.monster.luv_cocktail.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOM_COCKTAIL_RECOMMENDATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"MEMBER_ID", "CUSTOM_ID"})
})
public class CustomCocktailRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECOMMENDATION_ID")
    private Long recommendation_Id;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "CUSTOM_ID", nullable = false)
    private Long customId;
}
