package ua.softserveinc.tc.entity;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import ua.softserveinc.tc.constants.DiscountConstants;

@Entity
@Table(name = DiscountConstants.Entity.TABLE_NAME_DAYSDISCOUNTS)
public class DayDiscount {

  @Id
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = DiscountConstants.Entity.DAY_DISCOUNT_ID, nullable = false)
  private Long id;

  @Column(name = DiscountConstants.Entity.DISCOUNT_REASON,nullable = false)
  @NotNull
  @Size(min =3 ,max = 255)
  private String reason;

  @Column(name = DiscountConstants.Entity.DISCOUNT_VALUE,nullable = false)
  @NotNull
  @Max(value = 100)
  @Min(value = 1)
  private int value;

  @Column(name = DiscountConstants.Entity.DISCOUNT_START_DATE,nullable = false)
  @NotNull
  private Date startDate;

  @Column(name = DiscountConstants.Entity.DISCOUNT_END_DATE,nullable = false)
  @NotNull
  private Date endDate;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DayDiscount that = (DayDiscount) o;
    return id == that.id &&
        value == that.value &&
        Objects.equals(reason, that.reason) &&
        Objects.equals(startDate, that.startDate) &&
        Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, reason, value, startDate, endDate);
  }
}
