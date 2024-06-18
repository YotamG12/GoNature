package common;
/**
 * Defines the types of requests that can be sent to and from the server in a park management system.
 * These request types are used to specify the action or information being requested, such as creating
 * new reservations, checking availability, logging in or out, and generating reports.
 * <p>
 * The enum constants represent specific operations within the system, facilitating clear and consistent
 * communication between different components of the application.
 * </p>
 */
public enum RequestType {
	
	NEW_SINGLE_RESERVATION,
	ENTER_WAITING_LIST,
	CHECK_AVAILABILITY_SINGLE_RESERVATION,
	FREE_SLOTS_DATA,
	CONFIRMATION_SINGLE_RESERVATION,
	PREPAYMENT,
	PAYMENT,
	OCCASIONAL_PAYMENT,
	
	NEW_GROUP_RESERVATION,
	CONFIRMATION_GROUP_RESERVATION, 
	TOTAL_VISITORS_NUM_REPORT,
	USE_REPORT,
	RELEVANT_PARKS,
	VIEW_TOTAL_VISITORS_NUM_REPORT,
	VIEW_USE_REPORT,
	
	
	WORKER_LOGIN,
	TRAVELER_LOGIN,
	WORKER_LOGOUT,
	TRAVELER_LOGOUT,
	SET_ENTRANCE_TIME,
	SET_LEAVING_TIME,
	REGISTER_GROUP_GUIDE,
	NEW_OCCASIONAL_RESERVATION,
	VISITS_REPORT,
	CANCELATION_REPORT,
	CANCELATION_REPORT_ALL,
	
	CHECK_GROUP_GUIDE,
	CHECK_RESERVATION_ID,
	UPDATE_PARAMETERS,
	APPROVED_PARAMETERS,
	APPROVED_DECLINED,
	APPROVE_RESERVATION,
	CANCEL_RESERVATIONS,
	GET_ALL_RESERVATIONS,
	PARK_DETAILS_DM,
	PARK_DETAILS
	
}
