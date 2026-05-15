package com.Sedrak.AutoMessager.Model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(hidden = true)
  private Long id;

  private String username;

  @Schema(hidden = true)
  private int messagesSent;

  @Schema(hidden = true)
  @Column(length = 3000)
  @Builder.Default
  private String message1 = "fuck u1";

  @JsonIgnore
  private boolean isMessage1Sent;

  @Schema(hidden = true)
  @Column(length = 3000)
  @Builder.Default
  private String message2 = "fuck u2";

  @JsonIgnore
  private boolean isMessage2Sent;

  @Schema(hidden = true)
  @Column(length = 3000)
  @Builder.Default
  private String message3 = "fuck u3";

  @JsonIgnore
  private boolean isMessage3Sent;

}
