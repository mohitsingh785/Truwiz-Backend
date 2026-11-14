package org.Jtech.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.Jtech.Constant.EstimationMethod;
import org.Jtech.Constant.MatchMethod;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product_ingredient",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_product_ingredient_order",
                columnNames = {"barcode", "ingredient_order"}
        )
)
public class ProductIngredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inci_id")
    private InciIngredient inciIngredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="barcode",nullable = false)
    private Product product;

    @NotNull
    @Min(1)
    @Column(name = "ingredient_order", nullable = false)
    private Integer ingredientOrder;

    @NotBlank
    @Size(max = 255)
    @Column(name = "raw_label_name", nullable = false, length = 255)
    private String rawLabelName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "match_method", nullable = false)
    private MatchMethod matchMethod;

    @DecimalMin("0.000")
    @DecimalMax("100.000")
    @Column(name = "declared_pct", precision = 6, scale = 3)
    private BigDecimal declaredPct;

    @DecimalMin("0.000")
    @DecimalMax("100.000")
    @Column(name = "estimated_pct", precision = 6, scale = 3)
    private BigDecimal estimatedPct;

    @Enumerated(EnumType.STRING)
    @Column(name = "estimation_method")
    private EstimationMethod estimationMethod;


    @AssertTrue(message = "inciIngredient must be null only when matchMethod is UNMATCHED")
    private boolean isMatchConsistent() {
        if (matchMethod == null) {
            return true;                 // @NotNull on matchMethod reports this case
        }
        boolean unmatched = matchMethod == MatchMethod.UNMATCHED;
        boolean noInci = inciIngredient == null;
        return unmatched == noInci;
    }

    @AssertTrue(message = "estimatedPct and estimationMethod must be set together")
    private boolean isEstimateConsistent() {
        return (estimatedPct == null) == (estimationMethod == null);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InciIngredient getInciIngredient() {
        return inciIngredient;
    }

    public void setInciIngredient(InciIngredient inciIngredient) {
        this.inciIngredient = inciIngredient;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getIngredientOrder() {
        return ingredientOrder;
    }

    public void setIngredientOrder(Integer ingredientOrder) {
        this.ingredientOrder = ingredientOrder;
    }

    public String getRawLabelName() {
        return rawLabelName;
    }

    public void setRawLabelName(String rawLabelName) {
        this.rawLabelName = rawLabelName;
    }

    public MatchMethod getMatchMethod() {
        return matchMethod;
    }

    public void setMatchMethod(MatchMethod matchMethod) {
        this.matchMethod = matchMethod;
    }

    public BigDecimal getDeclaredPct() {
        return declaredPct;
    }

    public void setDeclaredPct(BigDecimal declaredPct) {
        this.declaredPct = declaredPct;
    }

    public BigDecimal getEstimatedPct() {
        return estimatedPct;
    }

    public void setEstimatedPct(BigDecimal estimatedPct) {
        this.estimatedPct = estimatedPct;
    }

    public EstimationMethod getEstimationMethod() {
        return estimationMethod;
    }

    public void setEstimationMethod(EstimationMethod estimationMethod) {
        this.estimationMethod = estimationMethod;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
