open util/ordering[StateVector]

abstract sig Value_S_flying {}
one sig flying_S_flying, on_the_ground_S_flying extends Value_S_flying {}

abstract sig Value_S_pylon_inspection {}
one sig complete_S_pylon_inspection, not_complete_S_pylon_inspection extends Value_S_pylon_inspection {}

abstract sig Value_S_at_landing_position {}
one sig yes_S_at_landing_position, no_S_at_landing_position extends Value_S_at_landing_position {}

abstract sig Value_S_communication {}
one sig stable_S_communication, lost_S_communication extends Value_S_communication {}

abstract sig Value_S_battery {}
one sig ok_S_battery, critical_S_battery extends Value_S_battery {}

abstract sig Value_S_critical_battery_notification {}
one sig not_yet_notified_S_critical_battery_notification, notified_S_critical_battery_notification extends Value_S_critical_battery_notification {}

abstract sig Action {}
one sig take_off, land, go_to_landing_zone, execute_mission_task, notify_critical_battery, reset_critical_battery_notification_status extends Action {}

sig StateVector {
    S_flying: Value_S_flying,
    S_pylon_inspection: Value_S_pylon_inspection,
    S_at_landing_position: Value_S_at_landing_position,
    S_communication: Value_S_communication,
    S_battery: Value_S_battery,
    S_critical_battery_notification: Value_S_critical_battery_notification,
    Executing: set Action
}

fact {
    all state: StateVector {
        take_off in state.Executing <=> (
            (state.S_flying = on_the_ground_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery)
        )
    }
}

fact {
    all state: StateVector {
        land in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_at_landing_position = yes_S_at_landing_position && state.S_communication = lost_S_communication) || 
            (state.S_flying = flying_S_flying && state.S_at_landing_position = yes_S_at_landing_position && state.S_battery = critical_S_battery) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = complete_S_pylon_inspection && state.S_at_landing_position = yes_S_at_landing_position)
        )
    }
}

fact {
    all state: StateVector {
        go_to_landing_zone in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_at_landing_position = no_S_at_landing_position && state.S_communication = lost_S_communication) || 
            (state.S_flying = flying_S_flying && state.S_at_landing_position = no_S_at_landing_position && state.S_battery = critical_S_battery) || 
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = complete_S_pylon_inspection && state.S_at_landing_position = no_S_at_landing_position)
        )
    }
}

fact {
    all state: StateVector {
        execute_mission_task in state.Executing <=> (
            (state.S_flying = flying_S_flying && state.S_pylon_inspection = not_complete_S_pylon_inspection && state.S_communication = stable_S_communication && state.S_battery = ok_S_battery)
        )
    }
}

fact {
    all state: StateVector {
        notify_critical_battery in state.Executing <=> (
            (state.S_communication = stable_S_communication && state.S_battery = critical_S_battery && state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification)
        )
    }
}

fact {
    all state: StateVector {
        reset_critical_battery_notification_status in state.Executing <=> (
            (state.S_battery = ok_S_battery && state.S_critical_battery_notification = notified_S_critical_battery_notification)
        )
    }
}

////////////////////////////////

// p1
// check {
//     all state: StateVector {
//         state.S_flying = on_the_ground_S_flying => state.S_at_landing_position = yes_S_at_landing_position
//     }
// } for 50

// a1.1
// fact {
//     first.S_flying = on_the_ground_S_flying &&
//     first.S_at_landing_position = yes_S_at_landing_position
// }

// a1.2
// fact {
//     all state: StateVector, nextState: state.next {
//         (state.S_at_landing_position != nextState.S_at_landing_position)
//         =>
//         (
//             state.S_flying = flying_S_flying &&
//             nextState.S_flying = flying_S_flying
//         )
//     }
// }

// a1.3
// fact {
//     all state: StateVector, nextState: state.next {
//         state.S_flying != nextState.S_flying => (take_off in state.Executing || land in state.Executing)
//     }
// }

////////////////////////////////

// p3
// check {
//     all state: StateVector {
//         (
//             state.S_battery = critical_S_battery &&
//             state.S_critical_battery_notification = not_yet_notified_S_critical_battery_notification
//         ) => notify_critical_battery in state.Executing
//     }
// } for 50 StateVector

// a3.2
// fact {
//     all state: StateVector {
//         state.S_communication != lost_S_communication
//     }
// }

////////////////////////////////

one sig Constants {
    StartLoop: StateVector
}

fact {
    Constants.StartLoop != last &&
    Constants.StartLoop.S_flying = last.S_flying &&
    Constants.StartLoop.S_pylon_inspection = last.S_pylon_inspection &&
    Constants.StartLoop.S_at_landing_position = last.S_at_landing_position &&
    Constants.StartLoop.S_communication = last.S_communication &&
    Constants.StartLoop.S_battery = last.S_battery &&
    Constants.StartLoop.S_critical_battery_notification = last.S_critical_battery_notification &&
    Constants.StartLoop.Executing = last.Executing
}

pred loop[s: StateVector] {
    s = Constants.StartLoop || s in Constants.StartLoop.nexts
}

// p4
check {
    some state: StateVector - last {
        state.S_pylon_inspection = complete_S_pylon_inspection
    }
} for 50 StateVector

// a4.1
fact {
    !(
        all state : StateVector - last, nextState: state.next {
            loop[state] => (
                execute_mission_task in state.Executing &&
                nextState.S_pylon_inspection != complete_S_pylon_inspection
            )
        }
    )
}

// a4.2
fact {
    !(
        all state : StateVector - last {
            loop[state] => (
                take_off in state.Executing &&
                state.next.S_flying != flying_S_flying
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

// a4.8
fact {
    all state: StateVector - last {
        loop[state] => state.S_battery = ok_S_battery
    }
}

// a4.10
fact {
    all state: StateVector - last {
        loop[state] => state.S_communication = stable_S_communication
    }
}
