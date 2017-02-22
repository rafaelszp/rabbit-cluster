package com.rabbitcharges.vo;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by rafael on 2/22/17.
 */
public class Charge {

  private UUID id;
  private String company;
  private Long contractId;
  private BigDecimal value;


  public Charge(){}

  public Charge(UUID id, String company, Long contractId, BigDecimal value) {
	this.id = id;
	this.company = company;
	this.contractId = contractId;
	this.value = value;
  }

  public UUID getId() {
	return id;
  }

  public void setId(UUID id) {
	this.id = id;
  }

  public String getCompany() {
	return company;
  }

  public void setCompany(String company) {
	this.company = company;
  }

  public Long getContractId() {
	return contractId;
  }

  public void setContractId(Long contractId) {
	this.contractId = contractId;
  }

  public BigDecimal getValue() {
	return value;
  }

  public void setValue(BigDecimal value) {
	this.value = value;
  }
}
