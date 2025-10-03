open util/ordering[StateVector]

abstract sig Value_S_flying {}
one sig flying_S_flying, on_the_ground_S_flying extends Value_S_flying {}

abstract sig Value_S_pylon_inspection {}
one sig complete_S_pylon_inspection, not_complete_S_pylon_inspection extends Value_S_pylon_inspection {}

abstract sig Value_S_manual_control_request {}
one sig on_S_manual_control_request, off_S_manual_control_request extends Value_S_manual_control_request {}

abstract sig Value_S_at_landing_position {}
one sig yes_S_at_landing_position, no_S_at_landing_position, unknown_S_at_landing_position extends Value_S_at_landing_position {}

abstract sig Value_S_mission_configuration {}
one sig configured_S_mission_configuration, not_configured_S_mission_configuration extends Value_S_mission_configuration {}

abstract sig Value_S_mission_started {}
one sig yes_S_mission_started, no_S_mission_started extends Value_S_mission_started {}

abstract sig Value_S_being_blocked {}
one sig yes_S_being_blocked, no_S_being_blocked extends Value_S_being_blocked {}

abstract sig Value_S_being_blocked_notification {}
one sig not_yet_notified_S_being_blocked_notification, notified_S_being_blocked_notification extends Value_S_being_blocked_notification {}

abstract sig Value_S_communication {}
one sig stable_S_communication, degraded_S_communication, lost_S_communication extends Value_S_communication {}

abstract sig Value_S_communication_failure_notification {}
one sig not_yet_notified_S_communication_failure_notification, notified_S_communication_failure_notification extends Value_S_communication_failure_notification {}

abstract sig Value_S_battery {}
one sig ok_S_battery, low_S_battery, critical_S_battery extends Value_S_battery {}

abstract sig Value_S_low_battery_notification {}
one sig not_yet_notified_S_low_battery_notification, notified_S_low_battery_notification extends Value_S_low_battery_notification {}

abstract sig Value_S_critical_battery_notification {}
one sig not_yet_notified_S_critical_battery_notification, notified_S_critical_battery_notification extends Value_S_critical_battery_notification {}

abstract sig Value_S_obstacle_detection {}
one sig stable_S_obstacle_detection, lost_S_obstacle_detection extends Value_S_obstacle_detection {}

abstract sig Value_S_obstacle_detection_failure_notification {}
one sig not_yet_notified_S_obstacle_detection_failure_notification, notified_S_obstacle_detection_failure_notification extends Value_S_obstacle_detection_failure_notification {}

abstract sig Value_S_mission_abort_request {}
one sig yes_S_mission_abort_request, no_S_mission_abort_request extends Value_S_mission_abort_request {}

abstract sig Action {}
one sig take_off, land, go_to_landing_zone, manual_control, execute_mission_task, configure_mission, wait_for_start, notify_being_blocked, notify_communication_failure, notify_low_battery, notify_critical_battery, notify_obstacle_detection_failure, reset_being_blocked_notification_status, reset_communication_failure_notification_status, reset_low_battery_notification_status, reset_critical_battery_notification_status, reset_obstacle_detection_failure_notification_status extends Action {}

sig StateVector {
    S_flying: Value_S_flying,
    S_pylon_inspection: Value_S_pylon_inspection,
    S_manual_control_request: Value_S_manual_control_request,
    S_at_landing_position: Value_S_at_landing_position,
    S_mission_configuration: Value_S_mission_configuration,
    S_mission_started: Value_S_mission_started,
    S_being_blocked: Value_S_being_blocked,
    S_being_blocked_notification: Value_S_being_blocked_notification,
    S_communication: Value_S_communication,
    S_communication_failure_notification: Value_S_communication_failure_notification,
    S_battery: Value_S_battery,
    S_low_battery_notification: Value_S_low_battery_notification,
    S_critical_battery_notification: Value_S_critical_battery_notification,
    S_obstacle_detection: Value_S_obstacle_detection,
    S_obstacle_detection_failure_notification: Value_S_obstacle_detection_failure_notification,
    S_mission_abort_request: Value_S_mission_abort_request,
    Executing: set Action
}

fact {
    all state: StateVector {
        take_off in state.Executing <=> (
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request)
        )
    }
}

fact {
    all state: StateVector {
        land in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = lost_S_communication) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_battery = critical_S_battery) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_mission_abort_request = yes_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started)
        )
    }
}

fact {
    all state: StateVector {
        go_to_landing_zone in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = lost_S_communication) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_battery = critical_S_battery) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_mission_abort_request = yes_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started)
        )
    }
}

fact {
    all state: StateVector {
        manual_control in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = on_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = on_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = on_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started)
        )
    }
}

fact {
    all state: StateVector {
        execute_mission_task in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_mission_abort_request = no_S_mission_abort_request)
        )
    }
}

fact {
    all state: StateVector {
        configure_mission in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_battery = ok_S_battery && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication = stable_S_communication && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication = stable_S_communication && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked_notification = notified_S_being_blocked_notification && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_being_blocked = no_S_being_blocked && state.S_communication_failure_notification = notified_S_communication_failure_notification && state.S_low_battery_notification = notified_S_low_battery_notification && state.S_obstacle_detection = stable_S_obstacle_detection) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_communication = lost_S_communication) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_battery = critical_S_battery) || 
            (state.S_flying = flying_S_flying && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started && state.S_mission_abort_request = yes_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = complete_S_pylon_inspection && state.S_manual_control_request = off_S_manual_control_request && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = yes_S_mission_started)
        )
    }
}

fact {
    all state: StateVector {
        wait_for_start in state.Executing <=> (
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = ok_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_mission_abort_request = no_S_mission_abort_request) || 
            (state.S_flying = flying_S_flying && state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_mission_started = no_S_mission_started) || 
            (state.S_flying = flying_S_flying && state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started) || 
            (state.S_flying = flying_S_flying && state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_mission_started = no_S_mission_started)
        )
    }
}

fact {
    all state: StateVector {
        notify_being_blocked in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = stable_S_communication) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = yes_S_being_blocked && state.S_being_blocked_notification = not_yet_notified_S_being_blocked_notification && state.S_communication = degraded_S_communication)
        )
    }
}

fact {
    all state: StateVector {
        notify_communication_failure in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_communication_failure_notification = not_yet_notified_S_communication_failure_notification)
        )
    }
}

fact {
    all state: StateVector {
        notify_low_battery in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = low_S_battery && state.S_low_battery_notification = not_yet_notified_S_low_battery_notification)
        )
    }
}

fact {
    all state: StateVector {
        notify_critical_battery in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification)
        )
    }
}

fact {
    all state: StateVector {
        notify_obstacle_detection_failure in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = degraded_S_communication && state.S_obstacle_detection = lost_S_obstacle_detection && state.S_obstacle_detection_failure_notification = not_yet_notified_S_obstacle_detection_failure_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_being_blocked_notification_status in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_being_blocked = no_S_being_blocked && state.S_being_blocked_notification = notified_S_being_blocked_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = no_S_being_blocked && state.S_being_blocked_notification = notified_S_being_blocked_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_being_blocked = no_S_being_blocked && state.S_being_blocked_notification = notified_S_being_blocked_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_communication_failure_notification_status in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_communication = stable_S_communication && state.S_communication_failure_notification = notified_S_communication_failure_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_low_battery_notification_status in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_battery = critical_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = critical_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = critical_S_battery && state.S_low_battery_notification = notified_S_low_battery_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_critical_battery_notification_status in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_battery = low_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = low_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = ok_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_battery = low_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_obstacle_detection_failure_notification_status in state.Executing <=> (
            (state.S_at_landing_position = unknown_S_at_landing_position && state.S_mission_configuration = not_configured_S_mission_configuration && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = yes_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification) || 
            (state.S_at_landing_position = no_S_at_landing_position && state.S_mission_configuration = configured_S_mission_configuration && state.S_obstacle_detection = stable_S_obstacle_detection && state.S_obstacle_detection_failure_notification = notified_S_obstacle_detection_failure_notification)
        )
    }
}

////////////////////////////////

// // a1.1
// fact {
//     first.S_flying = on_the_ground_S_flying &&
//     first.S_at_landing_position = yes_S_at_landing_position
// }

// // a1.2
// fact {
//     all state: StateVector, nextState: state.next {
//         (
//             state.S_at_landing_position != unknown_S_at_landing_position &&
//             state.S_at_landing_position != nextState.S_at_landing_position
//         ) 
//         =>
//         (
//             state.S_flying = flying_S_flying &&
//             nextState.S_flying = flying_S_flying
//         )
//     }
// }

// // a1.3
// fact {
//     all state: StateVector, nextState: state.next {
//         state.S_flying != nextState.S_flying => (take_off in state.Executing || land in state.Executing)
//     }
// }

// // p1
// check {
//     all state: StateVector {
//         state.S_flying = on_the_ground_S_flying => state.S_at_landing_position = yes_S_at_landing_position
//     }
// } for 50 StateVector

////////////////////////////////

// // a2.1
// fact {
//     first.S_flying = on_the_ground_S_flying
// }

// // a2.2
// fact {
//     all state: StateVector, nextState: state.next {
//         (
//             state.S_flying = on_the_ground_S_flying && nextState.S_flying = flying_S_flying
//         ) => take_off in state.Executing
//     }
// }

// // a2.3
// fact {
//     all state: StateVector, nextState: state.next {
//         state.S_mission_started = yes_S_mission_started => nextState.S_mission_started = yes_S_mission_started
//     }
// }

// // p2
// check {
//     all state: StateVector {
//         state.S_mission_started = no_S_mission_started => state.S_flying != flying_S_flying
//     }
// } for 50 StateVector

////////////////////////////////

// // a3.1
// fact {
//     all state: StateVector {
//         state.S_mission_configuration = configured_S_mission_configuration <=> state.S_at_landing_position != unknown_S_at_landing_position
//     }
// }

// // a3.2
// fact {
//     all state: StateVector {
//         state.S_communication != lost_S_communication
//     }
// }

// // p3
// check {
//     all state: StateVector {
//         (
//             state.S_battery = low_S_battery &&
//             state.S_low_battery_notification = not_yet_notified_S_low_battery_notification
//         ) => notify_low_battery in state.Executing
//     }
// } for 50 StateVector

////////////////////////////////

one sig Constants {
    StartLoop: StateVector
}

fact {
    Constants.StartLoop != last &&
    Constants.StartLoop.S_flying = last.S_flying &&
    Constants.StartLoop.S_pylon_inspection = last.S_pylon_inspection &&
    Constants.StartLoop.S_manual_control_request = last.S_manual_control_request &&
    Constants.StartLoop.S_at_landing_position = last.S_at_landing_position &&
    Constants.StartLoop.S_mission_configuration = last.S_mission_configuration &&
    Constants.StartLoop.S_mission_started = last.S_mission_started &&
    Constants.StartLoop.S_being_blocked = last.S_being_blocked &&
    Constants.StartLoop.S_being_blocked_notification = last.S_being_blocked_notification &&
    Constants.StartLoop.S_communication = last.S_communication &&
    Constants.StartLoop.S_communication_failure_notification = last.S_communication_failure_notification &&
    Constants.StartLoop.S_battery = last.S_battery &&
    Constants.StartLoop.S_low_battery_notification = last.S_low_battery_notification &&
    Constants.StartLoop.S_critical_battery_notification = last.S_critical_battery_notification &&
    Constants.StartLoop.S_obstacle_detection = last.S_obstacle_detection &&
    Constants.StartLoop.S_obstacle_detection_failure_notification = last.S_obstacle_detection_failure_notification &&
    Constants.StartLoop.S_mission_abort_request = last.S_mission_abort_request &&
    Constants.StartLoop.Executing = last.Executing
}

pred loop[s: StateVector] {
    s = Constants.StartLoop || s in Constants.StartLoop.nexts
}

// a4.1
fact {
    !(
        all state : StateVector - last, nextState: state.next {
            loop[state] => (
                execute_mission_task in state.Executing &&
                state.S_being_blocked = no_S_being_blocked &&
                nextState.S_pylon_inspection != complete_S_pylon_inspection
            )
        }
    )
}

// a4.2
fact {
    !(
        all state : StateVector - last, nextState: state.next {
            loop[state] => (
                take_off in state.Executing &&
                nextState.S_flying != flying_S_flying
            )
        }
    )
}

// a4.3
fact {
    !(
        all state : StateVector - last, nextState: state.next {
            loop[state] => (
                land in state.Executing &&
                nextState.S_flying != on_the_ground_S_flying
            )
        }
    )
}

// a4.4
fact {
    !(
        all state : StateVector - last, nextState: state.next {
            loop[state] => (
                configure_mission in state.Executing &&
                nextState.S_mission_configuration != configured_S_mission_configuration
            )
        }
    )
}

// a4.5
fact {
    all state: StateVector - last, nextState: state.next {
        state.S_flying != nextState.S_flying => (take_off in state.Executing || land in state.Executing)
    }
}

// a4.6
fact {
    all state: StateVector - last, nextState: state.next {
        state.S_mission_started = yes_S_mission_started => nextState.S_mission_started = yes_S_mission_started
    }
}

// a4.7
fact {
    all state: StateVector - last {
        state.S_mission_configuration = configured_S_mission_configuration <=> state.S_at_landing_position != unknown_S_at_landing_position
    }
}

// a4.8
fact {
    all state: StateVector - last {
        loop[state] => state.S_battery = ok_S_battery
    }
}

// a4.9
fact {
    all state: StateVector - last {
        loop[state] => state.S_being_blocked = no_S_being_blocked
    }
}

// a4.10
fact {
    all state: StateVector - last {
        loop[state] => state.S_communication = stable_S_communication
    }
}

// a4.11
fact {
    all state: StateVector - last {
        loop[state] => state.S_obstacle_detection = stable_S_obstacle_detection
    }
}

// a4.12
fact {
    all state: StateVector - last {
        state.S_mission_abort_request = no_S_mission_abort_request
    }
}

// a4.13
fact {
    all state: StateVector - last {
        loop[state] => state.S_manual_control_request = off_S_manual_control_request
    }
}

// a4.14
fact {
    some state: StateVector - last {
        state.S_mission_started = yes_S_mission_started
    }
}

// a4.15
fact {
    all state: StateVector - last, nextState: state.next {
        state.S_mission_configuration = configured_S_mission_configuration => nextState.S_mission_configuration = configured_S_mission_configuration
    }
}

// p4
check {
    some state: StateVector - last {
        state.S_pylon_inspection = complete_S_pylon_inspection
    }
} for 50 StateVector
