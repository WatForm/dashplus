open util/boolean[] 

open util/steps[Snapshot]
open util/ordering[Snapshot]
open util/boolean

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        stable: one Bool,
        events: set EventLabel,
        FlightModes_Pilot_Flying_Side : one Bool,
        FlightModes_Pilot_Flying_Transfer : one Bool,
        FlightModes_HDG_Switch_Pressed : one Bool,
        FlightModes_NAV_Switch_Pressed : one Bool,
        FlightModes_GA_Switch_Pressed : one Bool,
        FlightModes_When_AP_Engaged : one Bool,
        FlightModes_FD_Switch_Pressed : one Bool,
        FlightModes_Overspeed : one Bool,
        FlightModes_VS_Switch_Pressed : one Bool,
        FlightModes_FLC_Switch_Pressed : one Bool,
        FlightModes_ALT_Switch_Pressed : one Bool,
        FlightModes_APPR_Switch_Pressed : one Bool,
        FlightModes_VS_Pitch_Wheel_Rotated : one Bool,
        FlightModes_Selected_NAV_Source_Changed : one Bool,
        FlightModes_Selected_NAV_Frequency_Changed : one Bool,
        FlightModes_Is_AP_Engaged : one Bool,
        FlightModes_Is_Offside_FD_On : one Bool,
        FlightModes_LAPPR_Capture_Condition_Met : one Bool,
        FlightModes_SYNC_Switch_Pressed : one Bool,
        FlightModes_NAV_Capture_Condition_Met : one Bool,
        FlightModes_ALTSEL_Target_Changed : one Bool,
        FlightModes_ALTSEL_Capture_Condition_Met : one Bool,
        FlightModes_ALTSEL_Track_Condition_Met : one Bool,
        FlightModes_VAPPR_Capture_Condition_Met : one Bool,
        FlightModes_FD_On : one Bool,
        FlightModes_Modes_On : one Bool,
        FlightModes_HDG_Selected : one Bool,
        FlightModes_HDG_Active : one Bool,
        FlightModes_NAV_Selected : one Bool,
        FlightModes_NAV_Active : one Bool,
        FlightModes_VS_Active : one Bool,
        FlightModes_LAPPR_Selected : one Bool,
        FlightModes_LAPPR_Active : one Bool,
        FlightModes_LGA_Selected : one Bool,
        FlightModes_LGA_Active : one Bool,
        FlightModes_ROLL_Active : one Bool,
        FlightModes_ROLL_Selected : one Bool,
        FlightModes_VS_Selected : one Bool,
        FlightModes_FLC_Selected : one Bool,
        FlightModes_FLC_Active : one Bool,
        FlightModes_ALT_Active : one Bool,
        FlightModes_ALTSEL_Active : one Bool,
        FlightModes_ALT_Selected : one Bool,
        FlightModes_ALTSEL_Track : one Bool,
        FlightModes_ALTSEL_Selected : one Bool,
        FlightModes_PITCH_Selected : one Bool,
        FlightModes_VAPPR_Selected : one Bool,
        FlightModes_VAPPR_Active : one Bool,
        FlightModes_VGA_Selected : one Bool,
        FlightModes_VGA_Active : one Bool,
        FlightModes_PITCH_Active : one Bool,
        FlightModes_Independent_Mode : one Bool,
        FlightModes_Active_Side : one Bool
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig FlightModes extends SystemState {}
    abstract sig FlightModes_FD extends FlightModes {}
    one sig FlightModes_FD_OFF extends FlightModes_FD {}
    one sig FlightModes_FD_ON extends FlightModes_FD {}
    abstract sig FlightModes_ANNUNCIATIONS extends FlightModes {}
    one sig FlightModes_ANNUNCIATIONS_OFF extends FlightModes_ANNUNCIATIONS {}
    one sig FlightModes_ANNUNCIATIONS_ON extends FlightModes_ANNUNCIATIONS {}
    abstract sig FlightModes_LATERAL extends FlightModes {}
    abstract sig FlightModes_LATERAL_HDG extends FlightModes_LATERAL {}
    one sig FlightModes_LATERAL_HDG_CLEARED extends FlightModes_LATERAL_HDG {}
    abstract sig FlightModes_LATERAL_HDG_SELECTED extends FlightModes_LATERAL_HDG {}
    one sig FlightModes_LATERAL_HDG_SELECTED_ACTIVE extends FlightModes_LATERAL_HDG_SELECTED {}
    abstract sig FlightModes_LATERAL_NAV extends FlightModes_LATERAL {}
    one sig FlightModes_LATERAL_NAV_CLEARED extends FlightModes_LATERAL_NAV {}
    abstract sig FlightModes_LATERAL_NAV_SELECTED extends FlightModes_LATERAL_NAV {}
    one sig FlightModes_LATERAL_NAV_SELECTED_ARMED extends FlightModes_LATERAL_NAV_SELECTED {}
    one sig FlightModes_LATERAL_NAV_SELECTED_ACTIVE extends FlightModes_LATERAL_NAV_SELECTED {}
    abstract sig FlightModes_LATERAL_LAPPR extends FlightModes_LATERAL {}
    one sig FlightModes_LATERAL_LAPPR_CLEARED extends FlightModes_LATERAL_LAPPR {}
    abstract sig FlightModes_LATERAL_LAPPR_SELECTED extends FlightModes_LATERAL_LAPPR {}
    one sig FlightModes_LATERAL_LAPPR_SELECTED_ARMED extends FlightModes_LATERAL_LAPPR_SELECTED {}
    one sig FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE extends FlightModes_LATERAL_LAPPR_SELECTED {}
    abstract sig FlightModes_LATERAL_LGA extends FlightModes_LATERAL {}
    one sig FlightModes_LATERAL_LGA_CLEARED extends FlightModes_LATERAL_LGA {}
    abstract sig FlightModes_LATERAL_LGA_SELECTED extends FlightModes_LATERAL_LGA {}
    one sig FlightModes_LATERAL_LGA_SELECTED_ACTIVE extends FlightModes_LATERAL_LGA_SELECTED {}
    abstract sig FlightModes_LATERAL_ROLL extends FlightModes_LATERAL {}
    one sig FlightModes_LATERAL_ROLL_CLEARED extends FlightModes_LATERAL_ROLL {}
    abstract sig FlightModes_LATERAL_ROLL_SELECTED extends FlightModes_LATERAL_ROLL {}
    one sig FlightModes_LATERAL_ROLL_SELECTED_ACTIVE extends FlightModes_LATERAL_ROLL_SELECTED {}
    abstract sig FlightModes_VERTICAL extends FlightModes {}
    abstract sig FlightModes_VERTICAL_VS extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_VS_CLEARED extends FlightModes_VERTICAL_VS {}
    abstract sig FlightModes_VERTICAL_VS_SELECTED extends FlightModes_VERTICAL_VS {}
    one sig FlightModes_VERTICAL_VS_SELECTED_ACTIVE extends FlightModes_VERTICAL_VS_SELECTED {}
    abstract sig FlightModes_VERTICAL_FLC extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_FLC_CLEARED extends FlightModes_VERTICAL_FLC {}
    abstract sig FlightModes_VERTICAL_FLC_SELECTED extends FlightModes_VERTICAL_FLC {}
    one sig FlightModes_VERTICAL_FLC_SELECTED_ACTIVE extends FlightModes_VERTICAL_FLC_SELECTED {}
    abstract sig FlightModes_VERTICAL_ALT extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_ALT_CLEARED extends FlightModes_VERTICAL_ALT {}
    abstract sig FlightModes_VERTICAL_ALT_SELECTED extends FlightModes_VERTICAL_ALT {}
    one sig FlightModes_VERTICAL_ALT_SELECTED_ACTIVE extends FlightModes_VERTICAL_ALT_SELECTED {}
    abstract sig FlightModes_VERTICAL_ALTSEL extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_ALTSEL_CLEARED extends FlightModes_VERTICAL_ALTSEL {}
    abstract sig FlightModes_VERTICAL_ALTSEL_SELECTED extends FlightModes_VERTICAL_ALTSEL {}
    one sig FlightModes_VERTICAL_ALTSEL_SELECTED_ARMED extends FlightModes_VERTICAL_ALTSEL_SELECTED {}
    abstract sig FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE extends FlightModes_VERTICAL_ALTSEL_SELECTED {}
    one sig FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_CAPTURE extends FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE {}
    one sig FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK extends FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE {}
    abstract sig FlightModes_VERTICAL_VAPPR extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_VAPPR_CLEARED extends FlightModes_VERTICAL_VAPPR {}
    abstract sig FlightModes_VERTICAL_VAPPR_SELECTED extends FlightModes_VERTICAL_VAPPR {}
    one sig FlightModes_VERTICAL_VAPPR_SELECTED_ARMED extends FlightModes_VERTICAL_VAPPR_SELECTED {}
    one sig FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE extends FlightModes_VERTICAL_VAPPR_SELECTED {}
    abstract sig FlightModes_VERTICAL_VGA extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_VGA_CLEARED extends FlightModes_VERTICAL_VGA {}
    abstract sig FlightModes_VERTICAL_VGA_SELECTED extends FlightModes_VERTICAL_VGA {}
    one sig FlightModes_VERTICAL_VGA_SELECTED_ACTIVE extends FlightModes_VERTICAL_VGA_SELECTED {}
    abstract sig FlightModes_VERTICAL_PITCH extends FlightModes_VERTICAL {}
    one sig FlightModes_VERTICAL_PITCH_CLEARED extends FlightModes_VERTICAL_PITCH {}
    abstract sig FlightModes_VERTICAL_PITCH_SELECTED extends FlightModes_VERTICAL_PITCH {}
    one sig FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE extends FlightModes_VERTICAL_PITCH_SELECTED {}

/***************************** EVENTS SPACE ***********************************/
    one sig FlightModes_LATERAL_New_Lateral_Mode_Activated extends InternalEvent {}
    one sig FlightModes_VERTICAL_New_Vertical_Mode_Activated extends InternalEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig FlightModes_FD_TurnFDOn extends TransitionLabel {}
    one sig FlightModes_FD_TurnFDOff extends TransitionLabel {}
    one sig FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn extends TransitionLabel {}
    one sig FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff extends TransitionLabel {}
    one sig FlightModes_LATERAL_HDG_Select extends TransitionLabel {}
    one sig FlightModes_LATERAL_HDG_Clear extends TransitionLabel {}
    one sig FlightModes_LATERAL_HDG_NewLateralModeActivated extends TransitionLabel {}
    one sig FlightModes_LATERAL_NAV_Select extends TransitionLabel {}
    one sig FlightModes_LATERAL_NAV_Capture extends TransitionLabel {}
    one sig FlightModes_LATERAL_NAV_Clear extends TransitionLabel {}
    one sig FlightModes_LATERAL_NAV_NewLateralModeActivated extends TransitionLabel {}
    one sig FlightModes_LATERAL_LAPPR_Select extends TransitionLabel {}
    one sig FlightModes_LATERAL_LAPPR_Capture extends TransitionLabel {}
    one sig FlightModes_LATERAL_LAPPR_Clear extends TransitionLabel {}
    one sig FlightModes_LATERAL_LAPPR_NewLateralModeActivated extends TransitionLabel {}
    one sig FlightModes_LATERAL_LGA_Select extends TransitionLabel {}
    one sig FlightModes_LATERAL_LGA_Clear extends TransitionLabel {}
    one sig FlightModes_LATERAL_LGA_NewLateralModeActivated extends TransitionLabel {}
    one sig FlightModes_LATERAL_ROLL_Select extends TransitionLabel {}
    one sig FlightModes_LATERAL_ROLL_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VS_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VS_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VS_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_FLC_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_FLC_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_FLC_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALT_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALT_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALT_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALTSEL_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALTSEL_Capture extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALTSEL_Track extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALTSEL_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VAPPR_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VAPPR_Capture extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VAPPR_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VGA_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VGA_Clear extends TransitionLabel {}
    one sig FlightModes_VERTICAL_VGA_NewVerticalModeActivated extends TransitionLabel {}
    one sig FlightModes_VERTICAL_PITCH_Select extends TransitionLabel {}
    one sig FlightModes_VERTICAL_PITCH_Clear extends TransitionLabel {}

    // Transition FlightModes_FD_TurnFDOn
    pred pre_FlightModes_FD_TurnFDOn[s:Snapshot] {
        FlightModes_FD_OFF in s.conf
        (s.FlightModes_FD_Switch_Pressed) = True or (s.FlightModes_When_AP_Engaged) = True or (s.FlightModes_Overspeed) = True or (s.FlightModes_HDG_Switch_Pressed) = True or (s.FlightModes_NAV_Switch_Pressed) = True or (s.FlightModes_APPR_Switch_Pressed) = True or (s.FlightModes_GA_Switch_Pressed) = True or (s.FlightModes_VS_Switch_Pressed) = True or (s.FlightModes_FLC_Switch_Pressed) = True or (s.FlightModes_ALT_Switch_Pressed) = True or (s.FlightModes_APPR_Switch_Pressed) = True or (s.FlightModes_GA_Switch_Pressed) = True or ((s.FlightModes_VS_Pitch_Wheel_Rotated) = True and (s.FlightModes_VS_Active) = False and (s.FlightModes_VAPPR_Active) = False and (s.FlightModes_Overspeed) = False) or ((s.FlightModes_Pilot_Flying_Transfer) = True and (s.FlightModes_Pilot_Flying_Side) = True and (s.FlightModes_Modes_On) = True)
    }

    pred pos_FlightModes_FD_TurnFDOn[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_FD_OFF + {
            FlightModes_FD_ON
        }
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_FD_TurnFDOn] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_FD_ON[s']
    }

    pred FlightModes_FD_TurnFDOn[s, s': Snapshot] {
        pre_FlightModes_FD_TurnFDOn[s]
        pos_FlightModes_FD_TurnFDOn[s, s']
        semantics_FlightModes_FD_TurnFDOn[s, s']
    }

    pred enabledAfterStep_FlightModes_FD_TurnFDOn[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_FD_OFF in s.conf
        (_s.FlightModes_FD_Switch_Pressed) = True or (_s.FlightModes_When_AP_Engaged) = True or (_s.FlightModes_Overspeed) = True or (_s.FlightModes_HDG_Switch_Pressed) = True or (_s.FlightModes_NAV_Switch_Pressed) = True or (_s.FlightModes_APPR_Switch_Pressed) = True or (_s.FlightModes_GA_Switch_Pressed) = True or (_s.FlightModes_VS_Switch_Pressed) = True or (_s.FlightModes_FLC_Switch_Pressed) = True or (_s.FlightModes_ALT_Switch_Pressed) = True or (_s.FlightModes_APPR_Switch_Pressed) = True or (_s.FlightModes_GA_Switch_Pressed) = True or ((_s.FlightModes_VS_Pitch_Wheel_Rotated) = True and (s.FlightModes_VS_Active) = False and (s.FlightModes_VAPPR_Active) = False and (_s.FlightModes_Overspeed) = False) or ((_s.FlightModes_Pilot_Flying_Transfer) = True and (_s.FlightModes_Pilot_Flying_Side) = True and (s.FlightModes_Modes_On) = True)
        _s.stable = True => {
            no t & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        }
    }
    pred semantics_FlightModes_FD_TurnFDOn[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_FD_TurnFDOn
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_FD_TurnFDOn
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        }
    }
    // Transition FlightModes_FD_TurnFDOff
    pred pre_FlightModes_FD_TurnFDOff[s:Snapshot] {
        FlightModes_FD_ON in s.conf
        {
            (s.FlightModes_FD_Switch_Pressed) = True and (s.FlightModes_Overspeed) = False
        }
    }

    pred pos_FlightModes_FD_TurnFDOff[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_FD_ON + {
            FlightModes_FD_OFF
        }
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_FD_TurnFDOff] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_FD_ON in s.conf => exit_FlightModes_FD_ON[s']
    }

    pred FlightModes_FD_TurnFDOff[s, s': Snapshot] {
        pre_FlightModes_FD_TurnFDOff[s]
        pos_FlightModes_FD_TurnFDOff[s, s']
        semantics_FlightModes_FD_TurnFDOff[s, s']
    }

    pred enabledAfterStep_FlightModes_FD_TurnFDOff[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_FD_ON in s.conf
        {
            (_s.FlightModes_FD_Switch_Pressed) = True and (_s.FlightModes_Overspeed) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        }
    }
    pred semantics_FlightModes_FD_TurnFDOff[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_FD_TurnFDOff
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_FD_TurnFDOff
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_FD_TurnFDOn + 
                FlightModes_FD_TurnFDOff
            }
        }
    }
    // Transition FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
    pred pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s:Snapshot] {
        FlightModes_ANNUNCIATIONS_OFF in s.conf
        {
            (s.FlightModes_Is_AP_Engaged) = True or (s.FlightModes_Is_Offside_FD_On) = True or (s.FlightModes_FD_On) = True
        }
    }

    pred pos_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_ANNUNCIATIONS_OFF + {
            FlightModes_ANNUNCIATIONS_ON
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_ANNUNCIATIONS_ON[s']
        FlightModes_ANNUNCIATIONS_OFF in s.conf => exit_FlightModes_ANNUNCIATIONS_OFF[s']
    }

    pred FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s': Snapshot] {
        pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s]
        pos_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s']
        semantics_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s']
    }

    pred enabledAfterStep_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_ANNUNCIATIONS_OFF in s.conf
        {
            (_s.FlightModes_Is_AP_Engaged) = True or (_s.FlightModes_Is_Offside_FD_On) = True or (s.FlightModes_FD_On) = True
        }
        _s.stable = True => {
            no t & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        }
    }
    pred semantics_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        }
    }
    // Transition FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff
    pred pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s:Snapshot] {
        FlightModes_ANNUNCIATIONS_ON in s.conf
        {
            (s.FlightModes_Is_AP_Engaged) = False and (s.FlightModes_Is_Offside_FD_On) = False and (s.FlightModes_FD_On) = False
        }
    }

    pred pos_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_ANNUNCIATIONS_ON + {
            FlightModes_ANNUNCIATIONS_OFF
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_ANNUNCIATIONS_OFF[s']
        FlightModes_ANNUNCIATIONS_ON in s.conf => exit_FlightModes_ANNUNCIATIONS_ON[s']
    }

    pred FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s': Snapshot] {
        pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s]
        pos_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s']
        semantics_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s']
    }

    pred enabledAfterStep_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_ANNUNCIATIONS_ON in s.conf
        {
            (_s.FlightModes_Is_AP_Engaged) = False and (_s.FlightModes_Is_Offside_FD_On) = False and (s.FlightModes_FD_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        }
    }
    pred semantics_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff + 
                FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn
            }
        }
    }
    // Transition FlightModes_LATERAL_HDG_Select
    pred pre_FlightModes_LATERAL_HDG_Select[s:Snapshot] {
        FlightModes_LATERAL_HDG_CLEARED in s.conf
        (s.FlightModes_HDG_Switch_Pressed) = True
    }

    pred pos_FlightModes_LATERAL_HDG_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_HDG_CLEARED + {
            FlightModes_LATERAL_HDG_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_LATERAL_New_Lateral_Mode_Activated}, FlightModes_LATERAL_HDG_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_LATERAL_New_Lateral_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_LATERAL_New_Lateral_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_LATERAL_New_Lateral_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_LATERAL_New_Lateral_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_LATERAL_New_Lateral_Mode_Activated} in s'.events
        enter_FlightModes_LATERAL_HDG_SELECTED[s']
        enter_FlightModes_LATERAL_HDG_SELECTED_ACTIVE[s']
    }

    pred FlightModes_LATERAL_HDG_Select[s, s': Snapshot] {
        pre_FlightModes_LATERAL_HDG_Select[s]
        pos_FlightModes_LATERAL_HDG_Select[s, s']
        semantics_FlightModes_LATERAL_HDG_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_HDG_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_HDG_CLEARED in s.conf
        (_s.FlightModes_HDG_Switch_Pressed) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        }
    }
    pred semantics_FlightModes_LATERAL_HDG_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_HDG_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_HDG_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        }
    }
    // Transition FlightModes_LATERAL_HDG_Clear
    pred pre_FlightModes_LATERAL_HDG_Clear[s:Snapshot] {
        (some FlightModes_LATERAL_HDG_SELECTED & s.conf)
        {
            (s.FlightModes_HDG_Switch_Pressed) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_LATERAL_HDG_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_HDG_SELECTED + {
            FlightModes_LATERAL_HDG_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_HDG_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_HDG_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_HDG_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_HDG_SELECTED & s.conf) => exit_FlightModes_LATERAL_HDG_SELECTED[s']
    }

    pred FlightModes_LATERAL_HDG_Clear[s, s': Snapshot] {
        pre_FlightModes_LATERAL_HDG_Clear[s]
        pos_FlightModes_LATERAL_HDG_Clear[s, s']
        semantics_FlightModes_LATERAL_HDG_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_HDG_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_LATERAL_HDG_SELECTED & s.conf)
        {
            (_s.FlightModes_HDG_Switch_Pressed) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        }
    }
    pred semantics_FlightModes_LATERAL_HDG_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_HDG_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_HDG_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_LATERAL_HDG_NewLateralModeActivated[s]
    }
    // Transition FlightModes_LATERAL_HDG_NewLateralModeActivated
    pred pre_FlightModes_LATERAL_HDG_NewLateralModeActivated[s:Snapshot] {
        FlightModes_LATERAL_HDG_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_HDG_SELECTED + {
            FlightModes_LATERAL_HDG_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_HDG_NewLateralModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_HDG_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_HDG_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_HDG_SELECTED & s.conf) => exit_FlightModes_LATERAL_HDG_SELECTED[s']
    }

    pred FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s': Snapshot] {
        pre_FlightModes_LATERAL_HDG_NewLateralModeActivated[s]
        pos_FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s']
        semantics_FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_HDG_NewLateralModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_HDG_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_HDG_NewLateralModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_HDG_NewLateralModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_HDG_Clear + 
                FlightModes_LATERAL_HDG_NewLateralModeActivated + 
                FlightModes_LATERAL_HDG_Select
            }
        }
    }
    // Transition FlightModes_LATERAL_NAV_Select
    pred pre_FlightModes_LATERAL_NAV_Select[s:Snapshot] {
        FlightModes_LATERAL_NAV_CLEARED in s.conf
        (s.FlightModes_NAV_Switch_Pressed) = True
    }

    pred pos_FlightModes_LATERAL_NAV_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_NAV_CLEARED + {
            FlightModes_LATERAL_NAV_SELECTED_ARMED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_NAV_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_LATERAL_NAV_SELECTED[s']
    }

    pred FlightModes_LATERAL_NAV_Select[s, s': Snapshot] {
        pre_FlightModes_LATERAL_NAV_Select[s]
        pos_FlightModes_LATERAL_NAV_Select[s, s']
        semantics_FlightModes_LATERAL_NAV_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_NAV_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_NAV_CLEARED in s.conf
        (_s.FlightModes_NAV_Switch_Pressed) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        }
    }
    pred semantics_FlightModes_LATERAL_NAV_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_NAV_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_NAV_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        }
    }
    // Transition FlightModes_LATERAL_NAV_Capture
    pred pre_FlightModes_LATERAL_NAV_Capture[s:Snapshot] {
        FlightModes_LATERAL_NAV_SELECTED_ARMED in s.conf
        (s.FlightModes_NAV_Capture_Condition_Met) = True
    }

    pred pos_FlightModes_LATERAL_NAV_Capture[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_NAV_SELECTED_ARMED + {
            FlightModes_LATERAL_NAV_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_LATERAL_New_Lateral_Mode_Activated}, FlightModes_LATERAL_NAV_Capture] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_LATERAL_New_Lateral_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_LATERAL_New_Lateral_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_LATERAL_New_Lateral_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_LATERAL_New_Lateral_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_LATERAL_New_Lateral_Mode_Activated} in s'.events
        enter_FlightModes_LATERAL_NAV_SELECTED_ACTIVE[s']
    }

    pred FlightModes_LATERAL_NAV_Capture[s, s': Snapshot] {
        pre_FlightModes_LATERAL_NAV_Capture[s]
        pos_FlightModes_LATERAL_NAV_Capture[s, s']
        semantics_FlightModes_LATERAL_NAV_Capture[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_NAV_Capture[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_NAV_SELECTED_ARMED in s.conf
        (_s.FlightModes_NAV_Capture_Condition_Met) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_NAV_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_NAV_Capture
            }
        }
    }
    pred semantics_FlightModes_LATERAL_NAV_Capture[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_NAV_Capture
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_NAV_Capture
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_NAV_Capture
            }
        }
    }
    // Transition FlightModes_LATERAL_NAV_Clear
    pred pre_FlightModes_LATERAL_NAV_Clear[s:Snapshot] {
        (some FlightModes_LATERAL_NAV_SELECTED & s.conf)
        {
            (s.FlightModes_NAV_Switch_Pressed) = True or (s.FlightModes_Selected_NAV_Source_Changed) = True or (s.FlightModes_Selected_NAV_Frequency_Changed) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_LATERAL_NAV_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_NAV_SELECTED + {
            FlightModes_LATERAL_NAV_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_NAV_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_NAV_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_NAV_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_NAV_SELECTED & s.conf) => exit_FlightModes_LATERAL_NAV_SELECTED[s']
    }

    pred FlightModes_LATERAL_NAV_Clear[s, s': Snapshot] {
        pre_FlightModes_LATERAL_NAV_Clear[s]
        pos_FlightModes_LATERAL_NAV_Clear[s, s']
        semantics_FlightModes_LATERAL_NAV_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_NAV_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_LATERAL_NAV_SELECTED & s.conf)
        {
            (_s.FlightModes_NAV_Switch_Pressed) = True or (_s.FlightModes_Selected_NAV_Source_Changed) = True or (_s.FlightModes_Selected_NAV_Frequency_Changed) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        }
    }
    pred semantics_FlightModes_LATERAL_NAV_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_NAV_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_NAV_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_LATERAL_NAV_NewLateralModeActivated[s]
        !pre_FlightModes_LATERAL_NAV_Capture[s]
    }
    // Transition FlightModes_LATERAL_NAV_NewLateralModeActivated
    pred pre_FlightModes_LATERAL_NAV_NewLateralModeActivated[s:Snapshot] {
        FlightModes_LATERAL_NAV_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_NAV_SELECTED + {
            FlightModes_LATERAL_NAV_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_NAV_NewLateralModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_NAV_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_NAV_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_NAV_SELECTED & s.conf) => exit_FlightModes_LATERAL_NAV_SELECTED[s']
    }

    pred FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s': Snapshot] {
        pre_FlightModes_LATERAL_NAV_NewLateralModeActivated[s]
        pos_FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s']
        semantics_FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_NAV_NewLateralModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_NAV_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_NAV_NewLateralModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_NAV_NewLateralModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_NAV_Select + 
                FlightModes_LATERAL_NAV_NewLateralModeActivated + 
                FlightModes_LATERAL_NAV_Capture + 
                FlightModes_LATERAL_NAV_Clear
            }
        }
    }
    // Transition FlightModes_LATERAL_LAPPR_Select
    pred pre_FlightModes_LATERAL_LAPPR_Select[s:Snapshot] {
        FlightModes_LATERAL_LAPPR_CLEARED in s.conf
        (s.FlightModes_APPR_Switch_Pressed) = True
    }

    pred pos_FlightModes_LATERAL_LAPPR_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LAPPR_CLEARED + {
            FlightModes_LATERAL_LAPPR_SELECTED_ARMED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_LAPPR_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_LATERAL_LAPPR_SELECTED[s']
    }

    pred FlightModes_LATERAL_LAPPR_Select[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LAPPR_Select[s]
        pos_FlightModes_LATERAL_LAPPR_Select[s, s']
        semantics_FlightModes_LATERAL_LAPPR_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LAPPR_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_LAPPR_CLEARED in s.conf
        (_s.FlightModes_APPR_Switch_Pressed) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    pred semantics_FlightModes_LATERAL_LAPPR_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LAPPR_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LAPPR_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    // Transition FlightModes_LATERAL_LAPPR_Capture
    pred pre_FlightModes_LATERAL_LAPPR_Capture[s:Snapshot] {
        FlightModes_LATERAL_LAPPR_SELECTED_ARMED in s.conf
        (s.FlightModes_LAPPR_Capture_Condition_Met) = True
    }

    pred pos_FlightModes_LATERAL_LAPPR_Capture[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LAPPR_SELECTED_ARMED + {
            FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_LATERAL_New_Lateral_Mode_Activated}, FlightModes_LATERAL_LAPPR_Capture] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_LATERAL_New_Lateral_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_LATERAL_New_Lateral_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_LATERAL_New_Lateral_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_LATERAL_New_Lateral_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_LATERAL_New_Lateral_Mode_Activated} in s'.events
        enter_FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE[s']
    }

    pred FlightModes_LATERAL_LAPPR_Capture[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LAPPR_Capture[s]
        pos_FlightModes_LATERAL_LAPPR_Capture[s, s']
        semantics_FlightModes_LATERAL_LAPPR_Capture[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LAPPR_Capture[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_LAPPR_SELECTED_ARMED in s.conf
        (_s.FlightModes_LAPPR_Capture_Condition_Met) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LAPPR_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    pred semantics_FlightModes_LATERAL_LAPPR_Capture[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LAPPR_Capture
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LAPPR_Capture
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    // Transition FlightModes_LATERAL_LAPPR_Clear
    pred pre_FlightModes_LATERAL_LAPPR_Clear[s:Snapshot] {
        (some FlightModes_LATERAL_LAPPR_SELECTED & s.conf)
        {
            (s.FlightModes_APPR_Switch_Pressed) = True or (s.FlightModes_Selected_NAV_Source_Changed) = True or (s.FlightModes_Selected_NAV_Frequency_Changed) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_LATERAL_LAPPR_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LAPPR_SELECTED + {
            FlightModes_LATERAL_LAPPR_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_LAPPR_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_LAPPR_SELECTED & s.conf) => exit_FlightModes_LATERAL_LAPPR_SELECTED[s']
    }

    pred FlightModes_LATERAL_LAPPR_Clear[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LAPPR_Clear[s]
        pos_FlightModes_LATERAL_LAPPR_Clear[s, s']
        semantics_FlightModes_LATERAL_LAPPR_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LAPPR_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_LATERAL_LAPPR_SELECTED & s.conf)
        {
            (_s.FlightModes_APPR_Switch_Pressed) = True or (_s.FlightModes_Selected_NAV_Source_Changed) = True or (_s.FlightModes_Selected_NAV_Frequency_Changed) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    pred semantics_FlightModes_LATERAL_LAPPR_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LAPPR_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LAPPR_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s]
        !pre_FlightModes_LATERAL_LAPPR_Capture[s]
    }
    // Transition FlightModes_LATERAL_LAPPR_NewLateralModeActivated
    pred pre_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s:Snapshot] {
        FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LAPPR_SELECTED + {
            FlightModes_LATERAL_LAPPR_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_LAPPR_NewLateralModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_LAPPR_SELECTED & s.conf) => exit_FlightModes_LATERAL_LAPPR_SELECTED[s']
    }

    pred FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s]
        pos_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s']
        semantics_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LAPPR_NewLateralModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LAPPR_NewLateralModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LAPPR_NewLateralModeActivated + 
                FlightModes_LATERAL_LAPPR_Select + 
                FlightModes_LATERAL_LAPPR_Clear + 
                FlightModes_LATERAL_LAPPR_Capture
            }
        }
    }
    // Transition FlightModes_LATERAL_LGA_Select
    pred pre_FlightModes_LATERAL_LGA_Select[s:Snapshot] {
        FlightModes_LATERAL_LGA_CLEARED in s.conf
        (s.FlightModes_GA_Switch_Pressed) = True and (s.FlightModes_Overspeed) = False
    }

    pred pos_FlightModes_LATERAL_LGA_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LGA_CLEARED + {
            FlightModes_LATERAL_LGA_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_LATERAL_New_Lateral_Mode_Activated}, FlightModes_LATERAL_LGA_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_LATERAL_New_Lateral_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_LATERAL_New_Lateral_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_LATERAL_New_Lateral_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_LATERAL_New_Lateral_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_LATERAL_New_Lateral_Mode_Activated} in s'.events
        enter_FlightModes_LATERAL_LGA_SELECTED[s']
        enter_FlightModes_LATERAL_LGA_SELECTED_ACTIVE[s']
    }

    pred FlightModes_LATERAL_LGA_Select[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LGA_Select[s]
        pos_FlightModes_LATERAL_LGA_Select[s, s']
        semantics_FlightModes_LATERAL_LGA_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LGA_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_LGA_CLEARED in s.conf
        (_s.FlightModes_GA_Switch_Pressed) = True and (_s.FlightModes_Overspeed) = False
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        }
    }
    pred semantics_FlightModes_LATERAL_LGA_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LGA_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LGA_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        }
    }
    // Transition FlightModes_LATERAL_LGA_Clear
    pred pre_FlightModes_LATERAL_LGA_Clear[s:Snapshot] {
        (some FlightModes_LATERAL_LGA_SELECTED & s.conf)
        {
            (s.FlightModes_When_AP_Engaged) = True or (s.FlightModes_SYNC_Switch_Pressed) = True or (s.FlightModes_VGA_Active) = False or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_LATERAL_LGA_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LGA_SELECTED + {
            FlightModes_LATERAL_LGA_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_LGA_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_LGA_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_LGA_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_LGA_SELECTED & s.conf) => exit_FlightModes_LATERAL_LGA_SELECTED[s']
    }

    pred FlightModes_LATERAL_LGA_Clear[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LGA_Clear[s]
        pos_FlightModes_LATERAL_LGA_Clear[s, s']
        semantics_FlightModes_LATERAL_LGA_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LGA_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_LATERAL_LGA_SELECTED & s.conf)
        {
            (_s.FlightModes_When_AP_Engaged) = True or (_s.FlightModes_SYNC_Switch_Pressed) = True or (s.FlightModes_VGA_Active) = False or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        }
    }
    pred semantics_FlightModes_LATERAL_LGA_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LGA_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LGA_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_LATERAL_LGA_NewLateralModeActivated[s]
    }
    // Transition FlightModes_LATERAL_LGA_NewLateralModeActivated
    pred pre_FlightModes_LATERAL_LGA_NewLateralModeActivated[s:Snapshot] {
        FlightModes_LATERAL_LGA_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_LATERAL_New_Lateral_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_LGA_SELECTED + {
            FlightModes_LATERAL_LGA_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_LGA_NewLateralModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_LGA_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_LGA_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_LGA_SELECTED & s.conf) => exit_FlightModes_LATERAL_LGA_SELECTED[s']
    }

    pred FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s': Snapshot] {
        pre_FlightModes_LATERAL_LGA_NewLateralModeActivated[s]
        pos_FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s']
        semantics_FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_LGA_NewLateralModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_LGA_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
            FlightModes_LATERAL_New_Lateral_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_LGA_NewLateralModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_LGA_NewLateralModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_LGA_NewLateralModeActivated + 
                FlightModes_LATERAL_LGA_Select + 
                FlightModes_LATERAL_LGA_Clear
            }
        }
    }
    // Transition FlightModes_LATERAL_ROLL_Select
    pred pre_FlightModes_LATERAL_ROLL_Select[s:Snapshot] {
        FlightModes_LATERAL_ROLL_CLEARED in s.conf
        !((s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True)
    }

    pred pos_FlightModes_LATERAL_ROLL_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_ROLL_CLEARED + {
            FlightModes_LATERAL_ROLL_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_ROLL_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_LATERAL_ROLL_SELECTED[s']
        enter_FlightModes_LATERAL_ROLL_SELECTED_ACTIVE[s']
    }

    pred FlightModes_LATERAL_ROLL_Select[s, s': Snapshot] {
        pre_FlightModes_LATERAL_ROLL_Select[s]
        pos_FlightModes_LATERAL_ROLL_Select[s, s']
        semantics_FlightModes_LATERAL_ROLL_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_ROLL_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_ROLL_CLEARED in s.conf
        !((s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True)
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        }
    }
    pred semantics_FlightModes_LATERAL_ROLL_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_ROLL_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_ROLL_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        }
    }
    // Transition FlightModes_LATERAL_ROLL_Clear
    pred pre_FlightModes_LATERAL_ROLL_Clear[s:Snapshot] {
        FlightModes_LATERAL_ROLL_SELECTED_ACTIVE in s.conf
        (s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True
    }

    pred pos_FlightModes_LATERAL_ROLL_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_LATERAL_ROLL_SELECTED + {
            FlightModes_LATERAL_ROLL_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_LATERAL_ROLL_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_LATERAL_ROLL_SELECTED_ACTIVE in s.conf => exit_FlightModes_LATERAL_ROLL_SELECTED_ACTIVE[s']
        (some FlightModes_LATERAL_ROLL_SELECTED & s.conf) => exit_FlightModes_LATERAL_ROLL_SELECTED[s']
    }

    pred FlightModes_LATERAL_ROLL_Clear[s, s': Snapshot] {
        pre_FlightModes_LATERAL_ROLL_Clear[s]
        pos_FlightModes_LATERAL_ROLL_Clear[s, s']
        semantics_FlightModes_LATERAL_ROLL_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_LATERAL_ROLL_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_LATERAL_ROLL_SELECTED_ACTIVE in s.conf
        (s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True
        _s.stable = True => {
            no t & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        }
    }
    pred semantics_FlightModes_LATERAL_ROLL_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_LATERAL_ROLL_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_LATERAL_ROLL_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_LATERAL_ROLL_Clear + 
                FlightModes_LATERAL_ROLL_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_VS_Select
    pred pre_FlightModes_VERTICAL_VS_Select[s:Snapshot] {
        FlightModes_VERTICAL_VS_CLEARED in s.conf
        {
            (s.FlightModes_VS_Switch_Pressed) = True and (s.FlightModes_Overspeed) = False and (s.FlightModes_VAPPR_Active) = False
        }
    }

    pred pos_FlightModes_VERTICAL_VS_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VS_CLEARED + {
            FlightModes_VERTICAL_VS_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_VS_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_VS_SELECTED[s']
        enter_FlightModes_VERTICAL_VS_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_VS_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VS_Select[s]
        pos_FlightModes_VERTICAL_VS_Select[s, s']
        semantics_FlightModes_VERTICAL_VS_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VS_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VS_CLEARED in s.conf
        {
            (_s.FlightModes_VS_Switch_Pressed) = True and (_s.FlightModes_Overspeed) = False and (s.FlightModes_VAPPR_Active) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VS_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VS_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VS_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_VS_Clear
    pred pre_FlightModes_VERTICAL_VS_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_VS_SELECTED & s.conf)
        {
            (s.FlightModes_VS_Switch_Pressed) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_VS_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VS_SELECTED + {
            FlightModes_VERTICAL_VS_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VS_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VS_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VS_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VS_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VS_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VS_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VS_Clear[s]
        pos_FlightModes_VERTICAL_VS_Clear[s, s']
        semantics_FlightModes_VERTICAL_VS_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VS_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_VS_SELECTED & s.conf)
        {
            (_s.FlightModes_VS_Switch_Pressed) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VS_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VS_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VS_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s]
    }
    // Transition FlightModes_VERTICAL_VS_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s:Snapshot] {
        FlightModes_VERTICAL_VS_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VS_SELECTED + {
            FlightModes_VERTICAL_VS_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VS_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VS_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VS_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VS_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VS_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VS_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VS_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VS_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VS_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VS_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VS_Clear + 
                FlightModes_VERTICAL_VS_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_FLC_Select
    pred pre_FlightModes_VERTICAL_FLC_Select[s:Snapshot] {
        FlightModes_VERTICAL_FLC_CLEARED in s.conf
        {
            ((s.FlightModes_FLC_Switch_Pressed) = True and (s.FlightModes_VAPPR_Active) = False) or ((s.FlightModes_Overspeed) = True and (s.FlightModes_ALT_Active) = False and (s.FlightModes_ALTSEL_Active) = False)
        }
    }

    pred pos_FlightModes_VERTICAL_FLC_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_FLC_CLEARED + {
            FlightModes_VERTICAL_FLC_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_FLC_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_FLC_SELECTED[s']
        enter_FlightModes_VERTICAL_FLC_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_FLC_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_FLC_Select[s]
        pos_FlightModes_VERTICAL_FLC_Select[s, s']
        semantics_FlightModes_VERTICAL_FLC_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_FLC_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_FLC_CLEARED in s.conf
        {
            ((_s.FlightModes_FLC_Switch_Pressed) = True and (s.FlightModes_VAPPR_Active) = False) or ((_s.FlightModes_Overspeed) = True and (s.FlightModes_ALT_Active) = False and (s.FlightModes_ALTSEL_Active) = False)
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_FLC_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_FLC_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_FLC_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        }
    }
    // Transition FlightModes_VERTICAL_FLC_Clear
    pred pre_FlightModes_VERTICAL_FLC_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_FLC_SELECTED & s.conf)
        {
            ((s.FlightModes_FLC_Switch_Pressed) = True and (s.FlightModes_Overspeed) = False) or ((s.FlightModes_Overspeed) = False and (s.FlightModes_VS_Pitch_Wheel_Rotated) = True) or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_FLC_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_FLC_SELECTED + {
            FlightModes_VERTICAL_FLC_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_FLC_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_FLC_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_FLC_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_FLC_SELECTED & s.conf) => exit_FlightModes_VERTICAL_FLC_SELECTED[s']
    }

    pred FlightModes_VERTICAL_FLC_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_FLC_Clear[s]
        pos_FlightModes_VERTICAL_FLC_Clear[s, s']
        semantics_FlightModes_VERTICAL_FLC_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_FLC_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_FLC_SELECTED & s.conf)
        {
            ((_s.FlightModes_FLC_Switch_Pressed) = True and (_s.FlightModes_Overspeed) = False) or ((_s.FlightModes_Overspeed) = False and (_s.FlightModes_VS_Pitch_Wheel_Rotated) = True) or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_FLC_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_FLC_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_FLC_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s]
    }
    // Transition FlightModes_VERTICAL_FLC_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s:Snapshot] {
        FlightModes_VERTICAL_FLC_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_FLC_SELECTED + {
            FlightModes_VERTICAL_FLC_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_FLC_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_FLC_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_FLC_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_FLC_SELECTED & s.conf) => exit_FlightModes_VERTICAL_FLC_SELECTED[s']
    }

    pred FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_FLC_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_FLC_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_FLC_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_FLC_Select + 
                FlightModes_VERTICAL_FLC_NewVerticalModeActivated + 
                FlightModes_VERTICAL_FLC_Clear
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALT_Select
    pred pre_FlightModes_VERTICAL_ALT_Select[s:Snapshot] {
        FlightModes_VERTICAL_ALT_CLEARED in s.conf
        {
            ((s.FlightModes_ALT_Switch_Pressed) = True and (s.FlightModes_VAPPR_Active) = False) or ((s.FlightModes_VAPPR_Active) = False and (s.FlightModes_ALTSEL_Target_Changed) = True and (s.FlightModes_ALTSEL_Track) = True)
        }
    }

    pred pos_FlightModes_VERTICAL_ALT_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALT_CLEARED + {
            FlightModes_VERTICAL_ALT_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_ALT_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_ALT_SELECTED[s']
        enter_FlightModes_VERTICAL_ALT_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_ALT_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALT_Select[s]
        pos_FlightModes_VERTICAL_ALT_Select[s, s']
        semantics_FlightModes_VERTICAL_ALT_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALT_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_ALT_CLEARED in s.conf
        {
            ((_s.FlightModes_ALT_Switch_Pressed) = True and (s.FlightModes_VAPPR_Active) = False) or ((s.FlightModes_VAPPR_Active) = False and (_s.FlightModes_ALTSEL_Target_Changed) = True and (s.FlightModes_ALTSEL_Track) = True)
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALT_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALT_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALT_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALT_Clear
    pred pre_FlightModes_VERTICAL_ALT_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_ALT_SELECTED & s.conf)
        {
            (s.FlightModes_ALT_Switch_Pressed) = True or (s.FlightModes_VS_Pitch_Wheel_Rotated) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_ALT_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALT_SELECTED + {
            FlightModes_VERTICAL_ALT_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALT_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_ALT_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_ALT_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_ALT_SELECTED & s.conf) => exit_FlightModes_VERTICAL_ALT_SELECTED[s']
    }

    pred FlightModes_VERTICAL_ALT_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALT_Clear[s]
        pos_FlightModes_VERTICAL_ALT_Clear[s, s']
        semantics_FlightModes_VERTICAL_ALT_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALT_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_ALT_SELECTED & s.conf)
        {
            (_s.FlightModes_ALT_Switch_Pressed) = True or (_s.FlightModes_VS_Pitch_Wheel_Rotated) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALT_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALT_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALT_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_Select + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s]
    }
    // Transition FlightModes_VERTICAL_ALT_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s:Snapshot] {
        FlightModes_VERTICAL_ALT_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALT_SELECTED + {
            FlightModes_VERTICAL_ALT_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALT_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_ALT_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_ALT_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_ALT_SELECTED & s.conf) => exit_FlightModes_VERTICAL_ALT_SELECTED[s']
    }

    pred FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_ALT_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALT_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALT_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALT_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALT_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALT_Clear + 
                FlightModes_VERTICAL_ALT_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALT_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALTSEL_Select
    pred pre_FlightModes_VERTICAL_ALTSEL_Select[s:Snapshot] {
        FlightModes_VERTICAL_ALTSEL_CLEARED in s.conf
        {
            (s.FlightModes_VAPPR_Active) = False and (s.FlightModes_VGA_Active) = False and (s.FlightModes_ALT_Active) = False
        }
    }

    pred pos_FlightModes_VERTICAL_ALTSEL_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALTSEL_CLEARED + {
            FlightModes_VERTICAL_ALTSEL_SELECTED_ARMED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALTSEL_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_VERTICAL_ALTSEL_SELECTED[s']
    }

    pred FlightModes_VERTICAL_ALTSEL_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALTSEL_Select[s]
        pos_FlightModes_VERTICAL_ALTSEL_Select[s, s']
        semantics_FlightModes_VERTICAL_ALTSEL_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_ALTSEL_CLEARED in s.conf
        {
            (s.FlightModes_VAPPR_Active) = False and (s.FlightModes_VGA_Active) = False and (s.FlightModes_ALT_Active) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALTSEL_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALTSEL_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALTSEL_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALTSEL_Capture
    pred pre_FlightModes_VERTICAL_ALTSEL_Capture[s:Snapshot] {
        FlightModes_VERTICAL_ALTSEL_SELECTED_ARMED in s.conf
        (s.FlightModes_ALTSEL_Capture_Condition_Met) = True
    }

    pred pos_FlightModes_VERTICAL_ALTSEL_Capture[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALTSEL_SELECTED_ARMED + {
            FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_CAPTURE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_ALTSEL_Capture] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_ALTSEL_Capture[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALTSEL_Capture[s]
        pos_FlightModes_VERTICAL_ALTSEL_Capture[s, s']
        semantics_FlightModes_VERTICAL_ALTSEL_Capture[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Capture[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_ALTSEL_SELECTED_ARMED in s.conf
        (_s.FlightModes_ALTSEL_Capture_Condition_Met) = True
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_Capture
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALTSEL_Capture[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALTSEL_Capture
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALTSEL_Capture
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_Capture
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALTSEL_Track
    pred pre_FlightModes_VERTICAL_ALTSEL_Track[s:Snapshot] {
        FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_CAPTURE in s.conf
        (s.FlightModes_ALTSEL_Track_Condition_Met) = True
    }

    pred pos_FlightModes_VERTICAL_ALTSEL_Track[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_CAPTURE + {
            FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALTSEL_Track] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK[s']
    }

    pred FlightModes_VERTICAL_ALTSEL_Track[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALTSEL_Track[s]
        pos_FlightModes_VERTICAL_ALTSEL_Track[s, s']
        semantics_FlightModes_VERTICAL_ALTSEL_Track[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Track[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_CAPTURE in s.conf
        (_s.FlightModes_ALTSEL_Track_Condition_Met) = True
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALTSEL_Track
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALTSEL_Track
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALTSEL_Track[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALTSEL_Track
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALTSEL_Track
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALTSEL_Track
            }
        }
    }
    // Transition FlightModes_VERTICAL_ALTSEL_Clear
    pred pre_FlightModes_VERTICAL_ALTSEL_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_ALTSEL_SELECTED & s.conf)
        {
            (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_ALTSEL_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALTSEL_SELECTED + {
            FlightModes_VERTICAL_ALTSEL_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALTSEL_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        (some FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE & s.conf) => exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE[s']
        FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK in s.conf => exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK[s']
        (some FlightModes_VERTICAL_ALTSEL_SELECTED & s.conf) => exit_FlightModes_VERTICAL_ALTSEL_SELECTED[s']
    }

    pred FlightModes_VERTICAL_ALTSEL_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALTSEL_Clear[s]
        pos_FlightModes_VERTICAL_ALTSEL_Clear[s, s']
        semantics_FlightModes_VERTICAL_ALTSEL_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_ALTSEL_SELECTED & s.conf)
        {
            (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_ALTSEL_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALTSEL_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALTSEL_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_ALTSEL_Track[s]
        !pre_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s]
        !pre_FlightModes_VERTICAL_ALTSEL_Capture[s]
    }
    // Transition FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s:Snapshot] {
        (some FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE & s.conf)
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_ALTSEL_SELECTED + {
            FlightModes_VERTICAL_ALTSEL_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        (some FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE & s.conf) => exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE[s']
        FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK in s.conf => exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK[s']
        (some FlightModes_VERTICAL_ALTSEL_SELECTED & s.conf) => exit_FlightModes_VERTICAL_ALTSEL_SELECTED[s']
    }

    pred FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE & s.conf)
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_ALTSEL_Track + 
                FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated + 
                FlightModes_VERTICAL_ALTSEL_Capture + 
                FlightModes_VERTICAL_ALTSEL_Select + 
                FlightModes_VERTICAL_ALTSEL_Clear
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_ALTSEL_Track[s]
    }
    // Transition FlightModes_VERTICAL_VAPPR_Select
    pred pre_FlightModes_VERTICAL_VAPPR_Select[s:Snapshot] {
        FlightModes_VERTICAL_VAPPR_CLEARED in s.conf
        (s.FlightModes_APPR_Switch_Pressed) = True
    }

    pred pos_FlightModes_VERTICAL_VAPPR_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VAPPR_CLEARED + {
            FlightModes_VERTICAL_VAPPR_SELECTED_ARMED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VAPPR_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_VERTICAL_VAPPR_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VAPPR_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VAPPR_Select[s]
        pos_FlightModes_VERTICAL_VAPPR_Select[s, s']
        semantics_FlightModes_VERTICAL_VAPPR_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VAPPR_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VAPPR_CLEARED in s.conf
        (_s.FlightModes_APPR_Switch_Pressed) = True
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VAPPR_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VAPPR_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VAPPR_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_VAPPR_Capture
    pred pre_FlightModes_VERTICAL_VAPPR_Capture[s:Snapshot] {
        FlightModes_VERTICAL_VAPPR_SELECTED_ARMED in s.conf
        {
            (s.FlightModes_VAPPR_Capture_Condition_Met) = True and (s.FlightModes_LAPPR_Active) = True and (s.FlightModes_Overspeed) = False
        }
    }

    pred pos_FlightModes_VERTICAL_VAPPR_Capture[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VAPPR_SELECTED_ARMED + {
            FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_VAPPR_Capture] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_VAPPR_Capture[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VAPPR_Capture[s]
        pos_FlightModes_VERTICAL_VAPPR_Capture[s, s']
        semantics_FlightModes_VERTICAL_VAPPR_Capture[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VAPPR_Capture[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VAPPR_SELECTED_ARMED in s.conf
        {
            (_s.FlightModes_VAPPR_Capture_Condition_Met) = True and (s.FlightModes_LAPPR_Active) = True and (_s.FlightModes_Overspeed) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VAPPR_Capture
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VAPPR_Capture
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VAPPR_Capture[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VAPPR_Capture
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VAPPR_Capture
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VAPPR_Capture
            }
        }
    }
    // Transition FlightModes_VERTICAL_VAPPR_Clear
    pred pre_FlightModes_VERTICAL_VAPPR_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_VAPPR_SELECTED & s.conf)
        {
            (s.FlightModes_APPR_Switch_Pressed) = True or (s.FlightModes_LAPPR_Selected) = False or (s.FlightModes_Selected_NAV_Source_Changed) = True or (s.FlightModes_Selected_NAV_Frequency_Changed) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_VAPPR_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VAPPR_SELECTED + {
            FlightModes_VERTICAL_VAPPR_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VAPPR_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VAPPR_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VAPPR_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VAPPR_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VAPPR_Clear[s]
        pos_FlightModes_VERTICAL_VAPPR_Clear[s, s']
        semantics_FlightModes_VERTICAL_VAPPR_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VAPPR_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_VAPPR_SELECTED & s.conf)
        {
            (_s.FlightModes_APPR_Switch_Pressed) = True or (s.FlightModes_LAPPR_Selected) = False or (_s.FlightModes_Selected_NAV_Source_Changed) = True or (_s.FlightModes_Selected_NAV_Frequency_Changed) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VAPPR_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VAPPR_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VAPPR_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s]
        !pre_FlightModes_VERTICAL_VAPPR_Capture[s]
    }
    // Transition FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s:Snapshot] {
        FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VAPPR_SELECTED + {
            FlightModes_VERTICAL_VAPPR_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VAPPR_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VAPPR_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated + 
                FlightModes_VERTICAL_VAPPR_Capture + 
                FlightModes_VERTICAL_VAPPR_Clear + 
                FlightModes_VERTICAL_VAPPR_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_VGA_Select
    pred pre_FlightModes_VERTICAL_VGA_Select[s:Snapshot] {
        FlightModes_VERTICAL_VGA_CLEARED in s.conf
        (s.FlightModes_GA_Switch_Pressed) = True and (s.FlightModes_Overspeed) = False
    }

    pred pos_FlightModes_VERTICAL_VGA_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VGA_CLEARED + {
            FlightModes_VERTICAL_VGA_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {FlightModes_VERTICAL_New_Vertical_Mode_Activated}, FlightModes_VERTICAL_VGA_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) - {FlightModes_VERTICAL_New_Vertical_Mode_Activated})
            } else {
                no ((s'.events & InternalEvent) - {{FlightModes_VERTICAL_New_Vertical_Mode_Activated} + (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {FlightModes_VERTICAL_New_Vertical_Mode_Activated}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        {FlightModes_VERTICAL_New_Vertical_Mode_Activated} in s'.events
        enter_FlightModes_VERTICAL_VGA_SELECTED[s']
        enter_FlightModes_VERTICAL_VGA_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_VGA_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VGA_Select[s]
        pos_FlightModes_VERTICAL_VGA_Select[s, s']
        semantics_FlightModes_VERTICAL_VGA_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VGA_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VGA_CLEARED in s.conf
        (_s.FlightModes_GA_Switch_Pressed) = True and (_s.FlightModes_Overspeed) = False
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VGA_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VGA_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VGA_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        }
    }
    // Transition FlightModes_VERTICAL_VGA_Clear
    pred pre_FlightModes_VERTICAL_VGA_Clear[s:Snapshot] {
        (some FlightModes_VERTICAL_VGA_SELECTED & s.conf)
        {
            (s.FlightModes_When_AP_Engaged) = True or (s.FlightModes_SYNC_Switch_Pressed) = True or (s.FlightModes_VS_Pitch_Wheel_Rotated) = True or (s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
    }

    pred pos_FlightModes_VERTICAL_VGA_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VGA_SELECTED + {
            FlightModes_VERTICAL_VGA_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VGA_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VGA_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VGA_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VGA_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VGA_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VGA_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VGA_Clear[s]
        pos_FlightModes_VERTICAL_VGA_Clear[s, s']
        semantics_FlightModes_VERTICAL_VGA_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VGA_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        (some FlightModes_VERTICAL_VGA_SELECTED & s.conf)
        {
            (_s.FlightModes_When_AP_Engaged) = True or (_s.FlightModes_SYNC_Switch_Pressed) = True or (_s.FlightModes_VS_Pitch_Wheel_Rotated) = True or (_s.FlightModes_Pilot_Flying_Transfer) = True or (s.FlightModes_Modes_On) = False
        }
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_VGA_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VGA_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VGA_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        }
        // Priority "SOURCE-PARENT" semantics
        !pre_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s]
    }
    // Transition FlightModes_VERTICAL_VGA_NewVerticalModeActivated
    pred pre_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s:Snapshot] {
        FlightModes_VERTICAL_VGA_SELECTED_ACTIVE in s.conf
        s.stable = True => {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in (s.events & EnvironmentEvent)
        } else {
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in s.events
        }
    }

    pred pos_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_VGA_SELECTED + {
            FlightModes_VERTICAL_VGA_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_VGA_NewVerticalModeActivated] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_VGA_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_VGA_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_VGA_SELECTED & s.conf) => exit_FlightModes_VERTICAL_VGA_SELECTED[s']
    }

    pred FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s]
        pos_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s']
        semantics_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_VGA_SELECTED_ACTIVE in s.conf
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
            FlightModes_VERTICAL_New_Vertical_Mode_Activated in {_s.events  + genEvents}
        }
    }
    pred semantics_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_VGA_NewVerticalModeActivated
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_VGA_Select + 
                FlightModes_VERTICAL_VGA_Clear + 
                FlightModes_VERTICAL_VGA_NewVerticalModeActivated
            }
        }
    }
    // Transition FlightModes_VERTICAL_PITCH_Select
    pred pre_FlightModes_VERTICAL_PITCH_Select[s:Snapshot] {
        FlightModes_VERTICAL_PITCH_CLEARED in s.conf
        !((s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True)
    }

    pred pos_FlightModes_VERTICAL_PITCH_Select[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_PITCH_CLEARED + {
            FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_PITCH_Select] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        enter_FlightModes_VERTICAL_PITCH_SELECTED[s']
        enter_FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE[s']
    }

    pred FlightModes_VERTICAL_PITCH_Select[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_PITCH_Select[s]
        pos_FlightModes_VERTICAL_PITCH_Select[s, s']
        semantics_FlightModes_VERTICAL_PITCH_Select[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_PITCH_Select[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_PITCH_CLEARED in s.conf
        !((s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True)
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_PITCH_Select[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_PITCH_Select
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_PITCH_Select
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        }
    }
    // Transition FlightModes_VERTICAL_PITCH_Clear
    pred pre_FlightModes_VERTICAL_PITCH_Clear[s:Snapshot] {
        FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE in s.conf
        (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True
    }

    pred pos_FlightModes_VERTICAL_PITCH_Clear[s, s':Snapshot] {
        s'.conf = s.conf - FlightModes_VERTICAL_PITCH_SELECTED + {
            FlightModes_VERTICAL_PITCH_CLEARED
        }
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    
        testIfNextStable[s, s', {none}, FlightModes_VERTICAL_PITCH_Clear] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
            s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
            s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
            s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
            s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
            s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
            s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
            s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
            s'.FlightModes_Overspeed = s.FlightModes_Overspeed
            s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
            s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
            s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
            s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
            s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
            s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
            s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
            s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
            s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
            s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
            s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
            s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
            s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
            s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
            s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        }
        FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE in s.conf => exit_FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE[s']
        (some FlightModes_VERTICAL_PITCH_SELECTED & s.conf) => exit_FlightModes_VERTICAL_PITCH_SELECTED[s']
    }

    pred FlightModes_VERTICAL_PITCH_Clear[s, s': Snapshot] {
        pre_FlightModes_VERTICAL_PITCH_Clear[s]
        pos_FlightModes_VERTICAL_PITCH_Clear[s, s']
        semantics_FlightModes_VERTICAL_PITCH_Clear[s, s']
    }

    pred enabledAfterStep_FlightModes_VERTICAL_PITCH_Clear[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE in s.conf
        (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True
        _s.stable = True => {
            no t & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        } else {
            no {_s.taken + t} & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        }
    }
    pred semantics_FlightModes_VERTICAL_PITCH_Clear[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = FlightModes_VERTICAL_PITCH_Clear
        } else {
            // SINGLE semantics
            s'.taken = s.taken + FlightModes_VERTICAL_PITCH_Clear
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                FlightModes_VERTICAL_PITCH_Clear + 
                FlightModes_VERTICAL_PITCH_Select
            }
        }
    }

/****************************** ENTER PREDICATES *******************************/

    pred enter_FlightModes_FD_ON[s: Snapshot] {
        (s.FlightModes_FD_On) = True
    }
    pred enter_FlightModes_ANNUNCIATIONS_OFF[s: Snapshot] {
        (s.FlightModes_Modes_On) = False
    }
    pred enter_FlightModes_ANNUNCIATIONS_ON[s: Snapshot] {
        (s.FlightModes_Modes_On) = True
    }
    pred enter_FlightModes_LATERAL_HDG_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_HDG_Active) = True
    }
    pred enter_FlightModes_LATERAL_HDG_SELECTED[s: Snapshot] {
        (s.FlightModes_HDG_Selected) = True
    }
    pred enter_FlightModes_LATERAL_NAV_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_NAV_Active) = True
    }
    pred enter_FlightModes_LATERAL_NAV_SELECTED[s: Snapshot] {
        (s.FlightModes_NAV_Selected) = True
    }
    pred enter_FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_LAPPR_Active) = True
    }
    pred enter_FlightModes_LATERAL_LAPPR_SELECTED[s: Snapshot] {
        (s.FlightModes_LAPPR_Selected) = True
    }
    pred enter_FlightModes_LATERAL_LGA_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_LGA_Active) = True
    }
    pred enter_FlightModes_LATERAL_LGA_SELECTED[s: Snapshot] {
        (s.FlightModes_LGA_Selected) = True
    }
    pred enter_FlightModes_LATERAL_ROLL_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ROLL_Active) = True
    }
    pred enter_FlightModes_LATERAL_ROLL_SELECTED[s: Snapshot] {
        (s.FlightModes_ROLL_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_VS_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VS_Active) = True
    }
    pred enter_FlightModes_VERTICAL_VS_SELECTED[s: Snapshot] {
        (s.FlightModes_VS_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_FLC_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_FLC_Active) = True
    }
    pred enter_FlightModes_VERTICAL_FLC_SELECTED[s: Snapshot] {
        (s.FlightModes_FLC_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_ALT_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ALT_Active) = True
    }
    pred enter_FlightModes_VERTICAL_ALT_SELECTED[s: Snapshot] {
        (s.FlightModes_ALT_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK[s: Snapshot] {
        (s.FlightModes_ALTSEL_Track) = True
    }
    pred enter_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ALTSEL_Active) = True
    }
    pred enter_FlightModes_VERTICAL_ALTSEL_SELECTED[s: Snapshot] {
        (s.FlightModes_ALTSEL_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VAPPR_Active) = True
    }
    pred enter_FlightModes_VERTICAL_VAPPR_SELECTED[s: Snapshot] {
        (s.FlightModes_VAPPR_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_VGA_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VGA_Active) = True
    }
    pred enter_FlightModes_VERTICAL_VGA_SELECTED[s: Snapshot] {
        (s.FlightModes_VGA_Selected) = True
    }
    pred enter_FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_PITCH_Active) = True
    }
    pred enter_FlightModes_VERTICAL_PITCH_SELECTED[s: Snapshot] {
        (s.FlightModes_PITCH_Selected) = True
    }

/****************************** EXIT PREDICATES *******************************/

    pred exit_FlightModes_FD_ON[s: Snapshot] {
        (s.FlightModes_FD_On) = False
    }
    pred exit_FlightModes_ANNUNCIATIONS_OFF[s: Snapshot] {
        (s.FlightModes_Modes_On) = True
    }
    pred exit_FlightModes_ANNUNCIATIONS_ON[s: Snapshot] {
        (s.FlightModes_Modes_On) = False
    }
    pred exit_FlightModes_LATERAL_HDG_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_HDG_Active) = False
    }
    pred exit_FlightModes_LATERAL_HDG_SELECTED[s: Snapshot] {
        (s.FlightModes_HDG_Selected) = False
    }
    pred exit_FlightModes_LATERAL_NAV_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_NAV_Active) = False
    }
    pred exit_FlightModes_LATERAL_NAV_SELECTED[s: Snapshot] {
        (s.FlightModes_NAV_Selected) = False
    }
    pred exit_FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_LAPPR_Active) = False
    }
    pred exit_FlightModes_LATERAL_LAPPR_SELECTED[s: Snapshot] {
        (s.FlightModes_LAPPR_Selected) = False
    }
    pred exit_FlightModes_LATERAL_LGA_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_LGA_Active) = False
    }
    pred exit_FlightModes_LATERAL_LGA_SELECTED[s: Snapshot] {
        (s.FlightModes_LGA_Selected) = False
    }
    pred exit_FlightModes_LATERAL_ROLL_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ROLL_Active) = False
    }
    pred exit_FlightModes_LATERAL_ROLL_SELECTED[s: Snapshot] {
        (s.FlightModes_ROLL_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_VS_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VS_Active) = False
    }
    pred exit_FlightModes_VERTICAL_VS_SELECTED[s: Snapshot] {
        (s.FlightModes_VS_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_FLC_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_FLC_Active) = False
    }
    pred exit_FlightModes_VERTICAL_FLC_SELECTED[s: Snapshot] {
        (s.FlightModes_FLC_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_ALT_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ALT_Active) = False
    }
    pred exit_FlightModes_VERTICAL_ALT_SELECTED[s: Snapshot] {
        (s.FlightModes_ALT_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE_TRACK[s: Snapshot] {
        (s.FlightModes_ALTSEL_Track) = False
    }
    pred exit_FlightModes_VERTICAL_ALTSEL_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_ALTSEL_Active) = False
    }
    pred exit_FlightModes_VERTICAL_ALTSEL_SELECTED[s: Snapshot] {
        (s.FlightModes_ALTSEL_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_VAPPR_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VAPPR_Active) = False
    }
    pred exit_FlightModes_VERTICAL_VAPPR_SELECTED[s: Snapshot] {
        (s.FlightModes_VAPPR_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_VGA_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_VGA_Active) = False
    }
    pred exit_FlightModes_VERTICAL_VGA_SELECTED[s: Snapshot] {
        (s.FlightModes_VGA_Selected) = False
    }
    pred exit_FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE[s: Snapshot] {
        (s.FlightModes_PITCH_Active) = False
    }
    pred exit_FlightModes_VERTICAL_PITCH_SELECTED[s: Snapshot] {
        (s.FlightModes_PITCH_Selected) = False
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            FlightModes_FD_OFF + 
            FlightModes_ANNUNCIATIONS_OFF + 
            FlightModes_LATERAL_HDG_CLEARED + 
            FlightModes_LATERAL_NAV_CLEARED + 
            FlightModes_LATERAL_LAPPR_CLEARED + 
            FlightModes_LATERAL_LGA_CLEARED + 
            FlightModes_LATERAL_ROLL_SELECTED_ACTIVE + 
            FlightModes_VERTICAL_VS_CLEARED + 
            FlightModes_VERTICAL_FLC_CLEARED + 
            FlightModes_VERTICAL_ALT_CLEARED + 
            FlightModes_VERTICAL_ALTSEL_CLEARED + 
            FlightModes_VERTICAL_VAPPR_CLEARED + 
            FlightModes_VERTICAL_VGA_CLEARED + 
            FlightModes_VERTICAL_PITCH_SELECTED_ACTIVE
        }
        no s.taken
        s.stable = True
        no s.events & InternalEvent
        // Model specific constraints
        (s.FlightModes_FD_On) = False
            (s.FlightModes_Modes_On) = False
            (s.FlightModes_HDG_Selected) = False
            (s.FlightModes_HDG_Active) = False
            (s.FlightModes_NAV_Selected) = False
            (s.FlightModes_NAV_Active) = False
            (s.FlightModes_VS_Active) = False
            (s.FlightModes_LAPPR_Selected) = False
            (s.FlightModes_LAPPR_Active) = False
            (s.FlightModes_LGA_Selected) = False
            (s.FlightModes_LGA_Active) = False
            (s.FlightModes_ROLL_Active) = True
            (s.FlightModes_ROLL_Selected) = True
            (s.FlightModes_VS_Selected) = False
            (s.FlightModes_FLC_Selected) = False
            (s.FlightModes_FLC_Active) = False
            (s.FlightModes_ALT_Active) = False
            (s.FlightModes_ALTSEL_Active) = False
            (s.FlightModes_ALT_Selected) = False
            (s.FlightModes_ALTSEL_Track) = False
            (s.FlightModes_ALTSEL_Selected) = False
            (s.FlightModes_PITCH_Selected) = True
            (s.FlightModes_VAPPR_Selected) = False
            (s.FlightModes_VAPPR_Active) = False
            (s.FlightModes_VGA_Selected) = False
            (s.FlightModes_VGA_Active) = False
            (s.FlightModes_PITCH_Active) = True
            (s.FlightModes_Independent_Mode) = False
            (s.FlightModes_Active_Side) = False
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        FlightModes_FD_TurnFDOn[s, s'] or
        FlightModes_FD_TurnFDOff[s, s'] or
        FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s'] or
        FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s'] or
        FlightModes_LATERAL_HDG_Select[s, s'] or
        FlightModes_LATERAL_HDG_Clear[s, s'] or
        FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s'] or
        FlightModes_LATERAL_NAV_Select[s, s'] or
        FlightModes_LATERAL_NAV_Capture[s, s'] or
        FlightModes_LATERAL_NAV_Clear[s, s'] or
        FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s'] or
        FlightModes_LATERAL_LAPPR_Select[s, s'] or
        FlightModes_LATERAL_LAPPR_Capture[s, s'] or
        FlightModes_LATERAL_LAPPR_Clear[s, s'] or
        FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s'] or
        FlightModes_LATERAL_LGA_Select[s, s'] or
        FlightModes_LATERAL_LGA_Clear[s, s'] or
        FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s'] or
        FlightModes_LATERAL_ROLL_Select[s, s'] or
        FlightModes_LATERAL_ROLL_Clear[s, s'] or
        FlightModes_VERTICAL_VS_Select[s, s'] or
        FlightModes_VERTICAL_VS_Clear[s, s'] or
        FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_FLC_Select[s, s'] or
        FlightModes_VERTICAL_FLC_Clear[s, s'] or
        FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_ALT_Select[s, s'] or
        FlightModes_VERTICAL_ALT_Clear[s, s'] or
        FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_ALTSEL_Select[s, s'] or
        FlightModes_VERTICAL_ALTSEL_Capture[s, s'] or
        FlightModes_VERTICAL_ALTSEL_Track[s, s'] or
        FlightModes_VERTICAL_ALTSEL_Clear[s, s'] or
        FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_VAPPR_Select[s, s'] or
        FlightModes_VERTICAL_VAPPR_Capture[s, s'] or
        FlightModes_VERTICAL_VAPPR_Clear[s, s'] or
        FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_VGA_Select[s, s'] or
        FlightModes_VERTICAL_VGA_Clear[s, s'] or
        FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s'] or
        FlightModes_VERTICAL_PITCH_Select[s, s'] or
        FlightModes_VERTICAL_PITCH_Clear[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred testIfNextStable[s, s': Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_FlightModes_FD_TurnFDOn[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_FD_TurnFDOff[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_HDG_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_HDG_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_HDG_NewLateralModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_NAV_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_NAV_Capture[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_NAV_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_NAV_NewLateralModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LAPPR_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LAPPR_Capture[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LAPPR_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LGA_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LGA_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_LGA_NewLateralModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_ROLL_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_LATERAL_ROLL_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VS_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VS_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_FLC_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_FLC_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALT_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALT_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Capture[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Track[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALTSEL_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VAPPR_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VAPPR_Capture[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VAPPR_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VGA_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VGA_Clear[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_PITCH_Select[s, s', t, genEvents]
        !enabledAfterStep_FlightModes_VERTICAL_PITCH_Clear[s, s', t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_FlightModes_FD_TurnFDOn[s]or
        pre_FlightModes_FD_TurnFDOff[s]or
        pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOn[s]or
        pre_FlightModes_ANNUNCIATIONS_TurnAnnunciationsOff[s]or
        pre_FlightModes_LATERAL_HDG_Select[s]or
        pre_FlightModes_LATERAL_HDG_Clear[s]or
        pre_FlightModes_LATERAL_HDG_NewLateralModeActivated[s]or
        pre_FlightModes_LATERAL_NAV_Select[s]or
        pre_FlightModes_LATERAL_NAV_Capture[s]or
        pre_FlightModes_LATERAL_NAV_Clear[s]or
        pre_FlightModes_LATERAL_NAV_NewLateralModeActivated[s]or
        pre_FlightModes_LATERAL_LAPPR_Select[s]or
        pre_FlightModes_LATERAL_LAPPR_Capture[s]or
        pre_FlightModes_LATERAL_LAPPR_Clear[s]or
        pre_FlightModes_LATERAL_LAPPR_NewLateralModeActivated[s]or
        pre_FlightModes_LATERAL_LGA_Select[s]or
        pre_FlightModes_LATERAL_LGA_Clear[s]or
        pre_FlightModes_LATERAL_LGA_NewLateralModeActivated[s]or
        pre_FlightModes_LATERAL_ROLL_Select[s]or
        pre_FlightModes_LATERAL_ROLL_Clear[s]or
        pre_FlightModes_VERTICAL_VS_Select[s]or
        pre_FlightModes_VERTICAL_VS_Clear[s]or
        pre_FlightModes_VERTICAL_VS_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_FLC_Select[s]or
        pre_FlightModes_VERTICAL_FLC_Clear[s]or
        pre_FlightModes_VERTICAL_FLC_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_ALT_Select[s]or
        pre_FlightModes_VERTICAL_ALT_Clear[s]or
        pre_FlightModes_VERTICAL_ALT_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_ALTSEL_Select[s]or
        pre_FlightModes_VERTICAL_ALTSEL_Capture[s]or
        pre_FlightModes_VERTICAL_ALTSEL_Track[s]or
        pre_FlightModes_VERTICAL_ALTSEL_Clear[s]or
        pre_FlightModes_VERTICAL_ALTSEL_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_VAPPR_Select[s]or
        pre_FlightModes_VERTICAL_VAPPR_Capture[s]or
        pre_FlightModes_VERTICAL_VAPPR_Clear[s]or
        pre_FlightModes_VERTICAL_VAPPR_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_VGA_Select[s]or
        pre_FlightModes_VERTICAL_VGA_Clear[s]or
        pre_FlightModes_VERTICAL_VGA_NewVerticalModeActivated[s]or
        pre_FlightModes_VERTICAL_PITCH_Select[s]or
        pre_FlightModes_VERTICAL_PITCH_Clear[s]
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.events = s.events
        s'.taken = s.taken
        // Model specific declarations
        s'.FlightModes_Pilot_Flying_Side = s.FlightModes_Pilot_Flying_Side
        s'.FlightModes_Pilot_Flying_Transfer = s.FlightModes_Pilot_Flying_Transfer
        s'.FlightModes_HDG_Switch_Pressed = s.FlightModes_HDG_Switch_Pressed
        s'.FlightModes_NAV_Switch_Pressed = s.FlightModes_NAV_Switch_Pressed
        s'.FlightModes_GA_Switch_Pressed = s.FlightModes_GA_Switch_Pressed
        s'.FlightModes_When_AP_Engaged = s.FlightModes_When_AP_Engaged
        s'.FlightModes_FD_Switch_Pressed = s.FlightModes_FD_Switch_Pressed
        s'.FlightModes_Overspeed = s.FlightModes_Overspeed
        s'.FlightModes_VS_Switch_Pressed = s.FlightModes_VS_Switch_Pressed
        s'.FlightModes_FLC_Switch_Pressed = s.FlightModes_FLC_Switch_Pressed
        s'.FlightModes_ALT_Switch_Pressed = s.FlightModes_ALT_Switch_Pressed
        s'.FlightModes_APPR_Switch_Pressed = s.FlightModes_APPR_Switch_Pressed
        s'.FlightModes_VS_Pitch_Wheel_Rotated = s.FlightModes_VS_Pitch_Wheel_Rotated
        s'.FlightModes_Selected_NAV_Source_Changed = s.FlightModes_Selected_NAV_Source_Changed
        s'.FlightModes_Selected_NAV_Frequency_Changed = s.FlightModes_Selected_NAV_Frequency_Changed
        s'.FlightModes_Is_AP_Engaged = s.FlightModes_Is_AP_Engaged
        s'.FlightModes_Is_Offside_FD_On = s.FlightModes_Is_Offside_FD_On
        s'.FlightModes_LAPPR_Capture_Condition_Met = s.FlightModes_LAPPR_Capture_Condition_Met
        s'.FlightModes_SYNC_Switch_Pressed = s.FlightModes_SYNC_Switch_Pressed
        s'.FlightModes_NAV_Capture_Condition_Met = s.FlightModes_NAV_Capture_Condition_Met
        s'.FlightModes_ALTSEL_Target_Changed = s.FlightModes_ALTSEL_Target_Changed
        s'.FlightModes_ALTSEL_Capture_Condition_Met = s.FlightModes_ALTSEL_Capture_Condition_Met
        s'.FlightModes_ALTSEL_Track_Condition_Met = s.FlightModes_ALTSEL_Track_Condition_Met
        s'.FlightModes_VAPPR_Capture_Condition_Met = s.FlightModes_VAPPR_Capture_Condition_Met
        s'.FlightModes_FD_On = s.FlightModes_FD_On
        s'.FlightModes_Modes_On = s.FlightModes_Modes_On
        s'.FlightModes_HDG_Selected = s.FlightModes_HDG_Selected
        s'.FlightModes_HDG_Active = s.FlightModes_HDG_Active
        s'.FlightModes_NAV_Selected = s.FlightModes_NAV_Selected
        s'.FlightModes_NAV_Active = s.FlightModes_NAV_Active
        s'.FlightModes_VS_Active = s.FlightModes_VS_Active
        s'.FlightModes_LAPPR_Selected = s.FlightModes_LAPPR_Selected
        s'.FlightModes_LAPPR_Active = s.FlightModes_LAPPR_Active
        s'.FlightModes_LGA_Selected = s.FlightModes_LGA_Selected
        s'.FlightModes_LGA_Active = s.FlightModes_LGA_Active
        s'.FlightModes_ROLL_Active = s.FlightModes_ROLL_Active
        s'.FlightModes_ROLL_Selected = s.FlightModes_ROLL_Selected
        s'.FlightModes_VS_Selected = s.FlightModes_VS_Selected
        s'.FlightModes_FLC_Selected = s.FlightModes_FLC_Selected
        s'.FlightModes_FLC_Active = s.FlightModes_FLC_Active
        s'.FlightModes_ALT_Active = s.FlightModes_ALT_Active
        s'.FlightModes_ALTSEL_Active = s.FlightModes_ALTSEL_Active
        s'.FlightModes_ALT_Selected = s.FlightModes_ALT_Selected
        s'.FlightModes_ALTSEL_Track = s.FlightModes_ALTSEL_Track
        s'.FlightModes_ALTSEL_Selected = s.FlightModes_ALTSEL_Selected
        s'.FlightModes_PITCH_Selected = s.FlightModes_PITCH_Selected
        s'.FlightModes_VAPPR_Selected = s.FlightModes_VAPPR_Selected
        s'.FlightModes_VAPPR_Active = s.FlightModes_VAPPR_Active
        s'.FlightModes_VGA_Selected = s.FlightModes_VGA_Selected
        s'.FlightModes_VGA_Active = s.FlightModes_VGA_Active
        s'.FlightModes_PITCH_Active = s.FlightModes_PITCH_Active
        s'.FlightModes_Independent_Mode = s.FlightModes_Independent_Mode
        s'.FlightModes_Active_Side = s.FlightModes_Active_Side
    }

    fact {
        all s: Snapshot | s in initial iff init[s]
        all s, s': Snapshot | s->s' in nextStep iff small_step[s, s']
        all s, s': Snapshot | equals[s, s'] => s = s'
        all s: Snapshot | (isEnabled[s] && no s': Snapshot | small_step[s, s']) => s.stable = False
        all s: Snapshot | s.stable = False => some s.nextStep
        path
    }

    pred path {
        all s:Snapshot, s': s.next | operation[s, s']
        init[first]
    }
    run path for 5 Snapshot, 2 EventLabel
        expect 1

/****************************** INVARIANTS ************************************/
    fact FlightModes_EventProcessing {
        all s: Snapshot | (some FlightModes & s.conf) => {
            (s.FlightModes_When_AP_Engaged) = (s.FlightModes_Is_AP_Engaged)
        }
    }
    


    assert ctl_At_Least_One_Lateral_Mode_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => (((s.FlightModes_FD_On) = True or (s.FlightModes_Is_AP_Engaged) = True) => (s.FlightModes_ROLL_Active) = True or (s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True)}]]
    }
    
    check ctl_At_Least_One_Lateral_Mode_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert At_Least_One_Lateral_Mode_Active {
        all s: Snapshot | s.stable = True and (s.FlightModes_FD_On = True or s.FlightModes_Is_AP_Engaged = True) =>
            (
                s.FlightModes_ROLL_Active = True or
                s.FlightModes_HDG_Active = True or
                s.FlightModes_NAV_Active = True or
                s.FlightModes_LAPPR_Active = True or
                s.FlightModes_LGA_Active = True
            )
    }
    check At_Least_One_Lateral_Mode_Active for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_at_Most_One_Lateral_Mode_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => (((s.FlightModes_ROLL_Active) = True => !(((s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True))) and ((s.FlightModes_HDG_Active) = True => !(((s.FlightModes_ROLL_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True))) and ((s.FlightModes_NAV_Active) = True => !(((s.FlightModes_ROLL_Active) = True or (s.FlightModes_HDG_Active) = True or (s.FlightModes_LAPPR_Active) = True or (s.FlightModes_LGA_Active) = True))) and ((s.FlightModes_LAPPR_Active) = True => !(((s.FlightModes_ROLL_Active) = True or (s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LGA_Active) = True))) and ((s.FlightModes_LGA_Active) = True => !(((s.FlightModes_ROLL_Active) = True or (s.FlightModes_HDG_Active) = True or (s.FlightModes_NAV_Active) = True or (s.FlightModes_LAPPR_Active) = True))))}]]
    }
    
    check ctl_at_Most_One_Lateral_Mode_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert At_Most_One_Lateral_Mode_Active {
        all s: Snapshot | s.stable = True =>
            ((s.FlightModes_ROLL_Active = True =>
                !(s.FlightModes_HDG_Active = True or s.FlightModes_NAV_Active = True or
                 s.FlightModes_LAPPR_Active = True or s.FlightModes_LGA_Active = True)) and
            (s.FlightModes_HDG_Active = True =>
                !(s.FlightModes_ROLL_Active = True or s.FlightModes_NAV_Active = True or
                 s.FlightModes_LAPPR_Active = True or s.FlightModes_LGA_Active = True)) and
            (s.FlightModes_NAV_Active = True =>
                !(s.FlightModes_ROLL_Active = True or s.FlightModes_HDG_Active = True or
                 s.FlightModes_LAPPR_Active = True or s.FlightModes_LGA_Active = True)) and
            (s.FlightModes_LAPPR_Active = True =>
                !(s.FlightModes_ROLL_Active = True or s.FlightModes_HDG_Active = True or
                 s.FlightModes_NAV_Active = True or s.FlightModes_LGA_Active = True)) and
            (s.FlightModes_LGA_Active = True =>
                !(s.FlightModes_ROLL_Active = True or s.FlightModes_HDG_Active = True or
                 s.FlightModes_NAV_Active = True or s.FlightModes_LAPPR_Active = True))
        )
    }
    check At_Most_One_Lateral_Mode_Active for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_exactly_One_Lateral_Mode_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => one {
            FlightModes_LATERAL_ROLL_SELECTED_ACTIVE + FlightModes_LATERAL_HDG_SELECTED_ACTIVE + FlightModes_LATERAL_NAV_SELECTED_ACTIVE + FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE + FlightModes_LATERAL_LGA_SELECTED_ACTIVE
        }
         & s.conf}]]
    }
    
    check ctl_exactly_One_Lateral_Mode_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert exactly_One_Lateral_Mode_Active {
        all s: Snapshot |  s.stable = True => one {
            FlightModes_LATERAL_ROLL_SELECTED_ACTIVE +
            FlightModes_LATERAL_HDG_SELECTED_ACTIVE +
            FlightModes_LATERAL_NAV_SELECTED_ACTIVE +
            FlightModes_LATERAL_LAPPR_SELECTED_ACTIVE +
            FlightModes_LATERAL_LGA_SELECTED_ACTIVE
        } & s.conf
    }
    check exactly_One_Lateral_Mode_Active for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_At_Least_One_Vertical_Mode_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => ((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True)}]]
    }
    
    check ctl_At_Least_One_Vertical_Mode_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert At_Least_One_Vertical_Mode_Active {
        all s: Snapshot | s.stable = True =>
            (s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
            s.FlightModes_FLC_Active = True or s.FlightModes_ALT_Active = True or
            s.FlightModes_ALTSEL_Active = True or s.FlightModes_VAPPR_Active = True or
            s.FlightModes_VGA_Active = True)
    }
    check At_Least_One_Vertical_Mode_Active for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_At_Most_One_Vertical_Mode_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => (((s.FlightModes_PITCH_Active) = True => !(((s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_VS_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_FLC_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_ALT_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_ALTSEL_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_VAPPR_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VGA_Active) = True))) and ((s.FlightModes_VGA_Active) = True => !(((s.FlightModes_PITCH_Active) = True or (s.FlightModes_VS_Active) = True or (s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_VAPPR_Active) = True))))}]]
    }
    
    check ctl_At_Most_One_Vertical_Mode_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert At_Most_One_Vertical_Mode_Active {
        all s: Snapshot | s.stable = True =>
            ((s.FlightModes_PITCH_Active = True =>
                !(s.FlightModes_VS_Active = True or s.FlightModes_FLC_Active = True or
                 s.FlightModes_ALT_Active = True or s.FlightModes_ALTSEL_Active = True or
                 s.FlightModes_VAPPR_Active = True or s.FlightModes_VGA_Active = True)
            ) and
            (s.FlightModes_VS_Active = True =>
                !( s.FlightModes_PITCH_Active = True or s.FlightModes_FLC_Active = True or
                    s.FlightModes_ALT_Active = True or s.FlightModes_ALTSEL_Active = True or
                    s.FlightModes_VAPPR_Active = True or s.FlightModes_VGA_Active = True)
            ) and
            (s.FlightModes_FLC_Active = True =>
                !(s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
                s.FlightModes_ALT_Active = True or s.FlightModes_ALTSEL_Active = True or
                s.FlightModes_VAPPR_Active = True or s.FlightModes_VGA_Active = True)
            ) and
            (s.FlightModes_ALT_Active = True =>
                !(s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
                s.FlightModes_FLC_Active = True or s.FlightModes_ALTSEL_Active = True or
                s.FlightModes_VAPPR_Active = True or s.FlightModes_VGA_Active = True)
            ) and
            (s.FlightModes_ALTSEL_Active = True =>
                !(s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
                s.FlightModes_FLC_Active = True or s.FlightModes_ALT_Active = True or
                s.FlightModes_VAPPR_Active = True or s.FlightModes_VGA_Active = True)
            ) and (
            s.FlightModes_VAPPR_Active = True =>
                !(s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
                s.FlightModes_FLC_Active = True or s.FlightModes_ALT_Active = True or
                s.FlightModes_ALTSEL_Active = True or s.FlightModes_VGA_Active = True)
            ) and (
                s.FlightModes_VGA_Active = True =>
                !(s.FlightModes_PITCH_Active = True or s.FlightModes_VS_Active = True or
                s.FlightModes_FLC_Active = True or s.FlightModes_ALT_Active = True or
                s.FlightModes_ALTSEL_Active = True or s.FlightModes_VAPPR_Active = True)
            ))
    }
    check At_Most_One_Vertical_Mode_Active for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_ALTSEL_Selected_If_Not_ALT_VAPPR_VGA_Active  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => ((s.FlightModes_Modes_On) = True => !(((s.FlightModes_ALT_Active) = True or (s.FlightModes_VAPPR_Active) = True or (s.FlightModes_VGA_Active) = True)) => (s.FlightModes_ALTSEL_Selected) = True)}]]
    }
    
    check ctl_ALTSEL_Selected_If_Not_ALT_VAPPR_VGA_Active 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert ALTSEL_Selected_If_Not_ALT_VAPPR_VGA_Active {
        all s: Snapshot | s.stable = True and s.FlightModes_Modes_On = True =>
            !(
                s.FlightModes_ALT_Active = True or
                s.FlightModes_VAPPR_Active = True or
                s.FlightModes_VGA_Active = True)
            =>
            s.FlightModes_ALTSEL_Selected = True
    }
    check ALTSEL_Selected_If_Not_ALT_VAPPR_VGA_Active for 6 Snapshot, exactly 2 EventLabel
    
    
    --------------------------------------------------------------------------------
    -- FLC, ALT, ALTSEL, or PITCH mode shall be active
    -- while an overspeed condition exists.
    --------------------------------------------------------------------------------
    
    assert ctl_Overspeed_Implies_FLC_ALT_ALTSEL_PITCH  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => ((s.FlightModes_Overspeed) = True => ((s.FlightModes_FLC_Active) = True or (s.FlightModes_ALT_Active) = True or (s.FlightModes_ALTSEL_Active) = True or (s.FlightModes_PITCH_Active) = True))}]]
    }
    
    check ctl_Overspeed_Implies_FLC_ALT_ALTSEL_PITCH 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    assert  Overspeed_Implies_FLC_ALT_ALTSEL_PITCH {
        all s: Snapshot | s.stable = True => (
            s.FlightModes_Overspeed = True =>
            (
                s.FlightModes_FLC_Active = True or
                s.FlightModes_ALT_Active = True or
                s.FlightModes_ALTSEL_Active = True or
                s.FlightModes_PITCH_Active = True
            )
        )
    }
    check Overspeed_Implies_FLC_ALT_ALTSEL_PITCH for 6 Snapshot, exactly 2 EventLabel
    
    
    assert ctl_Modes_Off_At_Startup {
        ctl_mc[ag[
            { s: Snapshot | init[s] => s.FlightModes_Modes_On = False }
        ]]
    }
    check ctl_Modes_Off_At_Startup for 6 Snapshot, exactly 2 EventLabel
    
    assert check_Modes_Off_At_Startup {
        all s: Snapshot | init[s] => s.FlightModes_Modes_On = False
    }
    check check_Modes_Off_At_Startup for 6 Snapshot, exactly 2 EventLabel
    
    
    --------------------------------------------------------------------------------
    -- The mode annunciations shall be on if the AP is engaged.
    --
    -- Notes: We use the contrapositive because it is easier to reason about outputs
    -- on stable snapshots
    --------------------------------------------------------------------------------
    assert AP_Engaged_Implies_Modes_On {
        all s: Snapshot | s.stable = True =>
            s.FlightModes_Modes_On = False =>
            all _s: nextStep.s | _s.FlightModes_Is_AP_Engaged = False
    }
    check AP_Engaged_Implies_Modes_On for 6 Snapshot, exactly 2 EventLabel
    
    assert ctl_AP_Engaged_Implies_Modes_On  {
        ctl_mc[ag[{ (imp_ctl[
            {s: Snapshot | s.stable = True and (s.FlightModes_Is_AP_Engaged) = True},
            (ax[au[
                {s: Snapshot | s.stable = False},
                {s: Snapshot | s.stable = True and (s.FlightModes_Modes_On) = True}
            ]])
        ])}]]
    }
    
    check ctl_AP_Engaged_Implies_Modes_On 
        for 6 Snapshot, exactly 2 EventLabel
    
    
    --------------------------------------------------------------------------------
    -- The mode annunciations shall be on if the offside FD is on.
    --
    -- Notes: We use the contrapositive because it is easier to reason about outputs
    -- on stable snapshots
    --------------------------------------------------------------------------------
    assert Offside_FD_On_Implies_Modes_On {
        all s: Snapshot | s.stable = True =>
            s.FlightModes_Modes_On = False =>
            all _s: nextStep.s | _s.FlightModes_Is_Offside_FD_On = False
    }
    check Offside_FD_On_Implies_Modes_On for 6 Snapshot, exactly 2 EventLabel
    
    --------------------------------------------------------------------------------
    -- The mode annunciations shall be on if the onside FD is on.
    --
    -- Notes: We use the contrapositive because it is easier to reason about outputs
    -- on stable snapshots
    --------------------------------------------------------------------------------
    assert Onside_FD_On_Implies_Modes_On {
        all s: Snapshot | s.stable = True =>
            s.FlightModes_Modes_On = False =>
            all _s: nextStep.s | _s.FlightModes_FD_On = False
    }
    check Onside_FD_On_Implies_Modes_On for 6 Snapshot, exactly 2 EventLabel
    
    
    
    --------------------------------------------------------------------------------
    -- The onside FD shall be off at system start up
    --------------------------------------------------------------------------------
    assert FD_Off_At_Startup {
        all s: Snapshot | init[s] => s.FlightModes_FD_On = False
    }
    check FD_Off_At_Startup for 10 Snapshot, exactly 2 EventLabel
    
    
    --------------------------------------------------------------------------------
    -- The onside FD shall turn on when the AP is engaged.
    --------------------------------------------------------------------------------
    assert  AP_Engaged_Turns_FD_On {
        // This property is weaker than it is required, we're proving that the rising
        // Is_AP_Engaged will eventually turn the Modes On.
        // This is because of how our nextStep relation is built and we don't have an
        // easy way to refer to the next stable snapshot
        all s: Snapshot | s.stable = True =>
            rising[s, FlightModes_Is_AP_Engaged] => some s': s.*nextStep | s'.stable = True and s'.FlightModes_FD_On = True
    }
    check AP_Engaged_Turns_FD_On for 6 Snapshot, exactly 2 EventLabel
    
    pred rising[s: Snapshot, rel: Snapshot -> one Bool] {
        nextStep.s.rel = False and s.rel = True
    }
    --------------------------------------------------------------------------------
    -- The onside FD shall be on when an overspeed condition exists.
    --
    -- Notes: We use the contrapositive because it is easier to reason about outputs
    -- on stable snapshots.
    -- Property fails because we have not implemented the event processing module
    --------------------------------------------------------------------------------
    assert Overspeed_Implies_FD_On {
        all s: Snapshot | s.stable = True =>
            s.FlightModes_FD_On = False =>
            all _s: nextStep.s | _s.FlightModes_Overspeed = False
    }
    check Overspeed_Implies_FD_On for 6 Snapshot, exactly 2 EventLabel
    
    --------------------------------------------------------------------------------
    -- ROLL mode shall be active if and only if ROLL mode is selected.
    --------------------------------------------------------------------------------
    assert ROLL_Selected_Iff_ROLL_Active {
        all s: Snapshot | s.stable = True =>
            (s.FlightModes_ROLL_Active = True iff s.FlightModes_ROLL_Selected = True)
    }
    check ROLL_Selected_Iff_ROLL_Active for 6 Snapshot, exactly 2 EventLabel
    
    
    --------------------------------------------------------------------------------
    -- ROLL mode shall be active iff no other lateral mode is active.
    --
    -- Notes: we ca check this property on the same snapshot because all variables
    -- involved are controlled outputs and they do not change on stable snapshots
    --------------------------------------------------------------------------------
    assert Default_Lateral_Mode_Is_ROLL {
        all s: Snapshot | s.stable = True =>
            (s.FlightModes_ROLL_Active = True iff
            !(
                s.FlightModes_HDG_Active = True or
                s.FlightModes_NAV_Active = True or
                s.FlightModes_LAPPR_Active = True or
                s.FlightModes_LGA_Active = True
            ))
    }
    check Default_Lateral_Mode_Is_ROLL for 6 Snapshot, exactly 2 EventLabel
    

