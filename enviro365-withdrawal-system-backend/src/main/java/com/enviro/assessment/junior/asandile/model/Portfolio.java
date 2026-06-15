package com.enviro.assessment.junior.asandile.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Portfolio Entity representing an investor's investment portfolio
 *
 * @author Asandile
 * @version 1.0
 */
@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String portfolioName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalBalance = BigDecimal.ZERO;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    /**
     * Calculates total balance by summing all product balances
     *
     * @return total balance
     */
    public BigDecimal calculateTotalBalance() {
        return products.stream()
                .map(Product::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}