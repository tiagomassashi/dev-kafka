package br.com.nagata.dev.model;

import br.com.nagata.dev.model.dto.MessageRequestDTO;
import br.com.nagata.dev.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_APP_LOG", schema = "PUBLIC")
public class ApplicationLog {
  @Id
  @SequenceGenerator(
      name = "appLogSequence",
      sequenceName = "PUBLIC.SQ_APP_LOG",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "appLogSequence")
  @Column(name = "CD_SEQU_APP_LOG", length = 9, nullable = false)
  private Long applicationLogCode;

  @Column(name = "CD_CHANNEL", length = 23, nullable = false)
  private String channelReference;

  @Column(name = "CD_CUSTOMER", length = 9, nullable = false)
  private Long customerId;

  @Column(name = "NM_CUSTOMER", length = 80, nullable = false)
  private String customerName;

  @Column(name = "VL_ORDER", nullable = false)
  private BigDecimal orderValue;

  @Column(name = "DT_ORDER", nullable = false)
  private LocalDate orderDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "CD_STATUS", length = 15, nullable = false)
  private StatusEnum status;

  @Column(name = "DS_STATUS")
  private String statusDescription;

  public ApplicationLog(String id, MessageRequestDTO dto) {
    this.channelReference = id;
    this.customerId = dto.getCustomerId();
    this.customerName = dto.getCustomerName();
    this.orderValue = dto.getOrderValue();
    this.orderDate = dto.getOrderDate();
    this.status = dto.getStatus();
    this.statusDescription = dto.getStatusDescription();
  }
}
