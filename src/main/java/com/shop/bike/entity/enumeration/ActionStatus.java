package com.shop.bike.entity.enumeration;

public enum ActionStatus {
	
	DELETED,
	ACTIVATED,
	
	WAITING_FOR_APPROVED,

	REVIEWER_APPROVED_ADMIN_REJECTED,

	REVIEWER_REJECTED_ADMIN_APPROVED,

	WAITING_FOR_REVIEWER_REJECT,

	WAITING_FOR_REVIEWER_APPROVED,
	
	APPROVED,

	REJECTED
}
